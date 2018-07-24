package ru.loginov.security.exceptions;

import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

public class CustomUserNotAuthenticatedException extends RuntimeException{

    public static final String description = "USER-NOT-AUTHENTICATED";

    public CustomUserNotAuthenticatedException() {
        super();
        CustomLogManager.error(CustomUserNotAuthenticatedException.class.getName(), CustomExceptionHandler.getMessage(description));
    }

    public CustomUserNotAuthenticatedException(String username){
        super();
        CustomLogManager.error(CustomUserNotAuthenticatedException.class.getName(),username, LoggerEvents.LOGIN, LoggerAppStructure.Logging,"",CustomExceptionHandler.getMessage(description));
    }

    public CustomUserNotAuthenticatedException(String username, String message){
        super(message);
        CustomLogManager.error(CustomUserNotAuthenticatedException.class.getName(),username, LoggerEvents.LOGIN, LoggerAppStructure.Logging,"",message);
    }
}
