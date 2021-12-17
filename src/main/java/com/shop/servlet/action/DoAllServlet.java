package com.shop.servlet.action;

import com.shop.config.Config;
import com.shop.repository.PositionRepository;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.service.AddPositionService;
import com.shop.service.BuyPositionService;
import com.shop.service.ChangePositionService;
import com.shop.service.GetAllUsersHistoryService;
import com.shop.service.GetAvailablePositionsService;
import com.shop.service.GetUserHistoryService;
import com.shop.service.JSONParseService;
import com.shop.service.LoginService;
import com.shop.service.PageSizeValidatorService;
import com.shop.service.RegistrationService;
import com.shop.service.Response;
import com.shop.service.ValidatorService;
import com.shop.service.exception.ActionNotFoundException;
import com.shop.service.exception.ConfigException;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.service.exception.UnableToPerformSerializationException;
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
    private List<Action> getActions;
    private Response response;

    @Override
    public void init() throws ServletException {
        super.init();
        ObjectMapper objectMapper = new ObjectMapper();
        UserRepository userRepository = new UserRepository();
        JSONParseService jsonParseService = new JSONParseService(objectMapper);
        Config config = new Config();
        PositionRepository positionRepository = new PositionRepository(objectMapper, config);
        ValidatorService validatorService = new ValidatorService();
        UserTransactionRepository userTransactionRepository = new UserTransactionRepository(config, objectMapper);
        PageSizeValidatorService pageSizeValidatorService = new PageSizeValidatorService();

        response = new Response(objectMapper);
        postActions = Arrays.asList(
                new RegistrationAction(new RegistrationService(userRepository), jsonParseService, response),
                new LoginAction(new LoginService(userRepository), jsonParseService, response),
                new AddPositionAction(new AddPositionService(positionRepository, config, validatorService), jsonParseService, response),
                new BuyPositionAction(new BuyPositionService(positionRepository, userTransactionRepository, userRepository), jsonParseService, response)
        );

        putActions = Collections.singletonList(
                new ChangePositionAction(new ChangePositionService(positionRepository, config, validatorService), jsonParseService, response)
        );

        getActions = Arrays.asList(
                new GetUserHistoryAction(new GetUserHistoryService(userTransactionRepository, config, userRepository, pageSizeValidatorService), response),
                new GetAllUsersHistoryAction(new GetAllUsersHistoryService(userTransactionRepository, config, pageSizeValidatorService), response),
                new GetAvailablePositionsAction(new GetAvailablePositionsService(positionRepository, config, pageSizeValidatorService), response)
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
                ConfigException |
                UnableToPerformSerializationException e) {
            response.send(resp, new InformationResponse(e.getMessage()), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            final Action action = getActions.stream()
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
