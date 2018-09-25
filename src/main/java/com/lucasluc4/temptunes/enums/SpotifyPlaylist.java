package com.lucasluc4.temptunes.enums;

public enum SpotifyPlaylist {

    PARTY("37i9dQZF1DX9EM98aZosoy"),
    POP("37i9dQZF1DX6aTaZa0K6VA"),
    ROCK("37i9dQZF1DXcF6B6QPhFDv"),
    CLASSICAL("37i9dQZF1DWWEJlAGA9gs0");

    private final String id;

    SpotifyPlaylist(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
