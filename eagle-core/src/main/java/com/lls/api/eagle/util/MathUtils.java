package com.lls.api.eagle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/************************************
 * MathUtils
 * @author liliangshan
 * @date 2018/12/16
 ************************************/
public class MathUtils {

    private static final Logger logger = LoggerFactory.getLogger(MathUtils.class);
    /**
     * 针对int类型字符串进行解析，如果存在格式错误，则返回默认值（defaultValue）
     * Parse intStr, return defaultValue when numberFormatException occurs
     * @param intStr
     * @param defaultValue
     * @return
     */
    public static int parseInt(String intStr, int defaultValue) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            logger.debug("ParseInt false, for malformed intStr:" + intStr);
            return defaultValue;
        }
    }

    /**
     * 针对long类型字符串进行解析，如果存在格式错误，则返回默认值（defaultValue）
     * Parse longStr, return defaultValue when numberFormatException occurs
     * @param longStr
     * @param defaultValue
     * @return
     */
    public static long parseLong(String longStr, long defaultValue){
        try {
            return Long.parseLong(longStr);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 通过二进制位操作将originValue转化为非负数:
     *      0和正数返回本身
     *      负数通过二进制首位取反转化为正数或0（Integer.MIN_VALUE将转换为0）
     * return non-negative int value of originValue
     * @param originValue
     * @return positive int
     */
    public static int getNonNegative(int originValue){
        return 0x7fffffff & originValue;
    }

}
