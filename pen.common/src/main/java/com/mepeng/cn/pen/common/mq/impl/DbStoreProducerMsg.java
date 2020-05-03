package com.mepeng.cn.pen.common.mq.impl;

import com.mepeng.cn.pen.common.domains.DTO.ProducerDto;
import com.mepeng.cn.pen.common.exception.StoreException;
import com.mepeng.cn.pen.common.util.bean.ApplicationContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DbStoreProducerMsg implements ProducerMsgStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbStoreProducerMsg.class);

    public DbStoreProducerMsg() {
    }

    public void msgStore(String msgKey, String data, String exchange, String routerKey, String bizClassName) throws StoreException {
        ProducerStoreDBCallback producerStoreDBCallback = this.getCallBack();
        if (producerStoreDBCallback != null && data != null && msgKey != null) {
            LOGGER.debug("save msg to db.");
            producerStoreDBCallback.saveMsgData(msgKey, data, exchange, routerKey, bizClassName);
        } else {
            String errorMsg = "";
            if (producerStoreDBCallback == null) {
                errorMsg = "producerStoreDBCallback is null";
            } else if (data == null) {
                errorMsg = "data is null";
            } else {
                errorMsg = "msgKey is null";
            }

            LOGGER.error("msgKey is null");
            throw new StoreException(errorMsg);
        }
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {RuntimeException.class}
    )
    public void msgStoreFailed(String msgKey, String infoMsg, Long costTime, String exchange, String routerKey, String data, String bizClassName) throws StoreException {
        ProducerStoreDBCallback producerStoreDBCallback = this.getCallBack();
        if (producerStoreDBCallback != null && msgKey != null) {
            LOGGER.debug("data encounter error: " + infoMsg);
            producerStoreDBCallback.update2faild(msgKey, infoMsg, costTime, exchange, routerKey, data, bizClassName);
        } else {
            String errorMsg = "";
            if (producerStoreDBCallback == null) {
                errorMsg = "dbStoreUserCallback is null";
            } else {
                errorMsg = "msgKey is null";
            }

            LOGGER.error("msgKey is null");
            throw new StoreException(errorMsg);
        }
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {RuntimeException.class}
    )
    public void update2success(String msgKey) throws StoreException {
        ProducerStoreDBCallback producerStoreDBCallback = this.getCallBack();
        if (producerStoreDBCallback != null && msgKey != null) {
            LOGGER.debug("data encounter error: " + msgKey);
            producerStoreDBCallback.update2success(msgKey);
        } else {
            String errorMsg = "";
            if (producerStoreDBCallback == null) {
                errorMsg = "dbStoreUserCallback is null";
            } else {
                errorMsg = "msgKey is null";
            }

            LOGGER.error("msgKey is null");
            throw new StoreException(errorMsg);
        }
    }

    private ProducerStoreDBCallback getCallBack() {
        ProducerStoreDBCallback callback = (ProducerStoreDBCallback) ApplicationContextHelper.getBean(ProducerStoreDBCallback.class);
        return callback;
    }

    public List<ProducerDto> selectResendList(Integer status) {
        ProducerStoreDBCallback producerStoreDBCallback = this.getCallBack();
        return producerStoreDBCallback.selectResendList(status);
    }

    public ProducerDto getResendDto(String msgkey) {
        ProducerStoreDBCallback producerStoreDBCallback = this.getCallBack();
        return producerStoreDBCallback.getResendProducerDto(msgkey);
    }
}
