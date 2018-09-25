package com.lucasluc4.temptunes.service;

import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeoPlaylistsService {

    private WeatherService weatherService;
    private PlaylistTemperatureService playlistTemperatureService;

    @Autowired
    public GeoPlaylistsService(WeatherService weatherService, PlaylistTemperatureService playlistTemperatureService) {

        this.weatherService = weatherService;
        this.playlistTemperatureService = playlistTemperatureService;
    }

    public Playlist getByCity(String cityName) {
        Weather weather = weatherService.getByCity(cityName);
        return playlistTemperatureService.getByTemperature(weather.getTemperature());
    }

    public Playlist getByLatLng(Double lat, Double lng) {
        Weather weather = weatherService.getByLatLng(lat, lng);
        return playlistTemperatureService.getByTemperature(weather.getTemperature());
    }

}
