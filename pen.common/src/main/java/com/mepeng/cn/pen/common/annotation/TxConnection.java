package com.mepeng.cn.pen.common.annotation;

import java.lang.annotation.*;

/**
 * 只注释到要开启连接的方法上面
 */
//ElementType.TYPE,
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TxConnection {
}
