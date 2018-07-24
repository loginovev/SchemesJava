package ru.loginov.security.service;

import ru.loginov.security.dto.LoginDTO;
import ru.loginov.security.dto.SessionDTO;

public interface AuthenticationService {
    SessionDTO authenticate(LoginDTO loginDTO) throws Exception;
    void logout(String username, String securityToken) throws Exception;
    void securityTokenAuthenticate(String username, String password) throws Exception;
}
