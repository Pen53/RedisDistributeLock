package com.mepeng.cn.util;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * RedissonClient 工具类
 */
@Component
public class RedissonClientUtil {
    @Qualifier(value = "redissonClient")
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 指定缓存失效时间
     *
     * @param key
     *            键
     * @param time
     *            时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        RMap<String, Object> map = this.redissonClient.getMap(key);
        boolean r = map.expire(time, TimeUnit.SECONDS);
        return r;
    }

    /**
     * 设置属性
     *
     * @param key
     * 键
     * @param name
     * 属性名
     * @param obj implements Serializable
     * 属性对象
     * @return
     */
    public void set(String key,String name, Object obj) {
        RMap<String, Object> map = this.redissonClient.getMap(key);
        map.put(name,obj);
    }

    /**
     * 获取属性
     *
     * @param key
     * 键
     * @param name
     * 属性名
     * @return
     */
    public Object get(String key,String name) {
        RMap<String, Object> map = this.redissonClient.getMap(key);
        return map.get(name);
    }

    /**
     * 删除属性
     *
     * @param key
     * 键
     * @param name
     * 属性名
     * @return
     */
    public Object del(String key,String name) {
        RMap<String, Object> map = this.redissonClient.getMap(key);
        return map.remove(name);
    }


}
