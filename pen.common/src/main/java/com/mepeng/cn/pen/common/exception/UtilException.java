package com.mepeng.cn.pen.common.exception;

/*
*
* @author zhangxianchao
* UtilException
* @date 2016年2月26日
*/

public class UtilException extends RuntimeException {

    /*
     * @author zhangxianchao UtilException
     * @date 2016年2月26日 tags
     */

    private static final long serialVersionUID = -5620134357529456759L;

    public UtilException(Exception e){
        super(e);
    }

    public UtilException(String msg){
        super(msg);
    }

    public UtilException(String msg, Exception e){
        super(msg, e);
    }

}
