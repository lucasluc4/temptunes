package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.model.Weather;
import com.lucasluc4.temptunes.thirdparty.dto.OpenWeatherDTO;
import com.lucasluc4.temptunes.thirdparty.dto.parser.OpenWeatherDTOParser;
import com.lucasluc4.temptunes.utils.FeingBuilderUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class OpenWeatherApiService {

    private OpenWeatherApi openWeatherApi;

    @PostConstruct
    public void init () {
        openWeatherApi = new FeingBuilderUtil<>(OpenWeatherApi.class)
                .build("https://api.openweathermap.org/data/2.5");
    }

    public Weather getWeatherByCity (String city) {
        OpenWeatherDTO dto = openWeatherApi.getWeatherByCityName(city);
        return OpenWeatherDTOParser.parse(dto);
    }

    public Weather getWeatherByLatLng(Double lat, Double lng) {
        OpenWeatherDTO dto = openWeatherApi.getWeatherByLatLng(lat, lng);
        return OpenWeatherDTOParser.parse(dto);
    }
}
