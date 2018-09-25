package com.lucasluc4.temptunes.validation;

import com.lucasluc4.temptunes.exception.InvalidCityNameException;

public class CityNameValidation implements Validation {

    private static final String errorMessage = "City name shall not be null or empty";

    private String cityname;

    public CityNameValidation(String cityname) {
        this.cityname = cityname;
    }

    @Override
    public void validate() {
        if (cityname == null || cityname.isEmpty()) {
            throw new InvalidCityNameException(errorMessage);
        }
    }
}
