package ru.loginov.security.exceptions;

import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

public class CustomUsernameNotFoundException extends RuntimeException {
    public static final String description = "USER-NOT-FOUND";

    public CustomUsernameNotFoundException() {
        super();
        CustomLogManager.error(CustomUsernameNotFoundException.class.getName(), CustomExceptionHandler.getMessage(description));
    }

    public CustomUsernameNotFoundException(String username) {
        super();
        CustomLogManager.error(CustomUsernameNotFoundException.class.getName(),username, LoggerEvents.LOGIN, LoggerAppStructure.Logging,"",CustomExceptionHandler.getMessage(description));
    }

    public CustomUsernameNotFoundException(String username, String message){
        super(message);
        CustomLogManager.error(CustomUsernameNotFoundException.class.getName(),username, LoggerEvents.LOGIN, LoggerAppStructure.Logging,"",message);
    }
}
