package com.lls.api.eagle.rpc;

/************************************
 * RpcFutureResponse
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public interface ResponseFuture extends RpcFuture, Response {

    void onSuccess(Response response);

    void onFailure(Response response) ;

    long getCreateTime();

    void setReturnType(Class<?> clazz);

}
