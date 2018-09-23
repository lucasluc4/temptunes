package com.lucasluc4.temptunes.controllers;

import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.service.PlaylistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaylistsController {

    private PlaylistsService playlistsService;

    @Autowired
    public PlaylistsController (PlaylistsService playlistsService) {
        this.playlistsService = playlistsService;
    }

    @GetMapping(value = "/playlists/city/{cityName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Playlist getPlaylistsByCity(@PathVariable("cityName") String cityName) {

        return playlistsService.getByCity(cityName);
    }

    @GetMapping(value = "/playlists/lat/{lat}/lng/{lng}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Playlist getPlaylistsByCity(@PathVariable("lat") Double lat, @PathVariable("lng") Double lng) {

        return playlistsService.getByLatLng(lat, lng);
    }




}
