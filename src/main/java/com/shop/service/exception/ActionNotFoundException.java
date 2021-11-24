package com.shop.service.exception;

import lombok.Getter;

@Getter
public class ActionNotFoundException extends RuntimeException {
    private final int errorCode;

    public ActionNotFoundException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
