/**
 * 
 */
package com.mepeng.cn.pen.common.util.pinyin4j.format;

/**
 * Define the output format of character 'ü'
 * 
 * <p>
 * 'ü' is a special character of Hanyu Pinyin, which can not be simply
 * represented by English letters. In Hanyu Pinyin, such characters include 'ü',
 * 'üe', 'üan', and 'ün'.
 * 
 * <p>
 * This class provides several options for output of 'ü', which are listed
 * below.
 * 
 * <table>
 * <tr>
 * <th>Options</th>
 * <th>Output</th>
 * </tr>
 * <tr>
 * <td>WITH_U_AND_COLON</td>
 * <td align = "center">u:</td>
 * </tr>
 * <tr>
 * <td>WITH_V</td>
 * <td align = "center">v</td>
 * </tr>
 * <tr>
 * <td>WITH_U_UNICODE</td>
 * <td align = "center">ü</td>
 * </tr>
 * </table>
 * 
 * @author Li Min (xmlerlimin@gmail.com)
 * 
 */
public class ChinesePinyinVCharType
{

    /**
     * The option indicates that the output of 'ü' is "u:"
     */
    public static final ChinesePinyinVCharType WITH_U_AND_COLON = new ChinesePinyinVCharType("WITH_U_AND_COLON");

    /**
     * The option indicates that the output of 'ü' is "v"
     */
    public static final ChinesePinyinVCharType WITH_V = new ChinesePinyinVCharType("WITH_V");

    /**
     * The option indicates that the output of 'ü' is "ü" in Unicode form
     */
    public static final ChinesePinyinVCharType WITH_U_UNICODE = new ChinesePinyinVCharType("WITH_U_UNICODE");

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    protected void setName(String name)
    {
        this.name = name;
    }

    /**
     * Constructor
     */
    protected ChinesePinyinVCharType(String name)
    {
        setName(name);
    }

    protected String name;
}
