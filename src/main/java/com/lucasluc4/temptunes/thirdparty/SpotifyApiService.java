package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.exception.GenericTempTunesException;
import com.lucasluc4.temptunes.exception.PlaylistNotFoundException;
import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.thirdparty.dto.ResponseStatus;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistResponse;
import com.lucasluc4.temptunes.thirdparty.dto.parser.SpotifyPlaylistDTOParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SpotifyApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyApiService.class);

    private SpotifyApi spotifyApi;

    private SpotifyAuthService spotifyAuthService;

    @Autowired
    public SpotifyApiService(SpotifyApi spotifyApi, SpotifyAuthService spotifyAuthService) {
        this.spotifyApi = spotifyApi;
        this.spotifyAuthService = spotifyAuthService;
    }

    public Playlist getPlaylistById (String id) {

        LOGGER.info("Retrieving playlist for id: " + id);

        String bearerToken = spotifyAuthService.getToken();

        LOGGER.info("Retrieving playlist from Spotify");

        SpotifyPlaylistResponse response = spotifyApi.getPlaylistById(id, bearerToken);

        if (ResponseStatus.SUCCESS.equals(response.getInfo().getStatus())) {
            LOGGER.info("Playlist retrieved.");
            return SpotifyPlaylistDTOParser.parse(response.getPlaylist());
        }

        LOGGER.error("Error retrieving playlist from Spotify: " + response.getInfo().getMessage());

        if (response.getInfo().getCode() == HttpStatus.NOT_FOUND.value()) {
            throw new PlaylistNotFoundException("Playlist not found: " + id);
        }

        if (response.getInfo().getCode() == HttpStatus.UNAUTHORIZED.value()) {
            LOGGER.error("Invalid token. Trying to authenticate again.");
            spotifyAuthService.authenticate();
            return getPlaylistById(id);
        }

        throw new GenericTempTunesException();

    }

}
