package com.mepeng.cn.pen.common.mq.impl;

import com.rabbitmq.client.Channel;
import com.mepeng.cn.pen.common.annotation.MomConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractConsumerListener<T> implements ChannelAwareMessageListener {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumerListener.class);
    @Autowired
    MessageConverter messageConverter;

    public AbstractConsumerListener() {
    }

    @MomConsumer
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            Object obj = this.messageConverter.fromMessage(message);
            T object = (T) this.messageConverter.fromMessage(message);
            LOGGER.debug("AbstractConsumerListener onMessage msg content is:" + object);
            this.handleMessage(object);
        } catch (MessageConversionException var4) {
            LOGGER.error("AbstractConsumerListener onMessage MessageConversionException:", var4);
            throw  var4;
        }

    }

    public abstract void handleMessage(T var1) throws Exception;
}
