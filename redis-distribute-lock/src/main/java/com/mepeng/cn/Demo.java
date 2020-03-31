package com.mepeng.cn;

import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.SingleServerConfig;

public class Demo {
    public static void main(String[] args) {
        SingleServerConfig serverConfig = null;
        serverConfig.getDatabase();
        JsonJacksonCodec codec;
    }
}
