package com.got.common.core.util;

import java.math.BigDecimal;

/**
 * 距离计算工具类
 * 
 * @author Administrator
 */
public class DistanceUtil {

    private static final double EARTH_RADIUS = 6378.137;

    /** 
     * 转化为弧度(rad) 
     * */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：km)   
     * @param lng1 第一点的经度
     * @param lat1 第一点的纬度
     * @param lng2 第二点的经度
     * @param lat2 第二点的纬度
     * @return 
     */
    public static BigDecimal getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math
            .asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                            + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        return CalculationUtil.round(s, 3);
    }

    public static void main(String[] args) {
        System.out.println(DistanceUtil.getDistance(121.440647, 31.164074, 120.604294, 31.27512));
    }

}
