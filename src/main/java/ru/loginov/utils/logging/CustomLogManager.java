package ru.loginov.utils.logging;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.spi.ExtendedLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * The class for custom logging
 */
public class CustomLogManager extends LogManager {

    private static final String FQCN = CustomLogManager.class.getName();

    private static Map<Object,Logger> loggerMap = new HashMap<>();

    private static Logger getInnerLogger(String className){
        return LogManager.getLogger(CustomLogManager.class.getName(), className);
    }

    private static Logger getAppLogger(String className){
        Logger logger = loggerMap.get(className);
        if (logger==null){
            logger = CustomLogManager.getInnerLogger(className);
            loggerMap.put(className,logger);
        }

        return logger;
    }

    private static void logIfEnabled(String className, String username, LoggerEvents event,LoggerAppStructure structure, String dataReference, String message, Level level){
        ExtendedLogger logger = (ExtendedLogger) getAppLogger(className);
        // Add to the ThreadContext map for this try block only;
        try (
                final CloseableThreadContext.Instance ctc = CloseableThreadContext
                        .put("username", username)
                        .put("event",event.name())
                        .put("structure",structure.name())
                        .put("data",dataReference)
        )
        {
            logger.logIfEnabled(FQCN, level, (Marker)null, (String)message, (Throwable)((Throwable)null));
        }
    }

    //INFO
    public static void info(String className, String username, LoggerEvents event, LoggerAppStructure structure, String dataReference, String message){
        logIfEnabled(className, username, event, structure, dataReference, message, Level.INFO);
    }

    public static void info(String className, String username, String message){
        info(className, username, LoggerEvents.SYSTEM, LoggerAppStructure.Undefined,"", message);
    }

    public static void info(String className, String message){
        info(className, "SYSTEM", LoggerEvents.SYSTEM, LoggerAppStructure.Undefined, "", message);
    }

    //ERROR
    public static void error(String className, String username, LoggerEvents event, LoggerAppStructure structure, String dataReference, String message){
        logIfEnabled(className, username, event, structure, dataReference, message, Level.ERROR);
    }

    public static void error(String className, String username, String message){
        error(className, username, LoggerEvents.SYSTEM, LoggerAppStructure.Undefined, "", message);
    }

    public static void error(String className, String message){
        error(className, "SYSTEM", LoggerEvents.SYSTEM, LoggerAppStructure.Undefined, "", message);
    }

    //WARN
    public static void warn(String className, String username, LoggerEvents event, LoggerAppStructure structure, String dataReference, String message){
        logIfEnabled(className, username, event, structure, dataReference, message, Level.WARN);
    }

    public static void warn(String className, String username, String message){
        warn(className, username, LoggerEvents.SYSTEM, LoggerAppStructure.Undefined, "", message);
    }

    public static void warn(String className, String message){
        warn(className, "SYSTEM", LoggerEvents.SYSTEM, LoggerAppStructure.Undefined, "", message);
    }

    //DEBUG
    public static void debug(String className, String username, LoggerEvents event, LoggerAppStructure structure, String dataReference, String message){
        logIfEnabled(className, username, event, structure, dataReference, message, Level.DEBUG);
    }

    public static void debug(String className, String username, String message){
        debug(className, username, LoggerEvents.SYSTEM, LoggerAppStructure.Undefined, "", message);
    }

    public static void debug(String className, String message){
        debug(className, "SYSTEM", LoggerEvents.SYSTEM, LoggerAppStructure.Undefined, "", message);
    }
}
