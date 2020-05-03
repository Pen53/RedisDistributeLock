package com.mepeng.cn.pen.common.domains.DTO;

public class ConsumerDto {
    private String msgKey;
    private String routerKey;
    private String msgContent;
    private Integer status;
    private String infoMsg;
    private Integer retryCount;
    private String consumerClassName;
    private String bizClassName;
    private String exchange;

    public ConsumerDto() {
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

    public String getInfoMsg() {
        return this.infoMsg;
    }

    public void setInfoMsg(String infoMsg) {
        this.infoMsg = infoMsg;
    }

    public Integer getRetryCount() {
        return this.retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getConsumerClassName() {
        return this.consumerClassName;
    }

    public void setConsumerClassName(String consumerClassName) {
        this.consumerClassName = consumerClassName;
    }

    public String getBizClassName() {
        return this.bizClassName;
    }

    public void setBizClassName(String bizClassName) {
        this.bizClassName = bizClassName;
    }

    public String getRouterKey() {
        return this.routerKey;
    }

    public void setRouterKey(String routerKey) {
        this.routerKey = routerKey;
    }

    public String getExchange() {
        return this.exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String toString() {
        return "ConsumerDto [msgKey=" + this.msgKey + ", routerKey=" + this.routerKey + ", msgContent=" + this.msgContent + ", status=" + this.status + ", infoMsg=" + this.infoMsg + ", retryCount=" + this.retryCount + ", consumerClassName=" + this.consumerClassName + ", bizClassName=" + this.bizClassName + ", exchange=" + this.exchange + "]";
    }
}
