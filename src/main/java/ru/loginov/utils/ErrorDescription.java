package ru.loginov.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.loginov.security.exceptions.CustomErrorDescriptionNotFoundException;

import java.util.concurrent.ConcurrentMap;

/**
 * Class for creating json response
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDescription{
    private String description;
    private int status;
    private String message;

    public ErrorDescription(String description, ConcurrentMap<String,ErrorDescription> errors) throws CustomErrorDescriptionNotFoundException {

        ErrorDescription errorDescription = errors.get(description);
        if (errorDescription!=null){
            this.description = errorDescription.description;
            this.status = errorDescription.status;
            this.message = errorDescription.message;
        }else {
            throw new CustomErrorDescriptionNotFoundException(description);
        }
    }
}

