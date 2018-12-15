package com.lls.api.eagle.rpc;

/************************************
 * FutureListener
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public interface FutureListener {

    void onCompleted(RpcFuture future) throws Exception;

}
