
/** 
*Copyright 2017 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dmscloud.framework
*
* @File name : FrameworkDictConstants.java
*
* @Author : Administrator
*
* @Date : 2017年7月3日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2017年7月3日    Administrator    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.mepeng.cn.pen.common.constants;

/**
 * 定义常量类：TC_CODE 相关
 * 
 * @author Administrator
 * @date 2017年7月3日
 */

public class FrameworkDictCodeConstants {

    /**
     * 数据类型，用于区别此数据作为谁使用的数据
     */
    public static final int    DATA_TYPE                          = 1046;
    // 经销商
    public static final int    DATA_TYPE_BY_OWNER                 = 10461001;
    // 集团
    public static final int    DATA_TYPE_BY_GROUP                 = 10461002;

    // 是否
    public static final int    YESORNO                            = 1004;
    // 是
    public static final int    STATUS_IS_YES                      = 10041001;
    // 否
    public static final int    STATUS_IS_NOT                      = 10041002;

    // 省市区县-省份
    public static final int    REGION_TYPE_PROVINCE               = 10001001;
    // 省市区县-城市
    public static final int    REGION_TYPE_CITY                   = 10001002;
    // 省市区县-区县
    public static final int    REGION_TYPE_COUNTY                 = 10001003;
    // 省市区县-街道
    public static final int    REGION_TYPE_STREET                 = 10001004;
    // 有效状态
    public static final int    STATUS_IS_VALID                    = 10011001;
    // 无效状态
    public static final int    STATUS_NOT_VALID                   = 10011002;

    // 数据来源渠道-PC
    public static final int    DATA_SOURCE_CHANNEL_PC             = 22481001;
    // 数据来源渠道-微信公众号：车商云用友
    public static final int    DATA_SOURCE_CHANNEL_WECHAT_SESRVER = 22481002;
    // 数据来源渠道-APP：售前售后app
    public static final int    DATA_SOURCE_CHANNEL_APP_SERVER     = 22481003;
    // 数据来源渠道-PAD：用友车商云
    public static final int    DATA_SOURCE_CHANNEL_PAD_IOS_SERVER = 22481004;
    // 增加厂商dealercode常量
    public static final String DEALER_CODE                        = "GMS001";
}
