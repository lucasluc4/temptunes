package com.lucasluc4.temptunes.service.resolver;

import com.lucasluc4.temptunes.enums.SpotifyPlaylist;
import com.lucasluc4.temptunes.service.resolver.spotifyplaylistresolver.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SpotifyPlaylistByTemperatureFactoryTest {

    private SpotifyPlaylistByTemperatureFactory spotifyPlaylistByTemperatureFactory;

    @Before
    public void init () {

        WarmPlaylistSolver warmPlaylistSolver = new WarmPlaylistSolver();
        ChillyPlaylistSolver chillyPlaylistSolver = new ChillyPlaylistSolver();
        HotPlaylistSolver hotPlaylistSolver = new HotPlaylistSolver();
        FreezingPlaylistSolver freezingPlaylistSolver = new FreezingPlaylistSolver();

        List<SpotifyPlaylistByTemperatureSolver> list = new ArrayList();
        list.add(warmPlaylistSolver);
        list.add(chillyPlaylistSolver);
        list.add(hotPlaylistSolver);
        list.add(freezingPlaylistSolver);

        spotifyPlaylistByTemperatureFactory = new SpotifyPlaylistByTemperatureFactory(list);
    }

    @Test
    public void returnPartyPlaylistForHotTemperature() {
        SpotifyPlaylist playlist = spotifyPlaylistByTemperatureFactory.resolve(305.0);
        assertTrue(SpotifyPlaylist.PARTY.equals(playlist));
    }

    @Test
    public void returnPartyPlaylistForWarmTemperature() {
        SpotifyPlaylist playlist = spotifyPlaylistByTemperatureFactory.resolve(290.0);
        assertTrue(SpotifyPlaylist.POP.equals(playlist));
    }

    @Test
    public void returnPartyPlaylistForChillyTemperature() {
        SpotifyPlaylist playlist = spotifyPlaylistByTemperatureFactory.resolve(285.0);
        assertTrue(SpotifyPlaylist.ROCK.equals(playlist));
    }

    @Test
    public void returnPartyPlaylistForFreezingTemperature() {
        SpotifyPlaylist playlist = spotifyPlaylistByTemperatureFactory.resolve(280.0);
        assertTrue(SpotifyPlaylist.CLASSICAL.equals(playlist));
    }

}
