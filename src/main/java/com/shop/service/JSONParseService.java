package com.shop.service;

import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class JSONParseService {
    private final ObjectMapper objectMapper;

    public  <T> T parseFromJson(InputStream is, Class<T> clazz) throws IOException {
        return objectMapper.readValue(is, clazz);
    }
}
