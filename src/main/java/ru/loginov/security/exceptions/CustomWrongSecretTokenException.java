package ru.loginov.security.exceptions;

import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

public class CustomWrongSecretTokenException extends RuntimeException {
    public static final String description = "WRONG-SECRET-TOKEN";

    public CustomWrongSecretTokenException() {
        super();
        CustomLogManager.error(CustomWrongSecretTokenException.class.getName(), CustomExceptionHandler.getMessage(description));
    }

    public CustomWrongSecretTokenException(String username){
        super();
        CustomLogManager.error(CustomWrongSecretTokenException.class.getName(),username, LoggerEvents.LOGIN, LoggerAppStructure.Logging,"",CustomExceptionHandler.getMessage(description));
    }

    public CustomWrongSecretTokenException(String username, String message){
        super(message);
        CustomLogManager.error(CustomWrongSecretTokenException.class.getName(),username, LoggerEvents.LOGIN, LoggerAppStructure.Logging,"",message);
    }
}
