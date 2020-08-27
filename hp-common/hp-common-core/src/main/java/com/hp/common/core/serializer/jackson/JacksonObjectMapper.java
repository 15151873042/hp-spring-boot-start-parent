package com.hp.common.core.serializer.jackson;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    	/*
    	 * TODO 
    	 * 	1、反序列化的时候,如果日期格式不匹配dateFormatPattern,则会报错
    	 *	2、反序列化的时候,空字符串会反序列化到对应的属性值
    	 */
    }
}
