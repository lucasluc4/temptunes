package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import com.lucasluc4.temptunes.utils.FeingBuilderUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyApiService {

    private SpotifyApi spotifyApi;

    @PostConstruct
    public void init () {
        spotifyApi = new FeingBuilderUtil<>(SpotifyApi.class)
                .build("https://api.spotify.com/v1");
    }

    public SpotifyPlaylistDTO getPlaylistById (String id) {

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer BQBKV0ucJdwZDrEWILaTTl8suGcSRFhq0WERCk5xFe-U2qsV21s_YCqsyvjD6T3l4UZeppO0teoXKPz6idI");

        return spotifyApi.getPlaylistById(id, headerMap);
    }

}
