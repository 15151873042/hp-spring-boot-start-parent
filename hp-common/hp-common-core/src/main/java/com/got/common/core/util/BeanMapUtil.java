package com.got.common.core.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.LinkedMultiValueMap;

/**
 * 使用CGLib的BeanMap实现javabean与map互转工具类
 * @ClassName BeanMapUtil
 * @Author Create By 2017年5月17日 上午10:08:44
 * @Version v0.1
 */
public class BeanMapUtil {

    private BeanMapUtil() {}

    /** 
     * 将javabean对象转换为map
     * @param bean 
     * @return 
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        if(BeanUtils.isSimpleProperty(bean.getClass())){
            throw new RuntimeException("参数必须为非空javaBean对象");
        }
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if(beanMap.get(key) != null && !StringUtils.EMPTY.equals(beanMap.get(key))){
                    map.put(String.valueOf(key), beanMap.get(key));
                }
            }
        }
        return map;
    }

    /** 
     * 将map转换为javabean对象
     * @param map 
     * @param bean 
     * @return 
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /** 
     * 将javabean对象转换为LinkedMultiValueMap
     * @param bean 
     * @return 
     */
    public static <T> LinkedMultiValueMap<String, Object> beanToLinkedMultiValueMap(T bean) {
        if (bean == null) {
            return null;
        }
        if (BeanUtils.isSimpleProperty(bean.getClass())) {
            throw new RuntimeException("参数必须为javaBean对象");
        }
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if (beanMap.get(key) != null && !StringUtils.EMPTY.equals(beanMap.get(key))) {
                    map.add(String.valueOf(key), beanMap.get(key));
                }
            }
        }
        return map;
    }
}
