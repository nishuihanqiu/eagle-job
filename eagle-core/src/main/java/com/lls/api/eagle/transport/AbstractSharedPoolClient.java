package com.lls.api.eagle.transport;

import com.lls.api.eagle.config.ClientConfig;
import com.lls.api.eagle.core.EagleThreadFactory;
import com.lls.api.eagle.core.StandardThreadExecutor;
import com.lls.api.eagle.exception.EagleServiceException;
import com.lls.api.eagle.util.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/************************************
 * AbstractSharedPoolClient
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public abstract class AbstractSharedPoolClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSharedPoolClient.class);

    private static final int DEFAULT_MIN_CONNECTIONS = 2;
    private static final ThreadPoolExecutor executor = new StandardThreadExecutor(1, 300, 20000,
        new EagleThreadFactory("AbstractPoolClient-initPool-", true));
    private final AtomicInteger idx = new AtomicInteger();
    protected SharedObjectFactory factory;
    protected ArrayList<Channel> channels;
    protected int connections;


    public AbstractSharedPoolClient(ClientConfig clientConfig) {
        super(clientConfig);
        connections = clientConfig.getMinClientConnection();
        if (connections <= 0) {
            connections = DEFAULT_MIN_CONNECTIONS;
        }
    }

    protected void initPool() {
        factory = createChannelFactory();
        channels = new ArrayList<>(connections);
        for (int i = 0; i < connections; i++) {
            channels.add((Channel) factory.makeObject());
        }
        initConnections(clientConfig.isAsyncInitConnection());
    }

    protected abstract SharedObjectFactory createChannelFactory();

    protected void initConnections(boolean async) {
        if (!async) {
            createConnections();
            return;
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                createConnections();
            }
        });
    }

    private void createConnections() {
        for (Channel channel : channels) {
           try {
               channel.open();
           } catch (Exception e) {
               logger.error("NettyClient init pool create connect Error: url=" + clientConfig.getUrl(), e);
           }
        }
    }

    @SuppressWarnings("unchecked")
    protected Channel getChannel() throws EagleServiceException {
        int index = MathUtils.getNonNegative(idx.getAndIncrement());
        Channel channel;

        for (int i = index; i < connections + index; i++) {
            channel = channels.get(i % connections);
            if (channel.isAvailable()) {
                return channel;
            }
            factory.rebuildObject(channel);
        }

        String message = this.getClass().getSimpleName() + " getChannel Error: url=" + clientConfig.getUrl();
        logger.error(message);
        throw new EagleServiceException(message);
    }

    protected void closeAllChannels() {
        for (Channel channel : channels) {
            channel.close();
        }
    }
}
