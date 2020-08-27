package com.got.common.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;

/**
 * 日期处理工具类
 * @ClassName DateUtil
 * @Author Create By lijunhui 2016年11月30日 下午2:44:43
 * @Version v0.1
 */
public class DateUtil {
    
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    //每一天的毫秒数
    private static final Integer MS_EVERY_DAY = 1000 * 60 * 60 * 24;

    /**
     * 精确到月的时间（默认）如：yyyy-MM
     */
    public static final String FORMAT_MONTH = "yyyy-MM";

    /**
     * 精确到天的时间如：yyyyMM
     */
    public static final String FORMAT_MONTH_N = "yyyyMM";

    /**
     * 精确到天的时间（默认）如：yyyy-MM-dd
     */
    public static final String FORMAT_SHORT = "yyyy-MM-dd";

    /**
     * 精确到天的时间如：yyyyMMdd
     */
    public static final String FORMAT_SHORT_N = "yyyyMMdd";

    /**
     * 精确到秒的时间，默认的pattern。 如：yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

    /**
     * 英文全称 如：yyyyMMddHHmmss
     */
    public static final String FORMAT_LONG_N = "yyyyMMddHHmmss";

    /**
     * 精确到毫秒的时间 如：yyyy-MM-dd HH:mm:ss.S
     */
    public static final String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 精确到毫秒的时间,无分割 如：yyyyMMddHHmmssS
     */
    public static final String FORMAT_FULL_N = "yyyyMMddHHmmssS";

    /**
     * 精确到秒的小时时间 如：HH:mm
     */
    public static final String HOUR_SHORT = "HH:mm";

    /**
     * 精确到分的小时时间,无分割 如：HHmm
     */
    public static final String HOUR_SHORT_N = "HHmm";

    /**
     * 精确到秒的小时时间 如：HH:mm:ss
     */
    public static final String HOUR_LONG = "HH:mm:ss";

    /**
     * 精确到秒的小时时间,无分割 如：HHmmss
     */
    public static final String HOUR_LONG_N = "HHmmss";

    /**
     * 精确到毫秒的小时时间 如：HH:mm:ss.S
     */
    public static final String HOUR_FULL = "HH:mm:ss.S";

    /**
     * 精确到毫秒的小时时间,无分割 如：HHmmssS
     */
    public static final String HOUR_FULL_N = "HHmmssS";

    /**
     * 中文简写 如：2010年12月01日
     */
    public static final String FORMAT_SHORT_CN = "yyyy年MM月dd";

    /**
     * 中文全称 如：2010年12月01日 23时15分06秒
     */
    public static final String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";

    /**
     * 精确到毫秒的完整中文时间
     */
    public static final String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";

    /**
     * ThreadLocal存放SimpleDateFormat
     */
    private static final NamedThreadLocal<SimpleDateFormat> dateFormatTL = new NamedThreadLocal<>(
        "DateUtil.dateFormat");

    /** 锁对象 */
    private static final Lock lock = new ReentrantLock();

    /**
    *
    */
    private DateUtil() {
        super();
    }

    /**
     * 获取ThreadLocal中SimpleDateFormat对象，默认格式
     * 
     * @return
     */
    public static SimpleDateFormat getDateFormat() {
        return getDateFormat(getDefulatPattern());
    }

    /**
     * 获取ThreadLocal中SimpleDateFormat对象,设置日期格式
     * 
     * @param format 日期格式
     * @return
     */
    public static SimpleDateFormat getDateFormat(String format) {
        SimpleDateFormat df = dateFormatTL.get();
        if (df == null) {
            lock.lock();
            df = dateFormatTL.get();
            if (df == null) {
                df = new SimpleDateFormat();
                dateFormatTL.set(df);
            }
            lock.unlock();
        }
        df.applyPattern(format);
        return df;
    }

    /**
     * 获得默认的 date pattern
     */
    public static String getDefulatPattern() {
        return FORMAT_SHORT;
    }

    /**
     * 根据用户格式返回当前日期
     * 
     * @param format
     * @return
     */
    public static String getNowStr(String format) {
        return formatDate(getNowDate(), format);
    }

    /**
     * 根据预设格式返回当前日期
     * 
     * @return
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 根据用户格式返回当前日期
     * 
     * @param format
     * @return
     */
    public static Date getNowDate(String format) {
        return parseDate(formatDate(getNowDate(),format),format);
    }

