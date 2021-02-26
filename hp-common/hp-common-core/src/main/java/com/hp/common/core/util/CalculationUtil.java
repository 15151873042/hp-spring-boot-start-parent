package com.hp.common.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**  
 * 工具类提供精确的浮点数运算，包括加减乘除和四舍五入。  
 * 
 */
/**
 * 浮点计算工具类
 * 提供精确的浮点数运算，包括加减乘除和四舍五入。  
 * @ClassName CalculationUtil
 * @Author Create By lijunhui 2016年11月30日 下午2:43:51
 * @Version v0.1
 */
public class CalculationUtil {

    /**
     * 默认除法运算精度   
     */
    private static final Integer DEF_DIV_SCALE = 2;

    /**
     * 默认舍入模式：四舍五入
     */
    private static final RoundingMode ROUND_MODE = RoundingMode.HALF_UP;

    private CalculationUtil() {
    }

    private static BigDecimal getDecimalVal(Object value) {
        if (value instanceof BigDecimal) {
            BigDecimal b = (BigDecimal) value;
            b = BigDecimal.valueOf(b.doubleValue());
            return b;
        } else {
            return new BigDecimal(String.valueOf(value));
        }
    }

    /**  
     * 提供精确的加法运算。  
     * @param v1 被加数  
     * @param v2 加数  
     * @return 两个参数的和  
     */
    public static BigDecimal add(Object v1, Object v2) {
        BigDecimal b1 = getDecimalVal(v1);
        BigDecimal b2 = getDecimalVal(v2);
        return b1.add(b2);
    }

    /**  
     * 提供精确的加法运算。  
     * @param v1 被加数  
     * @param v2 加数  
     * @return 两个参数的和转换为字符串,去除多余的末尾0
     */
    public static String addToStr(Object v1, Object v2) {
        return add(v1, v2).stripTrailingZeros().toPlainString();
    }

    /**  
     * 提供精确的减法运算。  
     * @param v1 被减数  
     * @param v2 减数  
     * @return 两个参数的差  
     */
    public static BigDecimal sub(Object v1, Object v2) {
        BigDecimal b1 = getDecimalVal(v1);
        BigDecimal b2 = getDecimalVal(v2);
        return b1.subtract(b2);
    }

    /**  
     * 提供精确的减法运算。  
     * @param v1 被减数  
     * @param v2 减数  
     * @return 两个参数的差  转换为字符串,去除多余的末尾0
     */
    public static String subToStr(Object v1, Object v2) {
        return sub(v1, v2).stripTrailingZeros().toPlainString();
    }

    /**  
     * 提供精确的乘法运算。  
     * @param v1 被乘数  
     * @param v2 乘数  
     * @return 两个参数的积
     */
    public static BigDecimal mul(Object v1, Object v2) {
        BigDecimal b1 = getDecimalVal(v1);
        BigDecimal b2 = getDecimalVal(v2);
        return b1.multiply(b2);
    }

    /**  
     * 提供精确的乘法运算。  
     * @param v1 被乘数  
     * @param v2 乘数  
     * @return 两个参数的积  转换为字符串
     */
    public static String mulToStr(Object v1, Object v2) {
        return mul(v1, v2).stripTrailingZeros().toPlainString();
    }

    /**  
     * 除法计算，四舍五入精确到小数点以后2位。
     * @param v1 被除数  
     * @param v2 除数  
     * @return 两个参数的商  
     */
    public static BigDecimal div(Object v1, Object v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**  
     * 除法计算，四舍五入精确到小数点以后2位。
     * @param v1 被除数  
     * @param v2 除数  
     * @return 两个参数的商  转换为字符串
     */
    public static String divToStr(Object v1, Object v2) {
        return divToStr(v1, v2, DEF_DIV_SCALE);
    }

    /**  
     * 除法计算，四舍五入精确到小数点以后(scale)位。
     * @param v1 被除数  
     * @param v2 除数  
     * @param scale 表示表示需要精确到小数点以后几位。  
     * @return 两个参数的商  
     */
    public static BigDecimal div(Object v1, Object v2, Integer scale) {
        if (scale == null || scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = getDecimalVal(v1);
        BigDecimal b2 = getDecimalVal(v2);
        return b1.divide(b2, scale, ROUND_MODE);
    }

    /**  
     * 除法计算，四舍五入精确到小数点以后(scale)位。
     * @param v1 被除数  
     * @param v2 除数  
     * @param scale 表示表示需要精确到小数点以后几位。  
     * @return 两个参数的商  转换为字符串,去除多余的末尾0
     */
    public static String divToStr(Object v1, Object v2, Integer scale) {
        return div(v1, v2, scale).stripTrailingZeros().toPlainString();
    }

    /**  
     * 提供精确的小数位四舍五入处理。  
     * @param v 需要四舍五入的数字  
     * @param scale 小数点后保留几位  
     * @return 四舍五入后的结果  
     */
    public static BigDecimal round(Object v, Integer scale) {
        if (scale == null || scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = getDecimalVal(v);
        return b.setScale(scale, ROUND_MODE);
    }

    /**  
    * 返回两个数中大的一个值  
    * @param v1 需要被对比的第一个数  
    * @param v2 需要被对比的第二个数  
    * @return 返回两个数中大的一个值  
    */
    public static BigDecimal returnMax(Object v1, Object v2) {
        BigDecimal b1 = getDecimalVal(v1);
        BigDecimal b2 = getDecimalVal(v2);
        return b1.max(b2);
    }

    /**  
    * 返回两个数中小的一个值  
    * @param v1 需要被对比的第一个数  
    * @param v2 需要被对比的第二个数  
    * @return 返回两个数中小的一个值  
    */
    public static BigDecimal returnMin(Object v1, Object v2) {
        BigDecimal b1 = getDecimalVal(v1);
        BigDecimal b2 = getDecimalVal(v2);
        return b1.min(b2);
    }

    /**  
    * 精确对比两个数字  
    * @param v1 需要被对比的第一个数  
    * @param v2 需要被对比的第二个数  
    * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1  
    */
    public static int compareTo(Object v1, Object v2) {
        BigDecimal b1 = getDecimalVal(v1);
        BigDecimal b2 = getDecimalVal(v2);
        return b1.compareTo(b2);
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal(0.2));
        System.out.println(add(new BigDecimal(0.2132), new BigDecimal(0.1)));
    }
}