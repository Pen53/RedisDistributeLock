package com.mepeng.cn.pen.common.exception;

/**
 * auth ex to return 401
*
* @author zhangxianchao
* TODO description
* @date 2016年4月21日
 */
public class AuthForbiddenException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AuthForbiddenException(String msg) {
		super(msg);
	}

	public AuthForbiddenException(String msg, Throwable ex) {
		super(msg, ex);
	}

}
