package com.lls.api.eagle.rpc;

import com.lls.api.eagle.exception.TransportException;

import java.net.InetSocketAddress;

/************************************
 * Channel
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public interface Channel {


    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

    Response send(Request request) throws TransportException;

    boolean open();

    boolean close();

    boolean close(int timeout);

    boolean isClosed();

    boolean isAvailable();

    String getUrl();

}
