package com.xinge.demo;

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
            new MyBatisGenerator(config, callback, new ArrayList<String>()).generate(null);
        } catch (Exception e) {
            throw new RuntimeException("生成失败", e);
        }
    }

}
