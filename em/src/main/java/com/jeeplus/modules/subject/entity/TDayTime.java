/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.entity;
 
import java.util.List;

import com.jeeplus.common.persistence.DataEntity;

/**
 * 课程方案管理Entity
 * @author fly
 * @version 2016-09-13
 */
public class TDayTime extends DataEntity<TDayTime> {
	
	private static final long serialVersionUID = 1L;
	private String dayString;		// 学校
	private String timeString;		// 班级编码 
	private String istoday;
	private String weekString;
	private List<TCourseTimetable> tList;
	private List<List<TCourseTimetable>> ttList;
	private Integer size;
	private Integer begin;
	private Integer end;
	
	private List<List<TCourseClass>> tctList;
	
	public TDayTime() {
		super();
	}

	public TDayTime(String id){
		super(id);
	}

	public String getDayString() {
		return dayString;
	}

	public void setDayString(String dayString) {
		this.dayString = dayString;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public String getIstoday() {
		return istoday==null?"":istoday;
	}

	public void setIstoday(String istoday) {
		this.istoday = istoday;
	}

	public String getWeekString() {
		return weekString;
	}

	public void setWeekString(String weekString) {
		this.weekString = weekString;
	}

	public List<TCourseTimetable> gettList() {
		return tList;
	}

	public void settList(List<TCourseTimetable> tList) {
		this.tList = tList;
	}

	public List<List<TCourseTimetable>> getTtList() {
		return ttList;
	}

	public void setTtList(List<List<TCourseTimetable>> ttList) {
		this.ttList = ttList;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getBegin() {
		return begin;
	}

	public void setBegin(Integer begin) {
		this.begin = begin;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public List<List<TCourseClass>> getTctList() {
		return tctList;
	}

	public void setTctList(List<List<TCourseClass>> tctList) {
		this.tctList = tctList;
	}
 
}