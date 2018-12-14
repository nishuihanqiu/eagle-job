package com.lls.api.eagle.rpc;

import java.util.Map;

/************************************
 * Response
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public interface Response {

    String getRequestId();

    Exception getException();

    boolean hasException();

    Object getResult();

    String getVersion();

    void setVersion(String version);

    Map<String, String> getAttachments();

    void setAttachment(String key, String value);

    long getProcessTime();

    void setProcessTime(long time);

}
