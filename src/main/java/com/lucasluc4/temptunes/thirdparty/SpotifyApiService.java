package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.exception.CouldNotRetrievePlaylistException;
import com.lucasluc4.temptunes.exception.GenericTempTunesException;
import com.lucasluc4.temptunes.exception.PlaylistNotFoundException;
import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.redisson.TemptunesRedissonClient;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyTokenDTO;
import com.lucasluc4.temptunes.thirdparty.dto.parser.SpotifyPlaylistDTOParser;
import com.lucasluc4.temptunes.utils.FeignBuilder;
import feign.FeignException;
import feign.form.FormEncoder;
import org.apache.commons.codec.binary.Base64;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyApiService.class);

    private static final String REDIS_BUCKET_TOKEN = "spotify.redis.bucket.token";
    private static final String REDIS_AUTH_LOCK = "spotify.redis.lock.auth";
    private static final String SPOTIFY_CLIENT_ID = "spotify.client.id";
    private static final String SPOTIFY_CLIENT_SECRET = "spotify.client.secret";

    private ThirdPartyApisComponent thirdPartyApisComponent;

    private TemptunesRedissonClient redissonClient;

    private Environment environment;

    @Autowired
    public SpotifyApiService(TemptunesRedissonClient redissonClient, Environment environment, ThirdPartyApisComponent thirdPartyApisComponent) {
        this.redissonClient = redissonClient;
        this.environment = environment;
        this.thirdPartyApisComponent = thirdPartyApisComponent;
    }

    public Playlist getPlaylistById (String id) {

        LOGGER.info("Retrieving playlist for id: " + id);

        try {

            LOGGER.info("Fetching Spotify token");

            RBucket<String> bucket = redissonClient.getRedissonClient()
                    .getBucket(environment.getProperty(REDIS_BUCKET_TOKEN));
            String bearerToken = bucket.get();

            if (bearerToken == null) {
                LOGGER.info("No token found. Trying to authenticate again.");
                bearerToken = authenticate();
            }

            LOGGER.info("Retrieving playlist from Spotify");

            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + bearerToken);
            SpotifyPlaylistDTO spotifyPlaylistDTO = thirdPartyApisComponent.getSpotifyApi()
                    .getPlaylistById(id, headerMap);

            LOGGER.info("Playlist retrieved.");

            return SpotifyPlaylistDTOParser.parse(spotifyPlaylistDTO);

        } catch (FeignException e) {

            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                throw new PlaylistNotFoundException("Playlist not found: " + id);
            }

            if (e.status() == HttpStatus.UNAUTHORIZED.value()) {

                LOGGER.info("Token is invalid. Trying to authenticate again.");

                authenticate();
                return getPlaylistById(id);
            }

            throw new GenericTempTunesException();
        }

    }

    private String authenticate() {

        RLock lock = redissonClient.getRedissonClient().getLock(environment.getProperty(REDIS_AUTH_LOCK));

        LOGGER.info("Locking spotify auth lock.");

        lock.lock();

        if (lock.isHeldByCurrentThread()) {

            LOGGER.info("Spotify auth lock locked successfully.");

            String clientId = environment.getProperty(SPOTIFY_CLIENT_ID);
            String clientSecret = environment.getProperty(SPOTIFY_CLIENT_SECRET);

            String basicAuthHeader = clientId + ":" + clientSecret;
            String encodedBasicAuthHeader = new String(new Base64().encode(basicAuthHeader.getBytes()));

            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Basic " + encodedBasicAuthHeader);

            LOGGER.info("Authenticating...");

            SpotifyTokenDTO response = thirdPartyApisComponent.getLoginSpotifyApi()
                    .getToken("client_credentials", headerMap);

            LOGGER.info("Token retrieved.");

            String bearerToken = response.getAccess_token();

            RBucket<String> bucket = redissonClient.getRedissonClient()
                    .getBucket(environment.getProperty(REDIS_BUCKET_TOKEN));
            bucket.set(bearerToken);

            LOGGER.info("Token saved.");

            lock.unlock();

            LOGGER.info("Spotify auth lock unlocked.");

            return bearerToken;

        } else {
            throw new CouldNotRetrievePlaylistException("Could not get playlist information");
        }

    }

}
