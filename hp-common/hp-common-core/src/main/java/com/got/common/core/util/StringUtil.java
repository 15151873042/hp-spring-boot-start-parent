package com.got.common.core.util;

import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;


public class StringUtil {
    
    public static final String DEFAULT_PREFIX = "{";

    public static final String DEFAULT_SUFFIX = "}";

    private StringUtil() {
    }

    /** 
     * 使用java正则表达式去掉多余的.与0 
     * @param s 
     * @return  
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }
        return s;
    }

    /**
     * Object对象转String，如为Null返回空字符串
     * 
     * @param o
     * @return
     */
    public static String nullToEmpty(Object o) {
        if (o == null) {
            return "";
        } else {
            return String.valueOf(o);
        }
    }

    /**
     * 随机生成六位数验证码
     * @return
     */
    public static int getRandomNum() {
        Random r = new Random();
        return r.nextInt(900000) + 100000;//(Math.random()*(999999-100000)+100000)
    }

    /**
     * 字符串转换为字符串数组
     * @param str 字符串
     * @param splitRegex 分隔符
     * @return
     */
    public static String[] str2StrArray(String str, String splitRegex) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return str.split(splitRegex);
    }

    /**
     * 用默认的分隔符(,)将字符串转换为字符串数组
     * @param str   字符串
     * @return
     */
    public static String[] str2StrArray(String str) {
        return str2StrArray(str, ",\\s*");
    }
    
    /**
     * 占位符替换
     * 
     * @param pattern
     * @param argMap
     * @return
     */
    public static String formatString(String pattern, Map<String, String> argMap){
        String arg = "";
        for(Map.Entry<String, String> entry : argMap.entrySet()){
            arg = DEFAULT_PREFIX + entry.getKey() + DEFAULT_SUFFIX;
            if(pattern.contains(arg)){
                pattern = pattern.replace(arg, entry.getValue());
            }
        }
        return pattern;
        
    }
}
