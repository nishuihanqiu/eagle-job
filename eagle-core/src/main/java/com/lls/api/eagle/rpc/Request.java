package com.lls.api.eagle.rpc;

import java.util.Map;

/************************************
 * Request
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public interface Request {

    String getRequestId();

    String getInterfaceName();

    String getMethodName();

    Object[] getParameters();

    Class<?>[] getParameterTypes();

    String getParameterDescription();

    Map<String, String> getAttachments();

    void setAttachment(String name, String value);

    String getVersion();

    void setVersion(String version);

    void setProcessTime(long time);

    long getProcessTime();

}
