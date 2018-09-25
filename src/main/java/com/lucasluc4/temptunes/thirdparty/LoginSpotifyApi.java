package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.thirdparty.dto.SpotifyTokenDTO;
import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

public interface LoginSpotifyApi {

    @RequestLine("POST /token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    SpotifyTokenDTO getToken(@Param("grant_type") String grantType, @HeaderMap Map<String, Object> headerMap);

}
