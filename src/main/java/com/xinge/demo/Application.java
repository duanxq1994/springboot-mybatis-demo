package com.xinge.demo;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.SQLException;

/**
 * @author duanxq
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableAsync
@MapperScan("com.xinge.demo.mapper")
@Slf4j
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        startH2Server();
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        startH2Server();
        return builder.sources(Application.class);
    }

    private static void startH2Server() {
        try {
            Server h2Server = Server.createTcpServer().start();
            if (h2Server.isRunning(true)) {
                log.info("H2 server was started and is running.");
            } else {
                throw new RuntimeException("Could not start H2 server.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start H2 server: ", e);
        }
    }

}
