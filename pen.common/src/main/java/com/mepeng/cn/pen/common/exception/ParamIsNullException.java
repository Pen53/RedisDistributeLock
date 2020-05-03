package com.mepeng.cn.pen.common.exception;
import com.mepeng.cn.pen.common.enums.ErrorCodeEnum;

public class ParamIsNullException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParamIsNullException(String msg) {
        this.code = ErrorCodeEnum.PARAM_IS_NULL.getCode();
        this.msg = msg + ErrorCodeEnum.PARAM_IS_NULL.getMsg();
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
