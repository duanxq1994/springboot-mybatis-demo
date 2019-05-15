package com.xinge.demo;

import com.alibaba.fastjson.JSON;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.util.ArrayList;


/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 */
public class CodeGenerator {

    private static Configuration config;

    static {
        try {
            config = new ConfigurationParser(new ArrayList<String>()).parseConfiguration(CodeGenerator.class.getResourceAsStream("/generatorConfig.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //true:覆盖生成
        DefaultShellCallback callback = new DefaultShellCallback(true);
        try {
            ArrayList<String> warning = new ArrayList<>();
            new MyBatisGenerator(config, callback, warning).generate(null);
            if (!warning.isEmpty()) {
                System.out.println(JSON.toJSONString(warning));
            }
        } catch (Exception e) {
            throw new RuntimeException("生成失败", e);
        }
    }

}
