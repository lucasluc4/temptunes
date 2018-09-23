package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.thirdparty.dto.OpenWeatherDTO;
import feign.Param;
import feign.RequestLine;

public interface OpenWeatherApi {

    @RequestLine("GET /weather?q={cityName}&APPID=682dee6999740b69481061da544a257c")
    OpenWeatherDTO getWeatherByCityName(@Param("cityName") String cityName);

    @RequestLine("GET /weather?lat={lat}&lon={lng}&APPID=682dee6999740b69481061da544a257c")
    OpenWeatherDTO getWeatherByLatLng(@Param("lat") Double lat, @Param("lng") Double lng);
}
