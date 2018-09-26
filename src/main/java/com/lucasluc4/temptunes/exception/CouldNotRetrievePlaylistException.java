package com.lucasluc4.temptunes.exception;

public class CouldNotRetrievePlaylistException extends TempTunesException {

    public CouldNotRetrievePlaylistException(String message) {
        super(message);
    }

    @Override
    public String getErrorTitle() {
        return TempTunesError.COULD_NOT_RETRIEVE_PLAYLIST.getTitle();
    }
}
