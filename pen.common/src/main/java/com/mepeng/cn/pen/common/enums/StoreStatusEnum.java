package com.mepeng.cn.pen.common.enums;

public enum StoreStatusEnum {
    PRODUCER_INIT(0),
    PRODUCER_SUCCESS(1),
    PRODUCER_FAILD(2),
    CONSUMER_PROCESS(100),
    CONSUMER_SUCCESS(101),
    CONSUMER_FAILD(102);

    private final int value;

    private StoreStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
