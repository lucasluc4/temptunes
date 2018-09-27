package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.exception.GenericTempTunesException;
import com.lucasluc4.temptunes.thirdparty.dto.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class SpotifyApiServiceTest {

    @Mock
    private SpotifyAuthService spotifyAuthService;

    @Mock
    private SpotifyApi spotifyApi;

    @InjectMocks
    private SpotifyApiService spotifyApiService;

    @Test
    public void getTokenFromSpotifyAuthServiceAndGetPlaylistFromApi () {

        when(spotifyAuthService.getToken()).thenReturn("");
        when(spotifyApi.getPlaylistById(anyString(), anyString())).thenReturn(createSuccessfulSpotifyPlaylistResponse());

        spotifyApiService.getPlaylistById("test");

        verify(spotifyAuthService, times(1)).getToken();
        verify(spotifyApi, times(1)).getPlaylistById(anyString(), anyString());
    }

    @Test
    public void willTryToAuthenticateAgainInCaseOfInvalidToken () {

        when(spotifyAuthService.getToken()).thenReturn("");

        when(spotifyApi.getPlaylistById(anyString(), anyString())).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                if (count == 0) {
                    count++;
                    return createErrorSpotifyPlaylistResponse(HttpStatus.UNAUTHORIZED);
                }

                return createSuccessfulSpotifyPlaylistResponse();
            }
        });

        spotifyApiService.getPlaylistById("test");

        verify(spotifyAuthService, times(1)).authenticate();
        verify(spotifyAuthService, times(2)).getToken();
        verify(spotifyApi, times(2)).getPlaylistById(anyString(), anyString());
    }

    @Test(expected = GenericTempTunesException.class)
    public void willThrowExceptionInCaseOfUnmappedError () {

        when(spotifyAuthService.getToken()).thenReturn("");
        when(spotifyApi.getPlaylistById(anyString(), anyString())).thenReturn(createErrorSpotifyPlaylistResponse(HttpStatus.BAD_REQUEST));

        spotifyApiService.getPlaylistById("test");
    }

    private SpotifyPlaylistResponse createSuccessfulSpotifyPlaylistResponse () {
        SpotifyPlaylistResponse spotifyPlaylistResponse = new SpotifyPlaylistResponse();
        spotifyPlaylistResponse.setPlaylist(createSpotifyPlaylistDTOMock());
        spotifyPlaylistResponse.setInfo(createSuccessResponseInfoMock());

        return spotifyPlaylistResponse;
    }

    private ResponseInfo createSuccessResponseInfoMock () {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setCode(HttpStatus.OK.value());
        responseInfo.setStatus(ResponseStatus.SUCCESS);

        return responseInfo;
    }

    private SpotifyPlaylistDTO createSpotifyPlaylistDTOMock(){
        SpotifyPlaylistDTO spotifyPlaylistDTO = new SpotifyPlaylistDTO();

        SpotifyTracksDTO tracksDTO = new SpotifyTracksDTO();

        SpotifyItemDTO spotifyItemDTO = new SpotifyItemDTO();

        SpotifyTrackDTO spotifyTrackDTO = new SpotifyTrackDTO();

        spotifyItemDTO.setTrack(spotifyTrackDTO);

        List<SpotifyItemDTO> items = new ArrayList();
        items.add(spotifyItemDTO);

        tracksDTO.setItems(items);

        spotifyPlaylistDTO.setTracks(tracksDTO);

        return spotifyPlaylistDTO;
    }

    private SpotifyPlaylistResponse createErrorSpotifyPlaylistResponse(HttpStatus status) {
        SpotifyPlaylistResponse response = new SpotifyPlaylistResponse();
        response.setInfo(createErrorResponseInfoMock(status.value()));

        return response;
    }

    private ResponseInfo createErrorResponseInfoMock (int code) {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setCode(code);
        responseInfo.setStatus(ResponseStatus.ERROR);

        return responseInfo;
    }

}
