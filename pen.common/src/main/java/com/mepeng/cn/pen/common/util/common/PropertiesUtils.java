package com.mepeng.cn.pen.common.util.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    /**
     * load by clazz
     */
    public static Properties loadPropertiesFromFile(Class<?> clazz, String filePath) throws IOException {
        InputStream inputStream = clazz.getClassLoader().getResourceAsStream(filePath);
        return loadPropertiesFromInputStream(inputStream);
    }

    /**
     * load from stream
     */
    public static Properties loadPropertiesFromInputStream(InputStream inputStream) throws IOException {
        Properties pros = new Properties();
        try {
            pros.load(inputStream);
        } catch (IOException ex) {
            throw ex;
        }
        return pros;
    }

    public static Properties loadPropertiesFromSystem() {
        return System.getProperties();
    }
}
