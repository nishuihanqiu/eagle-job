package com.lls.eagle.netty;

import com.lls.api.eagle.rpc.ResponseFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/************************************
 * TimeoutMonitor
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
class TimeoutMonitor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TimeoutMonitor.class);
    private String name;
    private ConcurrentMap<Long, ResponseFuture> callbackMap;

    public TimeoutMonitor(String name, ConcurrentMap<Long, ResponseFuture> callbackMap) {
        this.name = name;
        this.callbackMap = callbackMap;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<Long, ResponseFuture> entry : this.callbackMap.entrySet()) {
            try {
                ResponseFuture future = entry.getValue();
                if (future.getCreateTime() + future.getTimeout() < currentTime) {
                    this.callbackMap.remove(entry.getKey());
                    future.cancel();
                }
            } catch (Exception e) {
                logger.error(name + " clear timeout future Error: " + " requestId=" + entry.getKey(), e);
            }
        }
    }
}
