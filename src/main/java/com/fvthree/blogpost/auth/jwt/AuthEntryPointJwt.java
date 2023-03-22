package com.fvthree.blogpost.auth.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fvthree.blogpost.auth.AuthAbstractController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthEntryPointJwt extends AuthAbstractController implements AuthenticationEntryPoint  {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.warn(
            "Unauthorized error: " + request.getServletPath() +
                "?" + request.getQueryString() + " {}", authException.getMessage());
        
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
        
    }
}
