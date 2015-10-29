package com.intuit.idea.ziplock.utils.exceptions;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/22/15
 * ************************************
 */
public class QueryCreationError extends Error {
    public QueryCreationError() {
    }

    public QueryCreationError(String message) {
        super(message);
    }

    public QueryCreationError(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryCreationError(Throwable cause) {
        super(cause);
    }

    public QueryCreationError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
