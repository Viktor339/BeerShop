package com.shop.servlet.action;

import com.shop.service.GetUserHistoryService;
import com.shop.service.Response;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.service.exception.ValidatorException;
import com.shop.servlet.dto.GetUserHistoryResponse;
import com.shop.servlet.dto.InformationResponse;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class GetUserHistoryAction implements Action {
    private final GetUserHistoryService getUserHistoryService;
    private final Response response;

    @Override
    public boolean isValid(HttpServletRequest req) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.equals("/history/user") & req.getMethod().equals("GET");
    }

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            try {

                Object uuid = req.getSession().getAttribute("UUID");
                Integer size = Integer.parseInt(req.getParameter("size"));
                GetUserHistoryResponse getUserHistoryResponse = getUserHistoryService.get(size, uuid);
                response.send(resp, getUserHistoryResponse, HttpServletResponse.SC_OK);

            } catch (NumberFormatException e) {
                throw new ValidatorException("Invalid parameter");
            }
        } catch (PositionNotFoundException |
                ValidatorException e) {
            response.send(resp, new InformationResponse(e.getMessage()), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
