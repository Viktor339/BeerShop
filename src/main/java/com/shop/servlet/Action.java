package com.shop.servlet;

import com.shop.servlet.request.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
    boolean isValid(HttpServletRequest req);

    void doAction(HttpServletRequest req, HttpServletResponse resp, Request request);
}
