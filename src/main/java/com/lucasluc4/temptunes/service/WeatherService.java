package com.lucasluc4.temptunes.service;

import com.lucasluc4.temptunes.model.Weather;
import com.lucasluc4.temptunes.thirdparty.OpenWeatherApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private OpenWeatherApiService openWeatherApiService;

    @Autowired
    public WeatherService (OpenWeatherApiService openWeatherApiService) {
        this.openWeatherApiService = openWeatherApiService;
    }

    public Weather getByCity (String city) {
        return openWeatherApiService.getWeatherByCity(city);
    }

    public Weather getByLatLng (Double lat, Double lng) {
        return openWeatherApiService.getWeatherByLatLng(lat, lng);
    }

}
