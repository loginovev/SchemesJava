package ru.loginov.security.exceptions;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.loginov.security.handlers.CustomExceptionHandler;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

public class CustomWrongFilterFieldsException extends RuntimeException {
    public static final String description = "WRONG-FILTER-FIELD";

    public CustomWrongFilterFieldsException() {
        super();
        CustomLogManager.error(
                CustomWrongFilterFieldsException.class.getName(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                LoggerEvents.REQUEST,
                LoggerAppStructure.Undefined,
                "",
                CustomExceptionHandler.getMessage(description));
    }

    public CustomWrongFilterFieldsException(String message){
        super(message);
        CustomLogManager.error(
                CustomWrongFilterFieldsException.class.getName(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                LoggerEvents.REQUEST,
                LoggerAppStructure.Undefined,
                "",
                message);
    }
}
