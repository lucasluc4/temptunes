package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.exception.CouldNotRetrievePlaylistException;
import com.lucasluc4.temptunes.redisson.TemptunesRedissonClient;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyTokenDTO;
import org.apache.commons.codec.binary.Base64;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyAuthService.class);

    private static final String REDIS_BUCKET_TOKEN = "spotify.redis.bucket.token";
    private static final String REDIS_AUTH_LOCK = "spotify.redis.lock.auth";
    private static final String SPOTIFY_CLIENT_ID = "spotify.client.id";
    private static final String SPOTIFY_CLIENT_SECRET = "spotify.client.secret";

    private TemptunesRedissonClient redissonClient;

    private ThirdPartyApisComponent thirdPartyApisComponent;

    private Environment environment;

    @Autowired
    public SpotifyAuthService(TemptunesRedissonClient redissonClient, ThirdPartyApisComponent thirdPartyApisComponent,
                              Environment environment) {

        this.redissonClient = redissonClient;
        this.thirdPartyApisComponent = thirdPartyApisComponent;
        this.environment = environment;
    }

    public String getToken () {
        LOGGER.info("Fetching Spotify token");

        RBucket<String> bucket = redissonClient.getRedissonClient()
                .getBucket(environment.getProperty(REDIS_BUCKET_TOKEN));
        String bearerToken = bucket.get();

        if (bearerToken == null) {
            LOGGER.info("No token found. Trying to authenticate again.");
            bearerToken = authenticate();
        }

        return bearerToken;
    }

    public String authenticate() {

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
