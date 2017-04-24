/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.school.entity.TCommSubject;
import com.jeeplus.modules.school.entity.TSchoolRoom;
import com.jeeplus.modules.school.entity.TSchoolTeacher;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 课程方案管理Entity
 * @author fly
 * @version 2016-09-13
 */
public class TCourseClass extends DataEntity<TCourseClass> {
	
	private static final long serialVersionUID = 1L;
	private String schoolId;		// 学校
	private Office campus;		// 校区
	private TCommSubject subject;		// 课程编号
	private String classCode;		// 班级编码
	private String classDesc;		// 班级名称
	private Integer classType;		// 班级类型（1:1对1  2：一对多）
	private Integer chargeType;     // 计费模式（班级制，每课时）
	private String courseDesc;		// 课程详情
	private Integer totalStu;		// 计划招生人数
	private Integer stuNum;		// 班级人数
	private String recruit;		// 招生对象
	private Integer slot;		// 上课时段
	private String slotdesc;		// 上课时段
	private Date beginDate;		// 开课年份
	private Integer classHour;		// 课次
	private Integer classMin;		// 课时时长
	private Integer week;		// 周几
	private Date begintime;		// 开始时间
	private Date endtime;		// 结束时间
	private TSchoolTeacher teacher;		// 教师
	private TSchoolRoom room;		// 计划教室
	private Double cost;		// 总费用
	private Double percost;
	private Integer status;		// 状态
	private Integer iscreate;   //是否已创建课程表
	private List<TCourseClassTime> tCourseClassTimeList = Lists.newArrayList();		// 子表列表
	
	private String shour;
	private String smin;
	private String ehour;
	private String emin;
	
	private String teactime;
	private String nametime;
	
	private String test;
	
	public TCourseClass() {
		super();
	}

	public TCourseClass(String id){
		super(id);
	}

	@Length(min=0, max=11, message="学校长度必须介于 0 和 11 之间")
	//@ExcelField(title="学校", align=2, sort=1)
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	@JsonIgnore
	@NotNull(message="校区不能为空")
	@ExcelField(title="校区", align=2, sort=1)
	public Office getCampus() {
		return campus;
	}

	public void setCampus(Office campus) {
		this.campus = campus;
	}
	
	@JsonIgnore
	@NotNull(message="课程不能为空")
	@ExcelField(title="专业", align=2, sort=3, value="subject.subjectName")
	public TCommSubject getSubject() {
		return subject;
	}

	public void setSubject(TCommSubject subject) {
		this.subject = subject;
	}
	
