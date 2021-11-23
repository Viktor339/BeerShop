package com.shop.service.exception;

import lombok.Getter;

@Getter
public class IncorrectEmailException extends RuntimeException {
    private final int errorCode;

    public IncorrectEmailException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
