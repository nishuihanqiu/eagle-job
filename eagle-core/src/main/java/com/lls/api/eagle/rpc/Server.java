package com.lls.api.eagle.rpc;

import java.net.InetSocketAddress;
import java.util.Collection;

/************************************
 * Server
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public interface Server extends Channel {

    boolean isBound();

    Collection<Channel> getChannels();

    Channel getChannel(InetSocketAddress remoteAddress);

}
