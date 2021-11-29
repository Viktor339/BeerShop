package com.shop.service.exception;

public class UnableToExecuteQueryException extends RuntimeException{
    public UnableToExecuteQueryException(String message) {
        super(message);
    }
}
