package com.xinge.demo.common.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 与snowflake算法区别,返回字符串id,占用更多字节,但直观从id中看出生成时间
 *
 * @author wgy-pc
 * @date 2016/7/19
 */
@Slf4j
public enum IdGenerator {


    INSTANCE;

    /**
     * id不重复周期
     */
    public enum GenerateType {
        DAY(1000 * 60 * 60 * 24, "yyyyMMdd"),
        HOUR(1000 * 60 * 60, "yyyyMMddHH"),
        MIN(1000 * 60, "yyyyMMddHHmm"),
        SEC(1000, "yyyyMMddHHmmss"),
        MIL(1, "yyyyMMddHHmmssSSS"),;

        private long fixTime;

        private String dateFormat;


        GenerateType(long time, String format) {
            this.fixTime = time;
            this.dateFormat = format;
        }

        public long getFixTime() {
            return fixTime;
        }

        public String getDateFormat() {
            return dateFormat;
        }
    }

    private GenerateType generateType = GenerateType.SEC;
    /** 用ip地址最后几个字节标示 */
    private long workerId;
    /** 可配置在properties中,启动时加载,此处默认先写成0 */
    private long datacenterId = 0L;
    private long sequence = 0L;
    /** 节点ID长度 */
    private long workerIdBits = 8L;
    /** 数据中心ID长度,可根据时间情况设定位数 */
    private long datacenterIdBits = 2L;
    /** 序列号12位，这里控制周期内可出现的id个数*/
    private long sequenceBits = 12L;
    /** 机器节点左移12位 */
    private long workerIdShift = sequenceBits;
    /** 数据中心节点左移14位 */
    private long datacenterIdShift = sequenceBits + workerIdBits;
    /** 4095 */
    private long sequenceMask = ~(-1L << sequenceBits);
    private long lastTimestamp = -1L;

    IdGenerator() {
//        workerId = 0x000000FF & getLastIP();
        //不考虑机器IP
        workerId = 0;
    }

    public synchronized String nextId() {
        //获取当前时间
        long timestamp = timeGen();
        //如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果上次生成时间和当前时间相同
        if (lastTimestamp == timestamp) {
            //sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & sequenceMask;
            //判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
            if (sequence == 0) {
                //自旋等待到下一周期
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //如果和上次生成时间不同,重置sequence，就是下一周期开始，sequence计数重新从0开始累加
            sequence = 0L;
        }
        lastTimestamp = timestamp;


        long suffix = (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;

        String datePrefix = DateUtil.format(new Date(lastTimestamp * this.generateType.fixTime), this.generateType.dateFormat);

        //从1开始
        suffix = 10000 + suffix + 1;
        return datePrefix + String.valueOf(suffix).substring(1);
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis() / this.generateType.fixTime;
    }

    private byte getLastIP() {
        byte lastip = 0;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            byte[] ipByte = ip.getAddress();
            lastip = ipByte[ipByte.length - 1];
        } catch (UnknownHostException e) {
            log.error("", e);
        }
        return lastip;
    }
}
