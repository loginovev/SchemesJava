package ru.loginov.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.loginov.security.exceptions.CustomUserIsBannedException;
import ru.loginov.security.exceptions.CustomUsernameNotFoundException;
import ru.loginov.security.dto.LoginDTO;
import ru.loginov.security.dto.SessionDTO;
import ru.loginov.security.dto.UserDTO;
import ru.loginov.security.exceptions.CustomWrongSecretTokenException;

/**
 * Authentication service
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    public static String HEADER_SECURITY_LOGIN = "secret-login";
    public static String HEADER_SECURITY_DIGIT = "secret-digit";

    private AuthenticationManager authenticationManager;
    private SessionService sessionService;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Authenticate user
     * @param loginDTO (LoginDTO) username and password
     * @return SessionDTO
     */
    public SessionDTO authenticate(LoginDTO loginDTO) {

        //Set authentication in app context
        securityTokenAuthenticate(loginDTO.getUsername(),loginDTO.getPassword());

        //Searching the session which was created in UserDetailsServiceAppImpl.loadUserByUsername
        SessionDTO sessionDTO = sessionService.findSessionByName(loginDTO.getUsername());

        //If user is banned, throw exception
        if (sessionDTO.getUserDTO().isBanned()){
            throw new CustomUserIsBannedException(loginDTO.getUsername());
        }

        return sessionDTO;
    }

    /**
     * Logout with username and security token
     * @param username (String)
     * @param securityToken (String)
     */
    public void logout(String username, String securityToken){

        //Searching the session
        SessionDTO sessionDTO = sessionService.findSessionByName(username);

        //If searched
        if (sessionDTO!=null){

            //If securityToken equls securityToken from the session
            if (sessionDTO.getSecurityToken().equals(securityToken)){
                //delete the session
                sessionService.deleteSessionByName(username);
            }else{
                //otherwise throw exception
                throw(new CustomWrongSecretTokenException(username, "No '"+username+"' session"));
            }
        }else{
            //otherwise throw exception
            throw(new CustomUsernameNotFoundException());
        }
    }

    /**
     * Set authentication in app context
     * @param username (String)
     * @param password (String)
     */
    public void securityTokenAuthenticate(String username, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
