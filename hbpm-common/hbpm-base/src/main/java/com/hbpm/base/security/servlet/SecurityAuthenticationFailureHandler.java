package com.hbpm.base.security.servlet;

import com.hbpm.base.utils.ServletJsonResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author huangxiuqi
 */
public class SecurityAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException ex) throws IOException, ServletException {
        ServletJsonResponseUtils.sendResponse(response, HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }
}
