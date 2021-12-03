package com.shop.service.exception;

public class PositionAlreadyExistsException extends RuntimeException{
    public PositionAlreadyExistsException(String message) {
        super(message);
    }
}