    /**
     * 使用预设格式格式化日期
     * 
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return formatDate(date, getDefulatPattern());
    }

    /**
     * 使用用户格式格式化日期
     * 
     * @param date
     *            日期
     * @param pattern
     *            日期格式
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        String returnValue = null;
        if (date != null) {
            returnValue = getDateFormat(pattern).format(date);
        }
        return returnValue;
    }
    
    /**
     * 使用预设格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @return
     */
    public static Date parseDate(String strDate) {
        return parseDate(strDate, getDefulatPattern());
    }

    /**
     * 使用用户格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     */
    public static Date parseDate(String strDate, String pattern) {
        try {
            return getDateFormat(pattern).parse(strDate);
        } catch (ParseException e) {
            logger.error("时间转换异常", e);
            return null;
        }
    }
    
    /**
     * 根据用户格式返回当前日期
     * 
     * @param format
     * @return
     */
    public static Date dateFormat(Date date,String format) {
        return parseDate(formatDate(date,format),format);
    }
    
    /**
     * 使用预设格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @return
     */
    public static Date parseLenient(String strDate) {
        return parseLenient(strDate, getDefulatPattern());
    }

    /**
     * 使用用户格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     */
    public static Date parseLenient(String strDate, String pattern) {
        try {
            SimpleDateFormat df = getDateFormat(pattern);
            df.setLenient(false);
            return df.parse(strDate);
        } catch (ParseException e) {
            logger.error("时间转换异常", e);
            return null;
        }
    }

    /**
     * 在日期上增加年
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的年
     * @return
     */
    public static Date addYear(Date date, Integer n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加数个整月
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的月数
     * @return
     */
    public static Date addMonth(Date date, Integer n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加天数
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的天数
     * @return
     */
    public static Date addDay(Date date, Integer n) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }
    
    /**
     * 在日期上增加小时
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的小时
     * @return
     */
    public static Date addHour(Date date, Integer n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加分钟
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的分钟
     * @return
     */
    public static Date addMinute(Date date, Integer n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加秒
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的秒数
     * @return
     */
    public static Date addSecond(Date date, Integer n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, n);
        return cal.getTime();
    }

    /**
     * 获取时间戳
     */
    public static String getTimeString() {
        Calendar calendar = Calendar.getInstance();
        return getDateFormat(FORMAT_FULL).format(calendar.getTime());
    }

    /**
     * 获取日期年份
     * 
     * @param date
     *            日期
     * @return
     */
    public static String getYear(Date date) {
        return formatDate(date).substring(0, 4);
    }

    /**
     * 按默认格式的字符串距离今天的天数
     * 
     * @param date
     *            日期字符串
     * @return
     */
    public static Integer countDays(String date) {
        Long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parseDate(date));
        Long t1 = c.getTime().getTime();
        t1 = (t / 1000 - t1 / 1000) / 3600 / 24;
        return t1.intValue();
    }

    /**
     * 按用户格式字符串距离今天的天数
     * 
     * @param date
     *            日期字符串
     * @param format
     *            日期格式
     * @return
     */
    public static Integer countDays(String date, String format) {
        Long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parseDate(date, format));
        Long t1 = c.getTime().getTime();
        t1 = (t / 1000 - t1 / 1000) / 3600 / 24;
        return t1.intValue();
    }

