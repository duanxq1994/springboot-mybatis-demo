package com.xinge.demo.core.retry.message;

/**
 * @author BG343674
 * created by BG343674 on 2019/11/24
 */

import java.util.function.Consumer;

public interface RetryMessage {

    /**
     * 在指定时间后通知
     * @param consumer
     * @param time
     */
    void noticeAfter(Consumer<Object> consumer, Long time);



}
