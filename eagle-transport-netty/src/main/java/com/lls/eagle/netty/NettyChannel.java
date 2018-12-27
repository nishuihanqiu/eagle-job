package com.lls.eagle.netty;

import com.lls.api.eagle.codec.Codec;
import com.lls.api.eagle.config.ClientConfig;
import com.lls.api.eagle.enums.ChannelState;
import com.lls.api.eagle.exception.TransportException;
import com.lls.api.eagle.rpc.Request;
import com.lls.api.eagle.rpc.Response;
import com.lls.api.eagle.transport.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantLock;

/************************************
 * NettyChannel
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public class NettyChannel implements Channel {
    private static final Logger logger = LoggerFactory.getLogger(NettyChannel.class);
    private volatile ChannelState state = ChannelState.UN_INIT;
    private NettyClient nettyClient;
    private io.netty.channel.Channel channel;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;
    private ReentrantLock lock = new ReentrantLock();
    private Codec codec;

    public NettyChannel(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
        this.remoteAddress = new InetSocketAddress(nettyClient.getClientConfig().getHost(), nettyClient.getClientConfig().getPort());
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public Response send(Request request) throws TransportException {
        return null;
    }

    @Override
    public boolean open() {
        return false;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public boolean close(int timeout) {
        return false;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    public void reconnect() {
        state = ChannelState.INIT;
    }

    public boolean isReconnect() {
        return state.isInitState();
    }

    @Override
    public ClientConfig getClientConfig() {
        return null;
    }

    public ReentrantLock getLock() {
        return lock;
    }



}
