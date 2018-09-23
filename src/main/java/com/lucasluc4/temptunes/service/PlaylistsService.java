package com.lucasluc4.temptunes.service;

import com.lucasluc4.temptunes.model.Playlist;
import org.springframework.stereotype.Service;

@Service
public class PlaylistsService {

    public Playlist getByCity(String cityName) {

        Playlist playlist = new Playlist();
        playlist.setName(cityName);

        return playlist;
    }

    public Playlist getByLatLng(Double lat, Double lng) {

        Playlist playlist = new Playlist();
        playlist.setName(lat + "," + lng);

        return playlist;
    }
}
