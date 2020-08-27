package com.got.common.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * UUID工具类
 * @ClassName IDGenerator
 * @Author Create By lijunhui 2016年11月30日 下午2:46:55
 * @Version v0.1
 */
public class IDGenerator {
    
    private final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
                                   'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                                   'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
                                   'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                                   'Y', 'Z' };

    private final static Map<Character, Integer> digitMap = new HashMap<Character, Integer>();

    static {
        for (int i = 0; i < digits.length; i++) {
            digitMap.put(digits[i], i);
        }
    }

    /**
     * 支持的最大进制数
     */
    private static final int MAX_RADIX = digits.length;

    /**
     * 支持的最小进制数
     */
    private static final int MIN_RADIX = 2;

    private IDGenerator() {
    }

    public static String genId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String genIdByName(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes()).toString().replace("-", "");
    }

    /**
     * 生成短8位UUID
     * 
     * @return
     */
    public static String genUUID8() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(digits[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * 将长整型数值转换为指定的进制数（最大支持62进制，字母数字已经用尽）
     *
     * @param i
     * @param radix
     * @return
     */
    private static String toString(long i, int radix) {
        if (radix < MIN_RADIX || radix > MAX_RADIX) radix = 10;
        if (radix == 10) return Long.toString(i);

        final int size = 65;
        int charPos = 64;

        char[] buf = new char[size];
        boolean negative = (i < 0);

        if (!negative) {
            i = -i;
        }

        while (i <= -radix) {
            buf[charPos--] = digits[(int) (-(i % radix))];
            i = i / radix;
        }
        buf[charPos] = digits[(int) (-i)];

        if (negative) {
            buf[--charPos] = '-';
        }

        return new String(buf, charPos, (size - charPos));
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return toString(hi | (val & (hi - 1)), MAX_RADIX).substring(1);
    }

    /**
     * 产生19位的UUID
     * @return
     */
    public static String genShortId() {
        //产生UUID
        UUID uuid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder();
        //分区转换
        sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
        sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
        sb.append(digits(uuid.getMostSignificantBits(), 4));
        sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
        sb.append(digits(uuid.getLeastSignificantBits(), 12));
        return sb.toString();
    }
    
    /**
     * 产生19位的UUID
     * @return
     */
    public static String genShortIdByName(String name) {
        //产生UUID
        UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
        StringBuilder sb = new StringBuilder();
        //分区转换
        sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
        sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
        sb.append(digits(uuid.getMostSignificantBits(), 4));
        sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
        sb.append(digits(uuid.getLeastSignificantBits(), 12));
        return sb.toString();
    }

    public static void main(String[] args) {
        //        System.out.println(System.nanoTime());
        //        System.out.println(DateUtil.getNowStr(null));
        //        String name = "上海果藤互联网金融信息服务有限公司";
        System.out.println(genShortId());
    }
}