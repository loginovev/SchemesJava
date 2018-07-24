package ru.loginov.security.filters;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;
import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.security.exceptions.CustomSecretHeaderNotFoundException;
import ru.loginov.security.exceptions.CustomUserNotAuthenticatedException;
import ru.loginov.security.dto.SessionDTO;
import ru.loginov.security.exceptions.CustomWrongSecretTokenException;
import ru.loginov.security.service.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * the filter checks whether the request meets access conditions:
 * 1. there are headers with the login and confidential number
 * 2. the user is already authorized, i.e. is in sessionsMap of SessionServiceImpl
 * 3. the confidential number is correct
 */
public class CustomSecurityFilter extends GenericFilterBean {

    private AuthenticationService authenticationService;
    private SessionService sessionService;

    public CustomSecurityFilter(AuthenticationService authenticationService, SessionService sessionService) {
        this.authenticationService = authenticationService;
        this.sessionService = sessionService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String securityLogin = "";
        String securityToken = "";

        if (!
                (!request.getRequestURI().contains("/api")
                || request.getRequestURI().contains("/api/authenticate")
                || request.getRequestURI().contains("/api/logout")
                || request.getRequestURI().contains("/v2/api-docs")//Swagger2
           )){

            try{

                //If the header "secret-login" does not exist then an error
                securityLogin = request.getHeader(AuthenticationServiceImpl.HEADER_SECURITY_LOGIN);
                if (securityLogin==null){
                    throw new CustomSecretHeaderNotFoundException();
                }

                //If the header "secret-digit" does not exist then an error
                securityToken = request.getHeader(AuthenticationServiceImpl.HEADER_SECURITY_DIGIT);
                if (securityToken==null){
                    throw new CustomSecretHeaderNotFoundException(securityLogin);
                }

                //If the user's session does not exist then an error
                SessionDTO sessionDTO = sessionService.findSessionByName(securityLogin);
                if (sessionDTO==null){
                    throw new CustomUserNotAuthenticatedException(securityLogin);
                }

                //If the transferred secret token is wrong then an error
                if (!sessionDTO.getSecurityToken().equals(securityToken)){
                    throw new CustomWrongSecretTokenException(securityLogin);
                }

                //Create a session for this request
                this.authenticationService.securityTokenAuthenticate(sessionDTO.getUserDTO().getUsername(), sessionDTO.getUserDTO().getPassword());

            }catch (CustomSecretHeaderNotFoundException e){
                CustomExceptionHandler.handleCustomSecretHeaderNotFoundException(response);
            }catch (CustomUserNotAuthenticatedException e){
                CustomExceptionHandler.handleCustomUserNotAuthenticatedException(response);
            }catch (CustomWrongSecretTokenException e){
                CustomExceptionHandler.handleCustomWrongSecretTokenException(response);
            }catch (Exception e){
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.getWriter().write(e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
