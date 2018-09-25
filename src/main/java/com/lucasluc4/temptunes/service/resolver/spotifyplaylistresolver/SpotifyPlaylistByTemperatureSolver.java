package com.lucasluc4.temptunes.service.resolver.spotifyplaylistresolver;

import com.lucasluc4.temptunes.enums.SpotifyPlaylist;

public interface SpotifyPlaylistByTemperatureSolver {
    boolean isTemperatureCorrect(Double temperature);
    SpotifyPlaylist getPlaylist();
}
