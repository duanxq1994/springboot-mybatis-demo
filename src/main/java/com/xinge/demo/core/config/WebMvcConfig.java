package com.xinge.demo.core.config;

import com.xinge.demo.core.exception.GlobalHandlerExceptionResolver;
import com.xinge.demo.core.format.TimeStampFormatAnnotationFormatterFactory;
import com.xinge.demo.core.interception.LoggerInterceptor;
import com.xinge.demo.core.interception.UserLoginInterception;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * web配置
 * @author duanxq
 * @date 2017/4/18
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterception());
        registry.addInterceptor(new LoggerInterceptor());
        super.addInterceptors(registry);
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new GlobalHandlerExceptionResolver());
        super.configureHandlerExceptionResolvers(exceptionResolvers);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new TimeStampFormatAnnotationFormatterFactory());
        super.addFormatters(registry);
    }
}
