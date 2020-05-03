package com.mepeng.cn.pen.common.util.pinyin4j;

import java.io.BufferedInputStream;

class ResourceAide
{
    /**
     * @param resourceName
     * @return resource (mainly file in file system or file in compressed
     *         package) as BufferedInputStream
     */
    static BufferedInputStream getResourceInputStream(String resourceName)
    {
        return new BufferedInputStream(ResourceAide.class.getResourceAsStream(resourceName));
    }
}
