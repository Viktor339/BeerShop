package com.shop.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseSenderService {
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";

    public void send(HttpServletResponse resp, String object) throws IOException {

        PrintWriter out = resp.getWriter();
        resp.setContentType(CONTENT_TYPE);
        resp.setCharacterEncoding(CHARACTER_ENCODING);

        out.print(object);
        out.flush();

    }
}
