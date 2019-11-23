package com.xinge.demo.model;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class Retry {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 下次重试时间
     */
    private Date retryTime;

    private String className;

    private String methodName;

    private String paramTypes;

    private Integer attemptTimes;

    private Integer status;

    private byte[] paramValueJsonStr;
}