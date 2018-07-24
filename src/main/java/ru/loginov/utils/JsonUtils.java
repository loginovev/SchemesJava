package ru.loginov.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * The class for JSON
 */
public class JsonUtils {

    public static String toJSON(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public static <T> T fromJSON(String json,Class<T> type) throws IOException {
        return new ObjectMapper().readValue(json,type);
    }

    public static <T> T fromFileJSON(String filePath,Class<T> type) throws IOException {

        ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] {});
        Resource resource = appContext.getResource(filePath);

        T value = null;
        try(FileInputStream fileInputStream = new FileInputStream(resource.getFile())){
            value = new ObjectMapper().readValue(fileInputStream,type);
        }
        return value;
    }
}
