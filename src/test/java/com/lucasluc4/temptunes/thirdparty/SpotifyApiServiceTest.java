package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.thirdparty.dto.SpotifyItemDTO;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyTrackDTO;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyTracksDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class SpotifyApiServiceTest {

    @Mock
    private SpotifyAuthService spotifyAuthService;

    @Mock
    private ThirdPartyApisComponent thirdPartyApisComponent;

    @InjectMocks
    private SpotifyApiService spotifyApiService;

    @Test
    public void getTokenFromSpotifyAuthServiceAndGetPlaylistFromApi () {

        SpotifyPlaylistDTO spotifyPlaylistDTO = createSpotifyPlaylistDTOMock();

        SpotifyApi spotifyApi = mock(SpotifyApi.class);

        when(spotifyAuthService.getToken()).thenReturn("");
        when(thirdPartyApisComponent.getSpotifyApi()).thenReturn(spotifyApi);
        when(spotifyApi.getPlaylistById(anyString(), anyMap())).thenReturn(spotifyPlaylistDTO);

        spotifyApiService.getPlaylistById("test");

        verify(spotifyAuthService, times(1)).getToken();
        verify(spotifyApi, times(1)).getPlaylistById(anyString(), anyMap());
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

}
