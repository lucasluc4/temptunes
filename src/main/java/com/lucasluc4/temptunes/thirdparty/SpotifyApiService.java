package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.exception.GenericTempTunesException;
import com.lucasluc4.temptunes.exception.PlaylistNotFoundException;
import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import com.lucasluc4.temptunes.thirdparty.dto.parser.SpotifyPlaylistDTOParser;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyApiService.class);

    private ThirdPartyApisComponent thirdPartyApisComponent;

    private SpotifyAuthService spotifyAuthService;

    @Autowired
    public SpotifyApiService(ThirdPartyApisComponent thirdPartyApisComponent,
                             SpotifyAuthService spotifyAuthService) {
        this.thirdPartyApisComponent = thirdPartyApisComponent;
        this.spotifyAuthService = spotifyAuthService;
    }

    public Playlist getPlaylistById (String id) {

        LOGGER.info("Retrieving playlist for id: " + id);

        try {

            String bearerToken = spotifyAuthService.getToken();

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

                spotifyAuthService.authenticate();
                return getPlaylistById(id);
            }

            throw new GenericTempTunesException();
        }

    }

}
