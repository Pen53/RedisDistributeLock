
/** 
*Copyright 2018 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dmscloud.framework
*
* @File name : ProducerMsgPO.java
*
* @Author : Lius
*
* @Date : 2018年7月24日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2018年7月24日    Lius    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.mepeng.cn.pen.common.domains.PO.msg;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

/**
 * 消息生产者PO
 * 
 * @author Lius
 * @date 2018年7月24日
 */
@Table("TT_PRODUCER_MSG")
@IdName("MSG_ID")
public class ProducerMsgPO extends Model {
//BaseModel
}
