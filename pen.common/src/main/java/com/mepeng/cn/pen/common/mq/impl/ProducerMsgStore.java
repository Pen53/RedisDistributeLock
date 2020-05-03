package com.mepeng.cn.pen.common.mq.impl;


import com.mepeng.cn.pen.common.domains.DTO.ProducerDto;
import com.mepeng.cn.pen.common.exception.StoreException;

import java.util.List;

public interface ProducerMsgStore {
    void msgStore(String var1, String var2, String var3, String var4, String var5) throws StoreException;

    void msgStoreFailed(String var1, String var2, Long var3, String var4, String var5, String var6, String var7) throws StoreException;

    void update2success(String var1) throws StoreException;

    List<ProducerDto> selectResendList(Integer var1);

    ProducerDto getResendDto(String var1);
}
