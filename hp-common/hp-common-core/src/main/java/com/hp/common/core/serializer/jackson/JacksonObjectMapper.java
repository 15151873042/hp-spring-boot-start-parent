package com.hp.common.core.serializer.jackson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class JacksonObjectMapper extends ObjectMapper {

    /**  */
    private static final long serialVersionUID = 1L;
    
    
    private String dateFormatPattern = "yyyy-MM-dd HH:mm:ss";

    public JacksonObjectMapper() {
        init();
    }
    
    @PostConstruct
    public void init(){
    	// 设置日期序列化格式（默认Date类型会序列化成时间戳）
    	setDateFormat(new SimpleDateFormat(dateFormatPattern));
    	//设置为中国上海时区
    	setTimeZone(TimeZone.getTimeZone("GMT+8"));
    	// 空字符串和nul都不参与序列化
    	setDefaultPropertyInclusion(Include.NON_EMPTY);
        //反序列化时，属性不存在的兼容处理
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 将Long类型转换成String类型，由于雪花算法生成的id long类型为19位，js number类型精度只能保持16位
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        registerModule(simpleModule);
    	/*
    	 * TODO 
    	 * 	1、反序列化的时候,如果日期格式不匹配dateFormatPattern,则会报错
    	 *	2、反序列化的时候,空字符串会反序列化到对应的属性值
    	 */

    }
}
