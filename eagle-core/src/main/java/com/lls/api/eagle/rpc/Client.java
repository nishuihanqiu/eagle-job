package com.lls.api.eagle.rpc;

/************************************
 * Client
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public interface Client extends Channel {

    void heartbeat(Request request);

}
