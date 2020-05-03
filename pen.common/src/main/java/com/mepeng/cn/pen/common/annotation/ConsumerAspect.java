package com.mepeng.cn.pen.common.annotation;

import com.rabbitmq.client.Channel;
import com.mepeng.cn.pen.common.mq.impl.ConsumerMsgStore;
import com.mepeng.cn.pen.common.mq.impl.DbStoreConsumerMsg;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ConsumerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerAspect.class);

    @Autowired
    MessageConverter messageConverter;

    private ConsumerMsgStore dbStoreConsumerMsg = new DbStoreConsumerMsg();

    @Around("@annotation(com.mepeng.cn.pen.common.annotation.MomConsumer)")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Message message = (Message)args[0];
        Channel channel = (Channel)args[1];
        Object object = null;
        boolean exist = false;
        String msgKey = message.getMessageProperties().getCorrelationId();

            try {
                object = this.messageConverter.fromMessage(message);
                LOGGER.info("ConsumerAspect aroundAdvice msg msgKey:{},message:{}" , msgKey,object);
                exist = this.dbStoreConsumerMsg.exist(msgKey);
            } catch (MessageConversionException var19) {
                LOGGER.error("异常转换 ConsumerAspect MessageConversionException1:", var19);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return null;
            }
            if (object != null) {
                if (!exist) {
                    ObjectMapper mapper = new ObjectMapper();
                    String dataConvert = mapper.writeValueAsString(object);
                    String bizclassName = message.getMessageProperties().getHeaders().get("__TypeId__").toString();
                    String consumerClassName = pjp.getTarget().getClass().getName();

                    this.dbStoreConsumerMsg.saveMsgData(msgKey, dataConvert, message.getMessageProperties().getReceivedExchange(), message.getMessageProperties().getConsumerQueue(), consumerClassName, bizclassName);

                    Object rtnOb;
                    try {
                        rtnOb = pjp.proceed();//调用方法

                        this.dbStoreConsumerMsg.updateMsgSuccess(msgKey);

                        //确认MQ消息已经消费
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        LOGGER.info("channel.basicAck 已经消费 msgKey:{}", msgKey);
                        return rtnOb;
                    } catch (MessageConversionException var19) {
                        this.dbStoreConsumerMsg.updateMsgFaild(msgKey);

                        LOGGER.error("channel.basicAck 异常已经消费 msgKey:" +msgKey + ",异常转换 ConsumerAspect MessageConversionException2:", var19);
                        //确认MQ消息已经消费(异常转换ＤＴＯ)
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        //throw var19;
                        return null;
                    } catch (Exception e1) {
                        this.dbStoreConsumerMsg.updateMsgFaild(msgKey);
                        /**
                         * TT_PRODUCER_MSG MSG_KEY:msgKey STATUS:100时 exist:true
                         * 运行成功STATUS:101 此时确认消息
                         * 运行失败STATUS:102 此时不确认消息
                         * 默认aroundAdvice 会接到Rabbit MQ三次发送消息
                         * 但是只会第一次处理时保存TT_PRODUCER_MSG MSG_KEY:msgKey STATUS:100 exist:false 这时会执行业务代码
                         * 业务代码正常处理，此次MQ消息会确认，否则不会确认，其他服务可以重新消费此MQ消息
                         * 第二次时 TT_PRODUCER_MSG MSG_KEY:msgKey STATUS:100  exist:true  这时不会执行业务代码 exist:false
                         * (可能后执行，此时第一个批量已经处理完STATUS为101或者102)会执行业务代码，
                         * 这时业务代码最好要有唯一识别是否处理过 业务代码正常处理，此次MQ消息会确认，否则不会确认，其他服务可以重新消费此MQ消息
                         * 第三次时 情况与第二次类似
                         *
                         */
                        LOGGER.error("不确认MQ消息已经消费等待下次消费 ConsumerAspect object is not null msgKey is not exist,msgKey:"+msgKey+",Exception:",e1);
                        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        //下次mq会继续推消息供消费者消费
                        //throw e1;
                        return null;
                    }
                }else{
                    LOGGER.error("确认MQ消息已经消费 ConsumerAspect object is not null msgKey is exists,msgKey:"+msgKey);
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    return null;
                }
            }else{
                LOGGER.error("确认MQ消息已经消费 ConsumerAspect object is null msgKey:"+msgKey);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return null;
            }

    }
}
