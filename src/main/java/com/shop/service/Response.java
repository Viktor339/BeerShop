package com.shop.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
@RequiredArgsConstructor
public class Response {
    public static final String HEADER_TYPE = "Content-Type";
    public static final String CONTENT_TYPE = "application/json";
    private final ObjectMapper objectMapper;


    public void send(HttpServletResponse resp, Object dto, int code) throws IOException {
        responseBuilder(resp, dto, code);
    }

    private void responseBuilder(HttpServletResponse resp, Object dto, int code) throws IOException {

        String json = objectMapper.writeValueAsString(dto);
        resp.resetBuffer();
        resp.setStatus(code);
        resp.setHeader(HEADER_TYPE, CONTENT_TYPE);
        resp.getOutputStream().print(json);
        resp.flushBuffer();
    }
}
