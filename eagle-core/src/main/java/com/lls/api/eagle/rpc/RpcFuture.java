package com.lls.api.eagle.rpc;

import java.util.concurrent.Future;

/************************************
 * RpcFuture
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public interface RpcFuture extends Future<Object> {

    boolean cancel();

    boolean isSuccess();

    Object getValue();

    Exception getException();

    void addListener(FutureListener listener);


}
