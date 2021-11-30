package com.shop.service.exception;

public class UnacceptableDatabaseDriverException extends RuntimeException{
    public UnacceptableDatabaseDriverException(String message) {
        super(message);
    }
}
