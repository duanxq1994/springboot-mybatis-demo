package com.xinge.demo.core.format;

import org.springframework.format.Formatter;

import java.util.Date;
import java.util.Locale;

/**
 *
 * @author duanxq
 * @date 2017/12/14
 */
public class TimeStampFormatter implements Formatter<Date>{
    @Override
    public Date parse(String text, Locale locale) {
        return new Date(Long.parseLong(text));
    }

    @Override
    public String print(Date object, Locale locale) {
        return object == null ? null : String.valueOf(object.getTime());
    }
}
