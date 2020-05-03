package com.mepeng.cn.pen.common.mq.impl;

import com.mepeng.cn.pen.common.annotation.TxConnection;
import com.mepeng.cn.pen.common.domains.DTO.ConsumerDto;
import com.mepeng.cn.pen.common.domains.PO.msg.ConsumerMsgPO;
import com.mepeng.cn.pen.common.enums.StoreStatusEnum;
import com.mepeng.cn.pen.common.exception.StoreDBCallbackException;
import com.mepeng.cn.pen.common.util.common.CommonUtils;
//import com.yonyou.dmscloud.common.domains.DTO.ConsumerDto;
//import com.yonyou.dmscloud.common.enums.StoreStatusEnum;
//import com.yonyou.dmscloud.common.exception.StoreDBCallbackException;
//import com.yonyou.dmscloud.framework.domains.PO.msg.ConsumerMsgPO;
//import com.yonyou.dmscloud.function.utils.common.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消费者实现回调 (消息状态：100 消息消费初始化,101 消息消费成功,102 消息消费失败 )
 *
 * @author pengwei
 * @date 2020年4月20日
 */
@Component
public class ConsumerCallbackImpl implements ConsumerStoreDbCallback {
    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(ConsumerCallbackImpl.class);

    /**
     * 根据msgkey判断消息是否存在{true：存在,false:不存在}
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgKey
     * @returnf
     */
    @Override
    @TxConnection
    public boolean exist(String msgKey) throws StoreDBCallbackException {
        logger.info("--MQ消费者,检查消息是否存在,msgKey=" + msgKey);
        List<ConsumerMsgPO> list = ConsumerMsgPO.find("msg_key = ? and status = ? ", msgKey,
                StoreStatusEnum.CONSUMER_PROCESS.getValue());// 100
        if (CommonUtils.isNullOrEmpty(list)) {
            return false;
        }
        return true;
    }

    /**
     * 保存接受到的信息
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgKey
     * @param data
     * @param exchange
     * @param routerKey
     * @param consumerClassName
     * @param bizClassName
     * @throws StoreDBCallbackException
     */
    @Override
    @TxConnection
    public void saveMsgData(String msgKey, String data, String exchange, String routerKey, String consumerClassName,
                            String bizClassName) throws StoreDBCallbackException {
        logger.info("--MQ消费者,保存消息,msgKey=" + msgKey + ",data=" + data + ",routerKey=" + routerKey);
        ConsumerMsgPO msg = ConsumerMsgPO.findFirst("msg_key = ?", msgKey);
        if (msg == null) {
            msg = new ConsumerMsgPO();
            msg.setString("msg_key", msgKey);
            msg.setString("exchange", exchange);
            msg.setString("msg_content", data);
            msg.setString("router_key", routerKey);
            msg.setInteger("retry_count", 0);
            msg.setTimestamp("create_time", new Date());
            msg.setString("biz_class_name", bizClassName);
            msg.setString("consumer_class_name", consumerClassName);
            msg.setInteger("status", StoreStatusEnum.CONSUMER_PROCESS.getValue());// 100
            msg.saveIt();
        }
    }

    /**
     * 消息消费成功后相关操作
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgKey
     * @throws StoreDBCallbackException (non-Javadoc)
     */
    @Override
    @TxConnection
    public void updateMsgSuccess(String msgKey) throws StoreDBCallbackException {
        logger.info("--MQ消费者,更新状态为成功,msgKey=" + msgKey);
        ConsumerMsgPO msg = ConsumerMsgPO.findFirst("msg_key = ?", msgKey);
        if (msg != null) {
            msg.setString("msg_key", msgKey);
            msg.setInteger("status", StoreStatusEnum.CONSUMER_SUCCESS.getValue());// 101
            msg.saveIt();
        }
    }

    /**
     * 消息消费失败后相关操作
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgKey
     */
    @Override
    @TxConnection
    public void updateMsgFaild(String msgKey) throws StoreDBCallbackException {
        logger.info("--MQ消费者,更新状态为失败,msgKey=" + msgKey);
        ConsumerMsgPO msg = ConsumerMsgPO.findFirst("msg_key = ?", msgKey);
        if (msg != null) {
            msg.setInteger("status", StoreStatusEnum.CONSUMER_FAILD.getValue());// 102
            msg.saveIt();
        }
    }

    /**
     * 查询需要重新消费的信息
     *
     * @author Lius
     * @date 2018年7月24日
     * @param status
     * @return (non-Javadoc)
     */
    @Override
    @TxConnection
    public List<ConsumerDto> selectReConsumerList(Integer status) {
        List<ConsumerDto> dtolist = new ArrayList<>();
        List<ConsumerMsgPO> list = ConsumerMsgPO.find("status = ?", status);
        for (ConsumerMsgPO msgs : list) {
            ConsumerDto dto = new ConsumerDto();
            BeanUtils.copyProperties(msgs, dto);
            dtolist.add(dto);
        }
        return dtolist;
    }

    /**
     * 查询需要重新消费的信息
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgKey
     * @return
     */
    @Override
//    @WithTransactional
    public ConsumerDto getReConsumerDto(String msgKey) {
        ConsumerDto dto = new ConsumerDto();
        ConsumerMsgPO msgs = ConsumerMsgPO.findFirst("msg_key = ?", msgKey);
        BeanUtils.copyProperties(msgs, dto);
        return dto;
    }

    /**
     * 根据msgkey判断消息是否存在{true：存在,false:不存在}
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgKey
     * @return
     * @throws StoreDBCallbackException (non-Javadoc)
     * @see com.yonyou.cloud.mom.core.store.callback.ConsumerStoreDbCallback#isProcessing(String)
     */
    @Deprecated
    public boolean isProcessing(String msgKey) throws StoreDBCallbackException {
        List<ConsumerMsgPO> list = ConsumerMsgPO.find("msg_key = ? and status = ? ", msgKey,
                StoreStatusEnum.CONSUMER_PROCESS.getValue()); // 100
        if (CommonUtils.isNullOrEmpty(list)) {
            return false;
        }
        return true;

    }

    /**
     * 保存接受到的信息
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgKey
     * @param data
     * @param exchange
     * @param routerKey
     * @param consumerClassName
     * @param bizClassName
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Deprecated
    public void updateMsgProcessing(String msgKey, String data, String exchange, String routerKey,
                                    String consumerClassName, String bizClassName) throws StoreDBCallbackException {
        ConsumerMsgPO msg = ConsumerMsgPO.findFirst("msg_key = ?", msgKey);
        if (msg == null) {
            msg = new ConsumerMsgPO();
            msg.setString("msg_key", msgKey);
            msg.setTimestamp("create_time", new Date());
            msg.setInteger("status", StoreStatusEnum.CONSUMER_PROCESS.getValue());
            msg.setTimestamp("update_time", new Date());
            msg.setString("msg_content", data);
            msg.setString("exchange", exchange);
            msg.setString("router_key", routerKey);
            msg.setInteger("retry_count", 0);
            msg.setString("biz_class_name", bizClassName);
            msg.setString("consumer_class_name", consumerClassName);
            msg.saveIt();
        }

    }
}
