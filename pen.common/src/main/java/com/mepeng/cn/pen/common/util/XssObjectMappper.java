package com.mepeng.cn.pen.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 定义DMS 的对象转换逻辑
 * 
 * @author zhangxc
 * @date 2016年9月6日
 */

@SuppressWarnings("serial")
public class XssObjectMappper extends ObjectMapper {

    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(XssObjectMappper.class);

    public XssObjectMappper(){
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.setSerializationInclusion(Include.NON_NULL);
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new JsonHtmlXssSerializer());
        this.registerModule(module);
    }

    static class JsonHtmlXssSerializer extends JsonSerializer<String> {

        public void serialize(String value, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            if (value != null) {
                jsonGenerator.writeString(value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;"));
            } else {
                jsonGenerator.writeString("");
            }
        }
    }
}
