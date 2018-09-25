package com.lucasluc4.temptunes.exception;

public enum TempTunesError {
    UNKNOWN("error.unknown"),
    INVALID_CITY_NAME("error.invalid.city.name"),
    INVALID_LAT_LONG("error.invalid.lat.long");

    private String title;



    TempTunesError(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
