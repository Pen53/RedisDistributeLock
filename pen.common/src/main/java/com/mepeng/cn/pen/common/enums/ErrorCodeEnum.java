package com.mepeng.cn.pen.common.enums;
/**
 * 异常代码错误定义
 */
public enum ErrorCodeEnum {
    SYSTEM_ERROR(1000, "系统错误,请与管理员联系!!"),PARAM_IS_NULL(1001, "参数不能为空"),FORMAT_ERROR(1002,"格式错误");

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
