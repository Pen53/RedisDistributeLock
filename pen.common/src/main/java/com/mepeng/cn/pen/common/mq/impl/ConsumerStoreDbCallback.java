package com.mepeng.cn.pen.common.mq.impl;

import com.mepeng.cn.pen.common.domains.DTO.ConsumerDto;
import com.mepeng.cn.pen.common.exception.StoreDBCallbackException;

import java.util.List;

public interface ConsumerStoreDbCallback {
    boolean exist(String var1) throws StoreDBCallbackException;

    void saveMsgData(String var1, String var2, String var3, String var4, String var5, String var6) throws StoreDBCallbackException;

    void updateMsgSuccess(String var1) throws StoreDBCallbackException;

    void updateMsgFaild(String var1) throws StoreDBCallbackException;

    List<ConsumerDto> selectReConsumerList(Integer var1);

    ConsumerDto getReConsumerDto(String var1);
}
