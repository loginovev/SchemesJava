package ru.loginov.security.filters;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.loginov.security.service.AuthenticationService;
import ru.loginov.security.service.SessionService;

/**
 * Add a filter for providing a security
 */
public class CustomSecurityFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private AuthenticationService authenticationService;
    private SessionService sessionService;

    public CustomSecurityFilterConfig(AuthenticationService authenticationService, SessionService sessionService) {
        this.authenticationService = authenticationService;
        this.sessionService = sessionService;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.addFilterBefore(new CustomSecurityFilter(authenticationService, sessionService), UsernamePasswordAuthenticationFilter.class);
    }

}
