package com.lucasluc4.temptunes.service;

import com.lucasluc4.temptunes.thirdparty.SpotifyApiService;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistTemperatureService {

    private SpotifyApiService spotifyApiService;

    @Autowired
    public PlaylistTemperatureService(SpotifyApiService spotifyApiService) {
        this.spotifyApiService = spotifyApiService;
    }

    public SpotifyPlaylistDTO test () {
        return spotifyApiService.getPlaylistById("7kPPQySvl3eF0SnqltYjVN");
    }

}
