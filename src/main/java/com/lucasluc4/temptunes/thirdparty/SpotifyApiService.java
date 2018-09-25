package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyTokenDTO;
import com.lucasluc4.temptunes.thirdparty.dto.parser.SpotifyPlaylistDTOParser;
import com.lucasluc4.temptunes.utils.FeingBuilderUtil;
import feign.form.FormEncoder;
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

        loginSpotifyApi = new FeingBuilderUtil<>(LoginSpotifyApi.class)
                .build("https://accounts.spotify.com/api", new FormEncoder());
    }

    public Playlist getPlaylistById (String id) {
        if (bearerToken == null) {
            authenticate();
        }

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + bearerToken);

        SpotifyPlaylistDTO spotifyPlaylistDTO = spotifyApi.getPlaylistById(id, headerMap);
        return SpotifyPlaylistDTOParser.parse(spotifyPlaylistDTO);
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
