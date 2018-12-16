package com.lls.api.eagle.rpc;

import com.lls.api.eagle.enums.ChannelState;
import com.lls.api.eagle.exception.EagleException;

import java.net.InetSocketAddress;

/************************************
 * AbstractClient
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public abstract class AbstractClient implements Client {

    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;
    private String url;
    protected ChannelState state = ChannelState.UN_INIT;

    public AbstractClient(String url) {
        this.url = url;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void heartbeat(Request request) {
        throw new EagleException("heartbeat not support:" + request.toString());
    }

    @Override
    public String getUrl() {
        return url;
    }
}
