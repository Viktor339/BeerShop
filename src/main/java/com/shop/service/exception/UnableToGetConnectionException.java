package com.shop.service.exception;

public class UnableToGetConnectionException extends RuntimeException {
    public UnableToGetConnectionException(String message) {
        super(message);
    }
}
