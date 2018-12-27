package com.lls.api.eagle.transport;

/************************************
 * MessageHandler
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public interface MessageHandler {

    Object handle(Channel channel, Object message);

}
