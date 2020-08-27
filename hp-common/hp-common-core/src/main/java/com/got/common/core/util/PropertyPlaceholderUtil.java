package com.got.common.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyPlaceholderUtil extends PropertyPlaceholderConfigurer {

    private static final Map<String, String> springPropertyMap = new HashMap<>();

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        String keyStr;
        String value;
        for (Object key : props.keySet()) {
            keyStr = StringUtil.nullToEmpty(key);
            value = StringUtil.nullToEmpty(props.getProperty(keyStr));
            springPropertyMap.put(keyStr, value);
        }
    }

    //static method for accessing context properties
    public static String getProperty(String name) {
        return springPropertyMap.get(name);
    }
    
}
