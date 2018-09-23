package com.lucasluc4.temptunes.thirdparty.dto.parser;

import com.lucasluc4.temptunes.model.Weather;
import com.lucasluc4.temptunes.thirdparty.dto.OpenWeatherDTO;

public class OpenWeatherDTOParser {

    private OpenWeatherDTOParser () {
        super();
    }

    public static Weather parse (OpenWeatherDTO openWeatherDTO) {
        Weather weather = new Weather();
        weather.setTemperature(openWeatherDTO.getMain().getTemp());
        return weather;
    }

}
