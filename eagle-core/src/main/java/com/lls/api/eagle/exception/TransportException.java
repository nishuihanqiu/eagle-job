package com.lls.api.eagle.exception;

import java.io.IOException;
import java.net.InetSocketAddress;

/************************************
 * TransportException
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public class TransportException extends IOException {

    private static final long serialVersionUID = 7053446235490722694L;

    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;

    public TransportException(String message, InetSocketAddress localAddress, InetSocketAddress remoteAddress) {
        super(message);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public TransportException(String message, Throwable cause, InetSocketAddress localAddress, InetSocketAddress remoteAddress) {
        super(message, cause);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

}
