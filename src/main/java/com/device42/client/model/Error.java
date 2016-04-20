package com.device42.client.model;

public class Error {
    private final String message;
    private final int code;

    public Error(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Error [message=" + message + ", code=" + code + "]";
    }
}
