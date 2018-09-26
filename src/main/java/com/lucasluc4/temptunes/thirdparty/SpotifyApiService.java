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
                .build("https://api.spotify.com/v1");

        loginSpotifyApi = new FeingBuilderUtil<>(LoginSpotifyApi.class)
                .build("https://accounts.spotify.com/api", new FormEncoder());
    }

    public Playlist getPlaylistById (String id) {

        try {

            RBucket<String> bucket = redissonClient.getRedissonClient().getBucket("SpotifyBearerToken");
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

        RLock lock = redissonClient.getRedissonClient()
                .getLock(environment.getProperty("spotify.redis.lock.auth"));

        lock.lock();

        if (lock.isHeldByCurrentThread()) {

            String clientId = "7922f406cc7d4f97816e7bc57dcb19bd";
            String clientSecret = "897d072d200443208293b435b7906993";

            String basicAuthHeader = clientId + ":" + clientSecret;
            String encodedBasicAuthHeader = new String(new Base64().encode(basicAuthHeader.getBytes()));

            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Basic " + encodedBasicAuthHeader);

            SpotifyTokenDTO response = loginSpotifyApi.getToken("client_credentials", headerMap);

            String bearerToken = response.getAccess_token();

            RBucket<String> bucket = redissonClient.getRedissonClient()
                    .getBucket(environment.getProperty("spotify.redis.bucket.token"));
            bucket.set(bearerToken);

            lock.unlock();

            return bearerToken;

        } else {
            throw new CouldNotRetrievePlaylistException("Could not get playlist information");
        }

    }

}
