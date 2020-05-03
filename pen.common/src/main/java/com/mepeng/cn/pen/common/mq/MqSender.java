package com.mepeng.cn.pen.common.mq;

public interface MqSender {
    void send(String var1, String var2, Object var3, String... var4);

    void justSend(String var1, String var2, Object var3);

    void resend(String... var1);
}
