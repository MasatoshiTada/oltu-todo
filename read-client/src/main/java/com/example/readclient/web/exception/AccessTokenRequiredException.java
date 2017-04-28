package com.example.readclient.web.exception;

public class AccessTokenRequiredException extends RuntimeException {
    public AccessTokenRequiredException(String message) {
        super(message);
    }
}
