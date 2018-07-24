package ru.loginov.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.loginov.security.UserRoleEnum;
import ru.loginov.utils.logging.LoggerAppStructure;

public interface SecurityService {
    boolean hasPermission(UserDetails principal, UserRoleEnum role);
    boolean hasPermission(UserDetails principal,UserRoleEnum[] roles);
    boolean hasPermissionWithLogging(UserDetails principal, UserRoleEnum role, LoggerAppStructure structure, String message);
    boolean hasPermissionWithLogging(UserDetails principal, UserRoleEnum[] roles, LoggerAppStructure structure, String message);
}
