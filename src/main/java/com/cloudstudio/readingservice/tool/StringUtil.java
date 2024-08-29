package com.cloudstudio.readingservice.tool;

/**
 * @ClassName StringUtil
 * @Author Create By matrix
 * @Date 2024/8/29 18:05
 */
public class StringUtil {
    /**
     * 检查字符串是否为null或者空（""）
     *
     * @param str 要检查的字符串
     * @return 如果字符串为null或者空,则返回true;否则返回false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
