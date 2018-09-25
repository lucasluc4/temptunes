package com.lucasluc4.temptunes.exception;

public abstract class TempTunesException extends RuntimeException {

    private String errorTitle;

    public TempTunesException() {
        this.errorTitle = getErrorTitle();
    }

    public TempTunesException(String message) {
        super(message);
        this.errorTitle = getErrorTitle();
    }

    public TempTunesException(String message, Throwable cause) {
        super(message, cause);
        this.errorTitle = getErrorTitle();
    }

    public TempTunesException(Throwable cause) {
        super(cause);
        this.errorTitle = getErrorTitle();
    }

    public TempTunesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorTitle = getErrorTitle();
    }

    public abstract String getErrorTitle();
}
