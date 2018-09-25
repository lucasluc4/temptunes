package com.lucasluc4.temptunes.exception;

public class PlaylistNotFoundException extends NotFoundException {

    public PlaylistNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getErrorTitle() {
        return TempTunesError.PLAYLIST_NOT_FOUND.getTitle();
    }
}
