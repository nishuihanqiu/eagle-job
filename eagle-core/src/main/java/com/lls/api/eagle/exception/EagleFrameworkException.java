package com.lls.api.eagle.exception;

/************************************
 * EagleFrameworkException
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public class EagleFrameworkException extends EagleException {

    public EagleFrameworkException() {
    }

    public EagleFrameworkException(String message) {
        super(message);
    }

    public EagleFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public EagleFrameworkException(Throwable cause) {
        super(cause);
    }
}
