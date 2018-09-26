package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.exception.CouldNotRetrievePlaylistException;
import com.lucasluc4.temptunes.exception.GenericTempTunesException;
import com.lucasluc4.temptunes.exception.PlaylistNotFoundException;
import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.redisson.TemptunesRedissonClient;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyTokenDTO;
import com.lucasluc4.temptunes.thirdparty.dto.parser.SpotifyPlaylistDTOParser;
import com.lucasluc4.temptunes.utils.FeingBuilderUtil;
import feign.FeignException;
import feign.form.FormEncoder;
import org.apache.commons.codec.binary.Base64;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyApiService {

    private static final String REDIS_BUCKET_TOKEN = "spotify.redis.bucket.token";
    private static final String REDIS_AUTH_LOCK = "spotify.redis.lock.auth";
    private static final String SPOTIFY_URL_API = "spotify.url.api";
    private static final String SPOTIFY_URL_ACCOUNTS = "spotify.url.accounts";
    private static final String SPOTIFY_CLIENT_ID = "spotify.client.id";
    private static final String SPOTIFY_CLIENT_SECRET = "spotify.client.secret";

    private SpotifyApi spotifyApi;

    private LoginSpotifyApi loginSpotifyApi;

    private TemptunesRedissonClient redissonClient;

    private Environment environment;

    @Autowired
    public SpotifyApiService(TemptunesRedissonClient redissonClient, Environment environment) {
        this.redissonClient = redissonClient;
        this.environment = environment;
    }

    @PostConstruct
    public void init () {
        spotifyApi = new FeingBuilderUtil<>(SpotifyApi.class)
                .build(environment.getProperty(SPOTIFY_URL_API));

        loginSpotifyApi = new FeingBuilderUtil<>(LoginSpotifyApi.class)
                .build(environment.getProperty(SPOTIFY_URL_ACCOUNTS), new FormEncoder());
    }

    public Playlist getPlaylistById (String id) {

        try {

            RBucket<String> bucket = redissonClient.getRedissonClient()
                    .getBucket(environment.getProperty(REDIS_BUCKET_TOKEN));
            String bearerToken = bucket.get();

            if (bearerToken == null) {
                bearerToken = authenticate();
            }

            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + bearerToken);

            SpotifyPlaylistDTO spotifyPlaylistDTO = spotifyApi.getPlaylistById(id, headerMap);
            return SpotifyPlaylistDTOParser.parse(spotifyPlaylistDTO);

        } catch (FeignException e) {

            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                throw new PlaylistNotFoundException("Playlist not found: " + id);
            }

            if (e.status() == HttpStatus.UNAUTHORIZED.value()) {
                authenticate();
                return getPlaylistById(id);
            }

            throw new GenericTempTunesException();
        }

    }

    private String authenticate() {

        RLock lock = redissonClient.getRedissonClient().getLock(environment.getProperty(REDIS_AUTH_LOCK));

        lock.lock();

        if (lock.isHeldByCurrentThread()) {

            String clientId = environment.getProperty(SPOTIFY_CLIENT_ID);
            String clientSecret = environment.getProperty(SPOTIFY_CLIENT_SECRET);

            String basicAuthHeader = clientId + ":" + clientSecret;
            String encodedBasicAuthHeader = new String(new Base64().encode(basicAuthHeader.getBytes()));

            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Basic " + encodedBasicAuthHeader);

            SpotifyTokenDTO response = loginSpotifyApi.getToken("client_credentials", headerMap);

            String bearerToken = response.getAccess_token();

            RBucket<String> bucket = redissonClient.getRedissonClient()
                    .getBucket(environment.getProperty(REDIS_BUCKET_TOKEN));
            bucket.set(bearerToken);

            lock.unlock();

            return bearerToken;

        } else {
            throw new CouldNotRetrievePlaylistException("Could not get playlist information");
        }

    }

}
