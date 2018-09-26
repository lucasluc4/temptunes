package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.thirdparty.dto.OpenWeatherDTO;
import feign.Param;
import feign.RequestLine;

public interface OpenWeatherApi {

    @RequestLine("GET /weather?q={cityName}&APPID={apiKey}")
    OpenWeatherDTO getWeatherByCityName(@Param("cityName") String cityName, @Param("apiKey") String apiKey);

    @RequestLine("GET /weather?lat={lat}&lon={lng}&APPID={apiKey}")
    OpenWeatherDTO getWeatherByLatLng(@Param("lat") Double lat, @Param("lng") Double lng, @Param("apiKey") String apiKey);
}
