/**
 * Copyright 2017 Yonyou Corporation Ltd. All Rights Reserved.
 * This software is published under the terms of the Yonyou Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * @Project Name : dmsgms.interfaceServer
 * @File name : HttpUtil.java
 * @Author : sunduoduo
 * @Date : 2017年12月7日
 * <p>
 * ----------------------------------------------------------------------------------
 * Date       Who       Version     Comments
 * 1. 2017年12月7日    sunduoduo    1.0
 * <p>
 * <p>
 * <p>
 * <p>
 * ----------------------------------------------------------------------------------
 */

package com.mepeng.cn.pen.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * TODO description
 *
 * @author sunduoduo
 * @date 2017年12月7日
 */

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static final String POST = "POST";

    public static final String GET = "GET";

    public static final String PUT = "PUT";

    public static final String CONTENT_TYPE_JSON = "application/json";

    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";


    /**
     * http请求 并返回响应字符串
     * @author sunduoduo
     * @date 2017年12月7日
     * @param requestUrl
     * @param requestMethod
     * @param params
     * @param contentType
     * @return
     * @throws Exception
     */
    public static String httpRequest(String requestUrl, String requestMethod, String params, String contentType) throws Exception {

        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        URL url = new URL(requestUrl);
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);
        httpUrlConn.setUseCaches(false);
        // 设置请求方式（GET/POST）
        httpUrlConn.setRequestMethod(requestMethod);

        if ("GET".equalsIgnoreCase(requestMethod)) {
            httpUrlConn.connect();
        } else {
            httpUrlConn.setRequestProperty("Accept-Charset", "utf-8");
            httpUrlConn.setRequestProperty("Content-Type", contentType);
            httpUrlConn.setRequestProperty("Content-Length", String.valueOf(params.length()));
        }

        // 当有数据需要提交时
        if (null != params) {
            OutputStream outputStream = httpUrlConn.getOutputStream();
            // 注意编码格式，防止中文乱码
            outputStream.write(params.getBytes("UTF-8"));
            outputStream.close();
        }

        // 将返回的输入流转换成字符串
        InputStream inputStream = httpUrlConn.getInputStream();
        return new String(readInputStream(inputStream), "UTF-8");
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
