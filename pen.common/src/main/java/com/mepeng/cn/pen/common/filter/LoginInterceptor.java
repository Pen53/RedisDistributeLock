package com.mepeng.cn.pen.common.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mepeng.cn.pen.common.annotation.LoginIntercept;
import com.mepeng.cn.pen.common.domains.DTO.LoginInfoDto;
import com.mepeng.cn.pen.common.domains.DTO.RequestPageInfoDto;
import com.mepeng.cn.pen.common.exception.AuthForbiddenExceptionHandler;
import com.mepeng.cn.pen.common.util.CookieUtils;
import com.mepeng.cn.pen.common.util.HTTPClientUtils;
import com.mepeng.cn.pen.common.util.bean.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过中台jwt信息获取登陆信息
 * 方法中带有LoginIntercept注解 并且带有jwt信息
 */
@Slf4j
@Component
public class LoginInterceptor  implements HandlerInterceptor {
    @Value("${spring.zt.url-prefix}") // 取配置文件的值
    private String zt_url_prefix;

    /**
     * 预处理回调 默认所有请求都是要被拦截的,除开方法或者类上有{@link LoginIntercept} 注解,并且参数等于false
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("uri:"+request.getRequestURI()+",url:"+request.getRequestURL()+",LoginInterceptor is running...");
        crossDomain(request, response);
        if(!(handler instanceof HandlerMethod)){
            //如果不是请求controller 直接返回
            return true;
        }
        RequestPageInfoDto requestPageInfoDto = ApplicationContextHelper.getBeanByType(RequestPageInfoDto.class);
        requestPageInfoDto.setLimit(request.getParameter("limit")!=null?request.getParameter("limit"):"5");
        requestPageInfoDto.setOffset(request.getHeader("offset")!=null?request.getHeader("offset"):"0");

        if (StringUtils.isEmpty(zt_url_prefix)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "请求地址错误");
        }
        // 获取jwt
        String jwt = CookieUtils.getCookieValue(request, "jwt");

        if (StringUtils.isEmpty(jwt)){
            jwt = request.getHeader("jwt");
        }
        if (StringUtils.isEmpty(jwt)){
            jwt = request.getParameter("jwt");
        }

        HandlerMethod methods = (HandlerMethod) handler;
        LoginIntercept intercept = methods.getMethodAnnotation(LoginIntercept.class);

        // 如果写了拦截注解,并且拦截注解里的参数,等于false.就不校验登录信息
        if (intercept != null && !intercept.required()) {
            if (!StringUtils.isEmpty(jwt)) {
                getUserDTO(request, jwt);
            }
            return true;
        }

        LoginIntercept classLoginIntercept = methods.getMethod().getDeclaringClass().getAnnotation(LoginIntercept.class);
        if (classLoginIntercept != null && !classLoginIntercept.required()) {
            if (!StringUtils.isEmpty(jwt)) {
                getUserDTO(request, jwt);
            }
            return true;
        }
        if (!StringUtils.isEmpty(jwt)) {
            getUserDTO(request, jwt);
            return true;
        }
        if(intercept!=null||classLoginIntercept!=null){
            //如果有注解要请求获取登陆信息，但是没有jwt信息 不请求获取信息
            if (StringUtils.isEmpty(jwt)) {
                log.debug("jwt is null not request url:"+zt_url_prefix + "vue.sysAuth/login/initUserData"+", return true." );
                return true;
            }
            Throwable throwable = new Throwable("认证失败!");
            throw new AuthForbiddenExceptionHandler("认证失败!",throwable);
        }else{
            return true;
        }
        //throw new AuthForbiddenExceptionHandler("认证失败!");
    }

    private void getUserDTO(HttpServletRequest request, String jwt) throws AuthForbiddenExceptionHandler {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("jwt", jwt);
            JSONObject json = HTTPClientUtils.sendGet(zt_url_prefix + "vue.sysAuth/login/initUserData", map);

            log.debug(zt_url_prefix + "vue.sysAuth/login/initUserData"+",调用中台的返回结果:" + json);

            LoginInfoDto sourceLoginInfo = JSON.toJavaObject(json.getJSONObject("userInfo"), LoginInfoDto.class);

            log.info("sourceLoginInfo:" + sourceLoginInfo);

            if (null == sourceLoginInfo) {
                Throwable throwable = new Throwable("认证失败!");
                throw new AuthForbiddenExceptionHandler("认证失败!",throwable);
            }

            BeanUtils.copyProperties(sourceLoginInfo, ApplicationContextHelper.getBeanByType(LoginInfoDto.class));
            LoginInfoDto targetLoginInfo = ApplicationContextHelper.getBeanByType(LoginInfoDto.class);
            /**
             * 中台OrgType 10461001 为主机厂商  骏客 10461001 为经销商
             * 中台OrgType 10461002 为经销商  骏客 10461002 为主机厂商
             */
            if("10461001".equals(targetLoginInfo.getOrgType().toString())){
                targetLoginInfo.setOrgType(10461002);
            }else{
                targetLoginInfo.setOrgType(10461001);
            }
            log.info("targetLoginInfo:" + targetLoginInfo);
        } catch (Exception e) {
            log.error("getUserDTO 认证失败Exception:",e);
            Throwable throwable = new Throwable("认证失败!");
            throw new AuthForbiddenExceptionHandler("认证失败!",throwable);
        }

    }
    //允许跨域
    private void crossDomain(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    }

    /**
     * 后处理回调
     */
    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 完处理回调
     */
    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) throws Exception {

    }

}
