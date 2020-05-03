package com.mepeng.cn.pen.common.exception;

public class StoreException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StoreException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public StoreException(String exceptionMessage, Throwable throwable) {
        super(exceptionMessage, throwable);
    }
}
