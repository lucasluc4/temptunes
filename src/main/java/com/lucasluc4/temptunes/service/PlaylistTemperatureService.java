package com.lucasluc4.temptunes.service;

import com.lucasluc4.temptunes.enums.SpotifyPlaylist;
import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.service.resolver.SpotifyPlaylistByTemperatureFactory;
import com.lucasluc4.temptunes.thirdparty.SpotifyApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistTemperatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistTemperatureService.class);

    private SpotifyApiService spotifyApiService;
    private SpotifyPlaylistByTemperatureFactory spotifyPlaylistByWeatherFactory;

    @Autowired
    public PlaylistTemperatureService(SpotifyApiService spotifyApiService,
                                      SpotifyPlaylistByTemperatureFactory spotifyPlaylistByWeatherFactory) {

        this.spotifyApiService = spotifyApiService;
        this.spotifyPlaylistByWeatherFactory = spotifyPlaylistByWeatherFactory;
    }

    public Playlist getByTemperature(Double temperature) {

        LOGGER.info("Retrieving playlist for temperature: " + temperature + "K");

        SpotifyPlaylist spotifyPlaylist = spotifyPlaylistByWeatherFactory.resolve(temperature);
        return spotifyApiService.getPlaylistById(spotifyPlaylist.getId());
    }
}
