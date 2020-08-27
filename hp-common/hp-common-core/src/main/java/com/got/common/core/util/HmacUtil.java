package com.got.common.core.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName HmacUtil
 */
public class HmacUtil {
    
    private static Logger logger = LoggerFactory.getLogger(HmacUtil.class);
    
    /**
     * 定义加密方式
     * MAC算法可选以下多种算法
     * <pre>
     * HmacMD5
     * HmacSHA1
     * HmacSHA256
     * HmacSHA384
     * HmacSHA512
     * </pre>
     */
    private final static String KEY_MAC = "HmacSHA256";
    
    /**
     * 默认编码
     */
    private final static String DEFAULT_CHARSET = CharEncoding.UTF_8;
    
    /**
     * 时间戳
     */
    public static final String DATE_FORMAT = "yyyyMMddHHmm";
    
    /**
     * 构造函数
     */
    private HmacUtil() {}

    /**
     * 初始化HMAC密钥
     * @return
     */
    public static String generateKey() {
        SecretKey key;
        String str = "";
        try {
            KeyGenerator generator = KeyGenerator.getInstance(KEY_MAC);
            key = generator.generateKey();
            str = Encodes.encodeBase64(key.getEncoded());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return str;
    }

    /**
     * HMAC加密
     * @param data 需要加密的字节数组
     * @param key 密钥
     * @return 字节数组
     */
    public static byte[] encryptHMAC(String key, byte[] data) {
        SecretKey secretKey;
        byte[] bytes = null;
        try {
            secretKey = new SecretKeySpec(Encodes.decodeBase64(key), KEY_MAC);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return bytes;
    }

    /**
     * HMAC加密
     * @param data 需要加密的字符串
     * @param key 密钥
     * @return 字符串
     * @throws UnsupportedEncodingException 
     */
    public static String encryptHMAC(String key, String data){
        return encryptHMAC(key, data, DateUtil.getNowDate(DATE_FORMAT));
    }
    
    /**
     * HMAC加密
     * @param data 需要加密的字符串
     * @param key 密钥
     * @return 字符串
     * @throws UnsupportedEncodingException 
     */
    public static String encryptHMAC(String key, String data, Date time) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        data = "REST_" + DateUtil.formatDate(time, DATE_FORMAT) + "_" + data;
        logger.debug("HMAC加密前：" + data);
        byte[] bytes = null;
        try {
            bytes = encryptHMAC(key, data.getBytes(DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        String rtn = Encodes.encodeHex(bytes);
        logger.debug("HMAC加密后：" + rtn);
        return rtn;
    }

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String key = HmacUtil.generateKey();
        System.out.println("Mac密钥:" + key + "----" + key.length());
        Map<String,Object> map = new HashMap<>();
        map.put("orgCode", "J1324567891234");
        String str = JacksonUtil.toJsonStr(map);
        System.out.println(str);
        System.out.println(encryptHMAC(key, str) + "----"+encryptHMAC(key, str).length());
        System.out.println("Hmac密钥:" + key + "  " + HmacUtils.hmacSha256Hex(key, str));
    }
}
