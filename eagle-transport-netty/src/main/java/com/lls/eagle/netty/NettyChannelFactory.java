package com.lls.eagle.netty;

import com.lls.api.eagle.core.EagleThreadFactory;
import com.lls.api.eagle.core.StandardThreadExecutor;
import com.lls.api.eagle.transport.SharedObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/************************************
 * NettyChannelFactory
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public class NettyChannelFactory implements SharedObjectFactory<NettyChannel> {
    private static final Logger logger = LoggerFactory.getLogger(NettyChannelFactory.class);
    private static final ExecutorService rebuildExecutorService = new StandardThreadExecutor(5, 30, 10L, TimeUnit.SECONDS, 100,
        new EagleThreadFactory("RebuildExecutorService", true),
        new ThreadPoolExecutor.CallerRunsPolicy());

    private NettyClient nettyClient;
    private String factoryName;

    public NettyChannelFactory(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
        this.factoryName = "NettyChannelFactory_" + nettyClient.getClientConfig().getHost() + "_" + nettyClient.getClientConfig().getPort();
    }

    @Override
    public NettyChannel makeObject() {
        return new NettyChannel(nettyClient);
    }

    @Override
    public boolean rebuildObject(NettyChannel nettyChannel) {
        ReentrantLock lock = nettyChannel.getLock();
        if (lock.tryLock()) {
            try {
                if (!nettyChannel.isAvailable() && !nettyChannel.isReconnect()) {
                    nettyChannel.reconnect();
                    rebuildExecutorService.submit(new RebuildTask(nettyChannel));
                }
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return factoryName;
    }

    class RebuildTask implements Runnable {

        private NettyChannel channel;

        public RebuildTask(NettyChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            try {
                channel.getLock().lock();
                channel.close();
                channel.open();
            } catch (Exception e) {
                logger.error("rebuild error: " + this.toString() + ", " + channel.getClientConfig().getUrl(), e);
            } finally {
                channel.getLock().unlock();
            }
        }
    }

}
