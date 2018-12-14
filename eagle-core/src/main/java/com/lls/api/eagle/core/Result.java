package com.lls.api.eagle.core;

import java.io.Serializable;

/************************************
 * Result
 * @author liliangshan
 * @date 2018/12/13
 ************************************/
public class Result implements Serializable {

    private String code;
    private String message;
    private Object data;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

}
