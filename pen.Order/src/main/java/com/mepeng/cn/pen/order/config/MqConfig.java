package com.mepeng.cn.pen.order.config;

import com.mepeng.cn.pen.common.constants.MQConstants;
import com.mepeng.cn.pen.order.consumer.DemoReceiveConsumer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ监听队列绑定
 *
 * @author pengwei
 * @date 2020年4月20日
 */
@Configuration
public class MqConfig {
    public Queue clueListenerQueue(String queueName) {
        // 队列持久
        return new Queue(queueName, true);
    }
    public DirectExchange clueExchange(String exchange) {
        return new DirectExchange(exchange);
    }

    public Binding clueBinding(Queue queue, String exchange, String routingKey) {
        return BindingBuilder.bind(queue).to(clueExchange(exchange)).with(routingKey);
    }

    private SimpleMessageListenerContainer addListener(ConnectionFactory connectionFactory, Queue queue,
                                                       ChannelAwareMessageListener listener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue);
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        // 设置确认模式手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(listener);
        // 设置最大消费者数量 防止大批量涌入
        container.setMaxConcurrentConsumers(10);
        return container;
    }
    /**
     * Demo 测试接受RabbitMq消息
     * @param connectionFactory
     * @param
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer demoReceiveConsumerResultReceiveHandler(ConnectionFactory connectionFactory,
                                                                                  DemoReceiveConsumer consumer) {
        System.out.println("666666------MqConfigdemoReceiveConsumerResultReceiveHandler is doing init");
        Queue queue = clueListenerQueue(MQConstants.JKYX_ORDER_AUDIT_QUEUE);
        clueBinding(queue, MQConstants.JKYX_ORDER_EXCHANGE, MQConstants.JKYX_ORDER_AUDIT_KEY);
        return this.addListener(connectionFactory, queue, consumer);
    }
}
