package ru.loginov.security.handlers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The class provides exception AccessDenied. The result of exception returns as the json of ErrorDescription.
 * See ru.loginov.security.configuration.SecurityConfig, configure(HttpSecurity http) method,
 * 'exceptionHandling().accessDeniedHandler(accessDeniedHandler)' string
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        CustomExceptionHandler.handleCustomAccessDeniedException(httpServletResponse);
    }
}
