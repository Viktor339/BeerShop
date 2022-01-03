package com.shop.servlet.action;

import com.shop.service.RegistrationService;
import com.shop.service.Response;
import com.shop.service.exception.UserAlreadyExistsException;
import com.shop.service.exception.ValidatorException;
import com.shop.servlet.dto.InformationResponse;
import com.shop.servlet.dto.UserRegistrationResponse;
import com.shop.servlet.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class RegistrationAction implements Action {
    private final RegistrationService registrationService;
    private final ObjectMapper objectMapper;
    private final Response response;

    @Override
    public boolean isValid(HttpServletRequest req) {

        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.equals("/registration");
    }

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse res) throws IOException {

        RegistrationRequest registrationRequest = objectMapper.readValue(req.getInputStream(), RegistrationRequest.class);

        try {
            String userUUID = registrationService.register(registrationRequest);
            response.send(res, new UserRegistrationResponse(userUUID), HttpServletResponse.SC_OK);

        } catch (ValidatorException |
                UserAlreadyExistsException e) {
            response.send(res, new InformationResponse(e.getMessage()), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
