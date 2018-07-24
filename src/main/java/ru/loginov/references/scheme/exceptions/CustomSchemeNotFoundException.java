package ru.loginov.references.scheme.exceptions;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.loginov.security.exceptions.CustomWrongFilterFieldsException;
import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

public class CustomSchemeNotFoundException extends RuntimeException {
    public static final String description = "SCHEME-NOT-FOUND";

    public CustomSchemeNotFoundException() {
        super();
        CustomLogManager.error(
                CustomWrongFilterFieldsException.class.getName(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                LoggerEvents.REQUEST,
                LoggerAppStructure.Reference_Scheme,
                "",
                CustomExceptionHandler.getMessage(description));
    }

    public CustomSchemeNotFoundException(String message){
        super(message);
        CustomLogManager.error(
                CustomWrongFilterFieldsException.class.getName(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                LoggerEvents.REQUEST,
                LoggerAppStructure.Reference_Scheme,
                "",
                message);
    }

}
