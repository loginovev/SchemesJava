package ru.loginov.security.exceptions;

import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

public class CustomDeleteYourOwnSessionException extends RuntimeException {
    public static final String description = "DELETE-YOUR-OWN-SESSION";

    public CustomDeleteYourOwnSessionException() {
        super();
        CustomLogManager.error(CustomDeleteYourOwnSessionException.class.getName(), CustomExceptionHandler.getMessage(description));
    }

    public CustomDeleteYourOwnSessionException(String username) {
        super();
        CustomLogManager.error(CustomDeleteYourOwnSessionException.class.getName(),username, LoggerEvents.DELETE, LoggerAppStructure.Session,"",CustomExceptionHandler.getMessage(description));
    }

    public CustomDeleteYourOwnSessionException(String username, String message){
        super(message);
        CustomLogManager.error(CustomDeleteYourOwnSessionException.class.getName(),username, LoggerEvents.DELETE, LoggerAppStructure.Session,"",message);
    }
}
