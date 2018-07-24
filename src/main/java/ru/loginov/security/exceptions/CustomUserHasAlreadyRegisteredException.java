package ru.loginov.security.exceptions;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

public class CustomUserHasAlreadyRegisteredException extends RuntimeException {
    public static final String description = "USER-HAS-ALREADY-REGISTERED";

    public CustomUserHasAlreadyRegisteredException() {
        super();
        CustomLogManager.error(CustomUserHasAlreadyRegisteredException.class.getName(), CustomExceptionHandler.getMessage(description));
    }

    public CustomUserHasAlreadyRegisteredException(String username) {
        super();
        CustomLogManager.error(
                CustomUserHasAlreadyRegisteredException.class.getName(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                LoggerEvents.NEW,
                LoggerAppStructure.Reference_Users,
                username,
                CustomExceptionHandler.getMessage(description)
        );
    }

    public CustomUserHasAlreadyRegisteredException(String username, String message){
        super(message);
        CustomLogManager.error(
                CustomUserHasAlreadyRegisteredException.class.getName(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                LoggerEvents.NEW,
                LoggerAppStructure.Reference_Users,
                username,
                message
        );
    }
}
