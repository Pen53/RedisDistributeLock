package com.mepeng.cn.pen.common.exception;


import com.mepeng.cn.pen.common.enums.ErrorCodeEnum;
import com.mepeng.cn.pen.common.util.common.CommonUtils;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 2L;

	public ServiceException(String msg) {
		super(msg);
		this.msg = !CommonUtils.chkValid(msg) ? ErrorCodeEnum.SYSTEM_ERROR.getMsg() : msg;
	}

	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
