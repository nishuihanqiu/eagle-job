package com.lls.api.eagle.enums;

/************************************
 * RpcVersion
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public enum RpcVersion {
    RPC_VERSION_1("rpc_v1");

    private String version;

    RpcVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
