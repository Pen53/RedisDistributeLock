package com.mepeng.cn.pen.common.mq.impl;

import com.mepeng.cn.pen.common.domains.DTO.ConsumerDto;
import com.mepeng.cn.pen.common.exception.StoreException;
//import com.yonyou.dmscloud.common.domains.DTO.ConsumerDto;
//import com.yonyou.dmscloud.common.exception.StoreException;

import java.util.List;

public interface ConsumerMsgStore {
    boolean exist(String var1) throws StoreException;

    void saveMsgData(String var1, String var2, String var3, String var4, String var5, String var6) throws StoreException;

    void updateMsgSuccess(String var1) throws StoreException;

    void updateMsgFaild(String var1) throws StoreException;

    List<ConsumerDto> selectReConsumerList(Integer var1);

    ConsumerDto getReConsumerDto(String var1);
}
