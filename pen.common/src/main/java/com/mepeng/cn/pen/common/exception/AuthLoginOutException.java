package com.mepeng.cn.pen.common.exception;

public class AuthLoginOutException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AuthLoginOutException(String msg) {
		super(msg);
	}

	public AuthLoginOutException(String msg, Throwable ex) {
		super(msg, ex);
	}

}
