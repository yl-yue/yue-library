package ai.yue.library.base.util;

import cn.hutool.core.date.DateUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类（JDK1.8）
 * 
 * @author	ylyue
 * @since	2017年10月25日（JDK1.8）
 */
public class DateUtils extends DateUtil {
	
	// ~ 格式化字符串
	// ================================================================================================
    
	/**
	 * DateTime 格式化字符串
	 * <p>年-月-日 时:分:秒（标准北京时间）
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * Date 格式化字符串
	 * <p>年-月-日
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * Time 格式化字符串
	 * <p>时:分:秒
	 */
	public static final String TIME_FORMAT = "HH:mm:ss";
	
	// ~ 格式化 DateTimeFormatter 对象
	// ================================================================================================
    
	/**
	 * DateTime 格式化
	 * <p>年-月-日 时:分:秒（标准北京时间）
	 */
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Date 格式化
	 * <p>年-月-日
	 */
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	/**
	 * Time 格式化
	 * <p>时:分:秒
	 */
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	/** 年-月-日T时:分:秒 */
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	/**
	 * DateTime 保留毫秒格式化
	 * <p>年-月-日 时:分:秒:毫秒
	 */
	public static final DateTimeFormatter DATE_TIME_KEEP_MS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
	
	/**
	 * 当天起始时间
	 * <p>年-月-日 00:00:00
	 */
	public static final DateTimeFormatter TODAY_START_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
	
	/**
	 * 当天结束时间
	 * <p>年-月-日 23:59:59
	 */
	public static final DateTimeFormatter TODAY_END_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59");
	
	/**
	 * 当月起始时间
	 * <p>年-月-01 00:00:00
	 */
	public static final DateTimeFormatter MONTH_START_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-01 00:00:00");
	
	// ~ 工具方法
	// ================================================================================================
    
