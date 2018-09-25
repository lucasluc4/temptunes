package com.lucasluc4.temptunes.exception;

public class GenericTempTunesException extends TempTunesException {
    @Override
    public String getErrorTitle() {
        return TempTunesError.UNKNOWN.getTitle();
    }
}
