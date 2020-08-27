package com.got.common.core.serializer.jackson;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonObjMapper extends ObjectMapper {

    /**  */
    private static final long serialVersionUID = 1L;
    
    private boolean isCamelCaseToSnakeCase = false;  
    
    private boolean isClassDefault = false;
    
    private String dateFormatPattern = "yyyy-MM-dd HH:mm:ss";

    public JacksonObjMapper() {
        init();
    }
    
    @PostConstruct
    public void init(){
        //去掉默认的时间戳格式 
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //设置为中国上海时区
        setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //序列化时，日期的统一格式
        if (StringUtils.isNotEmpty(dateFormatPattern)) {
            setDateFormat(new SimpleDateFormat(dateFormatPattern));
        }
        //设置输入:禁止把POJO中值为null的字段映射到json字符串中
        configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        //设置null值不参与序列化(字段不被显示)
        setSerializationInclusion(Include.NON_NULL);
        // 将驼峰转为下划线  
        if (isCamelCaseToSnakeCase) {
            setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        }
        //反序列化时，属性不存在的兼容处理
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 视空字符传为null
        enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        //单引号处理
        configure(Feature.ALLOW_SINGLE_QUOTES, true);
        //是否序列化类名
        if (isClassDefault) {
            enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
        }
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public void setIsCamelCaseToSnakeCase(boolean isCamelCaseToSnakeCase) {
        this.isCamelCaseToSnakeCase = isCamelCaseToSnakeCase;
    }

    public void setIsClassDefault(boolean isClassDefault) {
        this.isClassDefault = isClassDefault;
    }

    public void setDateFormatPattern(String dateFormatPattern) {
        this.dateFormatPattern = dateFormatPattern;
    }
}
