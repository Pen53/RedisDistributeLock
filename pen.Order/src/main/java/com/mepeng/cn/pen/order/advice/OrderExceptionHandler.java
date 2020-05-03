package com.mepeng.cn.pen.order.advice;

import com.mepeng.cn.pen.common.advice.ExceptionControllerAdvice;
import com.mepeng.cn.pen.common.dao.ResponseMsg;
import com.mepeng.cn.pen.common.enums.ErrorCodeEnum;
import com.mepeng.cn.pen.common.exception.RepeatedException;
import com.mepeng.cn.pen.common.util.common.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

//@ControllerAdvice
public class OrderExceptionHandler extends ExceptionControllerAdvice {

	private static final Logger logger = LoggerFactory.getLogger(OrderExceptionHandler.class);

	/**
	 * 全局异常捕捉处理
	 *
	 * @param ex
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public ResponseMsg errorHandler(Exception ex) {

		logger.error(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(ex));

		ResponseMsg msg = new ResponseMsg();
		msg.setStatus(false);
		if (null != ex.getCause())
			msg.setMsg(!CommonUtils.chkValid(ex.getCause().getMessage()) ? ErrorCodeEnum.SYSTEM_ERROR.getMsg() : ex.getCause().getMessage());
		else
			msg.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getMsg());

		msg.setData(null);

		return msg;
	}
	/**
	 * 捕捉自定义异常 RepeatedException.class
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(RepeatedException.class)
	@ResponseBody
	public ResponseMsg RepeatedException( RepeatedException ex) {
		logger.error(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(ex));

		ResponseMsg msg = new ResponseMsg();
		msg.setStatus(false);
		msg.setMsg(!CommonUtils.chkValid(ex.getMsg()) ? ErrorCodeEnum.SYSTEM_ERROR.getMsg() : ex.getMsg());
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", "-999");//数据重复提交
		msg.setData(result);

		return msg;
	}
	
}