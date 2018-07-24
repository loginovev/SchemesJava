package ru.loginov.security.exceptions;

import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

public class CustomSecretHeaderNotFoundException extends RuntimeException{
    public static final String description = "SECRET-HEADER-NOT-FOUND";

    public CustomSecretHeaderNotFoundException() {
        super();
        CustomLogManager.error(CustomSecretHeaderNotFoundException.class.getName(), CustomExceptionHandler.getMessage(description));
    }

    public CustomSecretHeaderNotFoundException(String username) {
        super();
        CustomLogManager.error(CustomSecretHeaderNotFoundException.class.getName(),username, LoggerEvents.LOGIN, LoggerAppStructure.Logging,"",CustomExceptionHandler.getMessage(description));
    }

    public CustomSecretHeaderNotFoundException(String username, String message){
        super(message);
        CustomLogManager.error(CustomSecretHeaderNotFoundException.class.getName(),username, LoggerEvents.LOGIN, LoggerAppStructure.Logging,"",message);
    }

}
