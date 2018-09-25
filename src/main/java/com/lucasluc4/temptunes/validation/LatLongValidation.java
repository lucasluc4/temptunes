package com.lucasluc4.temptunes.validation;

import com.lucasluc4.temptunes.exception.InvalidLatLongException;

public class LatLongValidation implements Validation {

    private final static String errorMessage = "Latitude shall not be null and its value should be between -90 and 90. " +
            "Longitude shall not be null and its value should be between -180 and 180.";

    private Double lat;
    private Double lng;

    public LatLongValidation(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public void validate() {
        if (isInvalidLat() || isInvalidLng()) {
            throw new InvalidLatLongException(errorMessage);
        }
    }

    private boolean isInvalidLat() {
        return lat == null || lat > 90 || lat < -90;
    }

    private boolean isInvalidLng() {
        return lng == null || lng > 180 || lng < -180;
    }
}
