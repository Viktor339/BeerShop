package com.shop.service.exception;

public class AvailableQuantityExceeded extends RuntimeException{
    public AvailableQuantityExceeded(String message) {
        super(message);
    }
}
