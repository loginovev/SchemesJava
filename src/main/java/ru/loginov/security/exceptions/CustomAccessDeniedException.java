package ru.loginov.security.exceptions;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

public class CustomAccessDeniedException extends RuntimeException {
    public static final String description = "ACCESS-DENIED";

    public CustomAccessDeniedException() {
        super();
        CustomLogManager.error(
                CustomAccessDeniedException.class.getName()
                ,SecurityContextHolder.getContext().getAuthentication().getName()
                ,LoggerEvents.REQUEST
                ,LoggerAppStructure.Logging
                ,""
                ,CustomExceptionHandler.getMessage(description)
        );
    }
}