	@Length(min=0, max=64, message="班级编码长度必须介于 0 和 64 之间")
	//@ExcelField(title="班级编码", align=2, sort=4)
	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	
	@Length(min=0, max=64, message="班级名称长度必须介于 0 和 64 之间")
	@ExcelField(title="课程名称", align=2, sort=2)
	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}
	
	@ExcelField(title="授课方式", dictType="course_type", align=2, sort=4)
	public Integer getClassType() {
		return classType;
	}

	public void setClassType(Integer classType) {
		this.classType = classType;
	}
	
	@ExcelField(title="计费方式", dictType="charge_type", align=2, sort=17)
	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}
 
	public Integer getSlot() {
		return slot;
	}

	public void setSlot(Integer slot) {
		this.slot = slot;
	}
	
	@ExcelField(title="上课时段", align=2, sort=6)
	public String getSlotdesc() {
		return slotdesc;
	}

	public void setSlotdesc(String slotdesc) {
		this.slotdesc = slotdesc;
	}

	@Length(min=0, max=500, message="课程详情长度必须介于 0 和 500 之间")
	//@ExcelField(title="课程详情", align=2, sort=7)
	public String getCourseDesc() {
		return courseDesc;
	}

	public void setCourseDesc(String courseDesc) {
		this.courseDesc = courseDesc;
	}
	
	@ExcelField(title="计划招生人数", align=2, sort=12)
	public Integer getTotalStu() {
		return totalStu;
	}

	public void setTotalStu(Integer totalStu) {
		this.totalStu = totalStu;
	}
	
	@ExcelField(title="班级人数", align=2, sort=13)
	public Integer getStuNum() {
		return stuNum;
	}

	public void setStuNum(Integer stuNum) {
		this.stuNum = stuNum;
	}
	
	@Length(min=0, max=200, message="招生对象长度必须介于 0 和 200 之间")
	@ExcelField(title="招生对象", align=2, sort=11)
	public String getRecruit() {
		return recruit;
	}

	public void setRecruit(String recruit) {
		this.recruit = recruit;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="开课日期不能为空")
	@ExcelField(title="开课日期", align=2, sort=5)
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@ExcelField(title="课次", align=2, sort=8)
	public Integer getClassHour() {
		return classHour;
	}
 
	public void setClassHour(Integer classHour) {
		this.classHour = classHour;
	}
	
	@ExcelField(title="课时时长（分钟）", align=2, sort=9)
	public Integer getClassMin() {
		return classMin;
	}

	public void setClassMin(Integer classMin) {
		this.classMin = classMin;
	}
	
	//@ExcelField(title="周几", dictType="", align=2, sort=14)
	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	
	@JsonIgnore
	@NotNull(message="教师不能为空")
	@ExcelField(title="教师", align=2, sort=14, value="teacher.name")
	public TSchoolTeacher getTeacher() {
		return teacher;
	}

	public void setTeacher(TSchoolTeacher teacher) {
		this.teacher = teacher;
	}
	 
	@JsonIgnore
	@NotNull(message="计划教室不能为空")
	@ExcelField(title="教室", align=2, sort=15, value="room.roomDesc")
	public TSchoolRoom getRoom() {
		return room;
	}

	public void setRoom(TSchoolRoom room) {
		this.room = room;
	}
	
	@ExcelField(title="总费用", align=2, sort=10)
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
	
	public Double getPercost() {
		return percost;
	}

	public void setPercost(Double percost) {
		this.percost = percost;
	}

	@ExcelField(title="状态", dictType="course_status",  align=2, sort=16)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
 
	public Integer getIscreate() {
		return iscreate;
	}

	public void setIscreate(Integer iscreate) {
		this.iscreate = iscreate;
	}

	public String getShour() {
		String test = begintime==null?"":DateUtils.formatDate(begintime, "HH:mm:ss");
		if (StringUtils.isBlank(this.shour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.shour;
	}

	public void setShour(String shour) {
		this.shour = shour;
	}

	public String getSmin() {
		String test = begintime==null?"":DateUtils.formatDate(begintime, "HH:mm:ss");
		if (StringUtils.isBlank(this.smin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.smin;
	}

	public void setSmin(String smin) {
		this.smin = smin;
	}

	public String getEhour() {
		String test = endtime==null?"":DateUtils.formatDate(endtime, "HH:mm:ss");
		if (StringUtils.isBlank(this.ehour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.ehour;
	}

	public void setEhour(String ehour) {
		this.ehour = ehour;
	}

	public String getEmin() {
		String test = endtime==null?"":DateUtils.formatDate(endtime, "HH:mm:ss");
		if (StringUtils.isBlank(this.emin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.emin;
	}

	public void setEmin(String emin) {
		this.emin = emin;
	}

	@ExcelField(title="上课时间", align=2, sort=7)
	public String getTeactime() {
		/**String returnValue = "";
		if (week == 1)
			returnValue = "周一";
		else if (week == 2)
			returnValue = "周二";
		else if (week == 3)
			returnValue = "周三";
		else if (week == 4)
			returnValue = "周四";
		else if (week == 5)
			returnValue = "周五";
		else if (week == 6)
			returnValue = "周六";
		else if (week == 7)
			returnValue = "周日"; 
		
		return returnValue + " " + getShour() + ":" + getSmin() + " — "  + getEhour() + ":" + getEmin();
		**/
		return this.teactime;
	}

	public void setTeactime(String teactime) {
		this.teactime = teactime;
	}

	public String getNametime() {
		return this.classDesc+"("+getShour() + ":" + getSmin() + " — "  + getEhour() + ":" + getEmin()+")";
	}

	public void setNametime(String nametime) {
		this.nametime = nametime;
	}

	public List<TCourseClassTime> gettCourseClassTimeList() {
		return tCourseClassTimeList;
	}

	public void settCourseClassTimeList(List<TCourseClassTime> tCourseClassTimeList) {
		this.tCourseClassTimeList = tCourseClassTimeList;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	 
	
}