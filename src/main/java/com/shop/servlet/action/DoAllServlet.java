package com.shop.servlet.action;

import com.shop.repository.UserRepository;
import com.shop.service.LoginService;
import com.shop.service.RegistrationService;
import com.shop.service.Response;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.service.message.InfoMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class DoAllServlet extends HttpServlet {
    private List<Action> actions;

    @Override
    public void init() throws ServletException {
        UserRepository userRepository = new UserRepository();
        super.init();
        actions = Arrays.asList(
                new RegistrationAction(new RegistrationService(userRepository)),
                new LoginAction(new LoginService(userRepository))
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            final Action action = actions.stream()
                    .filter(act -> act.isValid(req))
                    .findFirst()
                    .get();
            action.doAction(req, resp);
        } catch (NoSuchElementException |
                UnableToExecuteQueryException |
                UnableToGetConnectionException e) {
            new Response().send(resp, new InfoMessage().build(e.getMessage()), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
