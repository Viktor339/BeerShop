package com.shop.servlet.action;

import com.shop.service.Response;
import com.shop.service.message.InfoMessage;
import com.shop.service.message.Message;

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
        super.init();
        actions = Arrays.asList(
                new RegistrationAction(),
                new LoginAction()
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
        } catch (NoSuchElementException e) {
            Response response = new Response();
            Message message = new InfoMessage();
            response.send(resp, message.build("Action not found"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
