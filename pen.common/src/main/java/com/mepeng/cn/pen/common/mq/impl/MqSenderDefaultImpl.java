package com.mepeng.cn.pen.common.mq.impl;

import com.mepeng.cn.pen.common.domains.DTO.ProducerDto;
import com.mepeng.cn.pen.common.enums.StoreStatusEnum;
import com.mepeng.cn.pen.common.mq.MqSender;
import lombok.SneakyThrows;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitGatewaySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

//@Service("mqSenderDefaultImpl")
public class MqSenderDefaultImpl extends RabbitGatewaySupport implements MqSender {
    protected final Logger LOGGER = LoggerFactory.getLogger(MqSenderDefaultImpl.class);
    private TransactionExecutor beforeCommitExecutor = new PreCommitExecutorDefaultImpl();
    private TransactionExecutor afterCommitExecutor = new AfterCommitExecutorDefaultImpl();
    @Autowired
    private ProducerMsgStore msgStore;
    //RabbitTemplate rabbitTemplate;
    public MqSenderDefaultImpl() {

    }

//    @Transactional
    public void send(String exchange, String routeKey, Object data, String... bizCodes) {
        StringBuffer msgKey = new StringBuffer();
        msgKey.append(UUID.randomUUID().toString());
        msgKey.append(StringUtils.isEmpty(StringUtils.join(bizCodes, "&")) ? "" : "&" + StringUtils.join(bizCodes, "&"));
        ObjectMapper mapper = new ObjectMapper();

        try {
            String dataConvert = mapper.writeValueAsString(data);
            //this.storeMsg(dataConvert, msgKey.toString(), exchange, routeKey, data.getClass().getName());
            this.storeMsg(dataConvert, msgKey.toString(), exchange, routeKey, data.getClass().getName(),data);
        } catch (IOException var9) {
            throw new AmqpException(var9);
        }

        //this.sendToMQ(exchange, routeKey, msgKey.toString(), data);
    }

    public void justSend(String exchange, String routeKey, Object data) {
        this.sendRabbitQ(exchange, routeKey, "", data);
    }

    private void sendToMQ(final String exchange, final String routeKey, final String msgKey, final Object data) {
        this.afterCommitExecutor.execute(new Runnable() {
            public void run() {
                ObjectMapper mapper = new ObjectMapper();

                String dataConvert;
                try {
                    dataConvert = mapper.writeValueAsString(data);
                } catch (IOException var9) {
                    throw new AmqpException(var9);
                }

                Long startTime = 0L;

                try {
                    MqSenderDefaultImpl.this.LOGGER.debug("------发送消息开始------");
                    startTime = System.currentTimeMillis();
                    MqSenderDefaultImpl.this.sendRabbitQ(exchange, routeKey, msgKey, data);
                    MqSenderDefaultImpl.this.msgStore.update2success(msgKey);

//                    try {
//                        if (MqSenderDefaultImpl.this.isTacks) {
//                            Map<String, Object> properties = new HashMap();
//                            properties.put("type", "PRODUCER");
//                            properties.put("msgKey", msgKey);
//                            properties.put("sender", data.getClass().getName());
//                            properties.put("exchangeName", exchange);
//                            properties.put("routingKey", routeKey != null ? routeKey : "");
//                            properties.put("data", dataConvert);
//                            properties.put("success", "true");
//                            properties.put("host", MqSenderDefaultImpl.this.address.applicationAndHost().get("hostIpAndPro"));
//                            properties.put("serviceUrl", MqSenderDefaultImpl.this.address.applicationAndHost().get("applicationAddress"));
//                            MqSenderDefaultImpl.this.tack.track("msgProducer", "mqTrack", properties);
//                            MqSenderDefaultImpl.this.tack.shutdown();
//                        }
//                    } catch (Exception var7) {
//                        MqSenderDefaultImpl.this.LOGGER.error("埋点msgProducer 发生异常", var7);
//                    }

                } catch (Exception var8) {
                    Exception e = var8;
                    MqSenderDefaultImpl.this.LOGGER.debug("------发送消息异常，调用消息存储失败的方法------");
                    MqSenderDefaultImpl.this.msgStore.msgStoreFailed(msgKey, var8.getMessage(), System.currentTimeMillis() - startTime, exchange, routeKey, dataConvert, data.getClass().getName());

//                    try {
//                        if (MqSenderDefaultImpl.this.isTacks) {
//                            Map<String, Object> propertiesx = new HashMap();
//                            propertiesx.put("type", "PRODUCER");
//                            propertiesx.put("msgKey", msgKey.toString());
//                            propertiesx.put("sender", data.getClass().getName());
//                            propertiesx.put("serviceUrl", MqSenderDefaultImpl.this.address.applicationAddress());
//                            propertiesx.put("exchangeName", exchange);
//                            propertiesx.put("routingKey", routeKey != null ? routeKey : "");
//                            propertiesx.put("data", dataConvert);
//                            propertiesx.put("success", "false");
//                            propertiesx.put("host", MqSenderDefaultImpl.this.address.applicationAndHost().get("hostIpAndPro"));
//                            propertiesx.put("serviceUrl", MqSenderDefaultImpl.this.address.applicationAndHost().get("applicationAddress"));
//                            propertiesx.put("infoMsg", e.getMessage());
//                            MqSenderDefaultImpl.this.tack.track("msgProducer", "mqTrack", propertiesx);
//                            MqSenderDefaultImpl.this.tack.shutdown();
//                        }
//                    } catch (Exception var6) {
//                        MqSenderDefaultImpl.this.LOGGER.error("埋点msgProducer 发生异常", var6);
//                    }

                    throw var8;
                }
            }
        });
    }

