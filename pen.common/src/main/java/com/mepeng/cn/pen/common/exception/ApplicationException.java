package com.mepeng.cn.pen.common.exception;

/**
 * Service层异常统一封装的程序运行异常，不做异常捕捉与处理
 * INTERNAL_SERVER_ERROR
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = -7063395111284988266L;

    public ApplicationException(String msg) {
        super(msg);
    }
    
    public ApplicationException( Exception e){
        super(e);
    }

    
    public ApplicationException(String msg, Exception e){
        super(msg, e);
    }

}
