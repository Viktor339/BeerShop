package com.shop.service.exception;

public class AvailableQuantityExceededException extends RuntimeException{
    public AvailableQuantityExceededException(String message) {
        super(message);
    }
}
