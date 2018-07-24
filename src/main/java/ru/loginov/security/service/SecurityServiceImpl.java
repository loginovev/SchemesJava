package ru.loginov.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ru.loginov.security.UserRoleEnum;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

/**
 * Security service instead of EL
 */
@Component("Security")
public class SecurityServiceImpl implements SecurityService {

    /**
     * The method defines whether the specified user has this role
     * @param principal (UserDetails)
     * @param role      (UserRoleEnum)
     * @return          (boolean)
     */
    @Override
    public boolean hasPermission(UserDetails principal,UserRoleEnum role){
        UserRoleEnum[] roles = new UserRoleEnum[1];
        roles[0] = role;
        return hasPermission(principal,roles);
    }

    /**
     * The method defines whether the specified user has these roles
     * @param principal (UserDetails)
     * @param roles     (UserRoleEnum[])
     * @return          (boolean)
     */
    @Override
    public boolean hasPermission(UserDetails principal,UserRoleEnum[] roles){

        for (GrantedAuthority grantedAuthority:principal.getAuthorities()) {
            for (UserRoleEnum role:roles) {
                if (grantedAuthority.getAuthority().equals("ROLE_"+role.name())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The method defines whether the specified user has this role. If user does not have this role an error logging
     * @param principal (UserDetails)
     * @param role      (UserRoleEnum)
     * @param structure (LoggerAppStructure)
     * @param message   (String)
     * @return          (boolean)
     */
    @Override
    public boolean hasPermissionWithLogging(UserDetails principal, UserRoleEnum role, LoggerAppStructure structure, String message){
        UserRoleEnum[] roles = new UserRoleEnum[1];
        roles[0] = role;
        return hasPermissionWithLogging(principal,roles,structure,message);
    }

    /**
     * The method defines whether the specified user has these roles. If user does not have this role an error logging
     * @param principal (UserDetails)
     * @param roles      (UserRoleEnum[])
     * @param structure (LoggerAppStructure)
     * @param message   (String)
     * @return          (boolean)
     */
    @Override
    public boolean hasPermissionWithLogging(UserDetails principal, UserRoleEnum[] roles, LoggerAppStructure structure, String message){

        if (!hasPermission(principal,roles)){
            CustomLogManager.error(
                    SecurityServiceImpl.class.getName()
                    ,principal.getUsername()
                    ,LoggerEvents.ACCESS_DENIED
                    ,structure
                    ,""
                    ,message
            );
            return false;
        }
        return true;
    }
}
