package com.mepeng.cn.pen.order.controller;

import com.mepeng.cn.pen.common.domains.DTO.ParentDTO;

public class DemoInfoTestDTO extends ParentDTO {
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    private String orderNo;     //订单号
}
