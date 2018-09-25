package com.lucasluc4.temptunes.service.resolver.spotifyplaylistresolver;

import com.lucasluc4.temptunes.enums.SpotifyPlaylist;
import org.springframework.stereotype.Component;

@Component
public class FreezingPlaylistSolver implements SpotifyPlaylistByTemperatureSolver {

    @Override
    public boolean isTemperatureCorrect(Double temperature) {
        return temperature != null && temperature < 283.15;
    }

    @Override
    public SpotifyPlaylist getPlaylist() {
        return SpotifyPlaylist.CLASSICAL;
    }
}
