package ru.loginov.security.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.loginov.security.exceptions.*;
import ru.loginov.utils.ErrorDescription;
import ru.loginov.utils.JsonUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The class provides exceptions. The result of exceptions returns as the json of ErrorDescription.
 */
@ControllerAdvice(basePackages = {"ru.loginov.security"})
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // List of error from file resource/SecurityErrorsDescription.json
    private static ConcurrentMap<String,ErrorDescription> errors = new ConcurrentHashMap<>();


    //The static method initializes the errors map
    static {
        try{
            ErrorDescription[] errorDescriptions = JsonUtils.fromFileJSON("SecurityErrorsDescription.json",ErrorDescription[].class);

            for (ErrorDescription ed: errorDescriptions) {
                errors.put(ed.getDescription(),ed);
            }

        }catch (IOException e){
            errors.clear();
        }
    }

    /**
     * The method receives the description from the file "SecurityErrorsDescription.json" and the message matching the description returns
     * @param description is one of the items of "SecurityErrorsDescription.json", the column "description"
     * @return the message matching the description
     */
    public static String getMessage(String description){
        ErrorDescription errorDescription = errors.get(description);
        if (errorDescription!=null){
            return errorDescription.getMessage();
        }else {
            return description;
        }
    }

    /**
     * The method provides exception "Username not found"
     * @return json for this exception
     */
    @ExceptionHandler({CustomUsernameNotFoundException.class})
    public ResponseEntity<Object> handleCustomUsernameNotFoundException(){
        return CustomExceptionHandler.getResponseEntity(CustomUsernameNotFoundException.description);
    }

    /**
     * The method provides exception "Delete your own session"
     * @return json for this exception
     */
    @ExceptionHandler({CustomDeleteYourOwnSessionException.class})
    public ResponseEntity<Object> handleCustomDeleteYourOwnSessionException(){
        return CustomExceptionHandler.getResponseEntity(CustomDeleteYourOwnSessionException.description);
    }

    /**
     * The method provides exception "User has already registered"
     * @return json for this exception
     */
    @ExceptionHandler({CustomUserHasAlreadyRegisteredException.class})
    public ResponseEntity<Object> handleCustomUserHasAlreadyRegisteredException(){
        return CustomExceptionHandler.getResponseEntity(CustomUserHasAlreadyRegisteredException.description);
    }

    /**
     * The method provides exception "User is banned"
     * @return json for this exception
     */
    @ExceptionHandler({CustomUserIsBannedException.class})
    public ResponseEntity<Object> handleCustomUserIsBannedException(){
        return CustomExceptionHandler.getResponseEntity(CustomUserIsBannedException.description);
    }

    /**
     * The method provides exception "Access denied"
     * @param httpServletResponse the stream in which the json value writes
     */
    public static void handleCustomAccessDeniedException(HttpServletResponse httpServletResponse){
        CustomExceptionHandler.handleHttpServletException(httpServletResponse,CustomAccessDeniedException.description);
    }

    /**
     * The method provides exception "Secret header not found"
     * @param httpServletResponse the stream in which the json value writes
     */
    public static void handleCustomSecretHeaderNotFoundException(HttpServletResponse httpServletResponse){
        CustomExceptionHandler.handleHttpServletException(httpServletResponse,CustomSecretHeaderNotFoundException.description);
    }

    /**
     * The method provides exception "User not authenticated"
     * @param httpServletResponse the stream in which the json value writes
     */
    public static void handleCustomUserNotAuthenticatedException(HttpServletResponse httpServletResponse){
        CustomExceptionHandler.handleHttpServletException(httpServletResponse,CustomUserNotAuthenticatedException.description);
    }

    /**
     * The method provides exception "Wrong secret token"
     * @param httpServletResponse the stream in which the json value writes
     */
    public static void handleCustomWrongSecretTokenException(HttpServletResponse httpServletResponse){
        CustomExceptionHandler.handleHttpServletException(httpServletResponse,CustomWrongSecretTokenException.description);
    }

    /**
     * The method receives the description from the file "SecurityErrorsDescription.json" and the json matching the description returns
     * @param description from the file "SecurityErrorsDescription.json"
     * @return json matching the description
     */
    private static ResponseEntity<Object> getResponseEntity(String description){
        ErrorDescription errorDescription;
        try{
            errorDescription = new ErrorDescription(description,errors);
        }catch (CustomErrorDescriptionNotFoundException e){
            errorDescription = new ErrorDescription(CustomErrorDescriptionNotFoundException.description,HttpStatus.INTERNAL_SERVER_ERROR.value(),"");
        }
        return new ResponseEntity<>(errorDescription,HttpStatus.valueOf(errorDescription.getStatus()));
    }

    /**
     * The method writes the json matching the description to the httpServletResponse
     * @param httpServletResponse the stream in which the json value writes
     * @param description from the file "SecurityErrorsDescription.json"
     */
    private static void handleHttpServletException(HttpServletResponse httpServletResponse, String description){
        try{
            ErrorDescription errorDescription = new ErrorDescription(description,errors);
            httpServletResponse.setStatus(errorDescription.getStatus());
            httpServletResponse.getWriter().write(JsonUtils.toJSON(errorDescription));
        }catch (IOException | CustomErrorDescriptionNotFoundException  e){
            httpServletResponse.setStatus(500);
        }
    }
}
