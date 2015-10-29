package com.intuit.idea.chopsticks.utils.exceptions;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/22/15
 * ************************************
 */
public class QueryCreationWarning extends RuntimeException {
    public QueryCreationWarning() {
    }

    public QueryCreationWarning(String message) {
        super(message);
    }

    public QueryCreationWarning(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryCreationWarning(Throwable cause) {
        super(cause);
    }

    public QueryCreationWarning(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
