package com.xinge.demo.core.retry.model;

import com.xinge.demo.common.util.ObjectSerializerImpl;
import com.xinge.demo.mapper.RetryMapper;
import com.xinge.demo.model.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author BG343674
 * created by BG343674 on 2019/11/23
 */
@Component
public class RetryTaskCacheDbImpl implements RetryTaskCache {

    @Autowired
    private RetryMapper retryMapper;

    private ObjectSerializerImpl<RetryTargetInfo> objectSerializer = new ObjectSerializerImpl<>();


    @Override
    public List<MyRetryContext> getAllRetryTask() {
        List<Retry> retries = retryMapper.selectTask();
        List<MyRetryContext> contexts = new ArrayList<>();
        for (Retry retry : retries) {
            MyRetryContext myRetryContext = new MyRetryContext();
            RetryTargetInfo retryTargetInfo = objectSerializer.deserialize(retry.getParamValueJsonStr());
            myRetryContext.setRetryTargetInfo(retryTargetInfo);
            myRetryContext.setStartTime(retry.getStartTime().getTime());
            myRetryContext.setTime(retry.getRetryTime().getTime());
            myRetryContext.setUuid(retry.getUuid());
            contexts.add(myRetryContext);
        }
        return contexts;
    }

    @Override
    public MyRetryContext get(Object key) {
        Example uuid1 = Example.builder(Retry.class).where(
                Sqls.custom().andEqualTo("uuid", key)
        ).build();
        List<Retry> retries = retryMapper.selectByExample(uuid1);
        if (retries == null || retries.isEmpty()) {
            return null;
        }
        return new MyRetryContext();
    }

    @Override
    public void put(Object key, MyRetryContext value) {
        if (get(key) != null) {
            updateStatus(value);
            return;
        }
        Retry retry = new Retry();
        retry.setAttemptTimes(value.getRetryCount());
        RetryTargetInfo retryTargetInfo = value.getRetryTargetInfo();
        retry.setClassName(retryTargetInfo.getClazz().getSimpleName());
        retry.setMethodName(retryTargetInfo.getMethod());
        byte[] serialize = objectSerializer.serialize(retryTargetInfo);
        retry.setParamValueJsonStr(serialize);
        retry.setRetryTime(new Date(value.getTime()));
        retry.setStartTime(new Date());
        retry.setStatus(1);
        retry.setUuid(value.getUuid());
        retryMapper.insertSelective(retry);
    }

    @Override
    public void update(MyRetryContext retryContext) {
        RetryTargetInfo retryTargetInfo = retryContext.getRetryTargetInfo();
        Example uuid1 = Example.builder(Retry.class).where(
                Sqls.custom().andEqualTo("uuid", retryContext.getUuid())
        ).build();
        Retry update = new Retry();
        update.setStatus(2);
        retryMapper.updateByExampleSelective(update, uuid1);
    }

    @Override
    public void updateStatus(MyRetryContext retryContext) {
        RetryTargetInfo retryTargetInfo = retryContext.getRetryTargetInfo();
        Example uuid1 = Example.builder(Retry.class).where(
                Sqls.custom().andEqualTo("uuid", retryContext.getUuid())
        ).build();
        Retry update = new Retry();
        update.setStatus(retryContext.getStatus());
        update.setAttemptTimes(retryContext.getRetryCount());
        update.setRetryTime(new Date(retryContext.getTime()));
        retryMapper.updateByExampleSelective(update, uuid1);
    }
}
