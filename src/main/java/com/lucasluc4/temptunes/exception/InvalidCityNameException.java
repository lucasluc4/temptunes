package com.lucasluc4.temptunes.exception;

public class InvalidCityNameException extends ValidationException {

    public InvalidCityNameException(String message) {
        super(message);
    }

    @Override
    public String getErrorTitle() {
        return TempTunesError.INVALID_CITY_NAME.getTitle();
    }
}
