package com.lls.eagle.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/************************************
 * NettyServerChannelManage
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
@ChannelHandler.Sharable
public class NettyServerChannelManage extends ChannelInboundHandlerAdapter {

    private ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();

    private int maxChannel;



}
