package com.shop.servlet.action;

import com.shop.config.Config;
import com.shop.model.BottleBeerData;
import com.shop.model.BuyBottleBeerData;
import com.shop.model.BuyDraftBeerData;
import com.shop.model.DraftBeerData;
import com.shop.repository.PositionRepository;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.service.AddPositionService;
import com.shop.service.BuyPositionService;
import com.shop.service.ChangePositionService;
import com.shop.service.GetAllUsersHistoryService;
import com.shop.service.GetAvailablePositionsService;
import com.shop.service.GetUserHistoryService;
import com.shop.service.LoginService;
import com.shop.service.PageService;
import com.shop.service.RegistrationService;
import com.shop.service.Response;
import com.shop.service.ValidatorService;
import com.shop.service.exception.ActionNotFoundException;
import com.shop.service.exception.ConfigException;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.service.exception.UnableToPerformSerializationException;
import com.shop.service.exception.UnacceptableDatabaseDriverException;
import com.shop.service.performer.BottleBeerPerformer;
import com.shop.service.performer.BuyBottleBeerDataValidatorPerformer;
import com.shop.service.performer.BuyBottleBeerPerformer;
import com.shop.service.performer.BuyDraftBeerDataValidatorPerformer;
import com.shop.service.performer.BuyDraftBeerPerformer;
import com.shop.service.performer.DraftBeerPerformer;
import com.shop.service.validator.AlcoholPercentageValidator;
import com.shop.service.validator.BitternessValidator;
import com.shop.service.validator.ContainerTypeValidator;
import com.shop.service.validator.ContainerVolumeValidator;
import com.shop.service.validator.EmailValidator;
import com.shop.service.validator.NameValidator;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.servlet.dto.InformationResponse;
import com.shop.servlet.request.AddPositionRequest;
import com.shop.servlet.request.ChangePositionRequest;
import com.shop.servlet.request.LoginRequest;
import com.shop.servlet.request.RegistrationRequest;
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
        //     JSONParseService jsonParseService = new JSONParseService(objectMapper);
        Config config = new Config();
        PositionRepository positionRepository = new PositionRepository(objectMapper, config);
        ValidatorService validatorService = new ValidatorService();
        UserTransactionRepository userTransactionRepository = new UserTransactionRepository(config, objectMapper);
        PageService pageService = new PageService();

        response = new Response(objectMapper);
        postActions = Arrays.asList(
                new RegistrationAction(new RegistrationService(userRepository,
                        Arrays.asList(
                                new NotEmptyFieldValidator<>(RegistrationRequest::getPassword, "Password is null or empty"),
                                new NotEmptyFieldValidator<>(RegistrationRequest::getEmail, "Email is null or empty"),
                                new NotEmptyFieldValidator<>(RegistrationRequest::getName, "Name is null or empty"),
                                new EmailValidator(),
                                new NameValidator()
                        ), validatorService),
                        objectMapper,
                        response),
                new LoginAction(new LoginService(userRepository,
                        Arrays.asList(
                                new NotEmptyFieldValidator<>(LoginRequest::getName, "Name is null or empty"),
                                new NotEmptyFieldValidator<>(LoginRequest::getPassword, "Password is null or empty")
                        ), validatorService),
                        objectMapper,
                        response),
                new AddPositionAction(new AddPositionService(positionRepository, validatorService,
                        Arrays.asList(
                                new NotEmptyFieldValidator<>(AddPositionRequest::getName, "Name is null or empty"),
                                new NotEmptyFieldValidator<>(AddPositionRequest::getContainerType, "Container type is null or empty"),
                                new NotEmptyFieldValidator<>(AddPositionRequest::getBeerType, "Beer type is null or empty"),
                                new NotEmptyFieldValidator<>(AddPositionRequest::getAlcoholPercentage, "Alcohol percentage is null or empty"),
                                new NotEmptyFieldValidator<>(AddPositionRequest::getBitterness, "Bitterness is null or empty"),
                                new ContainerTypeValidator<>(AddPositionRequest::getContainerType, "Incorrect container type"),
                                new AlcoholPercentageValidator(config.getMinAlcoholPercentage(), config.getMaxAlcoholPercentage(), "Incorrect alcohol percentage"),
                                new BitternessValidator(config.getMinBitterness(), config.getMAxBitterness(), "Incorrect bitterness value")
                        ),
                        Arrays.asList(
                                new DraftBeerPerformer(validatorService,
                                        Collections.singletonList(
                                                new NotEmptyFieldValidator<>(DraftBeerData::getAvailableLiters, "Available litres is null or empty")
                                        )),
                                new BottleBeerPerformer(validatorService,
                                        Arrays.asList(
                                                new NotEmptyFieldValidator<>(BottleBeerData::getContainerVolume, "Container volume is null or empty"),
                                                new NotEmptyFieldValidator<>(BottleBeerData::getQuantity, "Quantity is null or empty"),
                                                new ContainerVolumeValidator<>(BottleBeerData::getContainerVolume, config.getMinContainerVolume(), config.getMaxContainerVolume(), "Incorrect container volume")
                                        ))
                        )
                ),
                        objectMapper,
                        response),
                new BuyPositionAction(new BuyPositionService(
                        Arrays.asList(
                                new BuyBottleBeerPerformer(positionRepository),
                                new BuyDraftBeerPerformer(positionRepository)
                        ),
                        positionRepository,
                        userTransactionRepository,
                        userRepository,
                        Arrays.asList(
                                new BuyDraftBeerDataValidatorPerformer(Arrays.asList(
                                        new NotEmptyFieldValidator<>(BuyDraftBeerData::getId, "Id is null or empty"),
                                        new NotEmptyFieldValidator<>(BuyDraftBeerData::getQuantity, "Quantity is null or empty")
                                )),
                                new BuyBottleBeerDataValidatorPerformer(Arrays.asList(
                                        new NotEmptyFieldValidator<>(BuyBottleBeerData::getId, "Id is null or empty"),
                                        new NotEmptyFieldValidator<>(BuyBottleBeerData::getQuantity, "Quantity is null or empty")
                                ))
                        )),
                        objectMapper,
                        response)
        );

        putActions = Collections.singletonList(
                new ChangePositionAction(new ChangePositionService(positionRepository,
                        validatorService,
                        Arrays.asList(
                                new NotEmptyFieldValidator<>(ChangePositionRequest::getId, "Id is null or empty"),
                                new NotEmptyFieldValidator<>(ChangePositionRequest::getContainerVolume, "Container volume is null or empty"),
                                new NotEmptyFieldValidator<>(ChangePositionRequest::getQuantity, "Quantity is null or empty"),
                                new ContainerVolumeValidator<>(ChangePositionRequest::getContainerVolume, config.getMinContainerVolume(), config.getMaxContainerVolume(), "Incorrect container volume")
                        )),
                        objectMapper,
                        response)
        );

        getActions = Arrays.asList(
                new GetUserHistoryAction(new GetUserHistoryService(userTransactionRepository, config, userRepository, pageService), response),
                new GetAllUsersHistoryAction(new GetAllUsersHistoryService(userTransactionRepository, config, pageService), response),
                new GetAvailablePositionsAction(new GetAvailablePositionsService(positionRepository, config, pageService), response)
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
