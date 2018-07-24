package ru.loginov.configuration;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import ru.loginov.utils.VoidResponseHandlerInterceptor;

/**
 * Web configuration
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * The method connects interceptor which instead of 'void' returns '{}'
     * @param registry the param for adding interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);

        registry.addInterceptor(new VoidResponseHandlerInterceptor());
    }
}
