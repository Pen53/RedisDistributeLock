package com.mepeng.cn.pen.order.consumer;

import com.mepeng.cn.pen.common.mq.impl.AbstractConsumerListener;
import com.mepeng.cn.pen.order.controller.DemoInfoTestDTO;
import com.mepeng.cn.pen.order.service.DemoInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Demo 测试接受RabbitMq消息
 */
@Component
public class DemoReceiveConsumer extends AbstractConsumerListener<DemoInfoTestDTO> {
    private static Logger logger = LoggerFactory.getLogger(DemoReceiveConsumer.class);

    @Autowired
    private DemoInfoService demoInfoServiceImpl;
    @Override
    public void handleMessage(DemoInfoTestDTO dto) throws Exception {
        logger.info("DemoReceiveConsumer handleMessage(DemoInfoTestDTO dto) 传结果接收：{}",dto.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                demoInfoServiceImpl.save(dto);
            }
        }).start();

    }
}
