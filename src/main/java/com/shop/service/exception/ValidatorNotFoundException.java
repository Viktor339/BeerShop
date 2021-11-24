package com.shop.service.exception;

import lombok.Getter;

@Getter
public class ValidatorNotFoundException extends RuntimeException{
    private final int errorCode;

    public ValidatorNotFoundException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
