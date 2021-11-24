package com.shop.service;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestBuilderService {

    public <T> T build(HttpServletRequest request, Class<T> clazz) throws IOException {
        final ServletInputStream inputStream = request.getInputStream();
        return new JSONParseService().parseFromJson(inputStream, clazz);
    }
}
