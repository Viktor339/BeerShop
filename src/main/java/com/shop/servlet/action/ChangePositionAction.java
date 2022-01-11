package com.shop.servlet.action;

import com.shop.service.ChangePositionService;
import com.shop.service.Response;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.service.exception.ValidatorException;
import com.shop.servlet.dto.ChangePositionResponse;
import com.shop.servlet.dto.InformationResponse;
import com.shop.servlet.request.ChangePositionRequest;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class ChangePositionAction implements Action {
    private final ChangePositionService changePositionService;
    private final ObjectMapper objectMapper;
    private final Response response;

    @Override
    public boolean isValid(HttpServletRequest req) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.equals("/positions") & req.getMethod().equals("PUT");
    }

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ChangePositionRequest changePositionRequest = objectMapper.readValue(req.getInputStream(), ChangePositionRequest.class);

        try {
            ChangePositionResponse changePositionResponse = changePositionService.change(changePositionRequest);

            response.send(resp, changePositionResponse, HttpServletResponse.SC_OK);
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        } catch (PositionNotFoundException |
                ValidatorException e) {
            response.send(resp, new InformationResponse(e.getMessage()), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
