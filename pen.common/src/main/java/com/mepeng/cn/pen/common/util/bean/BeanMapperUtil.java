package com.mepeng.cn.pen.common.util.bean;

import com.mepeng.cn.pen.common.exception.UtilException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 
* 简单封装BeanUtils, 实现深度转换Bean<->Bean的Mapper映射
* @author zhangxc
* @date 2017年2月12日
 */
public class BeanMapperUtil extends org.apache.commons.collections.MapUtils{
	// 定义日志接口
//	private static final Logger logger = LoggerFactory.getLogger(BeanMapperUtil.class);

	/**
	 * 
	* 将对象dest的属性值复制到对象orig中
	* @author zhangxc
	* @date 2017年2月12日
	* @param orig
	* @param clazz
	* @return
	* @throws UtilException
	 */
    public static <T> T copyProperties(Object orig, Class<T> clazz) {
        if (orig == null) {
            throw new UtilException("input cannot be none");
        }
        T t;
        try {
            t = clazz.newInstance();
        } catch (Exception e) {
            throw new UtilException(e);
        }
        BeanUtils.copyProperties(orig, t);
        return t;
    }
    
    /**
     * 
    * 将对象dest的属性值复制到对象orig中
    * @author zhangxc
    * @date 2017年2月12日
    * @param orig
    * @param clazz
    * @return
    * @throws UtilException
     */
    public static void copyProperties(Object orig, Object destObj) {
        BeanUtils.copyProperties(orig, destObj);
    }
    
    

    /**
     * 
    * 把origList 的属性值复制到 destList中
    * @author zhangxc
    * @date 2017年2月12日
    * @param origList
    * @param destClass
    * @return
    * @throws UtilException
     */
    public static <T> List<T> copyList(List<?> origList, Class<T> destClass){
        List<T> destList = new ArrayList<>();
        for (Object orig : origList) {
            destList.add(copyProperties(orig, destClass));
        }
        return destList;
    }

    /**
     * 
    * 将map 转换成对象
    * @author zhangxc
    * @date 2017年2月12日
    * @param clazz
    * @param map
    * @return
    * @throws InstantiationException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
     */
    public static <T, V> T toObject(Class<T> clazz, Map<String, V> map){  
        try{
            T object = clazz.newInstance();
            return toObject(object, map);
        }catch(Exception e){
            throw new UtilException("Map 转换对象失败",e);
        }
    }
    
    /**
     * 
    * 将map 转换成对象
    * @author zhangxc
    * @date 2017年2月12日
    * @param clazz
    * @param map
    * @return
    * @throws InstantiationException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
     */
    public static <T, V> T toObject(Class<T> clazz, Map<String, V> map, boolean toCamelCase) {
        try{
            T object = clazz.newInstance();  
            return toObject(object, map,toCamelCase);  
        }catch(Exception e){
            throw new UtilException("Map 转换对象失败",e);
        }
       
    }

    /**
     * 
    * 将map 转换成对象
    * @author zhangxc
    * @date 2017年2月12日
    * @param object
    * @param map
    * @return
    * @throws InstantiationException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
     */
    public static <T, V> T toObject(T object, Map<String, V> map) {
        try{
            org.apache.commons.beanutils.BeanUtils.populate(object, map);
            return object;
        }catch(Exception e){
            throw new UtilException("Map 转换对象失败",e);
        }
       
    }  
    
    /**
     * 
    * 将map 转换成对象
    * @author zhangxc
    * @date 2017年2月12日
    * @param object
    * @param map
    * @return
    * @throws InstantiationException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
     */
    public static <T, V> T toObject(T object, Map<String, V> map, boolean toCamelCase) {  
        try{
            if (toCamelCase){
                map = toCamelCaseMap(map);
            }
            org.apache.commons.beanutils.BeanUtils.populate(object, map);  
            return object;  
        }catch(Exception e){
            throw new UtilException("Map 转换对象失败",e);
        }
    }
    
    /**
     * 
    * 将对象转换成MAP
    * @author zhangxc
    * @date 2017年2月12日
    * @param object
    * @return
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    * @throws NoSuchMethodException
     */
    @SuppressWarnings("unchecked") 
    public static Map<String, String> toMap(Object object){  
        try{
            return org.apache.commons.beanutils.BeanUtils.describe(object);  
        }catch(Exception e){
            throw new UtilException("对象转换MAP失败",e);
        }
        
    }  
    
    /** 
     * 转换成Map并提供字段命名驼峰转平行 
     *  
     * @param clazz 
     *            目标对象所在类 
     * @param object 
     *            目标对象 
     * @param map 
     *            待转换Map 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */  
    public static Map<String, String> toMapForFlat(Object object) {  
        try{
            Map<String, String> map = toMap(object);
            return toUnderlineStringMap(map);  
        }catch(Exception e){
            throw new UtilException("对象转换MAP失败",e);
        }
    } 
    /**
     * 
    * 对对象集合转换成MAP 集合
    * @author zhangxc
    * @date 2017年2月12日
    * @param collection
    * @return
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    * @throws NoSuchMethodException
     */
    public static <T> Collection<Map<String, String>> toMapList(Collection<T> collection) {  
        List<Map<String, String>> retList = new ArrayList<Map<String, String>>();  
        if (collection != null && !collection.isEmpty()) {  
            for (Object object : collection) {  
                Map<String, String> map = toMap(object);  
                retList.add(map);  
            }  
        }  
        return retList;  
    }  
    
    /**
     * 
    * 转换成驼峰格式
    * @author zhangxc
    * @date 2017年2月25日
    * @param map
    * @return
     */
    public static <V> Map<String, V> toCamelCaseMap(Map<String, V> map) {  
        Map<String, V> newMap = new HashMap<String, V>();  
        for (String key : map.keySet()) {  
            safeAddToMap(newMap, JavaBeanUtil.toCamelCaseString(key), map.get(key));  
        }  
        return newMap;  
    }  
    
    /** 
     * 将Map的Keys转译成下划线格式的<br> 
     * (例:branchNo -> branch_no)<br> 
     *  
     * @param map 
     *            待转换Map 
     * @return 
     */  
    public static <V> Map<String, V> toUnderlineStringMap(Map<String, V> map) {  
        Map<String, V> newMap = new HashMap<String, V>();  
        for (String key : map.keySet()) {  
            newMap.put(JavaBeanUtil.toUnderlineString(key), map.get(key));  
        }  
        return newMap;  
    } 
    
    
    /** 
     * 转换为Collection,同时为字段做驼峰转换<Map<K, V>> 
     *  
     * @param collection 
     *            待转换对象集合 
     * @return 转换后的Collection<Map<K, V>> 
     * @throws IllegalAccessException 
     * @throws InvocationTargetException 
     * @throws NoSuchMethodException 
     */  
    public static <T> Collection<Map<String, String>> toMapListForFlat(Collection<T> collection){  
        List<Map<String, String>> retList = new ArrayList<Map<String, String>>();  
        if (collection != null && !collection.isEmpty()) {  
            for (Object object : collection) {  
                Map<String, String> map = toMapForFlat(object);  
                retList.add(map);  
            }  
        }  
        return retList;  
    }
     
}
