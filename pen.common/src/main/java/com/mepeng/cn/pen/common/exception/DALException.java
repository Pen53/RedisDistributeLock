package com.mepeng.cn.pen.common.exception;

public class DALException extends RuntimeException {

  
    	
    /*
    *
    * @author zhangxianchao
    * DALException
    * @date 2016年2月14日
    * @param string
    */
    	
    public DALException(Exception e){
       super(e);
    }

    /*
    *
    * @author zhangxianchao
    * DALException
    * @date 2016年2月14日
    * @param string
    */
    	
    public DALException(String str){
        super(str);
    }

    /*
    *
    * @author zhangxianchao
    * DALException
    * @date 2016年2月15日
    * @param string
    * @param e
    */
    	
    public DALException(String str, Exception e){
       super(str,e);
    }

    private static final long serialVersionUID = 1L;

}
