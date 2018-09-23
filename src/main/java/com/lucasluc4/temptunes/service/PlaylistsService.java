package com.lucasluc4.temptunes.service;

import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.model.Weather;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistsService {

    private WeatherService weatherService;
    private PlaylistTemperatureService playlistTemperatureService;

    @Autowired
    public PlaylistsService (WeatherService weatherService, PlaylistTemperatureService playlistTemperatureService) {
        this.weatherService = weatherService;
        this.playlistTemperatureService = playlistTemperatureService;
    }

    public Playlist getByCity(String cityName) {

//        Weather weather = weatherService.getByCity(cityName);

        SpotifyPlaylistDTO spotifyPlaylistDTO = playlistTemperatureService.test();

        Playlist playlist = new Playlist();
        playlist.setName(cityName);

        return playlist;
    }

    public Playlist getByLatLng(Double lat, Double lng) {

        Playlist playlist = new Playlist();
        playlist.setName(lat + "," + lng);

        return playlist;
    }
}
