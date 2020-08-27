package com.got.common.core.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.got.common.core.serializer.jackson.JacksonObjMapper;

public class JacksonUtil {

    private static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    public static final ObjectMapper objectMapper = new JacksonObjMapper();

    /**
     * Json字符串转Map对象
     * 
     * @param json
     * @param type
     * @return
     */
    public static <K,V> Map<K,V> toMap(String jsonStr) {
        return toObject(jsonStr,new TypeReference<Map<K,V>>(){});
    }
    
    /**
     * Json字符串转List对象
     * 
     * @param json
     * @param type
     * @return
     */
    public static <E> List<E> toList(String jsonStr) {
        return toObject(jsonStr,new TypeReference<List<E>>(){});
    }
    
    /**
     * Json字符串转Java对象
     * 
     * @param json
     * @param type
     * @return
     */
    public static <T> T toJavaBean(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Json字符串转Java对象
     * 
     * @param json
     * @param type
     * @return
     */
    public static <T> T toJavaBean(byte[] bstr, Class<T> clazz) {
        try {
            return objectMapper.readValue(bstr, clazz);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Json字符串转对象、集合等
     * 
     * @param json
     * @param type
     * @return
     */
    public static <T> T toObject(String jsonStr, TypeReference<T> type) {
        try {
            return objectMapper.readValue(jsonStr, type);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Json字符串转对象、集合等
     * 
     * @param json
     * @param type
     * @return
     */
    public static <T> T toObject(byte[] bstr, TypeReference<T> type) {
        try {
            return objectMapper.readValue(bstr, type);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 对象、集合等转json字符串
     * 
     * @param json
     * @param type
     * @return
     */
    public static <T> String toJsonStr(T entity) {
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * 对象、集合等转json字符串
     * 
     * @param json
     * @param type
     * @return
     */
    public static <T> byte[] toJsonBytes(T entity) {
        try {
            return objectMapper.writeValueAsBytes(entity);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
}
