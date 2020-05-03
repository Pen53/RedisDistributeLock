/*
* Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
*
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : cmol.common.function
*
* @File name : CommonConstants.java
*
* @Author : zhangxianchao
*
* @Date : 2016年2月24日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年2月24日    zhangxianchao    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.mepeng.cn.pen.common.constants;

/*
*
* @author zhangxianchao
* CommonConstants
* @date 2016年2月24日
*/

public class FunctionConstants {

    public final static String IOS_8601_DATE_FORMAT               = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public final static String SIMPLE_DATE_FORMAT                 = "yyyy-MM-dd";
    public final static String SIMPLE_DATE_TIME_FORMAT            = "yyyy-MM-dd HH:mm";
    public final static String FULL_DATE_TIME_FORMAT              = "yyyy-MM-dd HH:mm:ss";
    public final static String ACCURATE_DATE_FORMAT               = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public final static String SIMPLE_DATE_MONTH_FORMAT           = "yyyy-MM";
    public final static String SIMPLE_MONTH_FORMAT                = "MM/dd";
    public final static String SIMPLE_MONTH_DAY_FORMAT            = "MM-dd";
    public final static String SIMPLE_MONTH                       = "MM";
    public final static String SIMPLE_DAY_FORMAT                  = "dd";
    public final static String FULL_DATE_TIME_FORMAT_NUMBER       = "yyyyMMddHHmmss";

    // 订单号位数
    public static final int    SYSTEM_ORDER_NO_NUMBER             = 5;

    /**
     * 不进行任何格式操作，没有千位符，小数点不自动补全
     */
    public final static String Excel_NUMBER_FORMAT_SAMPLE         = "#";

    public final static String Excel_NUMBER_FORMAT_SAMPLE_ROUND2  = "0.00";
    // 设置百分比样式
    public final static String Excel_NUMBER_FORMAT_SAMPLE_PERCENT = "0.00%";

}
