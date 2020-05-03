package com.mepeng.cn.pen.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author THINK
 * @description 在需要登录验证的Controller的方法上使用此注解
 * @date 2019/7/10
 */
@Target({ElementType.METHOD})// 可用在方法名上
@Retention(RetentionPolicy.RUNTIME)// 运行时有效
public @interface RepeatedCheckInterface {
}
