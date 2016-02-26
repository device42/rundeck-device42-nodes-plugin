package com.device42.client.util;

public class Device42ClientException extends RuntimeException {
    private static final long serialVersionUID = -3104582443334015826L;

    public Device42ClientException() {
        super();
    }

    public Device42ClientException(String message) {
        super(message);
    }

    public Device42ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public Device42ClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Device42ClientException(Throwable cause) {
        super(cause);
    }
}
