package com.xinge.demo.core.retry;

import com.xinge.demo.core.retry.model.MyRetryContext;
import com.xinge.demo.core.retry.model.RetryTargetInfo;
import com.xinge.demo.core.retry.model.RetryTaskCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * 重试任务执行器
 *
 * @author BG343674
 * created by BG343674 on 2019/11/23
 */
@Slf4j
@Component
public class RetryTaskActuator implements InitializingBean {


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RetryTaskCache retryTaskCache;

    @PostConstruct
    public void initFromCache() {
        List<MyRetryContext> allRetryTask = retryTaskCache.getAllRetryTask();
        for (MyRetryContext myRetryContext : allRetryTask) {
            myRetryContext.setTime(getNextRetryTime(myRetryContext));
        }
        queue.addAll(allRetryTask);
    }

    private DelayQueue<MyRetryContext> queue = new DelayQueue<>();


    public void addRetryTask(MyRetryContext retryContext) {
        boolean haveException = retryContext.getException() != null;
        if (!haveException) {
            retryContext.setStatus(4);
            retryTaskCache.put(retryContext.getUuid(), retryContext);
            return;
        }

        Long nextRetryTime = getNextRetryTime(retryContext );
        boolean noTimes = nextRetryTime == null;
        if (noTimes) {
            retryContext.setStatus(3);
            retryTaskCache.put(retryContext.getUuid(), retryContext);
            return;
        }
        if (retryContext.getRetryCount() == 0) {
            retryContext.setStatus(1);
        } else {
            retryContext.setStatus(2);
        }
        retryContext.setTime(nextRetryTime);
        retryTaskCache.put(retryContext.getUuid(), retryContext);
        queue.put(retryContext);
        log.info("queue size {} -> {}", queue.size() - 1, queue.size());
    }

    @Override
    public void afterPropertiesSet() {
        threadPoolTaskExecutor.execute(() -> {
            try {
                while (true) {
                    MyRetryContext take = queue.take();
                    RetryTargetInfo retryTargetInfo = take.getRetryTargetInfo();
                    log.info("获取重试任务成功，任务数 {} -> {}", queue.size() + 1, queue.size());
                    log.info("任务[{} {}#{}]开始第{}次重试", take.getUuid(), retryTargetInfo.getClass().getName(), retryTargetInfo.getMethod(), take.getRetryCount() + 1);
                    // 新开线程执行重试
                    getMethodInvokeThreadExecutor().execute(() -> {
                        try {
                            taskUuid.set(take);
                            retryTargetInfo.invoke();
                        } catch (Exception e) {
                            log.error("", e);
                        } finally {
                            taskUuid.remove();
                        }
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * 等待规则，获取第几次重试等待的时间
     *
     * @param retryContext 重试信息
     * @return 等待的时间（毫秒）
     */
    private static Long getNextRetryTime(MyRetryContext retryContext) {

        // 重试10次 秒数
        Integer[] wait = {1, 2, 3, 4, 5, 10, 20, 30, 40};
        int length = wait.length;
        int count = retryContext.getRetryCount();
        if (count + 1 > length) {
            return null;
        }
        return System.currentTimeMillis() +  TimeUnit.MILLISECONDS.convert(wait[count], TimeUnit.SECONDS);
    }

    public static final String RETRY_THREAD_NAME_PREFIX = "retry-thread-";

    /**
     * 获取执行重试任务的线程组
     * @return
     */
    public ThreadPoolTaskExecutor getMethodInvokeThreadExecutor() {
        return threadPoolTaskExecutor;
    }

    private ThreadLocal<MyRetryContext> taskUuid = new ThreadLocal<>();

    public MyRetryContext getCurrentTaskContext() {
        return taskUuid.get();
    }

}
