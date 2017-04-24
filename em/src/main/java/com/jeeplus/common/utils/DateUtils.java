/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.google.common.collect.Lists;
import com.jeeplus.modules.iim.utils.DateUtil;
import com.jeeplus.modules.subject.entity.TCourseClassTime;
import com.jeeplus.modules.subject.entity.TDayTime;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author jeeplus
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyyMMdd）
	 */
	public static String getYearMonthDate() {
		return getDate("yyyyMMdd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	public static String getYesterday()
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date date = cal.getTime();
		return DateFormatUtils.format(date, "yyyy-MM-dd");
	}
	
	public static String getTomorrow()
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date date = cal.getTime();
		return DateFormatUtils.format(date, "yyyy-MM-dd");
	}
	
	public static String getBeginOfWeek()
	{
		Date InputDate;
		try {
			InputDate = new SimpleDateFormat("yyyyMMdd").parse(getYearMonthDate());
		} catch (ParseException e) { 
			InputDate = new Date();
			e.printStackTrace();
		}
		Calendar cDate = Calendar.getInstance();
		cDate.setFirstDayOfWeek(Calendar.MONDAY);
		cDate.setTime(InputDate);
		
		Calendar firstDate = Calendar.getInstance();
		firstDate.setFirstDayOfWeek(Calendar.MONDAY);
		firstDate.setTime(InputDate);
		
		if(cDate.get(Calendar.WEEK_OF_YEAR)==1&&cDate.get(Calendar.MONTH)==11){
			firstDate.set(Calendar.YEAR, cDate.get(Calendar.YEAR)+1);
		}
		 
		int typeNum  = cDate.get(Calendar.WEEK_OF_YEAR);
		 
		firstDate.set(Calendar.WEEK_OF_YEAR, typeNum);
		firstDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		//所在周开始日期
		//System.out.println(formatDate(firstDate.getTime()));
		 
		return DateFormatUtils.format(firstDate.getTime(), "yyyy-MM-dd");
	}
	
	public static String getEndOfWeek()
	{
		Date InputDate;
		try {
			InputDate = new SimpleDateFormat("yyyyMMdd").parse(getYearMonthDate());
		} catch (ParseException e) { 
			InputDate = new Date();
			e.printStackTrace();
		}
		Calendar cDate = Calendar.getInstance();
		cDate.setFirstDayOfWeek(Calendar.MONDAY);
		cDate.setTime(InputDate);
		
		Calendar lastDate = Calendar.getInstance();
		lastDate.setFirstDayOfWeek(Calendar.MONDAY);
		lastDate.setTime(InputDate);
		 
		if(cDate.get(Calendar.WEEK_OF_YEAR)==1&&cDate.get(Calendar.MONTH)==11){
			lastDate.set(Calendar.YEAR, cDate.get(Calendar.YEAR)+1);
		}
		 
		int typeNum  = cDate.get(Calendar.WEEK_OF_YEAR);
		 
		lastDate.set(Calendar.WEEK_OF_YEAR, typeNum);
		lastDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		//所在周结束日期
		//System.out.println(formatDate(lastDate.getTime()));
		return DateFormatUtils.format(lastDate.getTime(), "yyyy-MM-dd");
	}
	
	public static List<TDayTime> getWeekList(Date today)
	{
		List<TDayTime> dayList = new ArrayList();
		Date InputDate;
		try {
			InputDate = new SimpleDateFormat("yyyyMMdd").parse(getYearMonthDate());
		} catch (ParseException e) { 
			InputDate = new Date();
			e.printStackTrace();
		}
		Calendar cDate = Calendar.getInstance();
		cDate.setFirstDayOfWeek(Calendar.MONDAY);
		cDate.setTime(InputDate);
		
		Calendar firstDate = Calendar.getInstance();
		firstDate.setFirstDayOfWeek(Calendar.MONDAY);
		firstDate.setTime(InputDate);
		
		if(cDate.get(Calendar.WEEK_OF_YEAR)==1&&cDate.get(Calendar.MONTH)==11){
			firstDate.set(Calendar.YEAR, cDate.get(Calendar.YEAR)+1);
		}
		 
		int typeNum  = cDate.get(Calendar.WEEK_OF_YEAR);
		 
		firstDate.set(Calendar.WEEK_OF_YEAR, typeNum);
		firstDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		Calendar cal = Calendar.getInstance();
		TDayTime tt;
		//所在周开始日期
		for (int i=0; i<7; i++)
		{
			tt = new TDayTime();
			firstDate.add(Calendar.DAY_OF_MONTH, i==0?0:1);
			if (DateFormatUtils.format(firstDate, "yyyy-MM-dd").equals(formatDate(today)))
				tt.setIstoday("active");
			tt.setDayString(DateFormatUtils.format(firstDate, "yyyy-MM-dd"));
			tt.setWeekString(getWeekString(firstDate));
			dayList.add(tt);
		}
		 
		return dayList;
	}
	
	
	
	public static String getWeekString(Calendar firstDate)
	{
		String back = "";
		if (firstDate.get(Calendar.DAY_OF_WEEK) == 2)
			back = "周一";
		else if (firstDate.get(Calendar.DAY_OF_WEEK) == 3)
			back = "周二";
		else if (firstDate.get(Calendar.DAY_OF_WEEK) == 4)
			back = "周三";
		else if (firstDate.get(Calendar.DAY_OF_WEEK) == 5)
			back = "周四";
		else if (firstDate.get(Calendar.DAY_OF_WEEK) == 6)
			back = "周五";
		else if (firstDate.get(Calendar.DAY_OF_WEEK) == 7)
			back = "周六";
		else if (firstDate.get(Calendar.DAY_OF_WEEK) == 1)
			back = "周日";
		return back;
	}
	
	public static String getWeekStringByDate(Date firstDate)
	{
		Calendar cDate = Calendar.getInstance();
		cDate.setTime(firstDate);
		String back = "";
		if (cDate.get(Calendar.DAY_OF_WEEK) == 2)
			back = "周一";
		else if (cDate.get(Calendar.DAY_OF_WEEK) == 3)
			back = "周二";
		else if (cDate.get(Calendar.DAY_OF_WEEK) == 4)
			back = "周三";
		else if (cDate.get(Calendar.DAY_OF_WEEK) == 5)
			back = "周四";
		else if (cDate.get(Calendar.DAY_OF_WEEK) == 6)
			back = "周五";
		else if (cDate.get(Calendar.DAY_OF_WEEK) == 7)
			back = "周六";
		else if (cDate.get(Calendar.DAY_OF_WEEK) == 1)
			back = "周日";
		return back;
	}
	
	public static String getWeekStringByWeek(int week)
	{ 
		String back = "";
		if (week == 1)
			back = "周一";
		else if (week == 2)
			back = "周二";
		else if (week == 3)
			back = "周三";
		else if (week == 4)
			back = "周四";
		else if (week == 5)
			back = "周五";
		else if (week == 6)
			back = "周六";
		else if (week == 7)
			back = "周日";
		return back;
	}
	
	public static List<TDayTime> getTimeList()
	{
		List<TDayTime> dayList = new ArrayList(); 
		int begtime = 9;
		TDayTime tt;
		//所在周开始日期
		for (int i=0; i<12; i++)
		{
			tt = new TDayTime(); 
			tt.setTimeString((begtime+i)+":00");
			dayList.add(tt);
		}
		 
		return dayList;
	}
	
	public static List<TDayTime> getTimeList2()
	{
		List<TDayTime> dayList = new ArrayList(); 
		TDayTime tt= new TDayTime(); 
		tt.setTimeString("上午（8:00 - 12:00）");
		tt.setBegin(8);
		tt.setEnd(12);
		dayList.add(tt);
		 
		tt= new TDayTime(); 
		tt.setTimeString("下午（12:00 - 18:00）");
		tt.setBegin(12);
		tt.setEnd(18);
		dayList.add(tt);
		
		tt= new TDayTime(); 
		tt.setTimeString("晚上（18:00 - 21:00）");
		tt.setBegin(18);
		tt.setEnd(21);
		dayList.add(tt);
		 
		return dayList;
	}
	
	private static Date addTime(Date source, int count)
	{
		return DateUtils.parseDate(DateUtil.long2string(DateUtil.string2long(DateUtils.formatDateTime(source))+count));
	}
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
		double time = 0.0; 
		System.out.println(time == 0);
	}
}
