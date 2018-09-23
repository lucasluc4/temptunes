package com.lucasluc4.temptunes.service;

import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistsService {

    private WeatherService weatherService;

    @Autowired
    public PlaylistsService (WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public Playlist getByCity(String cityName) {

        Weather weather = weatherService.getByCity(cityName);

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
