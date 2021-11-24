package com.shop.service.exception;

import lombok.Getter;

@Getter
public class UnableToGetConnectionException extends RuntimeException {
    private final int errorCode;

    public UnableToGetConnectionException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
