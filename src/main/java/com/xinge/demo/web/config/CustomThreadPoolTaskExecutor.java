package com.xinge.demo.web.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author duanxq
 * @date 2017/9/21
 */
public class CustomThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    public CustomThreadPoolTaskExecutor() {
        super();
        setCorePoolSize(10);
        setMaxPoolSize(50);
        setQueueCapacity(100);
        setKeepAliveSeconds(300);
        setThreadNamePrefix("taskExecutor-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
