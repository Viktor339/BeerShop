package com.shop.service;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor
@Data
public class ResponseMessage {
    public static final String HEADER_TYPE = "Content-Type";
    public static final String CONTENT_TYPE = "application/json";
    private String message;
    private int code;
    private String resultMessage = "{\"message\":\"";


    public ResponseMessage(String message, int code) {
        this.message = message;
        this.code = code;

    }

    public void send(HttpServletResponse resp) throws IOException {
        build(resp,message,code);
    }

    public void send(HttpServletResponse resp, String message, int code) throws IOException {
        build(resp,message, code);
    }

    private void build(HttpServletResponse resp,String message, int code) throws IOException {
        resp.resetBuffer();
        resp.setStatus(code);
        resp.setHeader(HEADER_TYPE, CONTENT_TYPE);
        resp.getOutputStream().print(resultMessage + message + "\"}");
        resp.flushBuffer();
    }
}
