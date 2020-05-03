package com.mepeng.cn.pen.common.exception;

import java.io.Serializable;

/**
 * Service层异常统一封装的业务异常，一般无法直接处理
 * ServiceBizException
 */
public class ServiceBizException extends  RuntimeException{

    private static final long serialVersionUID = 5948018638602481391L;
    private Serializable exceptionData;
    
    public ServiceBizException(Exception e){
        super(e);
    }

    public ServiceBizException(String msg) {
        super(msg);
    }
    public ServiceBizException(String msg,Serializable exceptionData) {
        super(msg);
        this.exceptionData = exceptionData;
    }
    
    public ServiceBizException(String msg, Exception e){
        super(msg, e);
    }

    
    public Serializable getExceptionData() {
        return exceptionData;
    }

    
    public void setExceptionData(Serializable exceptionData) {
        this.exceptionData = exceptionData;
    }

}
