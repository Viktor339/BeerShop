package com.shop.service;

import com.shop.servlet.Action;
import com.shop.servlet.LoginAction;
import com.shop.servlet.RegistrationAction;
import com.shop.servlet.request.LoginRequest;
import com.shop.servlet.request.RegistrationRequest;
import com.shop.servlet.request.Request;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStreamReader;

public class JSONParseService {

    public Request parseFromJson(InputStreamReader inputStreamReader, Action action) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = null;
        if (action instanceof RegistrationAction) {
            request = objectMapper.readValue(inputStreamReader, RegistrationRequest.class);
        }
        if (action instanceof LoginAction) {
            request = objectMapper.readValue(inputStreamReader, LoginRequest.class);
        }
        return request;
    }

}
