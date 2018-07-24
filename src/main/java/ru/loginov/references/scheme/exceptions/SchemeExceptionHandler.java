package ru.loginov.references.scheme.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.loginov.security.exceptions.CustomErrorDescriptionNotFoundException;
import ru.loginov.utils.ErrorDescription;
import ru.loginov.utils.JsonUtils;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * The class provides exceptions. The result of exceptions returns as the json of ErrorDescription.
 */
@ControllerAdvice(basePackages = {"ru.loginov.references.scheme"})
public class SchemeExceptionHandler extends ResponseEntityExceptionHandler {

    // List of error from file resource/SecurityErrorsDescription.json
    private static ConcurrentMap<String,ErrorDescription> errors = new ConcurrentHashMap<>();


    //The static method initializes the errors map
    static {
        try{
            ErrorDescription[] errorDescriptions = JsonUtils.fromFileJSON("SchemeErrorsDescription.json",ErrorDescription[].class);

            for (ErrorDescription ed: errorDescriptions) {
                errors.put(ed.getDescription(),ed);
            }

        }catch (IOException e){
            errors.clear();
        }
    }

    /**
     * The method provides exception "Username not found"
     * @return json for this exception
     */
    @ExceptionHandler({CustomSchemeNotFoundException.class})
    public ResponseEntity<Object> handleCustomSchemeNotFoundException(){
        return SchemeExceptionHandler.getResponseEntity(CustomSchemeNotFoundException.description);
    }

    /**
     * The method receives the description from the file "SchemeErrorsDescription.json" and the json matching the description returns
     * @param description from the file "SchemeErrorsDescription.json"
     * @return json matching the description
     */
    private static ResponseEntity<Object> getResponseEntity(String description){
        ErrorDescription errorDescription;
        try{
            errorDescription = new ErrorDescription(description,errors);
        }catch (CustomErrorDescriptionNotFoundException e){
            errorDescription = new ErrorDescription(CustomErrorDescriptionNotFoundException.description, HttpStatus.INTERNAL_SERVER_ERROR.value(),"");
        }
        return new ResponseEntity<>(errorDescription,HttpStatus.valueOf(errorDescription.getStatus()));
    }
}
