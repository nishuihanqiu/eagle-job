package com.lls.api.eagle.rpc;

import com.lls.api.eagle.enums.FutureState;
import com.lls.api.eagle.enums.RpcVersion;
import com.lls.api.eagle.exception.EagleException;
import com.lls.api.eagle.exception.EagleFrameworkException;
import com.lls.api.eagle.serialize.SerailizationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/************************************
 * RpcResponseFuture
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public class RpcResponseFuture implements ResponseFuture {

    private static Logger logger = LoggerFactory.getLogger(RpcResponseFuture.class);

    private volatile FutureState state = FutureState.DOING;
    private final Object lock = new Object();
    private Exception exception;
    private Object result;
    private long createdTime = System.currentTimeMillis();
    private int timeout = 0;
    private long processTime = 0;

    private Request request;
    private Response response;
    private List<FutureListener> listeners;
    private Class returnType;

    public RpcResponseFuture(Request request, int timeout) {
        this.request = request;
        this.timeout = timeout;
    }

    @Override
    public void onSuccess(Response response) {
        this.result = response.getResult();
        this.processTime = response.getProcessTime();
        this.response = response;

        this.done();
    }

    @Override
    public void onFailure(Response response) {
        this.exception = response.getException();
        this.processTime = response.getProcessTime();
        this.timeout = response.getTimeout();
        this.response = response;

        this.done();
    }

    @Override
    public long getCreateTime() {
        return this.createdTime;
    }

    @Override
    public void setReturnType(Class<?> clazz) {
        this.returnType = clazz;
    }

    @Override
    public String getRequestId() {
        return this.request.getRequestId();
    }

    @Override
    public boolean hasException() {
        return this.exception != null;
    }

    @Override
    public Object getResult() {
        return this.result;
    }

    @Override
    public String getVersion() {
        return RpcVersion.RPC_VERSION_1.getVersion();
    }

    @Override
    public void setVersion(String version) {

    }

    @Override
    public Map<String, String> getAttachments() {
        return this.response.getAttachments();
    }

    @Override
    public void setAttachment(String key, String value) {

    }

    @Override
    public long getProcessTime() {
        return this.processTime;
    }

    @Override
    public void setProcessTime(long time) {

    }

    @Override
    public int getTimeout() {
        return this.timeout;
    }

    @Override
    public boolean cancel() {
        Exception e = new EagleException(this.getClass().getName() + " task cancel: serverPort=");
        return cancel(e);
    }

    private boolean cancel(Exception e) {
        synchronized (lock) {
            if (!this.isDoing()) {
                return false;
            }
            state = FutureState.CANCELLED;
            exception = e;
            lock.notifyAll();
        }
        notifyListeners();
        return true;
    }

    @Override
    public boolean isSuccess() {
        return state.isDoneState() && (exception == null);
    }

    @Override
    public Object getValue() {
        synchronized (lock) {
            if (!isDoing()) {
                return this.getValueOrThrowable();
            }

            if (this.timeout <= 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    cancel(e);
                }
                return getValueOrThrowable();
            }

            long waitTime = timeout - (System.currentTimeMillis() - createdTime);
            if (waitTime > 0) {
                while (true) {
                    try {
                        lock.wait(waitTime);
                    } catch (InterruptedException ignored) {

                    }

                    if (!isDoing()) {
                        break;
                    }

                    waitTime = timeout - (System.currentTimeMillis() - createdTime);
                    if (waitTime <= 0) {
                        break;
                    }
                }
            }

            if (isDoing()) {
                timeoutSoCancel();
            }
        }

        return getValueOrThrowable();
    }

    @Override
    public Exception getException() {
        return this.exception;
    }

    @Override
    public void addListener(FutureListener listener) {
        if (listener == null) {
            throw new NullPointerException("FutureListener is null");
        }
        synchronized (lock) {
            if (!isDoing()) {
                notifyListener(listener);
                return;
            }

            if (this.listeners == null) {
                this.listeners = new ArrayList<>();
            }
            this.listeners.add(listener);
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (mayInterruptIfRunning && isDoing()) {
            this.state = FutureState.DONE;
        }
        return cancel();
    }

    @Override
    public boolean isCancelled() {
        return state.isCancelledState();
    }

    @Override
    public boolean isDone() {
        return state.isDoneState();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return result;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return result;
    }

    protected boolean done() {
        synchronized (lock) {
            if (!this.isDoing()) {
                return false;
            }

            state = FutureState.DONE;
            lock.notifyAll();
        }

        this.notifyListeners();
        return true;
    }

    private boolean isDoing() {
        return state.isDoingState();
    }

    private void notifyListeners() {
        if (listeners == null) {
            return;
        }
        for (FutureListener listener : listeners) {
            notifyListener(listener);
        }
    }

    private void notifyListener(FutureListener listener) {
        try {
            listener.onCompleted(this);
        } catch (Exception e) {
            logger.error("notifyListeners errors:" + e.getMessage(), e);
        }
    }

    public Request getRequest() {
        return request;
    }

    public FutureState getState() {
        return state;
    }

    @SuppressWarnings("unchecked")
    private Object getValueOrThrowable() {
        if (exception != null) {
            throw (exception instanceof RuntimeException) ? ((RuntimeException) exception)
                : new EagleFrameworkException(exception.getMessage(), exception);
        }

        if (returnType != null && result instanceof SerailizationContext) {
            try {
                result = ((SerailizationContext) result).deserialize(returnType);
            } catch (IOException e) {
                throw new EagleFrameworkException("deserialize return value fail! deserialize type:" +
                    returnType + " message:" + e.getMessage(), e);
            }
        }

        return result;
    }

    private void timeoutSoCancel() {
        this.processTime = System.currentTimeMillis() - this.createdTime;

        synchronized (lock) {
            if (!isDoing()) {
                return;
            }
            state = FutureState.CANCELLED;
            exception = new EagleFrameworkException(this.getClass().getName() + " request timeout:" + this.timeout
                + " cost=" + (System.currentTimeMillis() - createdTime));
            lock.notifyAll();
        }

        this.notifyListeners();
    }

}
