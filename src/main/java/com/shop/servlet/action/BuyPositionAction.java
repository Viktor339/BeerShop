package com.shop.servlet.action;

import com.shop.service.BuyPositionService;
import com.shop.service.JSONParseService;
import com.shop.service.Response;
import com.shop.service.exception.AvailableQuantityExceeded;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.service.exception.ValidatorException;
import com.shop.servlet.dto.InformationResponse;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class BuyPositionAction implements Action {
    private final BuyPositionService buyPositionService;
    private final JSONParseService jsonParseService;
    private final Response response;

    @Override
    public boolean isValid(HttpServletRequest req) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.equals("/item") & req.getMethod().equals("POST");
    }

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        BuyPositionRequest buyPositionRequest = jsonParseService.parseFromJson(req.getInputStream(), BuyPositionRequest.class);

        try {
            Object uuid = req.getSession().getAttribute("UUID");
            String message = buyPositionService.buy(buyPositionRequest,uuid);

            response.send(resp, new InformationResponse(message), HttpServletResponse.SC_OK);

        } catch (PositionNotFoundException |
                ValidatorException |
                AvailableQuantityExceeded e) {
            response.send(resp, new InformationResponse(e.getMessage()), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
