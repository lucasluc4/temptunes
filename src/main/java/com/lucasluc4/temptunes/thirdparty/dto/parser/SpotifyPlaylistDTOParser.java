package com.lucasluc4.temptunes.thirdparty.dto.parser;

import com.lucasluc4.temptunes.model.Playlist;
import com.lucasluc4.temptunes.model.Song;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;

import java.util.stream.Collectors;

public class SpotifyPlaylistDTOParser {

    private SpotifyPlaylistDTOParser () {
        super();
    }

    public static Playlist parse (SpotifyPlaylistDTO spotifyPlaylistDTO) {

        Playlist playlist = new Playlist();
        playlist.setName(spotifyPlaylistDTO.getName());
        playlist.setDescription(spotifyPlaylistDTO.getDescription());

        playlist.setSongs(spotifyPlaylistDTO.getTracks().getItems()
                .stream()
                .map(item -> new Song(item.getTrack().getName()))
                .collect(Collectors.toList()));

        return playlist;
    }

}
