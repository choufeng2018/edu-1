/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.school.entity.TCommSubject;
import com.jeeplus.modules.school.entity.TSchoolRoom;
import com.jeeplus.modules.school.entity.TSchoolTeacher;
import com.jeeplus.modules.teaching.entity.TClassStudentCheck;

/**
 * 课程表Entity
 * @author fly
 * @version 2016-09-15
 */
public class TCourseTimetable extends DataEntity<TCourseTimetable> {
	
	private static final long serialVersionUID = 1L;
	private String schoolId;
	private String campusId;
	private String campusName;
	private TCommSubject subject;		// 课程编号
	private TCourseClass courseclass;		// 课程班级编号
	private Date courseDate;		// 日期
	private Date beginTime;		// 开始时间
	private Date endTime;		// 结束时间
	private TSchoolRoom room;		// 教室编码
	private Integer type;		// 类型(正常  取消  补课)
	private Integer status;		// 状态(未开始、已完成)
	private TSchoolTeacher teacher;		// 教师ID
	private Integer pId;		// 原课程序号
	
	private String shour;
	private String smin;
	private String ehour;
	private String emin;
	private String teactime;
	
	private boolean day1 = false;
	private boolean day2 = false;
	private boolean day3 = false;
	private boolean day4 = false;
	private boolean day5 = false;
	private boolean day6 = false;
	private boolean day7 = false;
	
	
	private List<TClassStudentCheck> tClassStudentCheckList = Lists.newArrayList();		// 子表列表
	
	public TCourseTimetable() {
		super();
	}

	public TCourseTimetable(String id){
		super(id);
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	@JsonIgnore
	@NotNull(message="课程不能为空")
	@ExcelField(title="课程", align=2, sort=3)
	public TCommSubject getSubject() {
		return subject;
	}

	public void setSubject(TCommSubject subject) {
		this.subject = subject;
	}

	@JsonIgnore
	@NotNull(message="课程班级不能为空")
	@ExcelField(title="课程班级编号", align=2, sort=1)
	public TCourseClass getCourseclass() {
		return courseclass;
	}

	public void setCourseclass(TCourseClass courseclass) {
		this.courseclass = courseclass;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="日期", align=2, sort=3)
	public Date getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(Date courseDate) {
		this.courseDate = courseDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开始时间", dictType="", align=2, sort=4)
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", dictType="", align=2, sort=5)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@JsonIgnore
	@NotNull(message="计划教室不能为空")
	@ExcelField(title="教室", align=2, sort=7)
	public TSchoolRoom getRoom() {
		return room;
	}

	public void setRoom(TSchoolRoom room) {
		this.room = room;
	}
	
	@ExcelField(title="类型(正常  取消  补课)", dictType="timetable_type", align=2, sort=8)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@ExcelField(title="状态(未开始、已完成)", dictType="timetable_status", align=2, sort=9)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	 
	@JsonIgnore
	@NotNull(message="教师不能为空")
	@ExcelField(title="教师", align=2, sort=11)
	public TSchoolTeacher getTeacher() {
		return teacher;
	}

	public void setTeacher(TSchoolTeacher teacher) {
		this.teacher = teacher;
	}
	
	@ExcelField(title="原课程序号", align=2, sort=12)
	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public String getShour() {
		String test = beginTime==null?"":DateUtils.formatDate(beginTime, "HH:mm:ss");
		if (StringUtils.isBlank(this.shour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.shour;
	}

	public void setShour(String shour) {
		this.shour = shour;
	}

	public String getSmin() {
		String test = beginTime==null?"":DateUtils.formatDate(beginTime, "HH:mm:ss");
		if (StringUtils.isBlank(this.smin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.smin;
	}

	public void setSmin(String smin) {
		this.smin = smin;
	}

	public String getEhour() {
		String test = endTime==null?"":DateUtils.formatDate(endTime, "HH:mm:ss");
		if (StringUtils.isBlank(this.ehour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.ehour;
	}

	public void setEhour(String ehour) {
		this.ehour = ehour;
	}

	public String getEmin() {
		String test = endTime==null?"":DateUtils.formatDate(endTime, "HH:mm:ss");
		if (StringUtils.isBlank(this.emin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.emin;
	}

	public void setEmin(String emin) {
		this.emin = emin;
	}

	public String getTeactime() { 
		return getShour() + ":" + getSmin() + " — "  + getEhour() + ":" + getEmin();
	}

	public void setTeactime(String teactime) {
		this.teactime = teactime;
	}

	public List<TClassStudentCheck> gettClassStudentCheckList() {
		return tClassStudentCheckList;
	}

	public void settClassStudentCheckList(
			List<TClassStudentCheck> tClassStudentCheckList) {
		this.tClassStudentCheckList = tClassStudentCheckList;
	}

	public boolean isDay1() {
		return day1;
	}

	public void setDay1(boolean day1) {
		this.day1 = day1;
	}

	public boolean isDay2() {
		return day2;
	}

	public void setDay2(boolean day2) {
		this.day2 = day2;
	}

	public boolean isDay3() {
		return day3;
	}

	public void setDay3(boolean day3) {
		this.day3 = day3;
	}

	public boolean isDay4() {
		return day4;
	}

	public void setDay4(boolean day4) {
		this.day4 = day4;
	}

	public boolean isDay5() {
		return day5;
	}

	public void setDay5(boolean day5) {
		this.day5 = day5;
	}

	public boolean isDay6() {
		return day6;
	}

	public void setDay6(boolean day6) {
		this.day6 = day6;
	}

	public boolean isDay7() {
		return day7;
	}

	public void setDay7(boolean day7) {
		this.day7 = day7;
	}

	 
	
}