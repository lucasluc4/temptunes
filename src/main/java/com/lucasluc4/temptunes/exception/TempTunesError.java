package com.lucasluc4.temptunes.exception;

public enum TempTunesError {
    UNKNOWN("error.unknown"),
    INVALID_CITY_NAME("error.invalid.city.name"),
    INVALID_LAT_LONG("error.invalid.lat.long"),
    CITY_NOT_FOUND("error.city.not.found"),
    PLAYLIST_NOT_FOUND("error.playlist.not.found");

    private String title;



    TempTunesError(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
