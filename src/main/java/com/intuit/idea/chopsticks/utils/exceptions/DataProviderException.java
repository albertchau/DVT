package com.intuit.idea.chopsticks.utils.exceptions;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class DataProviderException extends RuntimeException {
    public DataProviderException() {
    }

    public DataProviderException(String message) {
        super(message);
    }

    public DataProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataProviderException(Throwable cause) {
        super(cause);
    }

    public DataProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
