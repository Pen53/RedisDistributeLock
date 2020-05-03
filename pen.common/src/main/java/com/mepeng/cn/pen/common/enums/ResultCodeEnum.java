package com.mepeng.cn.pen.common.enums;

/**
 * 响应状态码
 *
 * @Description: 响应状态码
 * @File name : ResultCodeEnum.java
 * @Author : DELL
 * @Date : 2019/1/17 9:59
 */
public enum ResultCodeEnum {


    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    SUCCESS201(201, "操作成功"),
    /**
     * 查询没有数据 返回202
     */
    SUCCESS202(202, "暂无数据！"),

    /**
     * 部分成功
     */
    PARTIAL_SUCCESS(201, "部分成功"),

    /**
     * 数据重复请求
     */
    REPEAT_REQUEST(202, "数据重复请求"),

    /**
     * 非法jwt请求
     */
    ILLEGAL_JWT(40104, "非法jwt请求"),

    /**
     * jwt失效
     */
    INVALID_JWT(40105, "jwt失效"),

    /**
     * 业务异常
     */
    SERVICE_ERROR(901, "业务异常"),

    /**
     * XX必填字段为空
     */
    FIELD_NOT_NULL(90101, "【%s】为必填字段为空"),

    /**
     * XXX不存在
     */
    NOT_EXISTS(90102, "数据不合法，【%s】不存在"),

    /**
     * 非法操作，当前状态不允许操作
     */
    NOT_ALLOW_OPERATION(90103, "非法操作，当前状态不允许操作"),

    /**
     * 车辆释放失败
     */
    VEHICLE_RELEASE(90104, "车辆释放失败，VIN：%s"),

    /**
     * XXX已存在
     */
    ALREADY_EXISTS(90104, "【%s】：【%s】已存在"),

    /**
     * 自定义描述
     */
    CUSTOMIZE_ERROR(90105, "操作失败，失败原因：%s"),

    /**
     * 质损单不存在
     */
    CMASSLOSS_NO_EXISTS(90106, "质损单不存在：%s"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(902, "系统异常：%s"),

    /**
     * 不支持的请求方式
     */
    REQUSET_METHOD_NOT_SUPPORT(90201, "不支持的请求方式：%s，请使用：%s"),

    /**
     * 请求地址找不到
     */
    REQUEST_URL_NOT_FOUND(90202, "接口地址：%s未找到，请求方式：%s");

    private final Integer code;

    private final String desc;

    ResultCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }

    public String parseDesc(Object... params) {
        return String.format(this.desc, params);
    }
}
