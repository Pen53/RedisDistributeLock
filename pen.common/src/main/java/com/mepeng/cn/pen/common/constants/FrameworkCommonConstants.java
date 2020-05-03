
/** 
*Copyright 2017 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dmscloud.framework
*
* @File name : FrameworkCommonConstants.java
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
* 定义常量类：固定常量
* @author Administrator
* @date 2017年7月3日
*/

public class FrameworkCommonConstants {

    //定义资源分割符
    public final static String  ACL_RESOUCCE_SPLIT       = ":";
    
    // 默认经销商代码字段名称
    public static final String  PUBLIC_DEALER_CODE_NAME    = "DEALER_CODE";
    // 默认经销商代码字段名称
    public static final String  PUBLIC_ORGANIZATION_NAME    = "ORGANIZATION_ID";
    
    // 默认删除标记字段名称
    public static final String  PUBLIC_IS_DELETE_NAME    = "IS_DELETE";
   
    
    // 错误信息列表最大行数
    public static final Integer IMPORT_MAX_ERRORS_ROWS     = 30;
    
    // 订单号初始化
    public static final Integer INIT_ORDER_NO              = 1;

    
    
    //文件上传文件分隔符
    public static final String FILEUPLOADID_SPLIT_STR=",;";
    //文件上传文件分隔符---已经存在文件与新上传文件
    public static final String FILEUPLOADID_SPLIT_STR_TYPE="##@";
    
    
    //系统初始化数据缓存名称
    public static final String sysDataCacheName = "";
    
    //系统默认URL 缓存前缀:统一资源
    public static final String APPLICATION_URL_PREFIX = "GR";
    
    //权限相关缓存名称--登录信息
    public static final String CACHE_LOGIN_INFO_NAME = "loginInfo";
    
    //权限信息--用户资源信息
    public static final String CACHE_ACCESS_INFO_NAME = "userAccessInfo";
    
    // 默认数据来源渠道代码字段名称
    public static final String  PUBLIC_DATA_SOURCE_CHANNEL_NAME    = "DATA_SOURCE_CHANNEL";
    
    //基础数据服务名称
    public static final String SERVER_NAME_BASEDATA		="DMSCLOUD-BASEDATA-TEST";
    //配件服务名称
    public static final String SERVER_NAME_PART			="DMSCLOUD-PART-TEST";
    //客户服务名称
    public static final String SERVER_NAME_CUSTOMER		="DMSCLOUD-CUSTOMER-TEST";
    //维修服务名称
    public static final String SERVER_NAME_REPAIR		="DMSCLOUD-REPAIR-TEST";
    //销售服务名称
    public static final String SERVER_NAME_RETAIL		="DMSCLOUD-RETAIL-TEST";
    //车辆服务名称
    public static final String SERVER_NAME_VEHICLE		="DMSCLOUD-VEHICLE-TEST";
    //二手车服务名称
    public static final String SERVER_NAME_USEDVEHICLE	="DMSCLOUD-USEDVEHICLE-TEST";
    //财务服务名称
    public static final String SERVER_NAME_FINANCE		="DMSCLOUD-FINANCE-TEST";
    //衍生业务服务名称
    public static final String SERVER_NAME_VALUEADDED	="DMSCLOUD-VALUEADDED-TEST";
    //潜客服务名称
    public static final String SERVER_NAME_POTENCUS		="DMSCLOUD-POTENCUS-TEST";
    //管理服务名称
    public static final String SERVER_NAME_MANAGE		="DMSCLOUD-MANAGE-TEST";
}
