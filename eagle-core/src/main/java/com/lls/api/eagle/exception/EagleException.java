package com.lls.api.eagle.exception;

/************************************
 * EagleException
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public class EagleException extends RuntimeException {

    private static final long serialVersionUID = -8742311167276890503L;

    private String errorMessage;

    public EagleException() {
        this.errorMessage = null;
    }

    public EagleException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public EagleException(String message, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
    }

    public EagleException(Throwable cause) {
        super(cause);
        this.errorMessage = cause.getMessage();
    }

}