    /**
     * 返回昨天的日期
     * @return
     */
    public static String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date beginDate = calendar.getTime();
        return getDateFormat(getDefulatPattern()).format(beginDate);
    }

    /**
     * 返回昨天的日期
     * @return
     */
    public static String getYesterdayDate(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date beginDate = calendar.getTime();
        return getDateFormat(format).format(beginDate);
    }


    /**
     * 得到两个日期型数据之间所差的天数,此处为闭区间
     * @param beginDate 起始的日期  yyyy-MM-dd HH:mm:ss
     * @param endDate 结束的日期 yyyy-MM-dd HH:mm:ss
     * @return 相差的天数
     */
    public static Integer compare(Date beginDate, Date endDate) {
        Long beginTime = beginDate.getTime();
        Long endTime = endDate.getTime();
        Long betweenDays = (endTime - beginTime) / MS_EVERY_DAY;
        if (betweenDays >= 0) {
            return betweenDays.intValue() + 1;
        }
        return betweenDays.intValue() - 1;
    }

    /**
     * 得到一个日期与当前时间之间所差的天数,此处为闭区间,
     * @param beginDate 起始的日期 yyyy-MM-dd
     * @return 相差的天数 yyyy-MM-dd
     */
    public static Integer compare(Date beginDate) {
        Long beginTime = beginDate.getTime();
        Calendar calendar = GregorianCalendar.getInstance();
        Date endDate = calendar.getTime();
        Long endTime = endDate.getTime();
        Long betweenDays = (endTime - beginTime) / MS_EVERY_DAY;
        if (betweenDays >= 0) {
            return betweenDays.intValue() + 1;
        }
        return betweenDays.intValue() - 1;
    }

    /**
     * 取date后第n天的Date
     * @param date
     * @param n
     * @return
     */
    public static Date nextDate(Date date, Integer n) {
        Integer day = n * MS_EVERY_DAY;
        Date d = new Date(date.getTime() + day);
        return d;
    }

    /**
     * 检查日期合法性,默认formatType为yyyyMMdd
     * @param date
     * @param n
     * @return
     */
    public static boolean checkDate(String date, String formatType) {
        if (null == date) return false;
        if ("".equalsIgnoreCase(formatType) || null == formatType) {
            formatType = FORMAT_SHORT_N;
        }
        try {
            //以SimpleDateFormat來檢核日期
            //關於 SimpleDateFormat 請自己看Java api spec
            SimpleDateFormat dFormat = getDateFormat(formatType);
            //就是這下面這一行，花了我大半天找問題…
            dFormat.setLenient(false);
            dFormat.parse(date);
            return true;
        } catch (ParseException e) {
            //告訴user，這個日期不是一個正確的日期"
            return false;
        }
    }

    /**
     * 根据日期获取工作日，如日期为周末着返回下周一的日期，否则返回传入日期
     * @param date
     * @return
     */
    public static Date getNextWorkDay(Date date) {
        Date resultDate = getNowDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            resultDate = addDay(date, 2);
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            resultDate = addDay(date, 1);
        } else {
            resultDate = date;
        }
        return resultDate;
    }

    /**
     * 判断传入日期是否为周末
     * @param date
     * @return
     */
    public static Boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
               || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * 判断传入日期是否为周六
     * @param date
     * @return
     */
    public static Boolean isSaturday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }

    /**
     * 判断传入日期是否为周日
     * @param date
     * @return
     */
    public static Boolean isSunday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
    
    

    /**
    * 取得n天时间
    *
    * @param n n=0 今天 n=1 明天；n=0 今天 n=-1 昨天
    * @return String 返回n天时间 yyyy-mm-dd
    */
    public static String getDateList(Integer n) {
        GregorianCalendar gcDate = new GregorianCalendar();
        String sbDateTodayTime;
        Integer year = gcDate.get(1);
        Integer month = gcDate.get(2);
        Integer date = gcDate.get(5);
        GregorianCalendar oneWeek = new GregorianCalendar(year, month, date);
        oneWeek.add(5, n);
        Date date2 = oneWeek.getTime();
        sbDateTodayTime = formatDate(date2);
        return sbDateTodayTime;
    }

    /**
     * 根据传入格式获取当前时间
     * @param time boolean
     * @return String
     *
     */
    public static String getNowTime(String format) {
        return getDateFormat(format).format(getNowDate());
    }

    /**
     * 获取当前时间，返回时间格式yyyyMMdd
     * @param time boolean
     * @return String
     *
     */
    public static String getNowTime() {
        return getNowTime(getDefulatPattern());
    }

    /**
     * 计算某一月份的最大天数
     * @param year
     * @param month
     * @return
     */
    public static Integer getMonthDay(Integer year, Integer month) {
        Calendar time = Calendar.getInstance();
        time.clear();//注：在使用set方法之前，必须先clear一下，否则很多信息会继承自系统当前时间
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        Integer day = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        return day;
    }
    
    /**
     * 计算某一月份的最大天数
     * @param year
     * @param month
     * @return
     */
    public static Integer getMonthDay(Date monthDate) {
        Calendar time = Calendar.getInstance();
        time.clear();//注：在使用set方法之前，必须先clear一下，否则很多信息会继承自系统当前时间
        time.setTime(monthDate);
        Integer day = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        return day;
    }

    /**
     * 计算两个时间之间相隔天数
     * @param startday  开始时间 yyyy-MM-dd
     * @param endday 结束时间 yyyy-MM-dd
     * @return
     */
    public static Integer getDaysBetween(Date startday, Date endday) {
        Calendar d1 = Calendar.getInstance();
        d1.setTime(startday);
        Calendar d2 = Calendar.getInstance();
        d2.setTime(endday);
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        Integer days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        Integer y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 返回时间间隔的秒数
     * @param endDate
     * @param nowDate
     * @return
     */
    public static Integer calLastedTime(Date endDate, Date nowDate) {
        Long a = endDate.getTime();
        Long b = nowDate.getTime();
        Long c = ((a - b) / 1000);
        return c.intValue();
    }

    /**
     * 计算2个日期之间的月差，包含日期计算，即止日期超出起日期也算作一个月
     * @param startDate
     * @param endDate
     * @return
     */
    public static Integer calcMonthDiffContainDay(Date startDate, Date endDate) {
        Calendar startCal = DateUtils.toCalendar(startDate);
        Calendar endCal = DateUtils.toCalendar(endDate);
        return (endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)) * 12 + endCal.get(Calendar.MONTH)
               - startCal.get(Calendar.MONTH)
               + (endCal.get(Calendar.DAY_OF_MONTH) >= startCal.get(Calendar.DAY_OF_MONTH) ? 1 : 0);
    }

    /**
     * 获取传入日期的当前月份第一天
     * 
     * @param date
     * @return
     */
    public static Date getFistDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    
    /**
     * 根据时间格式获取传入日期的当前月份第一天
     * 
     * @param date
     * @return
     */
    public static Date getFistDayOfMonth(Date date,String format) {
        return dateFormat(getFistDayOfMonth(date), format);
    }

    /**
     * 获取传入日期的当前月份最后一天
     * 
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        return cal.getTime();
    }
    
    /**
     * 根据时间格式获取传入日期的当前月份最后一天
     * 
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date,String format) {
        return dateFormat(getLastDayOfMonth(date), format);
    }
    
    /**
     * 判断两个日期是否相同年月
     */
    public static Boolean isSameMonth(Date date1, Date date2){
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date1);
        Integer month1 = cal.get(Calendar.MONTH);
        cal.clear();
        cal.setTime(date2);
        Integer month2 = cal.get(Calendar.MONTH);
        return month1.intValue() == month2.intValue();
        
    }

    /**
     * 根据时间格式获取传入日期所在周的周日
     * 
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        int i = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == i) {//当天就是星期日
            return date;
        }
        cal.add(Calendar.DAY_OF_MONTH, 8 - i);
        return cal.getTime();
    }

    /**
     * 根据时间格式获取传入日期所在周的周一
     * 
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        int i = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == i) {
            i = 8;
        }
        cal.add(Calendar.DAY_OF_MONTH, 2 - i);
        return cal.getTime();
    }

    public static void main(String[] args) {
//        Date now = parseDate("2016-12", FORMAT_MONTH);
//        System.out.println(DateUtil.compare(parseDate("2016-06-01 01:33:53", FORMAT_LONG),parseDate("2016-06-02 01:33:53", FORMAT_LONG)));
//        System.out.println(DateUtil.countDays("2016-12-01",FORMAT_SHORT));
//        System.out.println(DateUtil.getDaysBetween(parseDate("2016-6-1", FORMAT_SHORT),parseDate("2016-6-2", FORMAT_SHORT)));
//        System.out.println(formatDate(addMonth(parseDate("2016-01-31", FORMAT_SHORT), 1)));
//        System.out.println(formatDate(DateUtil.getFistDayOfMonth(new Date(),DateUtil.FORMAT_SHORT),DateUtil.FORMAT_LONG));
//        Long a = 1483372800000L;
//        now.setTime(a);
//        System.out.println(DateUtil.formatDate(DateUtil.getFistDayOfMonth(new Date()),FORMAT_LONG));
//        System.out.println(DateUtil.compare(DateUtil.getFistDayOfMonth(new Date()),now));
        System.out.println(formatDate(getLastDayOfMonth(parseDate("2017-2-15", FORMAT_SHORT))));
        
        
    }
}
