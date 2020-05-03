package com.mepeng.cn.pen.common.mq.impl;

import com.mepeng.cn.pen.common.domains.DTO.ProducerDto;
import com.mepeng.cn.pen.common.exception.StoreDBCallbackException;

import java.util.List;

public interface ProducerStoreDBCallback {
    void saveMsgData(String var1, String var2, String var3, String var4, String var5) throws StoreDBCallbackException;

    void update2success(String var1) throws StoreDBCallbackException;

    void update2faild(String var1, String var2, Long var3, String var4, String var5, String var6, String var7) throws StoreDBCallbackException;

    List<ProducerDto> selectResendList(Integer var1);

    ProducerDto getResendProducerDto(String var1);
}
