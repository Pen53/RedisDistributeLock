package com.mepeng.cn.pen.common.advice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.mepeng.cn.pen.common.exception.AuthForbiddenException;
import com.mepeng.cn.pen.common.exception.AuthLoginOutException;
import com.mepeng.cn.pen.common.exception.DALException;
import com.mepeng.cn.pen.common.exception.ServiceBizException;
import com.mepeng.cn.pen.common.util.bean.ApplicationContextHelper;
import com.mepeng.cn.pen.common.util.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

//import com.yonyou.dmscloud.framework.util.bean.ApplicationContextHelper;
//import com.yonyou.dmscloud.function.exception.AuthForbiddenException;
//import com.yonyou.dmscloud.function.exception.AuthLoginOutException;
//import com.yonyou.dmscloud.function.exception.DALException;
//import com.yonyou.dmscloud.function.exception.ServiceBizException;
//import com.yonyou.dmscloud.function.utils.common.StringUtils;

/**
 * @author zhangxc
 *
 */
@ControllerAdvice
@Order(0)//Spring 4.2 利用@Order控制配置类的加载顺序
public class ExceptionControllerAdvice {


    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    /**
     *
     * 处理Throwable
     * @return
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> ThrowableAdvice(Throwable error) {
        logger.error("未知错误："+error.getMessage(),error);
        Map<String, Object> map = new HashMap<>();
        map.put("errorMsg", "系统出错,请与管理员联系!!");
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * 处理ServiceBizException
     */
    @ExceptionHandler(ServiceBizException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> bizExceptionAdvice(ServiceBizException error) {
        if(StringUtils.isNullOrEmpty(error.getCause())){
            logger.error("业务错误："+error.getMessage());
        }else{
            logger.error("业务错误："+error.getMessage(),error);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("errorMsg", "错误原因:" + error.getMessage());
        if(error.getExceptionData()!=null){
            map.put("errorData", error.getExceptionData());
        }
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * 处理AuthException
     */
    @ExceptionHandler(AuthLoginOutException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> authExceptionAdvice(AuthLoginOutException error) {
        logger.error("未认证通过,被退出登录："+error.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("errorMsg", "错误原因:" + error.getMessage());
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

    /**
     *
     * 处理AuthException
     */
    @ExceptionHandler(AuthForbiddenException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> authForbiddenExceptionAdvice(AuthForbiddenException error) {
        logger.error("访问被阻止："+error.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("errorMsg", "错误原因:" + error.getMessage());
        return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
    }

    /**
     *
     * 处理DALException
     */
    @ExceptionHandler(DALException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> authExceptionAdvice(DALException error) {
        logger.error("数据操作异常："+error.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("errorMsg", "错误原因:" + error.getMessage());
        return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
    }

    /**
     *
     * 处理MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> authExceptionAdvice(MethodArgumentNotValidException error) {
        logger.error("数据格式不正确："+error.getMessage());
        String errorDtoName = error.getBindingResult().getTarget().getClass().getSimpleName();
        Map<String, Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (FieldError errorInfo : error.getBindingResult().getFieldErrors()) {
            String tipMessage = getTipMessage(errorDtoName,errorInfo.getCodes(),null,errorInfo.getDefaultMessage());
            if(StringUtils.isNullOrEmpty(errorInfo.getRejectedValue())){
                sb.append(errorInfo.getField()).append(":").append(tipMessage).append("</br>");
            }else{
                sb.append(errorInfo.getRejectedValue()).append(":").append(tipMessage).append("</br>");
            }
        }
        map.put("errorMsg", "错误原因:</br>" + sb.toString());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * 处理上传文件超过最大大小
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void handleMaxUploadSizeException(MaxUploadSizeExceededException ex,HttpServletResponse response){
        logger.error("上传文件大小超过最大限制："+ex.getMessage());
        try {
            response.sendError(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE.value(), "文件大小不应大于"+((MaxUploadSizeExceededException)ex).getMaxUploadSize()/1000+"kb");
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }
    /**
     *
     * 获得提示信息
     */
    private String getTipMessage(String errorDtoName,String[] codes,Object[] args,String defaultMessage){
        //获取messageResource
        ResourceBundleMessageSource messageSource = ApplicationContextHelper.getBeanByType(ResourceBundleMessageSource.class);
        for(int i=-1;i<codes.length;i++){
            try{
                String message = null;
                if(i==-1){
                    String codeFirst = codes[0];
                    if(!StringUtils.isNullOrEmpty(codeFirst)){
                        String[] splitArray = codeFirst.split("\\.");
                        String newCode;
                        if(splitArray.length>=2){
                            newCode = codeFirst.replace(splitArray[1], errorDtoName);
                            message = messageSource.getMessage(newCode, args, Locale.CHINESE);
                        }
                    }
                }else{
                    message = messageSource.getMessage(codes[i], args, Locale.CHINESE);
                }
                if(!StringUtils.isNullOrEmpty(message)){
                    return message;
                }
            }catch(Exception e){
                logger.debug("error:"+e.getMessage(),e);
            }
        }
        return defaultMessage;
    }
}