	/**
	 * 判断某个时间是否在某个时间段
	 * <p>{@linkplain String} 类型值应该是一个按照 yyyy-MM-dd HH:mm:ss 规则格式化后的字符串
	 * <p>若类型不确定请使用 {@link #isBetween(LocalDateTime, LocalDateTime, LocalDateTime)}
	 * 
	 * @param startTime 起始时间
	 * @param dateTime 比较时间
	 * @param endTime 结束时间
	 * @return 是否在…之间
	 */
	public static boolean isBetween(String startTime, String dateTime, String endTime) {
		LocalDateTime start = Timestamp.valueOf(startTime).toLocalDateTime();
		LocalDateTime time = Timestamp.valueOf(dateTime).toLocalDateTime();
		LocalDateTime end = Timestamp.valueOf(endTime).toLocalDateTime();
		
		if (time.isBefore(end) && time.isAfter(start)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断某个时间是否在某个时间段
	 * 
	 * @param startTime 起始时间
	 * @param dateTime 比较时间
	 * @param endTime 结束时间
	 * @return 是否在…之间
	 */
	public static boolean isBetween(LocalDateTime startTime, LocalDateTime dateTime, LocalDateTime endTime) {
		if (dateTime.isBefore(endTime) && dateTime.isAfter(startTime)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断某个时间是否在某个时间段
	 * 
	 * @param startTime 起始时间
	 * @param dateTime 比较时间
	 * @param endTime 结束时间
	 * @return 是否在…之间
	 */
	public static boolean isBetween(Date startTime, Date dateTime, Date endTime) {
		if (dateTime.before(endTime) && dateTime.after(startTime)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获得当前时间戳
	 * 
	 * @return 当前时间戳
	 */
	public static long getTimestamp() {
		return System.currentTimeMillis();
	}

	/**
	 * 获得当前时间偏移后的时间戳
	 *
	 * @param offsetMillis 偏移毫秒
	 * @return 当前时间偏移后的时间戳
	 */
	public static long getTimestamp(int offsetMillis) {
		return getTimestamp() + offsetMillis;
	}

	/**
	 * 获得当前日期的标准字符串格式
	 * 
	 * @return 年-月-日
	 */
	public static String getDateFormatter() {
		return DATE_FORMATTER.format(LocalDate.now());
	}
	
	/**
	 * 获得当前日期时间的标准字符串格式
	 * 
	 * @return 年-月-日 时:分:秒（标准北京时间）
	 */
	public static String getDatetimeFormatter() {
		return DATE_TIME_FORMATTER.format(LocalDateTime.now());
	}
	
	/**
	 * 获得当天起始时间的标准字符串格式
	 * 
	 * @return 年-月-日 00:00:00
	 */
	public static String getTodayStartFormatter() {
		return TODAY_START_FORMATTER.format(LocalDateTime.now());
	}
	
	/**
	 * 获得当天结束时间的标准字符串格式
	 * 
	 * @return 年-月-日 23:59:59
	 */
	public static String getTodayEndFormatter() {
		return TODAY_END_FORMATTER.format(LocalDateTime.now());
	}
	
	/**
	 * 获得本周开始时间的标准字符串格式
	 * 
	 * @return 年-月-日 00:00:00
	 */
	public static String getWeekmorningFormatter() {
		Calendar cal = Calendar.getInstance();
		// 设置年月日时分秒
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return toDateTimeFormatter(cal.getTimeInMillis());
	}
	
	/**
	 * 获得本周日结束时间的标准字符串格式
	 * 
	 * @return 年-月-日 23:59:59
	 */
	public static String getWeeknight() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DAY_OF_WEEK, 6);
		return toDateTimeFormatter(cal.getTimeInMillis());
	}
	
	/**
	 * 将日期时间转换成时间戳
	 * 
	 * @param dateTime 日期时间
	 * @return 时间戳
	 */
	public static Long toTimestamp(String dateTime) {
		return Timestamp.valueOf(dateTime).getTime();
	}
	
	/**
	 * 将 {@linkplain Date} 转 {@linkplain LocalDateTime}
	 * <p>默认使用系统时区转换
	 * 
	 * @param date 日期
	 * @return 本地日期时间
	 */
	public static LocalDateTime toLocalDateTime(Date date) {
		Instant instant = date.toInstant();// 时间线上的一个瞬时点
		ZoneId zoneId = ZoneId.systemDefault();// 时区
		return instant.atZone(zoneId).toLocalDateTime();
	}
	
	/**
	 * 将毫秒时间（时间戳）转化为标准日期字符串格式
	 * 
	 * @param timestamp 毫秒（时间戳）
	 * @return 年-月-日
	 */
	public static String toDateFormatter(long timestamp) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		return DATE_FORMATTER.format(LocalDate.ofInstant(instant, ZoneId.systemDefault()));
	}
	
	/**
	 * 将 {@link Date} 类型进行标准日期字符串格式化
	 * 
	 * @param date 日期
	 * @return 年-月-日
	 */
	public static String toDateFormatter(Date date) {
		return DATE_FORMATTER.format(toLocalDateTime(date));
	}
	
	/**
	 * 将 {@link TemporalAccessor} 类型进行标准日期字符串格式化
	 * 
	 * @param date 日期
	 * @return 年-月-日
	 */
	public static String toDateFormatter(TemporalAccessor date) {
		return DATE_FORMATTER.format(date);
	}
	
	/**
	 * 将毫秒时间（时间戳）转化为标准日期时间字符串格式
	 * 
	 * @param timestamp 毫秒（时间戳）
	 * @return 年-月-日 时:分:秒
	 */
	public static String toDateTimeFormatter(long timestamp) {
		LocalDateTime localDateTime = toLocalDateTime(new Date(timestamp));
		return DATE_TIME_FORMATTER.format(localDateTime);
	}
	
	/**
	 * 将 {@link Date} 类型进行标准日期时间字符串格式化
	 * 
	 * @param date 日期
	 * @return 年-月-日 时:分:秒
	 */
	public static String toDateTimeFormatter(Date date) {
		return DATE_TIME_FORMATTER.format(toLocalDateTime(date));
	}

	/**
	 * 将 {@link TemporalAccessor} 类型进行标准日期时间字符串格式化
	 *
	 * @param date 日期
	 * @return 年-月-日 时:分:秒
	 */
	public static String toDateTimeFormatter(TemporalAccessor date) {
		return DATE_TIME_FORMATTER.format(date);
	}
	
	/**
	 * 将 {@link TemporalAccessor} 类型进行标准日期时间字符串格式化
	 * 
	 * @deprecated 请使用：{@link #toDateTimeFormatter(TemporalAccessor)}
	 * @param date 日期
	 * @return 年-月-日 时:分:秒
	 */
	@Deprecated
	public static String to_y_M_d_H_m_s(TemporalAccessor date) {
		return DATE_TIME_FORMATTER.format(date);
	}
	
	/**
	 * 计算日期相差天数
	 * 
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 相差天数
	 */
	public static long dateDaysDifference(LocalDateTime startTime, LocalDateTime endTime) {
		return startTime.toLocalDate().toEpochDay() - endTime.toLocalDate().toEpochDay();
	}
	
	/**
	 * 计算日期相差天数
	 * 
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 相差天数
	 */
	public static long dateDaysDifference(LocalDate startDate, LocalDate endDate) {
		return startDate.toEpochDay() - endDate.toEpochDay();
	}
	
}
