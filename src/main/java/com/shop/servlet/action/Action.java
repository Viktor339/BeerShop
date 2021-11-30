package com.shop.servlet.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Action {
    boolean isValid(HttpServletRequest req);

    void doAction(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
