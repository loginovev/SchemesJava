package ru.loginov.security.exceptions;

import ru.loginov.utils.logging.CustomLogManager;

public class CustomErrorDescriptionNotFoundException extends Exception {
    public static final String description = "UNKNOWN-ERROR";

    public CustomErrorDescriptionNotFoundException(String message) {
        super("'"+message+"' description does not found in 'SecurityErrorsDescription.json'");
        CustomLogManager.error(CustomAccessDeniedException.class.getName(), "'"+message+"' description does not found in 'SecurityErrorsDescription.json'");
    }
}
