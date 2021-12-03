package com.shop.servlet.action;

import com.shop.service.AddPositionService;
import com.shop.service.JSONParseService;
import com.shop.service.Response;
import com.shop.service.exception.BeerPositionExecutorNotFoundException;
import com.shop.service.exception.PositionAlreadyExistsException;
import com.shop.service.exception.ValidatorException;
import com.shop.servlet.dto.AddPositionResponse;
import com.shop.servlet.dto.InformationResponse;
import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class AddPositionAction implements Action {
    private final AddPositionService addPositionService;
    private final JSONParseService jsonParseService;
    private final Response response;

    @Override
    public boolean isValid(HttpServletRequest req) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.equals("/position")&req.getMethod().equals("POST");
    }

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        AddPositionRequest data = jsonParseService.parseFromJson(req.getInputStream(), AddPositionRequest.class);

        try {
          AddPositionResponse addPositionResponse = addPositionService.add(data);

            response.send(resp, addPositionResponse, HttpServletResponse.SC_OK);
        } catch (PositionAlreadyExistsException |
                ValidatorException |
                BeerPositionExecutorNotFoundException e) {
            response.send(resp, new InformationResponse(e.getMessage()), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
