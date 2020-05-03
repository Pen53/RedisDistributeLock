package com.mepeng.cn.pen.common.dao;

/**
 * AppServe 接口返回信息
 */
public class ResponseMsg {
    private boolean status;
    private String msg;
    private Object data;
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResponseMsg(){
        super();
    }

    public ResponseMsg(boolean status, String msg){
        super();
        this.status = status;
        this.msg = msg;
    }

    
    public Object getData() {
        return data;
    }

    
    public void setData(Object data) {
        this.data = data;
    }

    public ResponseMsg(boolean status, String msg, Object data){
        super();
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
