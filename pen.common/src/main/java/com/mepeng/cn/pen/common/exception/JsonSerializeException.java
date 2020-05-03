package com.mepeng.cn.pen.common.exception;

/*
*
* @author zhangxianchao
* JsonSerializeException
* @date 2016年2月24日
*/

public class JsonSerializeException extends RuntimeException {

    /*
     * @author zhangxianchao JsonSerializeException
     * @date 2016年2月24日
     * @param e
     */

    public JsonSerializeException(Exception e){
        super(e);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 7807894278848938989L;

}
