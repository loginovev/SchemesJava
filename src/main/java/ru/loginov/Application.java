package ru.loginov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

/**
 * Main application
 * @author Loginov Evgeny
 * @version 1.0.0.1
 *
 */

@SpringBootApplication
public class Application {

    /**
     * This method run application
     * @param args external parameters are transferred to this parameter
     */
    public static void main(String[] args){

        SpringApplication.run(Application.class, args);
        CustomLogManager.info(
                Application.class.getName(),
                "SYSTEM",
                LoggerEvents.START_SYSTEM,
                LoggerAppStructure.Undefined,
                "",
                "Application started"
        );
    }
}
