package com.shop.service.exception;

import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class ActionNotFoundException extends NoSuchElementException {
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
