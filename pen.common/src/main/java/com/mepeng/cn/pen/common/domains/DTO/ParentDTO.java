package com.mepeng.cn.pen.common.domains.DTO;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 父DTO
 */
public class ParentDTO implements Serializable {


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
