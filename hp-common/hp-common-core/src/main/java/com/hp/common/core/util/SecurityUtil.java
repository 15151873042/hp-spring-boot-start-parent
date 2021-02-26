package com.hp.common.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 
* @ClassName: SecurityUtil
* @Description: 密码加密
* @author Administrator
* @date 2016年3月1日 下午9:04:49
*
 */
public class SecurityUtil {
    public static final int HASH_ITERATIONS = 1024;

    private SecurityUtil() {
    }

    public static String getSignedStr(String originalStr) {
        byte[] originalStrByteArray = originalStr.getBytes();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            return originalStr;
        }
        md.reset();
        md.update(originalStrByteArray);
        // now calculate the hash
        byte[] encodedOriginalStr = md.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < encodedOriginalStr.length; i++) {
            if ((encodedOriginalStr[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(encodedOriginalStr[i] & 0xff, 16));
        }

        return buf.toString();
    }

    public static final String randomString(int length) {
        Random randGen = null;
        char[] numbersAndLetters = null;
        if (length < 1) {
            return null;
        }

        if (randGen == null) {
            randGen = new Random();
            numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        }

        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
        }

        return new String(randBuffer);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println(getEncryptedPassword("123456", "y6yQr6QKMaziyCxQtOw"));
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    public static String getEncryptedPassword(String password, String salt) {
        byte[] hashPassword = Digests.sha1(password.getBytes(), salt.getBytes(), HASH_ITERATIONS);
        return Encodes.encodeHex(hashPassword);
    }

}
