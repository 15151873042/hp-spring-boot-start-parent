package com.got.common.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

/**
 * CGLib的BeanCopier工具类
 * TODO：目前只支持对象和List的浅拷贝，深拷贝有待实现
 * @ClassName BeanCopierUtil
 * @Author Create By 2016年11月30日 下午2:40:57
 * @Version v0.1
 */
public class BeanCopierUtil {

    public static final Map<String, BeanCopier> cacheMap = new ConcurrentHashMap<>();

    private BeanCopierUtil() {
    }

    /**
     * 使用CGLib的BeanCopier拷贝对象
     * 
     * @param srcObj 源对象
     * @param destObj 目标对象
     */
    public static <T> T copyObj(Object srcObj, Class<T> destClass) {
        if (srcObj == null || destClass == null || BeanUtils.isSimpleProperty(srcObj.getClass())
            || BeanUtils.isSimpleProperty(destClass)) {
            throw new RuntimeException("参数必须为非空javaBean对象!");
        }
        String key = srcObj.getClass().toString() + destClass.toString();
        BeanCopier copier = null;
        if (cacheMap.containsKey(key)) {
            copier = cacheMap.get(key);
        } else {
            copier = BeanCopier.create(srcObj.getClass(), destClass, false);
            cacheMap.put(key, copier);
        }
        T destObj = null;
        try {
            destObj = destClass.getConstructor().newInstance();
            copier.copy(srcObj, destObj, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return destObj;
    }

    /**
     * 使用CGLib的BeanCopier拷贝对象
     * 
     * @param srcObj 源对象
     * @param destObj 目标对象
     */
    public static <T> T copyObj(Object srcObj, T destObj) {
        if (srcObj == null || destObj == null || BeanUtils.isSimpleProperty(srcObj.getClass())
            || BeanUtils.isSimpleProperty(destObj.getClass())) {
            throw new RuntimeException("参数必须为非空javaBean对象");
        }
        String key = srcObj.getClass().toString() + destObj.getClass().toString();
        BeanCopier copier = null;
        if (cacheMap.containsKey(key)) {
            copier = cacheMap.get(key);
        } else {
            copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
            cacheMap.put(key, copier);
        }
        copier.copy(srcObj, destObj, null);
        return destObj;
    }

    /**
     * 使用CGLib的BeanCopier拷贝List
     * 
     * @param sourceList
     * @param targetClass
     * @return
     */
    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        List<T> resultList = new ArrayList<>(sourceList.size());
        T t = null;
        for (Object o : sourceList) {
            t = copyObj(o, targetClass);
            resultList.add(t);
        }
        return resultList;
    }

    /*static class MyConverter implements Converter {
        @SuppressWarnings({ "rawtypes" })
        @Override
        public Object convert(Object value, Class target, Object context) {
            if (value == null) {
                return null;
            } else if (value.getClass().equals(target)) {
                return value;
            } else if (!isJavaClass(target)) {
                return copyObj(value, newObject(target));
            } else if (value instanceof List) {
                List list = (List) value;
                List list1 = new ArrayList(list.size());
                try {
                    TypeVariable[] tvs = target.getTypeParameters();
                    Object tarObj;
                    System.out.println(tvs[0].getBounds()[0]);
                    for (int i = 0; i < list.size(); i++) {
                        tarObj = tvs[0].getBounds()[0].getClass().newInstance();
                        list1.add(copy(list.get(i), tarObj));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return null;
                }
                return list1;
            } else {
                return value;
            }
        }
    
        private boolean isJavaClass(Class<?> clz) {
            return clz != null && clz.getClassLoader() == null;
        }
    
        private Object newObject(Class<?> target) {
            Object targetObj = null;
            try {
                targetObj = target.getConstructor().newInstance();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return null;
            }
            return targetObj;
        }
    }*/
}
