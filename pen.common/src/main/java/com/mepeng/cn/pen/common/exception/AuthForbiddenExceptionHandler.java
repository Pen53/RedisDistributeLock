package com.mepeng.cn.pen.common.exception;

public class AuthForbiddenExceptionHandler extends Exception {
    private static final long serialVersionUID = -7125898457348432029L;

    public AuthForbiddenExceptionHandler(String msg) {
        super(msg);
    }

    public AuthForbiddenExceptionHandler(String msg, Throwable ex) {
        super(msg, ex);

    }
}
