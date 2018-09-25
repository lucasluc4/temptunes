package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyTokenDTO;
import com.lucasluc4.temptunes.utils.FeingBuilderUtil;
import feign.Feign;
import feign.Logger;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyApiService {

    private SpotifyApi spotifyApi;

    private LoginSpotifyApi loginSpotifyApi;

    private String bearerToken;

    @PostConstruct
    public void init () {
        spotifyApi = new FeingBuilderUtil<>(SpotifyApi.class)
                .build("https://api.spotify.com/v1");

        loginSpotifyApi = Feign.builder().encoder(new FormEncoder())
                .decoder(new JacksonDecoder())
                .logLevel(Logger.Level.FULL)
                .target(LoginSpotifyApi.class, "https://accounts.spotify.com/api");
//                new FeingBuilderUtil<>(LoginSpotifyApi.class).build("https://accounts.spotify.com/api");
    }

    public SpotifyPlaylistDTO getPlaylistById (String id) {

        if (bearerToken == null) {
            authenticate();
        }

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + bearerToken);

        //BQBKV0ucJdwZDrEWILaTTl8suGcSRFhq0WERCk5xFe-U2qsV21s_YCqsyvjD6T3l4UZeppO0teoXKPz6idI

        return spotifyApi.getPlaylistById(id, headerMap);
    }

    private void authenticate() {
        String clientId = "7922f406cc7d4f97816e7bc57dcb19bd";
        String clientSecret = "897d072d200443208293b435b7906993";

        String basicAuthHeader = clientId + ":" + clientSecret;
        String encodedBasicAuthHeader = new String(new Base64().encode(basicAuthHeader.getBytes()));

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Basic " + encodedBasicAuthHeader);

        SpotifyTokenDTO response = loginSpotifyApi.getToken("client_credentials", headerMap);

        bearerToken = response.getAccess_token();
    }

}
