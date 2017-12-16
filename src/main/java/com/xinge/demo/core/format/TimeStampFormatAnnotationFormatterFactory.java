package com.xinge.demo.core.format;

import com.xinge.demo.core.annotation.TimeStampFormat;
import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 时间戳注解 格式转换器
 * @author duanxq
 * @date 2017/12/14
 */
public class TimeStampFormatAnnotationFormatterFactory extends EmbeddedValueResolutionSupport implements AnnotationFormatterFactory<TimeStampFormat>{
    @Override
    public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(Date.class);
        return classes;
    }

    @Override
    public Printer<?> getPrinter(TimeStampFormat annotation, Class<?> fieldType) {
        return new TimeStampFormatter();
    }

    @Override
    public Parser<?> getParser(TimeStampFormat annotation, Class<?> fieldType) {
        return new TimeStampFormatter();
    }
}
