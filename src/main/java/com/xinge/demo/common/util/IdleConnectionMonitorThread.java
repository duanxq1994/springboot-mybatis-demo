package com.xinge.demo.common.util;

import org.apache.http.conn.HttpClientConnectionManager;

import java.util.concurrent.TimeUnit;


/**
 * 功能说明: httpclient连接回收器
 * @author duanxq
 * @date 2018/01/23
 */
public class IdleConnectionMonitorThread extends Thread {

    private final HttpClientConnectionManager connMgr;

    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
        super();
        //设置为守护线程
        super.setDaemon(true);
        this.connMgr = connMgr;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 关闭失效的连接
            connMgr.closeExpiredConnections();
            // 可选的, 关闭30秒内不活动的连接
            connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
        }
    }

}
