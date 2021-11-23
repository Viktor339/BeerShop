package com.shop.service.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final int errorCode;

    public UserAlreadyExistsException(String message,int errorCode) {
        super(message);
        this.errorCode=errorCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
