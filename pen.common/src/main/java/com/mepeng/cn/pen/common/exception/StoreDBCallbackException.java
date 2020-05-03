package com.mepeng.cn.pen.common.exception;

public class StoreDBCallbackException extends RuntimeException  {
    private static final long serialVersionUID = 1L;

    public StoreDBCallbackException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public StoreDBCallbackException(String exceptionMessage, Throwable throwable) {
        super(exceptionMessage, throwable);
    }
}
