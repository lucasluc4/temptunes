package com.lucasluc4.temptunes.exception;

public class InvalidLatLongException extends ValidationException {

    public InvalidLatLongException(String message) {
        super(message);
    }

    @Override
    public String getErrorTitle() {
        return TempTunesError.INVALID_LAT_LONG.getTitle();
    }
}
