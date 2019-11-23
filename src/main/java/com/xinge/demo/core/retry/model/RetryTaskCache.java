package com.xinge.demo.core.retry.model;

import java.util.List;

/**
 * @author BG343674
 * created by BG343674 on 2019/11/23
 */
public interface RetryTaskCache {
    /**
     * 获取所有重试任务
     * @return
     */
    List<MyRetryContext> getAllRetryTask();

    /**
     * 获取重试任务
     * @param key
     * @return
     */
    MyRetryContext get(Object key);

    /**
     * 保存重试任务
     * @param key
     * @param value
     */
    void put(Object key, MyRetryContext value);

    /**
     * 修改重试任务
     */
    void update(MyRetryContext retryContext);

    /**
     *
     * @param retryContext
     */
    void updateStatus(MyRetryContext retryContext);

}
