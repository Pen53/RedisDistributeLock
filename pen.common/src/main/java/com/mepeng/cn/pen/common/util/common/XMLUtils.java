package com.mepeng.cn.pen.common.util.common;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
* @author 
* @version date：2017年7月25日 下午3:34:17
* 类说明
*/
@SuppressWarnings(value="rawtypes")
public class XMLUtils {
    
    //按照Document生成XML文件
    public static File createXMLFile(Document document,String fileName){
        XMLWriter writer = null;
        File file = new File(fileName);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        createXMLFile(out,document);
        return file;
    }
    
    
    public static void createXMLFile(OutputStream out,Document document){
        XMLWriter writer = null;
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            writer = new XMLWriter(out,format);
            writer.write(document);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void createXMLFile(OutputStream out,List<Map> head, List<Map> entrys,Map<String,String> map){
        Document document = createDocumentByList(head, entrys,map);
        XMLWriter writer = null;
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            writer = new XMLWriter(out,format);
            writer.write(document);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("生成文件出现错误");
        }finally {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }  

    //按照head和entrys对象生成Document对象
    static Document createDocument(Object head, List entrys,Map<String,String> map) {
        Document document = DocumentHelper.createDocument();
        /*
         * 前几级的标签名称先写死，之后按照需求改
         */
        Element ufinterfaceEle = document.addElement( "ufinterface" );//一级标签
        if(null != map){
        	if(null != map.get("account")){
        		ufinterfaceEle.addAttribute("account", map.get("account").toString());
        	}
        	
        	if(null != map.get("billtype")){
        		ufinterfaceEle.addAttribute("billtype", map.get("billtype").toString());
        	}
        	
        	if(null != map.get("receiver")){
        		ufinterfaceEle.addAttribute("receiver", map.get("receiver").toString());
        	}
        	
        	if(null != map.get("sender")){
        		ufinterfaceEle.addAttribute("sender", map.get("sender").toString());
        	}
        	
        }
        ufinterfaceEle.addAttribute("roottag", "voucher");//一级标签对应的属性
        ufinterfaceEle.addAttribute("proc", "add");
        ufinterfaceEle.addAttribute("isexchange", "Y");
        ufinterfaceEle.addAttribute("replace", "Y");
        ufinterfaceEle.addAttribute("subbilltype", "");
        
        Element voucherEle = ufinterfaceEle.addElement("voucher");//二级标签对应的属性
     
        /*
         * head部分
         */
        try {
            if(head !=null){            	
                Element voucher_headEle = voucherEle.addElement("voucher_head");//head元素
                Class<? extends Object> clazzHead = head.getClass();
                Field[] headFields = clazzHead.getDeclaredFields();
                for(Field f : headFields){
                    f.setAccessible(true);                 //private字段在访问的时候 setAccessible(true) sun源代码中有判断
                    String fieldName = f.getName();
                    Element fieldNameEle = voucher_headEle.addElement(fieldName);
                    Object fieldVale = clazzHead.getMethod("get"+captureName(fieldName)).invoke(head, null);
                    fieldNameEle.addText(fieldVale.toString());
                    f.setAccessible(false);
                }
            }
            if(entrys!=null && entrys.size() > 0){
                Element voucher_bodyEle = voucherEle.addElement("voucher_body");//body元素
                formList(entrys, voucher_bodyEle);
            }
        } catch (SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("生成Document出现错误");
        }
        return document;
    }
    
    public static Document createDocumentByList(List<Map> head, List<Map> entrys,Map<String,String> map) {
        Document document = DocumentHelper.createDocument();
        /*
         * 前几级的标签名称先写死，之后按照需求改
         */
        Element ufinterfaceEle = document.addElement( "ufinterface" );//一级标签
        if(null != map){
        	if(null != map.get("account")){
        		ufinterfaceEle.addAttribute("account", map.get("account").toString());
        	}
        	
        	if(null != map.get("billtype")){
        		ufinterfaceEle.addAttribute("billtype", map.get("billtype").toString());
        	}
        	
        	if(null != map.get("receiver")){
        		ufinterfaceEle.addAttribute("receiver", map.get("receiver").toString());
        	}
        	
        	if(null != map.get("sender")){
        		ufinterfaceEle.addAttribute("sender", map.get("sender").toString());
        	}
        	
        }
        ufinterfaceEle.addAttribute("roottag", "voucher");//一级标签对应的属性
        ufinterfaceEle.addAttribute("proc", "add");
        ufinterfaceEle.addAttribute("isexchange", "Y");
        ufinterfaceEle.addAttribute("replace", "Y");
        ufinterfaceEle.addAttribute("subbilltype", "");
        
        Element voucherEle = ufinterfaceEle.addElement("voucher");//二级标签对应的属性
     
        /*
         * head部分
         */
        try {
            if(head !=null){            	
            Element voucher_headEle = voucherEle.addElement("voucher_head");//head元素
            
            for(int i = 0 ;i<head.size();i++){
            	Map headMap = head.get(i);
            	 Iterator iter = headMap.keySet().iterator(); 
                 while(iter.hasNext()){ 
                     String key=(String) iter.next(); 
                     String value = headMap.get(key).toString();
                     Element fieldNameEle = voucher_headEle.addElement(key);
                     fieldNameEle.addText(value);
              }          	
            }    
            
            }
            if(entrys!=null && entrys.size() > 0){
                Element voucher_bodyEle = voucherEle.addElement("voucher_body");//body元素
                formList(entrys, voucher_bodyEle);
            }
        } catch (SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("生成Document出现错误");
        }
        return document;
    }
    
    private static void formList(List<Map> list,Element parent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
        if(list!= null && list.size()>0){
            if(list.get(0) == null){ //为了照顾自定义类型属性为空的情况
                return;
            }
            for(Map e :list){           	
            	Map bodyMap = e;
           	 	Iterator iter = bodyMap.keySet().iterator(); 
                while(iter.hasNext()){ 
                    String key=(String) iter.next(); 
                    String value = bodyMap.get(key).toString();
                    Element entryEle = parent.addElement(key);
                    entryEle.addText(value);
                }   
               
//                Class<? extends Object> clazzEntry = e.getClass();
//                Field[] entryFields = clazzEntry.getDeclaredFields();               
//                Element entryEle = parent.addElement(e.getClass().getSimpleName().toLowerCase());
//                for(Field f : entryFields){
//                    boolean accessible = f.isAccessible();
//                    f.setAccessible(true);
//                    Class fClazz = f.getType();
//                    if("java.util.List".equals(fClazz.getName())){//如果属性是List类型
//                        String fieldName = f.getName();
//                        Element fieldNameEle = entryEle.addElement(fieldName);
//                        formList((List)clazzEntry.getMethod("get"+captureName(fieldName)).invoke(e, null), fieldNameEle);
//                    }else if(fClazz.getName().startsWith("java.lang.")){ //如果属性是Integer.String等等类型
//                        String fieldName = f.getName();
//                        Element fieldNameEle = entryEle.addElement(fieldName);
//                        Object fieldVale = clazzEntry.getMethod("get"+captureName(fieldName)).invoke(e, null);
//                        if(fieldVale == null){  //若属性值为null
//                            fieldNameEle.addText("");
//                        }else{
//                            fieldNameEle.addText(fieldVale.toString().trim());
//                        }
//                    }else{          //若是自定义类型
//                        String fieldName = f.getName();
//                        Element fieldNameEle = entryEle.addElement(fieldName);
//                        Object object = clazzEntry.getMethod("get"+captureName(fieldName)).invoke(e, null);
//                        formList(Arrays.asList(object), parent);
//                    }
//                    f.setAccessible(accessible);
//                }
            }
        }
    }
    
    //首字母大写
    private static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
        
    }
}
