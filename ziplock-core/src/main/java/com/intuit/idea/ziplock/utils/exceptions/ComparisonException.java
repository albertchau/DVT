package com.intuit.idea.ziplock.utils.exceptions;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class ComparisonException extends Exception {

    public ComparisonException() {
    }

    public ComparisonException(String message) {
        super(message);
    }

    public ComparisonException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComparisonException(Throwable cause) {
        super(cause);
    }

    public ComparisonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
