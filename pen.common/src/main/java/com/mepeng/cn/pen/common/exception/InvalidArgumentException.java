package com.mepeng.cn.pen.common.exception;

public class InvalidArgumentException  extends Exception  {
    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(Throwable error) {
        super(error);
    }
}
