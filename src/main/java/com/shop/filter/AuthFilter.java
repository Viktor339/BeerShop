package com.shop.filter;

import com.shop.service.ResponseMessage;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain filterChain)

            throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        final HttpSession session = req.getSession(false);

        String path = req.getRequestURI().substring(req.getContextPath().length());
        ResponseMessage responseMessage = new ResponseMessage();

        if (path.startsWith("/login") | path.startsWith("/registration")) {
            res.sendRedirect("/doAllServlet");
        } else {
            if (session == null) {
                responseMessage.send(res, "Please sign in", HttpServletResponse.SC_BAD_REQUEST);
            } else {

                if (session.getAttribute("role").equals("user")) {
                    if (path.startsWith("/basket") | path.startsWith("/history")) {
                        filterChain.doFilter(request, response);
                    }
                }

                if (session.getAttribute("role").equals("admin")) {
                    if (path.startsWith("/position") | path.startsWith("/users-history")) {
                        filterChain.doFilter(request, response);
                    }
                }

                responseMessage.send(res, "access denied ", HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    public void destroy() {
    }
}