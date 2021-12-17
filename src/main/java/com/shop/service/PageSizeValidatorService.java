package com.shop.service;

public class PageSizeValidatorService {

    public Integer validate(Integer userPageSize, Integer configMinSize, Integer configMaxSize){

        if (userPageSize < configMinSize) {
            return configMinSize;
        }
        if (userPageSize > configMaxSize) {
            return configMaxSize;
        }
        return userPageSize;
    }
}
