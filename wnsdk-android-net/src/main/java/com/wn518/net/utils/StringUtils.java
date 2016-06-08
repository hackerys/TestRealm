package com.wn518.net.utils;

import java.util.Random;

/**
 * Created Jansen on 2016/6/6.
 */
public class StringUtils {
    /**
     * Java 获得随机一段字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }
}
