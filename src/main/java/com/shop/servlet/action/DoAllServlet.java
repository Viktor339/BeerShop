package com.shop.servlet.action;

import com.shop.repository.PositionRepository;
import com.shop.repository.UserRepository;
import com.shop.service.AddPositionService;
import com.shop.service.ChangePositionService;
import com.shop.service.JSONParseService;
import com.shop.service.LoginService;
import com.shop.service.RegistrationService;
import com.shop.service.Response;
import com.shop.service.exception.ActionNotFoundException;
import com.shop.service.exception.ConfigException;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.service.exception.UnacceptableDatabaseDriverException;
import com.shop.servlet.dto.InformationResponse;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DoAllServlet extends HttpServlet {
    private List<Action> postActions;
    private List<Action> putActions;
    private Response response;

    @Override
    public void init() throws ServletException {
        super.init();
        ObjectMapper objectMapper = new ObjectMapper();
        UserRepository userRepository = new UserRepository();
        JSONParseService jsonParseService = new JSONParseService(objectMapper);
        PositionRepository positionRepository = new PositionRepository();
        response = new Response(objectMapper);
        postActions = Arrays.asList(
                new RegistrationAction(new RegistrationService(userRepository), jsonParseService, response),
                new LoginAction(new LoginService(userRepository),jsonParseService, response),
                new AddPositionAction(new AddPositionService(positionRepository),jsonParseService,response)
        );

        putActions = Collections.singletonList(
                new ChangePositionAction(new ChangePositionService(positionRepository), jsonParseService, response)
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            final Action action = postActions.stream()
                    .filter(act -> act.isValid(req))
                    .findFirst()
                    .orElseThrow(() -> new ActionNotFoundException("Action not found"));
            action.doAction(req, resp);

        } catch (ActionNotFoundException |
                UnableToExecuteQueryException |
                UnableToGetConnectionException |
                UnacceptableDatabaseDriverException |
                ConfigException e) {
            response.send(resp, new InformationResponse(e.getMessage()), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            final Action action = putActions.stream()
                    .filter(act -> act.isValid(req))
                    .findFirst()
                    .orElseThrow(() -> new ActionNotFoundException("Action not found"));
            action.doAction(req, resp);

        } catch (ActionNotFoundException |
                UnableToExecuteQueryException |
                UnableToGetConnectionException |
                UnacceptableDatabaseDriverException |
                ConfigException e) {
            response.send(resp, new InformationResponse(e.getMessage()), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
