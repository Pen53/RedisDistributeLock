package com.mepeng.cn.pen.common.domains.DTO;

public class ProducerDto {
    private String msgKey;
    private String msgContent;
    private Integer status;
    private String exchange;
    private String routerKey;
    private String bizClassName;

    public ProducerDto() {
    }

    public String getMsgKey() {
        return this.msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMsgContent() {
        return this.msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExchange() {
        return this.exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRouterKey() {
        return this.routerKey;
    }

    public void setRouterKey(String routerKey) {
        this.routerKey = routerKey;
    }

    public String getBizClassName() {
        return this.bizClassName;
    }

    public void setBizClassName(String bizClassName) {
        this.bizClassName = bizClassName;
    }
}
