package com.lls.api.eagle.transport;

import com.lls.api.eagle.config.ClientConfig;
import com.lls.api.eagle.core.EagleThreadFactory;
import com.lls.api.eagle.core.StandardThreadExecutor;
import com.lls.api.eagle.exception.EagleServiceException;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/************************************
 * AbstractPoolClient
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public abstract class AbstractPoolClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPoolClient.class);

    private static StandardThreadExecutor executor = new StandardThreadExecutor(1, 300, 20000,
        new EagleThreadFactory("AbstractPoolClient-init-pool-", true));
    protected static long defaultMinEvictableIdleTimeMillis = (long) 1000 * 60 * 60;//默认链接空闲时间
    protected static long defaultSoftMinEvictableIdleTimeMillis = (long) 1000 * 60 * 10;//
    protected static long defaultTimeBetweenEvictionRunsMillis = (long) 1000 * 60 * 10;//默认回收周期
    protected GenericObjectPool pool;
    protected GenericObjectPool.Config poolConfig;
    protected PoolableObjectFactory factory;


    public AbstractPoolClient(ClientConfig clientConfig) {
        super(clientConfig);
    }

    @SuppressWarnings("unchecked")
    protected void initPool() {
        poolConfig = new GenericObjectPool.Config();
        poolConfig.minIdle = clientConfig.getMinClientConnection();
        poolConfig.maxIdle = clientConfig.getMaxClientConnection();
        poolConfig.maxActive = poolConfig.maxIdle;
        poolConfig.maxWait = clientConfig.getRequestTimeout();
        poolConfig.lifo = clientConfig.isPoolLifo();

        poolConfig.minEvictableIdleTimeMillis = defaultMinEvictableIdleTimeMillis;
        poolConfig.softMinEvictableIdleTimeMillis = defaultSoftMinEvictableIdleTimeMillis;
        poolConfig.timeBetweenEvictionRunsMillis = defaultTimeBetweenEvictionRunsMillis;

        factory = createChannelFactory();
        pool = new GenericObjectPool(factory, poolConfig);

        boolean lazyInit = clientConfig.isLazyInit();
        if (!lazyInit) {
            initConnection(true);
        }
    }

    protected void initConnection(boolean async) {
        if (!async) {
            this.createConnections();
            return;
        }

        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    createConnections();
                    logger.info("async initPool success! url:{}", clientConfig.getUrl());
                }
            });
        } catch (Exception e) {
            logger.error("async init-pool exception! error:" + e.getMessage(), e);
        }
    }

    private void createConnections() {
        for (int i = 0; i < poolConfig.minIdle; i++) {
            try {
                pool.addObject();
            } catch (Exception e) {
                logger.error("NettyClient init pool create connect Error: url=" + clientConfig.getUrl(), e);
            }
        }
    }

    protected abstract BasePoolableObjectFactory createChannelFactory();

    protected Channel borrowObject() throws Exception {
        Channel nettyChannel = (Channel) pool.borrowObject();

        if (nettyChannel != null && nettyChannel.isAvailable()) {
            return nettyChannel;
        }

        invalidateObject(nettyChannel);
        String message = this.getClass().getSimpleName() + " borrowObject Error: url=" + clientConfig.getUrl();
        logger.error(message);
        throw new EagleServiceException(message);
    }

    @SuppressWarnings("unchecked")
    protected void invalidateObject(Channel nettyChannel) {
        if (nettyChannel == null) {
            return;
        }
        try {
            pool.invalidateObject(nettyChannel);
        } catch (Exception e) {
            logger.error(this.getClass().getSimpleName() + " invalidate client Error: url=" + clientConfig.getUrl(), e);
        }
    }

    @SuppressWarnings("unchecked")
    protected void returnObject(Channel channel) {
        if (channel == null) {
            return;
        }
        try {
            pool.returnObject(channel);
        } catch (Exception ie) {
            logger.error(this.getClass().getSimpleName() + " return client Error: url=" + clientConfig.getUrl(), ie);
        }
    }

}
