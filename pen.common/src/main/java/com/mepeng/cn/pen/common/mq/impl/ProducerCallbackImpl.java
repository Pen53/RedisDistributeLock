package com.mepeng.cn.pen.common.mq.impl;

import com.mepeng.cn.pen.common.annotation.TxConnection;
import com.mepeng.cn.pen.common.domains.DTO.ProducerDto;
import com.mepeng.cn.pen.common.domains.PO.msg.ProducerMsgPO;
import com.mepeng.cn.pen.common.enums.StoreStatusEnum;
import com.mepeng.cn.pen.common.exception.StoreDBCallbackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ProducerCallbackImpl implements ProducerStoreDBCallback {
    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(ProducerCallbackImpl.class);

//    @Autowired
//    FrameworkParamBean frameworkParam;

    /**
     * 保存消息
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgKey
     * @param data
     * @param exchange
     * @param routerKey
     * @param bizClassName
     * @throws StoreDBCallbackException (non-Javadoc)
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @TxConnection
    public void saveMsgData(String msgKey, String data, String exchange, String routerKey,
                            String bizClassName) throws StoreDBCallbackException {
        logger.info("--MQ生产者,保存消息,msgKey=" + msgKey + ",data=" + data + ",routerKey=" + routerKey);

        ProducerMsgPO msgPO = new ProducerMsgPO();
        msgPO.setString("msg_key", msgKey);
        msgPO.setString("biz_class_name", bizClassName);
        msgPO.setTimestamp("create_time", new Date());
        msgPO.setString("exchange", exchange);
        msgPO.setString("router_key", routerKey);
        msgPO.setInteger("status", StoreStatusEnum.PRODUCER_INIT.getValue());// 0
        msgPO.setTimestamp("update_time", new Date());
        msgPO.setString("msg_content", data);
        boolean f = msgPO.saveIt();
        System.out.println("ProducerCallbackImpl saveMsgData---- f:"+f);

//        List<Map> list = Base.findAll("select * from TT_PRODUCER_MSG");
//        for(Map map:list){
//            System.out.println("ProducerCallbackImpl saveMsgData---map:"+map);
//        }
    }

    /**
     * 发送成功后相关处理
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgKey
     * @throws StoreDBCallbackException (non-Javadoc)
     */
    @Override
    @Transactional
    @TxConnection
    public void update2success(String msgKey) throws StoreDBCallbackException {
        logger.info("--MQ生产者,更新状态为成功,msgKey=" + msgKey);
        ProducerMsgPO msg = ProducerMsgPO.findFirst("MSG_KEY = ? ", msgKey);
        if(msg!=null) {
            msg.setInteger("status", StoreStatusEnum.PRODUCER_SUCCESS.getValue());//
            msg.setTimestamp("update_time", new Date());
            boolean f = msg.saveIt();
            System.out.println("ProducerCallbackImpl update2success --- f:"+f);
        }
    }

    /**
     * 发送成功后相关处理
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgKey
     * @param infoMsg
     * @param costTime
     * @param exchange
     * @param routerKey
     * @param data
     * @param bizClassName
     * @throws StoreDBCallbackException (non-Javadoc)
     * java.lang.String, java.lang.Long, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    @TxConnection
    public void update2faild(String msgKey, String infoMsg, Long costTime, String exchange, String routerKey,
                             String data, String bizClassName) throws StoreDBCallbackException {
        logger.info("--MQ生产者,更新状态为成功,msgKey=" + msgKey);
        ProducerMsgPO msg = ProducerMsgPO.findFirst("MSG_KEY = ? ", msgKey);
        if(msg!=null) {
            msg.setString("info_msg", infoMsg);
            msg.setString("router_key", routerKey);
            msg.setInteger("status", StoreStatusEnum.PRODUCER_FAILD.getValue());
            msg.setTimestamp("update_time", new Date());
            boolean r = msg.saveIt();
            System.out.println("ProducerCallbackImpl update2faild r:"+r);
        }
    }

    /**
     * 查询需要重发的消息
     *
     * @author Lius
     * @date 2018年7月24日
     * @param status
     * @return (non-Javadoc)
     */
    @Override
    @Transactional
    @TxConnection
    public List<ProducerDto> selectResendList(Integer status) {
        List<ProducerDto> returnList = new ArrayList<ProducerDto>();
        List<ProducerMsgPO> msgList = ProducerMsgPO.find("STATUS = ? ", StoreStatusEnum.PRODUCER_FAILD.getValue());
        for (int i = 0; i < msgList.size(); i++) {
            ProducerDto returnDto = new ProducerDto();
            returnDto.setBizClassName(msgList.get(i).getString("BIZ_CLASS_NAME"));
            returnDto.setExchange(msgList.get(i).getString("EXCHANGE"));
            returnDto.setMsgContent(msgList.get(i).getString("MSG_CONTENT"));
            returnDto.setMsgKey(msgList.get(i).getString("MSG_KEY"));
            returnDto.setRouterKey(msgList.get(i).getInteger("RETRY_COUNT").toString());
            returnDto.setStatus(status);
            returnList.add(returnDto);
        }
        return returnList;
    }

    /**
     * 根据msgKey查询单条需要重发的消息
     *
     * @author Lius
     * @date 2018年7月24日
     * @param msgkey
     * @return
     */
    @Transactional
    @TxConnection
    public ProducerDto getResendProducerDto(String msgkey) {
        ProducerDto dto = new ProducerDto();
        ProducerMsgPO msg = ProducerMsgPO.findFirst("MSG_KEY = ? ", msgkey);
        BeanUtils.copyProperties(msg, dto);
        return dto;
    }
}
