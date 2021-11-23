package com.shop.service;

import com.shop.servlet.request.Request;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JSONParseService {

    public Request parseFromJson(InputStream is, Class<?> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = null;
        request = (Request) objectMapper.readValue(is, clazz);
        return request;
    }


}
