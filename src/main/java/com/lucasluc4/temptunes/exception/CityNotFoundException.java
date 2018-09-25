package com.lucasluc4.temptunes.exception;

public class CityNotFoundException extends NotFoundException {

    public CityNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getErrorTitle() {
        return TempTunesError.CITY_NOT_FOUND.getTitle();
    }
}
