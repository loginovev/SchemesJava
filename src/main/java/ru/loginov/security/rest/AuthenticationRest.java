package ru.loginov.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.loginov.security.dto.LoginDTO;
import ru.loginov.security.dto.SessionDTO;
import ru.loginov.security.service.AuthenticationService;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

/**
 * Rest-service for authentication
 */

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AuthenticationRest {

    private AuthenticationService authenticationService;

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * The method creates the session of user
     * @param loginDTO where 'username' and 'password' used to an authentication
     * @return SessionDTO in 'authentication = true' case, i.e. {username,securityToken,authorities,options}
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/authenticate",method = RequestMethod.POST)
    public SessionDTO authenticate(@RequestBody LoginDTO loginDTO) throws Exception {

        SessionDTO sessionDTO = authenticationService.authenticate(loginDTO);
        sessionDTO.authentication = true;

        CustomLogManager.info(
                AuthenticationRest.class.getName(),
                loginDTO.getUsername(),
                LoggerEvents.LOGIN,
                LoggerAppStructure.Logging,
                "",
                "User '"+loginDTO.getUsername()+"' login successfully"
        );

        return sessionDTO;
    }

    /**
     * The method removes the session of user
     * @param username username of user
     * @param securityToken security token given within authentication
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public void logout(@RequestParam(name = "username") String username, @RequestParam(name = "securityToken") String securityToken) throws Exception{
        authenticationService.logout(username,securityToken);

        CustomLogManager.info(
                AuthenticationRest.class.getName(),
                username,
                LoggerEvents.LOGOUT,
                LoggerAppStructure.Logging,
                "",
                "User '"+username+"' logout successfully");
    }
}
