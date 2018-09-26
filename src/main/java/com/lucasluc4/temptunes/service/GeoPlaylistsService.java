package com.lucasluc4.temptunes.service;

import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.model.Weather;
import com.lucasluc4.temptunes.validation.CityNameValidation;
import com.lucasluc4.temptunes.validation.LatLongValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeoPlaylistsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoPlaylistsService.class);

    private WeatherService weatherService;
    private PlaylistTemperatureService playlistTemperatureService;

    @Autowired
    public GeoPlaylistsService(WeatherService weatherService, PlaylistTemperatureService playlistTemperatureService) {

        this.weatherService = weatherService;
        this.playlistTemperatureService = playlistTemperatureService;
    }

    public Playlist getByCity(String cityName) {

        LOGGER.info("Retrieving current playlist for city: " + cityName);

        new CityNameValidation(cityName).validate();

        Weather weather = weatherService.getByCity(cityName);
        return playlistTemperatureService.getByTemperature(weather.getTemperature());
    }

    public Playlist getByLatLng(Double lat, Double lng) {

        LOGGER.info("Retrieving current playlist for lat/long: " + lat + "/" + lng);

        new LatLongValidation(lat, lng).validate();

        Weather weather = weatherService.getByLatLng(lat, lng);
        return playlistTemperatureService.getByTemperature(weather.getTemperature());
    }

}
