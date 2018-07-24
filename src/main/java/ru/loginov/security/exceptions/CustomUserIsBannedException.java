package ru.loginov.security.exceptions;

import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

public class CustomUserIsBannedException  extends RuntimeException{
    public static final String description = "USER-IS-BANNED";

    public CustomUserIsBannedException() {
        super();
        CustomLogManager.error(CustomUserIsBannedException.class.getName(), CustomExceptionHandler.getMessage(description));
    }

    public CustomUserIsBannedException(String username) {
        super();
        CustomLogManager.error(CustomUserIsBannedException.class.getName(),username, LoggerEvents.LOGIN, LoggerAppStructure.Logging,"",CustomExceptionHandler.getMessage(description));
    }

    public CustomUserIsBannedException(String username, String message){
        super(message);
        CustomLogManager.error(CustomUserIsBannedException.class.getName(),username, LoggerEvents.LOGIN, LoggerAppStructure.Logging,"",message);
    }
}
