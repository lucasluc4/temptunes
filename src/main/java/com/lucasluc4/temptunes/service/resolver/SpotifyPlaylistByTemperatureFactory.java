package com.lucasluc4.temptunes.service.resolver;

import com.lucasluc4.temptunes.enums.SpotifyPlaylist;
import com.lucasluc4.temptunes.exception.PlaylistNotFoundException;
import com.lucasluc4.temptunes.service.resolver.spotifyplaylistresolver.SpotifyPlaylistByTemperatureSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpotifyPlaylistByTemperatureFactory {

    private List<SpotifyPlaylistByTemperatureSolver> solvers;

    @Autowired
    public SpotifyPlaylistByTemperatureFactory(List<SpotifyPlaylistByTemperatureSolver> solvers) {
        this.solvers = solvers;
    }

    public SpotifyPlaylist resolve(Double temperature) {

        for (SpotifyPlaylistByTemperatureSolver solver : solvers) {
            if (solver.isTemperatureCorrect(temperature)) {
                return solver.getPlaylist();
            }
        }

        throw new PlaylistNotFoundException("Playlist not found for temperature: " + temperature);
    }
}
