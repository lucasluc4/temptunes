package com.lucasluc4.temptunes.controllers;

import com.lucasluc4.temptunes.response.RestResponse;
import com.lucasluc4.temptunes.service.GeoPlaylistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaylistsController {

    private GeoPlaylistsService geoPlaylistsService;

    @Autowired
    public PlaylistsController (GeoPlaylistsService geoPlaylistsService) {
        this.geoPlaylistsService = geoPlaylistsService;
    }

    @GetMapping(value = "/playlists/city/{cityName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse getPlaylistsByCity(@PathVariable("cityName") String cityName) {

        return new RestResponse(HttpStatus.OK.value(), null, geoPlaylistsService.getByCity(cityName));
    }

    @GetMapping(value = "/playlists/lat/{lat}/lng/{lng}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse getPlaylistsByCity(@PathVariable("lat") Double lat, @PathVariable("lng") Double lng) {

        return new RestResponse(HttpStatus.OK.value(), null, geoPlaylistsService.getByLatLng(lat, lng));
    }

}
