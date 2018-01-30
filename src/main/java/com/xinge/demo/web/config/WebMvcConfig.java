package com.xinge.demo.web.config;

import com.xinge.demo.core.exception.GlobalHandlerExceptionResolver;
import com.xinge.demo.core.format.TimeStampFormatAnnotationFormatterFactory;
import com.xinge.demo.core.interception.LoggerInterceptor;
import com.xinge.demo.core.interception.UserLoginInterception;
import com.xinge.demo.core.resolver.SessionArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.List;

/**
 * web配置
 * @author duanxq
 * @date 2017/4/18
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(new CustomThreadPoolTaskExecutor());
        super.configureAsyncSupport(configurer);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterception());
        registry.addInterceptor(new LoggerInterceptor());
        super.addInterceptors(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SessionArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new GlobalHandlerExceptionResolver());
        super.extendHandlerExceptionResolvers(exceptionResolvers);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new TimeStampFormatAnnotationFormatterFactory());
        super.addFormatters(registry);
    }
}
