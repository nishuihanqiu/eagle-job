package com.lls.api.eagle.rpc;

import java.util.HashMap;
import java.util.Map;

/************************************
 * RpcRequest
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public class RpcRequest implements Request {

    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] parameterTypes;
    private String parameterDescription;
    private Map<String, String> attachments;
    private String version;
    private long processTime;

    @Override
    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getInterfaceName() {
        return this.interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Override
    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public Object[] getParameters() {
        return this.parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return this.parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    @Override
    public String getParameterDescription() {
        return this.parameterDescription;
    }

    public void setParameterDescription(String parameterDescription) {
        this.parameterDescription = parameterDescription;
    }

    @Override
    public Map<String, String> getAttachments() {
        return this.attachments;
    }

    @Override
    public void setAttachment(String name, String value) {
        if (this.attachments == null) {
            this.attachments = new HashMap<>();
        }
        this.attachments.put(name, value);
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public void setProcessTime(long time) {
        this.processTime = time;
    }

    @Override
    public long getProcessTime() {
        return this.processTime;
    }
}
