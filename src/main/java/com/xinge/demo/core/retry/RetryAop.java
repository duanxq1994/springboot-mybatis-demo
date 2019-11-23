package com.xinge.demo.core.retry;

import com.xinge.demo.core.retry.annotation.Retry;
import com.xinge.demo.core.retry.model.MyRetryContext;
import com.xinge.demo.core.retry.model.RetryTargetInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author BG343674
 * created by BG343674 on 2019/9/19
 */
@Aspect
@Component
public class RetryAop {


    @Autowired
    private RetryTaskActuator retryTaskActuator;

    /**
     * 扫描所有带retry注解的方法
     * 抛出异常后进行重试
     * @param point
     * @param annotation
     * @return
     */
    @Around("execution(public * com.xinge.demo..*.*(..)) && @annotation(annotation)")
    public Object after(ProceedingJoinPoint point, Retry annotation) throws Throwable {
        String threadNamePrefix = retryTaskActuator.getMethodInvokeThreadExecutor().getThreadNamePrefix();
        boolean isRetry = Thread.currentThread().getName().startsWith(threadNamePrefix);

        MyRetryContext retryContext = getTaskContext(point, isRetry);
        try {
            Object proceed = point.proceed(point.getArgs());
            if (isRetry) {
                retryTaskActuator.addRetryTask(retryContext);
            }
            return proceed;
        } catch (Throwable throwable) {
            retryContext.setException(throwable);
            retryTaskActuator.addRetryTask(retryContext);
            throw throwable;
        }
    }

    /**
     * 如果是重试 uuid不变
     * @return
     */
    private MyRetryContext getTaskContext(ProceedingJoinPoint point, Boolean isRetry) throws NoSuchMethodException {
        if (isRetry) {
            MyRetryContext currentTaskContext = retryTaskActuator.getCurrentTaskContext();
            currentTaskContext.setRetryCount(currentTaskContext.getRetryCount() + 1);
            return currentTaskContext;
        }
        final Method method = getCurrentMethod(point);
        MyRetryContext retryContext = new MyRetryContext();
        // 获取uuid
        retryContext.setUuid(UUID.randomUUID().toString());
        RetryTargetInfo retryTargetInfo = new RetryTargetInfo();
        retryTargetInfo.setClazz(point.getTarget().getClass());
        retryTargetInfo.setMethod(method.getName());
        retryTargetInfo.setArgsType(method.getParameterTypes());
        retryTargetInfo.setArgs(point.getArgs());
        retryContext.setRetryTargetInfo(retryTargetInfo);
        retryContext.setStatus(1);
        return retryContext;
    }

    /**
     * 获取当前方法信息
     *
     * @param point 切点
     * @return 方法
     */
    private Method getCurrentMethod(JoinPoint point) throws NoSuchMethodException {
        Signature sig = point.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        Object target = point.getTarget();
        return target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
    }

}