    protected void sendRabbitQ(String exchange, String routeKey, final String correlation, Object data) {
        this.getRabbitOperations().convertAndSend(exchange, routeKey, data, new MessagePostProcessor() {
            public Message postProcessMessage(Message message) throws AmqpException {
                try {
                    message.getMessageProperties().setCorrelationId(correlation);
                    message.getMessageProperties().setContentType("json");
                    return message;
                } catch (Exception var3) {
                    throw new AmqpException(var3);
                }
            }
        });
        this.LOGGER.debug("------消息发送完成------");
    }
    @Deprecated
    private void storeMsg(final String data, final String msgKey, final String exchange, final String routerKey, final String bizClassName) throws IOException {
        /*this.beforeCommitExecutor.execute(new Runnable() {
            public void run() {
                MqSenderDefaultImpl.this.msgStore.msgStore(msgKey, data, exchange, routerKey, bizClassName);
            }
        });*/
        new Thread(new Runnable() {
            public void run() {
                System.out.println("666666------storeMsg is doing");
                MqSenderDefaultImpl.this.msgStore.msgStore(msgKey, data, exchange, routerKey, bizClassName);
            }
        }).start();
    }

    private void storeMsg(final String dataStr, final String msgKey, final String exchange, final String routerKey, final String bizClassName,Object data) throws IOException {
        final String routeKey = routerKey;
        Thread t2 = new Thread(new Runnable() {
            @SneakyThrows
            public void run() {
                final Long startTime = System.currentTimeMillis();

                try {
                    MqSenderDefaultImpl.this.msgStore.msgStore(msgKey, dataStr, exchange, routerKey, bizClassName);
                }catch (Exception e){
                    MqSenderDefaultImpl.this.LOGGER.error("------发送消息异常，保存消息状态失败------msgKey:{},e:{}",msgKey,e);
                    throw e;
                }

                try {
                    MqSenderDefaultImpl.this.LOGGER.debug("------发送消息开始------");
                    MqSenderDefaultImpl.this.sendRabbitQ(exchange, routeKey, msgKey, data);
                    try {
                        MqSenderDefaultImpl.this.msgStore.update2success(msgKey);
                    }catch (Exception e){
                        MqSenderDefaultImpl.this.LOGGER.error("------发送消息ＭＱ成功，保存消息状态失败------msgKey:{},e:{}",msgKey,e);
                        throw e;
                    }


                } catch (Exception var8) {
                    Exception e = var8;
                    MqSenderDefaultImpl.this.LOGGER.debug("------发送消息异常，调用消息存储失败的方法------",var8);
                    Thread td = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MqSenderDefaultImpl.this.msgStore.msgStoreFailed(msgKey, var8.getMessage(), System.currentTimeMillis() - startTime, exchange, routeKey, dataStr, data.getClass().getName());
                        }
                    });
                    td.start();
                    throw var8;
                }

            }
        });
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
    public void resend(String... msgKeys) {
        if (msgKeys.length > 0) {
            List<ProducerDto> list = new ArrayList();
            String[] var3 = msgKeys;
            int var4 = msgKeys.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String msgKey = var3[var5];
                ProducerDto dto = this.msgStore.getResendDto(msgKey);
                list.add(dto);
            }

            this.sendListToMQ(list);
        } else {
            List<ProducerDto> list = this.msgStore.selectResendList(StoreStatusEnum.PRODUCER_FAILD.getValue());
            this.sendListToMQ(list);
        }

    }

    public void sendListToMQ(List<ProducerDto> list) {
        Iterator it = list.iterator();

        while(it.hasNext()) {
            ProducerDto msgEntity = (ProducerDto)it.next();
            this.LOGGER.info(msgEntity.getMsgContent() + "消息内容");

            try {
                Class<?> c = Class.forName(msgEntity.getBizClassName());
                JSONObject obj = JSONObject.fromObject(msgEntity.getMsgContent());
                Object ojbClass = JSONObject.toBean(obj, c);
                this.sendToMQ(msgEntity.getExchange(), msgEntity.getRouterKey(), msgEntity.getMsgKey(), ojbClass);
            } catch (ClassNotFoundException var7) {
                var7.printStackTrace();
            }
        }

    }
}
