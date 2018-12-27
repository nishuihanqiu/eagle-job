package com.lls.api.eagle.transport;

import com.lls.api.eagle.config.ClientConfig;
import com.lls.api.eagle.enums.ChannelState;
import com.lls.api.eagle.exception.EagleFrameworkException;
import java.net.InetSocketAddress;
import java.util.Collection;

/************************************
 * AbstractServer
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public abstract class AbstractServer implements Server {
    protected InetSocketAddress localAddress;
    protected InetSocketAddress remoteAddress;
    protected ClientConfig clientConfig;

    protected volatile ChannelState state = ChannelState.UN_INIT;

    public AbstractServer() {
    }

    public AbstractServer(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public Collection<Channel> getChannels() {
        throw new EagleFrameworkException(this.getClass().getName() + " getChannels() method unsupport " + clientConfig);
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        throw new EagleFrameworkException(this.getClass().getName() + " getChannel(InetSocketAddress) method unsupport " + clientConfig);
    }
}
