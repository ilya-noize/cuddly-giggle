package org.shummi.mvc.exception;

public class MvpException extends RuntimeException {
    public MvpException() {
    }

    public MvpException(String message) {
        super(message);
    }

    public MvpException(String message, Throwable cause) {
        super(message, cause);
    }

    public MvpException(Throwable cause) {
        super(cause);
    }

    public MvpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
