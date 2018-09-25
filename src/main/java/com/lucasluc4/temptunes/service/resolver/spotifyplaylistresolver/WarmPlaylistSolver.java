package com.lucasluc4.temptunes.service.resolver.spotifyplaylistresolver;

import com.lucasluc4.temptunes.enums.SpotifyPlaylist;
import org.springframework.stereotype.Component;

@Component
public class WarmPlaylistSolver implements SpotifyPlaylistByTemperatureSolver {

    @Override
    public boolean isTemperatureCorrect(Double temperature) {
        return temperature != null && temperature < 303.15 && temperature >= 288.15;
    }

    @Override
    public SpotifyPlaylist getPlaylist() {
        return SpotifyPlaylist.POP;
    }
}
