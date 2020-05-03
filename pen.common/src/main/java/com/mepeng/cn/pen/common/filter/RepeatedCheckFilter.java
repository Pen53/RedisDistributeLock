package com.mepeng.cn.pen.common.filter;

import com.mepeng.cn.pen.common.annotation.RepeatedCheckInterface;
import com.mepeng.cn.pen.common.exception.RepeatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author THINK
 * @description 重复提交检查
 * @date 2019/7/10
 */
@Component
public class RepeatedCheckFilter extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RepeatedCheckFilter.class);

    @Autowired
    private RedisTemplate redisTemplate;
    Lock lock = new ReentrantLock();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String uuid1 = request.getHeader("UUID");
    	
    	System.err.println("6666 ---RepeatedCheckFilter  ---uuid1:"+uuid1);
    	// 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // ①:START 方法注解级拦截器
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录
        RepeatedCheckInterface methodAnnotation = method.getAnnotation(RepeatedCheckInterface.class);
        // 有 @LoginRequired 注解，需要认证
        if (methodAnnotation != null) {
        	String methodName = method.getName();
        	String devicetype = request.getHeader("devicetype");
        	if(devicetype==null||"".equals(devicetype)) {
        		//如果为APP请求,不检测重复提交
        		logger.info("devicetype:"+devicetype+",非APP请求,不检测重复提交 "+",方法名:"+methodName+"=========");
        		return true;
        	}
            // 这写你拦截需要干的事儿，比如取缓存，SESSION，权限判断等
            String uuid = request.getHeader("UUID");
            if(uuid == null || uuid.length() <= 0){
                logger.info("RepeatedCheckFilter-uuid-null"+",方法名:"+methodName+"=========,Url:"+request.getRequestURL());
                //throw new RepeatedException("header:UUID is null");
                return true;
            }
            
       	    if(!uuid.startsWith(methodName+"_"+devicetype+":")) {
       	    	//增加检测重复提交方法作为标示头 防止不同用户使用同个UUID不同重复检测方法保存出现 数据已处理异常
       	    	//主要减少使用同一UUID但是检测不同重复提交接口 检测为重复提交问题
       	    	uuid = methodName+"_"+devicetype+":" + uuid;
	 			
	 		}
            logger.info("=========验证UUID重复性提交UUID:"+uuid+",方法名:"+methodName+"=========,Url:"+request.getRequestURL());

            //判断redis是否存在，如果一段时间内存在，则属于重复提交，不做业务处理
            Object objVal = redisTemplate.opsForValue().get("RCC:"+uuid);
            if(objVal == null || objVal.toString().length() <= 0){

                lock.lock();
                try{
                    //重新提取
                    objVal = objVal = redisTemplate.opsForValue().get("RCC:"+uuid);
                    if(objVal == null || objVal.toString().length() <= 0){
                        //一个小时后过期
                        redisTemplate.opsForValue().set("RCC:"+uuid, methodName,60*60, TimeUnit.SECONDS);
                    }else{
                        logger.info("===========判断为重复提交");//，请刷新页面后重试
                        throw new RepeatedException("数据已处理.");
                    }
                }finally{
                    lock.unlock();
                }
            }else{
                logger.info("===========判断为重复提交");
                throw new RepeatedException("数据已处理.");//，请刷新页面后重试
            }
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
    
}
