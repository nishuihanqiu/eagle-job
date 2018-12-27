package com.lls.eagle.netty;

import com.lls.api.eagle.config.ClientConfig;
import com.lls.api.eagle.enums.ChannelState;
import com.lls.api.eagle.exception.EagleFrameworkException;
import com.lls.api.eagle.exception.TransportException;
import com.lls.api.eagle.rpc.Request;
import com.lls.api.eagle.rpc.Response;
import com.lls.api.eagle.rpc.ResponseFuture;
import com.lls.api.eagle.transport.AbstractSharedPoolClient;
import com.lls.api.eagle.transport.SharedObjectFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/************************************
 * NettyClient
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public class NettyClient extends AbstractSharedPoolClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

    private static ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(4);

    protected ConcurrentMap<Long, ResponseFuture> callbackMap = new ConcurrentHashMap<>();
    private ScheduledFuture<?> timeMonitorFuture;
    private Bootstrap bootstrap;
    private AtomicLong errorCount = new AtomicLong(0);
    public static final int NETTY_TIMEOUT_TIMER_PERIOD = 100;


    public NettyClient(ClientConfig clientConfig) {
        super(clientConfig);
        timeMonitorFuture = scheduledExecutor.scheduleWithFixedDelay(new TimeoutMonitor("timeout_monitor_" + clientConfig.getHost() + "_" + clientConfig.getPort(),
                this.callbackMap), NETTY_TIMEOUT_TIMER_PERIOD, NETTY_TIMEOUT_TIMER_PERIOD, TimeUnit.MILLISECONDS);

    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    protected SharedObjectFactory createChannelFactory() {
        return new NettyChannelFactory(this);
    }

    @Override
    public Response send(Request request) throws TransportException {
        return null;
    }

    @Override
    public boolean open() {
        if (isAvailable()) {
            return true;
        }

        bootstrap = new Bootstrap();
        int timeout = clientConfig.getConnectTimeout();
        if (timeout <= 0) {
            throw new EagleFrameworkException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.");
        }
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

        final int maxContentLength = clientConfig.getMaxContentLength();
        bootstrap.group(nioEventLoopGroup)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                }
            });

        // 初始化连接池
        initPool();

        logger.info("NettyClient finish Open: url={}", clientConfig.getUrl());

        // 设置可用状态
        state = ChannelState.ALIVE;
        return true;
    }

    @Override
    public synchronized boolean close() {
        return close(0);
    }

    @Override
    public synchronized boolean close(int timeout) {
        if (state.isCloseState()) {
            logger.info("netty client close fail: already close, url={}", clientConfig.getUrl());
            return false;
        }

        // 如果当前nettyClient还没有初始化，那么就没有close的理由。
        if (state.isUnInitState()) {
            logger.info("netty client close failed, due to not init => url:{}", clientConfig.getUrl());
            return false;
        }

        try {
            // 取消定期的回收任务
            timeMonitorFuture.cancel(true);
            // 清空callback
            callbackMap.clear();
            // 设置close状态
            state = ChannelState.CLOSE;
            // 关闭client持有的channel
            closeAllChannels();
            logger.info("NettyClient close Success: url={}", clientConfig.getUrl());
            return true;
        } catch (Exception e) {
            logger.error("nettyClient close failed: url={}, due to " + e.getMessage(), clientConfig.getUrl());
            return false;
        }
    }

    @Override
    public boolean isClosed() {
        return state.isCloseState();
    }

    @Override
    public boolean isAvailable() {
        return state.isAliveState();
    }
}
