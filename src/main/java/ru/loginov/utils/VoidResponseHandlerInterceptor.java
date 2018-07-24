package ru.loginov.utils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

/**
 * The class applies returning '{}' instead of empty string.
 * It is connected by ru.loginov.configuration.WebConfig.addInterceptors.
 */
public class VoidResponseHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final String voidResponse = "{}";

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (!response.isCommitted()) {
            // Used ModelAndView?
            if (modelAndView != null) {
                return;
            }
            // Access static resource?
            if (DefaultServletHttpRequestHandler.class == handler.getClass()) {
                return;
            }
            response.setStatus(200);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try (final Writer writer = response.getWriter()) {
                writer.write(voidResponse);
            }
            response.flushBuffer();
        }
    }
}
