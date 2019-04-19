package com.xinge.demo.core;

import com.alibaba.druid.sql.SQLUtils;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * @author BG343674
 * created by BG343674 on 2019/4/19
 */
public class P6SpyLogger implements MessageFormattingStrategy {


    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
        if (!"".equals(sql.trim())) {
            return "took " + elapsed + "ms | "
                    + category + " | connection " + connectionId + "\n" + SQLUtils.formatMySql(sql) + ";";
        }
        return "";
    }
}
