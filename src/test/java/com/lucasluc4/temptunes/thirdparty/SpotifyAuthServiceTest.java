package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.exception.CouldNotRetrievePlaylistException;
import com.lucasluc4.temptunes.redisson.TemptunesRedissonClient;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyTokenDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class SpotifyAuthServiceTest {

    @Mock
    private TemptunesRedissonClient temptunesRedissonClient;

    @Mock
    private ThirdPartyApisComponent thirdPartyApisComponent;

    @Mock
    private Environment environment;

    @InjectMocks
    private SpotifyAuthService spotifyAuthService;

    private RedissonClient redissonClient;

    private LoginSpotifyApi loginSpotifyApi;

    @Before
    public void init() {
        loginSpotifyApi = mock(LoginSpotifyApi.class);
        redissonClient = mock(RedissonClient.class);
        when(temptunesRedissonClient.getRedissonClient()).thenReturn(redissonClient);

        when(environment.getProperty(anyString())).thenReturn("");

        when(thirdPartyApisComponent.getLoginSpotifyApi()).thenReturn(loginSpotifyApi);
    }

    @Test
    public void willNotTryToAuthenticateAgainIfBucketReturnsAToken () {
        RBucket bucket = mock(RBucket.class);

        when(bucket.get()).thenReturn("willNotTryToAuthenticateAgainIfBucketReturnsAToken");

        when(redissonClient.getBucket(anyString())).thenReturn(bucket);

        String token = spotifyAuthService.getToken();

        verify(loginSpotifyApi, times(0)).getToken(anyString(), anyMap());
        assertTrue("willNotTryToAuthenticateAgainIfBucketReturnsAToken".equals(token));
    }

    @Test
    public void willAuthenticateAgainIfBucketReturnsNull () {

        SpotifyTokenDTO spotifyTokenDTO = new SpotifyTokenDTO();
        spotifyTokenDTO.setAccess_token("willAuthenticateAgainIfBucketReturnsNull");

        when(loginSpotifyApi.getToken(anyString(), anyMap())).thenReturn(spotifyTokenDTO);

        RBucket bucket = mock(RBucket.class);
        RLock lock = mock(RLock.class);

        when(bucket.get()).thenReturn(null);

        when(redissonClient.getBucket(anyString())).thenReturn(bucket);
        when(redissonClient.getLock(anyString())).thenReturn(lock);

        when(lock.isHeldByCurrentThread()).thenReturn(true);

        String token = spotifyAuthService.getToken();

        verify(loginSpotifyApi, times(1)).getToken(anyString(), anyMap());
        verify(lock, times(1)).lock();
        verify(lock, times(1)).unlock();
        verify(lock, times(1)).isHeldByCurrentThread();
        assertTrue("willAuthenticateAgainIfBucketReturnsNull".equals(token));
    }

    @Test(expected = CouldNotRetrievePlaylistException.class)
    public void willThrowExceptionIfCanNotLockAuthLock() {
        SpotifyTokenDTO spotifyTokenDTO = new SpotifyTokenDTO();
        spotifyTokenDTO.setAccess_token("willAuthenticateAgainIfBucketReturnsNull");

        when(loginSpotifyApi.getToken(anyString(), anyMap())).thenReturn(spotifyTokenDTO);

        RBucket bucket = mock(RBucket.class);
        RLock lock = mock(RLock.class);

        when(bucket.get()).thenReturn(null);

        when(redissonClient.getBucket(anyString())).thenReturn(bucket);
        when(redissonClient.getLock(anyString())).thenReturn(lock);

        when(lock.isHeldByCurrentThread()).thenReturn(false);

        spotifyAuthService.getToken();
    }


}
