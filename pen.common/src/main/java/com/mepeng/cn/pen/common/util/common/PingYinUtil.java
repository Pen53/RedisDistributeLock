package com.mepeng.cn.pen.common.util.common;
 
import com.mepeng.cn.pen.common.exception.UtilException;
import com.mepeng.cn.pen.common.util.pinyin4j.PinyinAide;
import com.mepeng.cn.pen.common.util.pinyin4j.format.ChinesePinyinCaseType;
import com.mepeng.cn.pen.common.util.pinyin4j.format.ChinesePinyinOutputFormat;
import com.mepeng.cn.pen.common.util.pinyin4j.format.ChinesePinyinToneType;
import com.mepeng.cn.pen.common.util.pinyin4j.format.ChinesePinyinVCharType;
import com.mepeng.cn.pen.common.util.pinyin4j.format.exception.ChinesePinyinOutputFormatCombinationException;

/**
 * 拼音工具类
 * 
 * @author lsf
 */
public class PingYinUtil {
    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     * 
     * @param inputString
     * @return
     */
    public static String getPingYin(String inputString) {
        ChinesePinyinOutputFormat format = new ChinesePinyinOutputFormat();
        format.setCaseType(ChinesePinyinCaseType.LOWERCASE);
        format.setToneType(ChinesePinyinToneType.WITHOUT_TONE);
        format.setVCharType(ChinesePinyinVCharType.WITH_V);
 
        char[] input = inputString.trim().toCharArray();
        String output = "";
 
        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinAide.toChinesePinyinStringArray(input[i], format);
                    output += temp[0];
                } else
                    output += Character.toString(input[i]);
            }
        } catch (ChinesePinyinOutputFormatCombinationException e) {
            throw new UtilException(" Pinyin Output Format Combination", e);
        }
        return output;
    }
    /**  
     * 获取汉字串拼音首字母，英文字符不变  
     * @param chinese 汉字串  
     * @return 汉语拼音首字母  
     */  
    public static String getFirstSpell(String chinese) {   
            StringBuffer pybf = new StringBuffer();   
            char[] arr = chinese.toCharArray();   
            ChinesePinyinOutputFormat defaultFormat = new ChinesePinyinOutputFormat();   
            defaultFormat.setCaseType(ChinesePinyinCaseType.UPPERCASE);   
            defaultFormat.setToneType(ChinesePinyinToneType.WITHOUT_TONE);   
            for (int i = 0; i < arr.length; i++) {   
                    if (arr[i] > 128) {   
                            try {   
                                    String[] temp = PinyinAide.toChinesePinyinStringArray(arr[i], defaultFormat);   
                                    if (temp != null) {   
                                            pybf.append(temp[0].charAt(0));   
                                    }   
                            } catch (ChinesePinyinOutputFormatCombinationException e) {   
                                throw new UtilException(" Pinyin Output Format Combination", e);
                            }   
                    } else {   
                            pybf.append(arr[i]);   
                    }   
            }   
            return pybf.toString().replaceAll("\\W", "").trim();   
    }   
    /**  
     * 获取汉字串拼音，英文字符不变  
     * @param chinese 汉字串  
     * @return 汉语拼音  
     */  
    public static String getFullSpell(String chinese) {   
            StringBuffer pybf = new StringBuffer();   
            char[] arr = chinese.toCharArray();   
            ChinesePinyinOutputFormat defaultFormat = new ChinesePinyinOutputFormat();   
            defaultFormat.setCaseType(ChinesePinyinCaseType.LOWERCASE);   
            defaultFormat.setToneType(ChinesePinyinToneType.WITHOUT_TONE);   
            for (int i = 0; i < arr.length; i++) {   
                    if (arr[i] > 128) {   
                            try {   
                                    pybf.append(PinyinAide.toChinesePinyinStringArray(arr[i], defaultFormat)[0]);   
                            } catch (ChinesePinyinOutputFormatCombinationException e) {   
                                throw new UtilException(" Pinyin Output Format Combination", e);
                            }   
                    } else {   
                            pybf.append(arr[i]);   
                    }   
            }   
            return pybf.toString();   
    }  
}  