package com.lucasluc4.temptunes.controllers;

import com.lucasluc4.temptunes.model.Playlist;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaylistsController {

    @GetMapping(value = "/playlists", produces = MediaType.APPLICATION_JSON_VALUE)
    public Playlist getPlaylists() {

        Playlist playlist = new Playlist();
        playlist.setName("Hello, World!");
        playlist.setDescription("This is a example playlist. Congrats! You are reading my git history.");

        return playlist;
    }


}
