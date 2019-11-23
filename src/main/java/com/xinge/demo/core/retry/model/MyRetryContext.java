package com.xinge.demo.core.retry.model;

import lombok.Data;

import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author BG343674
 * created by BG343674 on 2019/9/20
 */
@Data
public class MyRetryContext implements Delayed {

    /**
     * 任务id
     */
    private String uuid;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 已重试次数
     */
    private int retryCount;
    /**
     *  下次重试时间
     */
    private Long time;
    /**
     * 对象信息
     */
    private RetryTargetInfo retryTargetInfo;
    /**
     * 异常信息
     */
    private Throwable exception;
    /**
     * 执行成功
     */
    private Integer status;



    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o == null || !(o instanceof MyRetryContext)) {
            return 1;
        }
        if (this == o) {
            return 0;
        }
        MyRetryContext myRetryContext = (MyRetryContext) o;
        if (Objects.equals(this.time, myRetryContext.getTime())) {
            return 0;
        }
        if (this.time < myRetryContext.getTime()) {
            return -1;
        }
        return 1;
    }
}
