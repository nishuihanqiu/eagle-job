package com.lls.api.eagle.config;

/************************************
 * ClientConfig
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public class ClientConfig {

    private String name;
    private int minClientConnection;
    private int maxClientConnection;
    private int requestTimeout;
    private boolean poolLifo;
    private boolean lazyInit;
    private String host;
    private int port;
    private String url;
    private boolean asyncInitConnection;
    private int connectTimeout;
    private int maxContentLength;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMinClientConnection(int minClientConnection) {
        this.minClientConnection = minClientConnection;
    }

    public int getMinClientConnection() {
        return minClientConnection;
    }

    public void setMaxClientConnection(int maxClientConnection) {
        this.maxClientConnection = maxClientConnection;
    }

    public int getMaxClientConnection() {
        return maxClientConnection;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setPoolLifo(boolean poolLifo) {
        this.poolLifo = poolLifo;
    }

    public boolean isPoolLifo() {
        return poolLifo;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setAsyncInitConnection(boolean asyncInitConnection) {
        this.asyncInitConnection = asyncInitConnection;
    }

    public boolean isAsyncInitConnection() {
        return asyncInitConnection;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }
}
