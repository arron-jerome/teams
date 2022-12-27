
package com.disney.teams.utils.type;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public abstract class DateUtils{

	private static final ThreadLocal<Map<String, DateFormat>> safeDateFormats = new ThreadLocal<Map<String, DateFormat>>(){
		@Override
		protected Map<String, DateFormat> initialValue() {
			return new HashMap<>();
		}
	};

	public static final String NORMAL_DATE_FORMAT_STR = "yyyy-MM-dd";
	public static final String NORMAL_DATE_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2}";
	public static final String NORMAL_DATE_TIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	public static final String NORMAL_DATE_TIME_FORMAT_REGEX = NORMAL_DATE_FORMAT_REGEX + " \\d{2}:\\d{2}:\\d{2}";

	public static final int DAY_SECONDS = 24 * 60 * 60;
	public static final int DAY_MILLISECONDS = DAY_SECONDS * 1000;

	public static final DateFormat NORMAL_DATE_TIME_FORMAT = safeDateFormat(NORMAL_DATE_TIME_FORMAT_STR);
	public static final DateFormat NORMAL_DATE_FORMAT = safeDateFormat(NORMAL_DATE_FORMAT_STR);

	private DateUtils(){}

	/**
	 * 获取线程安全的{@link DateFormat}
	 * @param dateformat
	 * @return
     */
	public static DateFormat safeDateFormat(String dateformat){
		Map<String, DateFormat> map = safeDateFormats.get();
		DateFormat df = map.get(dateformat);
		if(df == null){
			df = new SimpleDateFormat(dateformat);
			map.put(dateformat, df);
		}
		return df;
	}

	/**
	 * 将Object对象转换成日期对象
	 * @param value
	 * @return
     */
	public static Date valueOf(Object value){
		if(value == null || "null".equals(value)){
	        return null;
	    }
	    if(value instanceof Date){
	        return (Date)value;
        }else if(value instanceof Long){
			long val = (Long)value;
            return val == 0L ? null : new Date(val);
        }else{
            String str = value.toString();
			if(StringUtils.isBlank(str)) {
				return null;
			}
            try {
                long val = LongUtils.parse(str);
				return val == 0L ? null : new Date(val);
            } catch (RuntimeException e) {
                try {
                    return safeDateFormat(NORMAL_DATE_TIME_FORMAT_STR).parse(str);
                } catch (ParseException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
	}

	/**
	 * 将字符串类型的时间转换成long型的时间
	 * @param timeText,字符串格式的时间
	 * @param format,时间格式化表达式
	 * @return
	 */
	public static long longValue(String timeText, String format) {
		DateFormat df = safeDateFormat(format);
		Date date;
		try {
			date = df.parse(timeText);
		} catch (ParseException e) {
			throw new IllegalArgumentException("错误的参数格式");
		}
		return date.getTime();
	}

	private static Calendar instance(int year, int month, int day, int hour, int minute, int second, int millisecond){
		Calendar c = Calendar.getInstance();
		return set(c, year, month, day, hour, minute, second, minute);
	}

	private static Calendar set(Calendar c, int year, int month, int day, int hour, int minute, int second, int millisecond){
		if(year > -1){
			c.set(Calendar.YEAR, year);
		}
		if(month < -1){
			c.set(Calendar.MONTH, month);
		}
		if(day > -1){
			c.set(Calendar.DAY_OF_MONTH, day);
		}
		if(hour > -1) {
			c.set(Calendar.HOUR_OF_DAY, hour);
		}
		if(minute > -1) {
			c.set(Calendar.MINUTE, minute);
		}
		if(second > -1) {
			c.set(Calendar.SECOND, second);
		}
		if(millisecond > -1) {
			c.set(Calendar.MILLISECOND, millisecond);
		}
		return c;
	}

	private static Calendar instance(int hour, int minute, int second, int millisecond){
		return set(Calendar.getInstance(), -1, -1, -1, hour, minute, second, millisecond);
	}

	private static Calendar set(Calendar c, int hour, int minute, int second, int millisecond){
		return set(c, -1, -1, -1, hour, minute, second, millisecond);
	}

	/**
	 * 当前刻开始时间
	 * @return
	 */
	public static Date getHourOfQuarterStart(){
		Calendar c = Calendar.getInstance();
		int minute = c.get(Calendar.MINUTE);
		minute = minute / 15 * 15;
		set(c, -1, minute, 0, 0);
		return c.getTime();
	}

	/**
	 * 当前刻结束时间
	 * @return
	 */
	public static Date getHourOfQuarterEnd(){
		Calendar c = Calendar.getInstance();
		int minute = c.get(Calendar.MINUTE);
		minute = minute / 15 * 15 + 14;
		set(c, -1, minute, 59, 999);
		return c.getTime();
	}

	public static Date getHourStart(){
		Calendar c = Calendar.getInstance();
		set(c, -1, 0, 0, 0);
		return c.getTime();
	}

	public static Date getHourEnd(){
		Calendar c = Calendar.getInstance();
		set(c, -1, 59, 59, 999);
		return c.getTime();
	}

	/**
	 * 小于0表示对应参数不设置
	 * @param date
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param seconds
     * @param millis
     * @return
     */
	public static Date resetDateTime(Date date, int year, int month, int day, int hour, int minute, int seconds, int millis) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		set(c, year, month, day, hour, minute, seconds, millis);
		return c.getTime();
	}

	/**
	 * 小于0表示对应参数不设置
	 * @param date
	 * @param hour
	 * @param minute
	 * @param seconds
	 * @param millis
     * @return
     */
	public static Date resetTime(Date date, int hour, int minute, int seconds, int millis) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		set(c, hour, minute, seconds, millis);
		return c.getTime();
	}
	
	/**
	 * 获得本周的开始时间
	 *
	 * @return
	 */
	public static Date getCurrentWeekDayStartTime() {
		Calendar c = Calendar.getInstance();
		int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
		c.add(Calendar.DATE, -weekday);
		set(c, 0, 0, 0, 0);
		return c.getTime();
	}

	/**
	 * 获得本周的最后时间
	 *
	 * @return
	 */
	public static Date getCurrentWeekDayEndTime() {
		Calendar c = Calendar.getInstance();
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.DATE, 8 - weekday);
		set(c, 23, 59, 59, 999);
		return c.getTime();
	}

	/**
	 * 获取当天开始时间
	 * @return
     */
	public static Date getTodayStart(){
		Calendar c = Calendar.getInstance();
		set(c, 0, 0, 0, 0);
		return c.getTime();
	}

	/**
	 * 获取当天结束时间
	 * @return
	 */
	public static Date getTodayEnd(){
		Calendar c = Calendar.getInstance();
		set(c, 23, 59, 59, 999);
		return c.getTime();
	}

	/**
	 * 获取明天开始时间
	 * @return
	 */
	public static Date getTomorrowStart(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		set(c, 0, 0, 0, 0);
		return c.getTime();
	}

	public static Date getTomorrowEnd(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		set(c, 23, 59, 59, 999);
		return c.getTime();
	}

	/**
	 * 获取某天开始时间
	 * @return
	 */
	public static Date getDateStart(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		set(c, 0, 0, 0, 0);
		return c.getTime();
	}

	/**
	 * 获取某天结束时间
	 * @return
	 */
	public static Date getDateEnd(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		set(c, 23, 59, 59, 999);
		return c.getTime();
	}

	/**
	 * 获取某天明天开始时间
	 * @param date
	 * @return
     */
	public static Date getDateTomorrowStart(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		set(c, 0, 0, 0, 0);
		return c.getTime();
	}

	/**
	 * 获取某天明天结束时间
	 * @param date
	 * @return
	 */
	public static Date getDateTomorrowEnd(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		set(c, 23, 59, 59, 999);
		return c.getTime();
	}

	/**
	 * 当前季度开始时间
	 * @return
	 */
	public static Date getYearOfQuarterStart(){
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		month = month / 3 * 3;
		set(c, -1, month, 1, 0 , 0, 0, 0);
		return c.getTime();
	}

	/**
	 * 当前季度结束时间
	 * @return
	 */
	public static Date getYearOfQuarterEnd(){
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		month = month / 3 * 3 + 2;
		set(c, -1, month, 1, 23 , 59, 59, 999);
		return c.getTime();
	}

	public static Date getHalfYearStart(){
		Calendar c = Calendar.getInstance();
		int money = c.get(Calendar.MONTH) < 6 ? 0 : 6;
		set(c, -1, money, 1, 0, 0, 0, 0);
		return c.getTime();
	}

	public static Date getHalfYearEnd(){
		Calendar c = Calendar.getInstance();
		int money = c.get(Calendar.MONTH) < 6 ? 5 : 11;
		set(c, -1, money, 31, 0, 0, 0, 0);
		return c.getTime();
	}

	public static Date getMonthStart(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		set(c, -1, -1, 1, 0, 0, 0, 0);
		return c.getTime();
	}

	public static Date getMonthStart(){
		return getMonthStart(new Date());
	}

	public static Date getMonthEnd(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);
		set(c, 23, 59, 59, 999);
		return c.getTime();
	}

	public static Date getMonthEnd(){
		return getMonthEnd(new Date());
	}

	public static Date getNextMonthStart(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		set(c, -1, -1, 1, 0, 0, 0, 0);
		return c.getTime();
	}

	public static Date getNextMonthEnd(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 2);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);
		set(c, 23, 59, 59, 999);
		return c.getTime();
	}

	public static Date getYearStart(){
		return instance(-1, 0, 1, 0, 0, 0, 0).getTime();
	}

	public static Date getYearEnd(){
		return instance(-1, 11, 31, 23, 59, 59, 999).getTime();
	}


	/**
	 * 获取当前时间的标准格式
	 * @return "yyyy-MM-dd HH:mm:ss"
	 */
	public static String getNowNormalDateTime(){
		return getNow(NORMAL_DATE_TIME_FORMAT_STR);
	}

	/**
	 *
	 * @return "yyyy-MM-dd"
     */
	public static String getNowNormalDate(){
		return getNow(NORMAL_DATE_FORMAT_STR);
	}

	public static String getNow(String format){
		return format(new Date(), format);
	}

	public static Date parse(String date, String dateFormat){
		if(date == null) {
			return null;
		}
		DateFormat sdf = safeDateFormat(dateFormat);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Date parseNormalDateTime(String date){
		return parse(date, NORMAL_DATE_TIME_FORMAT_STR);
	}

	public static Date parseNormalDate(String date){
		return parse(date, NORMAL_DATE_FORMAT_STR);
	}

	public static String format(Date date, String dateformat){
		if(date == null){
			return "";
		}
		return safeDateFormat(dateformat).format(date);
	}

	public static String formatNormalDateTime(Date date){
		return format(date, NORMAL_DATE_TIME_FORMAT_STR);
	}

	public static String formatNormalDate(Date date){
		return format(date, NORMAL_DATE_FORMAT_STR);
	}

	/**
	 * 获取离截止时间的剩余时间
	 *
	 * @param endDate 截止时间,格式yyyyMMddHHmmss
	 * @return
	 */
	public static String getRemainingTime(String endDate) {
		String rtn = "";
		if (StringUtils.isBlank(endDate))
			return rtn;
		DateFormat sdf = safeDateFormat("yyyyMMddHHmmss");// ComConstants.SDF_YYYYMMDDHHMMSS;//存在线程安全问题，故不能使用静态
		try {
			Date deadline = sdf.parse(endDate);
			long remaining = deadline.getTime() - System.currentTimeMillis();
			// long remaining=lngDeadline-System.currentTimeMillis();
			if (remaining > 0) {
				// int ms = (int) (remaining % 1000);
				remaining /= 1000;
				// int sc = (int) (remaining % 60);
				remaining /= 60;
				int mn = (int) (remaining % 60);
				remaining /= 60;
				int hr = (int) (remaining % 24);
				long dy = (int) remaining / 24;
				rtn = dy + "天" + hr + "小时" + mn + "分";// + sc + "秒";
			} else {
				rtn = "过期";
			}
		} catch (ParseException e) {

		}
		return rtn;
	}

	/**
	 * 获取前几天的日期
	 * @param dayCount 天数，负数为后几天
	 * @return
     */
	public static Date getDateBefore(int dayCount){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -dayCount);
		return c.getTime();
	}

	public static Date getDateBefore(Date date, int dayCount){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -dayCount);
		return c.getTime();
	}

	/**
	 * 计算当前一天还剩余多少秒
	 * @return int
	 */
	public static int remainSecondsInToday() {
	    long cur = System.currentTimeMillis();
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, 1);
	    c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.MILLISECOND, 0);
		return (int)((c.getTimeInMillis() - cur) / 1000);
	}


	/**
	 *
	 * @param date
	 * @param type like {@link Calendar.MINUTE}
	 * @param count
     * @return
     */
	public static Date add(Date date, int type, int count) {
		if (null == date) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(type, count);
		return c.getTime();
	}

	public static Date addDays(Date date, int days) {
		return add(date, Calendar.DAY_OF_YEAR, days);
	}

	public static Date addHours(Date date, int hours) {
		return add(date, Calendar.HOUR, hours);
	}

	public static Date addMinutes(Date date, int minutes) {
		return add(date, Calendar.MINUTE, minutes);
	}

	/**
	 *
	 * @param date
	 * @param minutecount
     * @return
	 * @see #addMinutes(Date, int)
     */
	@Deprecated
	public static Date getBeforeOrAfterMinuteToDate(Date date, int minutecount) {
		return addMinutes(date, minutecount);
	}

	public static Date addSeconds(Date date, int seconds) {
		return add(date, Calendar.SECOND, seconds);
	}

	/**
	 *
	 * @param date
	 * @param secondcount
     * @return
	 * @see #addSeconds(Date, int)
     */
	@Deprecated
	public static Date getBeforeOrAfterSecondToDate(Date date, int secondcount) {
		if (null == date) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, secondcount);
		return c.getTime();
	}

	private static char[] CODE = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
        'E', 'F'};

    /**
     * 通过一定算法将当前当前纳秒级时间转换成由字母组成的字符串
     * 
     * @return
     */
    public synchronized static String getTimeString() {
        StringBuilder sb = new StringBuilder();
        long current = System.nanoTime();
        while (current > 0) {
            sb.append(CODE[(int) (current & 31L)]);
            current >>= 5;
        }
        return sb.toString();
    }
    
    private static char[] NEW_CODE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    
    /**
     * 通过一定算法将当前当前纳秒级时间转换成由0-f组成的字符串
     * 
     * @return
     */
    public synchronized static String getNanoString() {
        StringBuilder sb = new StringBuilder();
        long current = System.nanoTime();
        while (current > 0) {
            sb.append(NEW_CODE[(int) (current & 0xf)]);
            current >>= 4;
        }
        return sb.toString();
    }

	public static String remainTimeDescr(long timeGap) {
		if (timeGap <= 0) {
			return "";
		}
		long day = timeGap / 86400000L;
		timeGap = timeGap % 86400000L;
		long hour = timeGap / 3600000L;
		timeGap = timeGap % 3600000L;
		long minute = timeGap / 60000L;
		timeGap = timeGap % 60000L;
		long second = timeGap / 1000L;
		timeGap = timeGap % 1000L;
		long[] num = new long[] {day, hour, minute, second, timeGap};
		final String[] numName = new String[] {"天", "小时", "分", "秒", "毫秒"};
		StringBuilder sb = new StringBuilder();
		int i, len = num.length;
		for (i = 0; i < len; ++i) {
			if (num[i] > 0) {
				sb.append(num[i]).append(numName[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * 当前时间到截止时间的剩余时间描述
	 * @param endDate
	 * @return
     */
	public static String remainTimeDescr(Date endDate){
		long timeGap = remainTime(endDate);
		return remainTimeDescr(timeGap);
	}

	public static long remainTime(Date endDate){
		if(endDate == null){
			return 0L;
		}
		long timeGap = endDate.getTime() - System.currentTimeMillis();
		return timeGap;
	}

	public static boolean isBetween(Date src, Date start, Date end){
		return ObjectUtils.lte(start, src) && ObjectUtils.lte(src, end);
	}

	public static boolean isBetweenNow(Date start ,Date end){
		return isBetween(new Date(), start, end);
	}

	public static boolean isBetweenIgnoreNull(Date now, Date start, Date end) {
		return ObjectUtils.betweenIgnoreNull(now, start, end);
	}

	public static boolean isBetweenNowIgnoreNull(Date start, Date end) {
		return isBetweenIgnoreNull(new Date(), start, end);
	}

	/**
	 * endTime - startTime, null value will be cast to 0
	 * @param startTime
	 * @param endTime
     * @return
     */
	public static long secondDiff(Date startTime, Date endTime) {
		long start = startTime == null ? 0L : startTime.getTime();
		long end = endTime == null ? 0L : endTime.getTime();
		return (end - start) / 1000;
	}

}
