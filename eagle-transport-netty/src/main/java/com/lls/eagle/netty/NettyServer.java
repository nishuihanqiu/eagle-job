package com.lls.eagle.netty;

import com.lls.api.eagle.config.ClientConfig;
import com.lls.api.eagle.core.StandardThreadExecutor;
import com.lls.api.eagle.exception.EagleFrameworkException;
import com.lls.api.eagle.exception.TransportException;
import com.lls.api.eagle.rpc.Request;
import com.lls.api.eagle.rpc.Response;
import com.lls.api.eagle.transport.AbstractServer;
import com.lls.api.eagle.transport.MessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/************************************
 * NettyServer
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public class NettyServer extends AbstractServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private NettyServerChannelManage channelManage = null;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;
    private MessageHandler messageHandler;
    private StandardThreadExecutor standardThreadExecutor = null;

    public NettyServer(ClientConfig clientConfig, MessageHandler messageHandler) {
        super(clientConfig);
        this.messageHandler = messageHandler;
    }

    @Override
    public boolean isBound() {
        return (serverChannel != null && serverChannel.isActive());
    }

    @Override
    public Response send(Request request) throws TransportException {
        throw new EagleFrameworkException("NettyServer request(Request request) method not support: url: " + clientConfig.getUrl());
    }

    @Override
    public boolean open() {
        if (isAvailable()) {
            logger.warn("NettyServer ServerChannel already Open: url=" + clientConfig.getUrl());
            return state.isAliveState();
        }

        if (bossGroup == null) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
        }
        logger.info("NettyServer ServerChannel start Open: url=" + clientConfig.getUrl());

        return false;
    }

    @Override
    public boolean close() {
        return close(0);
    }

    @Override
    public boolean close(int timeout) {
        return false;
    }

    @Override
    public boolean isClosed() {
        return state.isCloseState();
    }

    @Override
    public boolean isAvailable() {
        return state.isAliveState();
    }

    @Override
    public ClientConfig getClientConfig() {
        return clientConfig;
    }

}
