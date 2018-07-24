package ru.loginov.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ru.loginov.security.exceptions.CustomUsernameNotFoundException;
import ru.loginov.security.UserRoleEnum;
import ru.loginov.security.dto.UserDTO;
import ru.loginov.security.dto.SessionDTO;

import java.util.*;

/**
 * User details service
 */
@Service
public class UserDetailsServiceAppImpl implements UserDetailsServiceApp {

    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;
    private SessionService sessionService;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Authentication user by username
     * @param username (string)
     * @return (UserDetails)
     */
    @Override
    public UserDetails loadUserByUsername(String username){

        //Find out a session by username
        SessionDTO sessionDTO = sessionService.findSessionByName(username);
        String encodedPassword = "";
        List<UserRoleEnum> roles = new ArrayList<>();

        //If a session is not found, create a new session
        if (sessionDTO==null){

            //Find user by username
            UserDTO userDTO = userService.getUser(username);

            //If the user is not found
            if (userDTO==null){

                //If the list of users is empty
                if (userService.count()==0){

                    //Allow to enter with the ADMIN role and the '123' password
                    roles.add(UserRoleEnum.ADMIN);
                    encodedPassword = passwordEncoder.encode("123");

                }
                //Otherwise, throw exception
                else {
                    throw new CustomUsernameNotFoundException(username, "User '"+username+"' not found");
                }
            }else{
                //Otherwise, get encodedPassword and roles from the found user
                encodedPassword = userDTO.getEncodedPassword();
                roles = userDTO.getAuthorities();
            }

            //Save session with the found user
            sessionService.saveSession(new SessionDTO(userDTO));
        }
        //Otherwise, get encodedPassword and roles from the found session
        else{
            encodedPassword = sessionDTO.getUserDTO().getEncodedPassword();
            roles = sessionDTO.getUserDTO().getAuthorities();
            sessionDTO.setLastActivity(Calendar.getInstance().getTime());
        }

        //Get 'userRoles' from 'roles'
        List<GrantedAuthority> userRoles = new ArrayList<>();
        for (UserRoleEnum role : roles){
            userRoles.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
        }

        return new org.springframework.security.core.userdetails.User(username, encodedPassword, userRoles);
    }
}
