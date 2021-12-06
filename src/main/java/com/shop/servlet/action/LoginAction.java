package com.shop.servlet.action;

import com.shop.model.User;
import com.shop.service.JSONParseService;
import com.shop.service.LoginService;
import com.shop.service.Response;
import com.shop.service.exception.UserNotFoundException;
import com.shop.service.exception.ValidatorException;
import com.shop.servlet.dto.InformationResponse;
import com.shop.servlet.request.LoginRequest;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class LoginAction implements Action {
    private final LoginService loginService;
    private final JSONParseService jsonParseService;
    private final Response response;

    @Override
    public boolean isValid(HttpServletRequest req) {

        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.equals("/login");
    }

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LoginRequest loginRequest = jsonParseService.parseFromJson(req.getInputStream(), LoginRequest.class);

        try {
            User user = loginService.login(loginRequest);

            if (user.getRole().equals("user")) {
                req.getSession().setAttribute("role", "user");
                req.getSession().setAttribute("UUID", user.getUUID());
                response.send(resp, new InformationResponse("You have successfully logged in like user"), HttpServletResponse.SC_OK);
            }

            if (user.getRole().equals("admin")) {
                req.getSession().setAttribute("role", "admin");
                response.send(resp, new InformationResponse("You have successfully logged in like admin"), HttpServletResponse.SC_OK);
            }

        } catch (UserNotFoundException |
                ValidatorException e) {
            response.send(resp, new InformationResponse(e.getMessage()), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
