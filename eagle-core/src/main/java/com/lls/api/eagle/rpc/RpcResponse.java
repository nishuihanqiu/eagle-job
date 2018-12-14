package com.lls.api.eagle.rpc;

import java.util.HashMap;
import java.util.Map;

/************************************
 * RpcResponse
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public class RpcResponse implements Response {

    private String requestId;
    private Exception exception;
    private Object result;
    private String version;
    private Map<String, String> attachments;
    private long processTime;

    @Override
    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public Exception getException() {
        return this.exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public boolean hasException() {
        return this.exception != null;
    }

    @Override
    public Object getResult() {
        return this.result;
    }

    public void setResult(Object result) {
        this.result = result;
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
    public Map<String, String> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    @Override
    public void setAttachment(String key, String value) {
        if (this.attachments == null) {
            this.attachments = new HashMap<>();
        }
        this.attachments.put(key, value);
    }

    @Override
    public long getProcessTime() {
        return this.processTime;
    }

    @Override
    public void setProcessTime(long time) {
        this.processTime = time;
    }
}
