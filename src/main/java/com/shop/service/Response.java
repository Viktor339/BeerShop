package com.shop.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@NoArgsConstructor
@Data
public class Response {
    public static final String HEADER_TYPE = "Content-Type";
    public static final String CONTENT_TYPE = "application/json";
     Map<String, String> message;
    private int code;


    public void send(HttpServletResponse resp, Map<String, String> message, int code) throws IOException {
        responseBuilder(resp, message, code);
    }

    private void responseBuilder(HttpServletResponse resp, Map<String, String> message, int code) throws IOException {

        String json = new ObjectMapper().writeValueAsString(message);
        resp.resetBuffer();
        resp.setStatus(code);
        resp.setHeader(HEADER_TYPE, CONTENT_TYPE);
        resp.getOutputStream().print(json);
        resp.flushBuffer();
    }
}
