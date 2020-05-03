package com.mepeng.cn.pen.common.exception;


import com.mepeng.cn.pen.common.enums.ErrorCodeEnum;

public class FormatException extends RuntimeException {

	private static final long serialVersionUID = 2L;

	public FormatException(String msg) {
        this.code = ErrorCodeEnum.FORMAT_ERROR.getCode();
        this.msg = msg + ErrorCodeEnum.FORMAT_ERROR.getMsg();
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
