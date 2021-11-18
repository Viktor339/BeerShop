package com.shop.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseMessage {

    public void send(HttpServletResponse resp, String message,int code) throws IOException {
        resp.resetBuffer();
        resp.setStatus(code);
        resp.setHeader("Content-Type", "application/json");
        resp.getOutputStream().print("{\"message\":\"" + message + "\"}");
        resp.flushBuffer();

    }
}
