
/**
*Copyright 2018 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dmscloud.framework
*
* @File name : MQBaseConfig.java
*
* @Author : Lius
*
* @Date : 2018年7月25日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2018年7月25日    Lius    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.mepeng.cn.pen.common.configurer;

import com.mepeng.cn.pen.common.mq.impl.MqSenderDefaultImpl;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * RabbitMQ 消息配置
 *
 * @author pengwei
 * @date 2020年4月20日
 */
@Configuration
public class MyMQBaseConfig {

    @Bean
    public MessageConverter messageConverter() {
        System.out.println("MyMQBaseConfig init ------messageConverter init-is doing");
        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        return jsonMessageConverter;
    }

    @Bean
    public MqSenderDefaultImpl mqSenderDefaultImpl(RabbitOperations rabbitOperations) {
        System.out.println("MyMQBaseConfig init ------mqSenderDefaultImpl init-is doing");
        MqSenderDefaultImpl mqSenderDefaultImpl = new MqSenderDefaultImpl();
        mqSenderDefaultImpl.setRabbitOperations(rabbitOperations);
        return mqSenderDefaultImpl;
    }

}
