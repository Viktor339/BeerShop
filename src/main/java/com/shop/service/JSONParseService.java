package com.shop.service;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JSONParseService {

    public  <T> T parseFromJson(InputStream is, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(is, clazz);
    }


}
