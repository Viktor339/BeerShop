package com.shop.servlet;

import com.shop.service.JSONParseService;
import com.shop.service.ResponseMessage;
import com.shop.servlet.request.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class DoAllServlet extends HttpServlet {
    private List<Action> actions;

    @Override
    public void init() throws ServletException {
        super.init();
        actions = Arrays.asList(new RegistrationAction(), new LoginAction());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final Action action = actions.stream()
                .filter(act -> act.isValid(req))
                .findFirst()
                .get();

        JSONParseService jsonParseService = new JSONParseService();
        Request deserializeRequest = jsonParseService.parseFromJson(new InputStreamReader(req.getInputStream()), action);

        if (deserializeRequest == null) {
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.send(resp, "Could not find a suitable object for deserialization", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        action.doAction(req, resp, deserializeRequest);
    }
}
