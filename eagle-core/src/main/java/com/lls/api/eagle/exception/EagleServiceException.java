package com.lls.api.eagle.exception;

/************************************
 * EagleServiceException
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public class EagleServiceException extends EagleException {
    public EagleServiceException() {
    }

    public EagleServiceException(String message) {
        super(message);
    }

    public EagleServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EagleServiceException(Throwable cause) {
        super(cause);
    }
}
