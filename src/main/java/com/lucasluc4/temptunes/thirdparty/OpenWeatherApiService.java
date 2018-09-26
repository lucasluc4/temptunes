package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.exception.CityNotFoundException;
import com.lucasluc4.temptunes.exception.GenericTempTunesException;
import com.lucasluc4.temptunes.model.Weather;
import com.lucasluc4.temptunes.thirdparty.dto.OpenWeatherDTO;
import com.lucasluc4.temptunes.thirdparty.dto.parser.OpenWeatherDTOParser;
import com.lucasluc4.temptunes.utils.FeingBuilderUtil;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class OpenWeatherApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherApiService.class);

    private static final String OPEN_WEATHER_API_URL = "openweather.url.api";
    private static final String OPEN_WEATHER_API_KEY = "openweather.api.key";

    private OpenWeatherApi openWeatherApi;

    private Environment environment;

    @Autowired
    public OpenWeatherApiService(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init () {
        openWeatherApi = new FeingBuilderUtil<>(OpenWeatherApi.class)
                .build(environment.getProperty(OPEN_WEATHER_API_URL));
    }

    public Weather getWeatherByCity (String city) {

        try {
            OpenWeatherDTO dto = openWeatherApi.getWeatherByCityName(city, environment.getProperty(OPEN_WEATHER_API_KEY));

            LOGGER.info("Weather found!");

            return OpenWeatherDTOParser.parse(dto);
        } catch (FeignException e) {

            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                throw new CityNotFoundException("City " + city + " not found.");
            }

            throw new GenericTempTunesException();
        }

    }

    public Weather getWeatherByLatLng(Double lat, Double lng) {
        OpenWeatherDTO dto = openWeatherApi.getWeatherByLatLng(lat, lng, environment.getProperty(OPEN_WEATHER_API_KEY));

        LOGGER.info("Weather found!");

        return OpenWeatherDTOParser.parse(dto);
    }
}
