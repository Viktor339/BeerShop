package com.shop.service;

import com.shop.service.exception.ValidatorException;

public class PageService {

    public Integer getSize(Integer userPageSize, Integer configMinSize, Integer configMaxSize) {

        if (userPageSize < configMinSize) {
            return configMinSize;
        }
        if (userPageSize > configMaxSize) {
            return configMaxSize;
        }
        return userPageSize;
    }

    public void validatePage(Integer page) {
        if (page <= 0) {
            throw new ValidatorException("Invalid page");
        }
    }

}
