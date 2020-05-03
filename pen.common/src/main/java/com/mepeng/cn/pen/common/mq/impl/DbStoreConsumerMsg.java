package com.mepeng.cn.pen.common.mq.impl;

import com.mepeng.cn.pen.common.domains.DTO.ConsumerDto;
import com.mepeng.cn.pen.common.exception.StoreException;
import com.mepeng.cn.pen.common.util.bean.ApplicationContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbStoreConsumerMsg implements ConsumerMsgStore{
    private static final Logger LOGGER = LoggerFactory.getLogger(DbStoreConsumerMsg.class);
    public DbStoreConsumerMsg() {
    }

    private ConsumerStoreDbCallback getCallBack() {
        ConsumerStoreDbCallback callback = (ConsumerStoreDbCallback) ApplicationContextHelper.getBeanByType(ConsumerStoreDbCallback.class);
        return callback;
    }

    public boolean exist(String msgKey) throws StoreException {
        ConsumerStoreDbCallback callback = this.getCallBack();
        final boolean[] isProcessing = new boolean[1];
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ConsumerStoreDbCallback callback = (ConsumerStoreDbCallback) ApplicationContextHelper.getBeanByType(ConsumerStoreDbCallback.class);
                isProcessing[0] = callback.exist(msgKey);
            }
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isProcessing[0];
    }

    public void saveMsgData(String msgKey, String data, String exchange, String queue, String consumerClassName, String bizClassName) throws StoreException {
       //ConsumerStoreDbCallback callback = this.getCallBack();
       //callback.saveMsgData(msgKey, data, exchange, queue, consumerClassName, bizClassName);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ConsumerStoreDbCallback callback = (ConsumerStoreDbCallback) ApplicationContextHelper.getBean(ConsumerStoreDbCallback.class);
                callback.saveMsgData(msgKey, data, exchange, queue, consumerClassName, bizClassName);
            }
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateMsgSuccess(String msgKey) throws StoreException {
//        ConsumerStoreDbCallback callback = this.getCallBack();
//        callback.updateMsgSuccess(msgKey);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ConsumerStoreDbCallback callback = (ConsumerStoreDbCallback) ApplicationContextHelper.getBean(ConsumerStoreDbCallback.class);
                callback.updateMsgSuccess(msgKey);

            }
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateMsgFaild(String msgKey) throws StoreException {
//        ConsumerStoreDbCallback callback = this.getCallBack();
//        callback.updateMsgFaild(msgKey);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ConsumerStoreDbCallback callback = (ConsumerStoreDbCallback) ApplicationContextHelper.getBean(ConsumerStoreDbCallback.class);
                callback.updateMsgFaild(msgKey);

            }
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<ConsumerDto> selectReConsumerList(Integer status) {
//        ConsumerStoreDbCallback callback = this.getCallBack();
//        return callback.selectReConsumerList(status);
        final List<ConsumerDto>[] list = new List[1];
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ConsumerStoreDbCallback callback = (ConsumerStoreDbCallback) ApplicationContextHelper.getBean(ConsumerStoreDbCallback.class);
                list[0] = callback.selectReConsumerList(status);

            }
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list[0];
    }

    public ConsumerDto getReConsumerDto(String msgKey) {
//        ConsumerStoreDbCallback callback = this.getCallBack();
//        return callback.getReConsumerDto(msgKey);
        final ConsumerDto[] dto = new ConsumerDto[1];
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ConsumerStoreDbCallback callback = (ConsumerStoreDbCallback) ApplicationContextHelper.getBean(ConsumerStoreDbCallback.class);
                dto[0] = callback.getReConsumerDto(msgKey);

            }
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dto[0];
    }
}
