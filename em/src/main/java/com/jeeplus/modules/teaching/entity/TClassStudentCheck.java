/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.entity;
 
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.subject.entity.TCourseTimetable;

/**
 * 考勤记录管理Entity
 * @author fly
 * @version 2016-09-18
 */
public class TClassStudentCheck extends DataEntity<TClassStudentCheck> {
	
	private static final long serialVersionUID = 1L;
	private TCourseTimetable tCourseTimetable;		// 课程课次编码
	private String studentId;		// 学生
	private String studentName;		// 学生名称
	private Integer bkstatus;		// 学生状态（已补课、未补课）
	private Integer status;		// 学生状态（签到、缺勤、请假）
	private Integer type;
	private Integer canmakeup;
	private Date hopedate;
	private TClassStudentCheck parent;
	
	private String teactime;
	private String ttid;
	
	private String shour;
	private String smin;
	
	public TClassStudentCheck() {
		super();
	}

	public TClassStudentCheck(String id){
		super(id);
	}

	@JsonIgnore
	@NotNull(message="课程课次不能为空")
	@ExcelField(title="课程课次编码", align=2, sort=1)
	public TCourseTimetable gettCourseTimetable() {
		return tCourseTimetable;
	}

	public void settCourseTimetable(TCourseTimetable tCourseTimetable) {
		this.tCourseTimetable = tCourseTimetable;
	}

	@Length(min=0, max=30, message="学生长度必须介于 0 和 30 之间")
	@ExcelField(title="学生", align=2, sort=2)
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	@Length(min=0, max=30, message="学生名称长度必须介于 0 和 30 之间")
	@ExcelField(title="学生名称", align=2, sort=3)
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	@ExcelField(title="学生状态（签到、缺勤、请假）", dictType="check_status", align=2, sort=4)
	public Integer getStatus() {
		return status;
	}
	 
	public Integer getBkstatus() {
		return bkstatus;
	}

	public void setBkstatus(Integer bkstatus) {
		this.bkstatus = bkstatus;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCanmakeup() {
		return canmakeup;
	}

	public void setCanmakeup(Integer canmakeup) {
		this.canmakeup = canmakeup;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getHopedate() {
		return hopedate;
	}

	public void setHopedate(Date hopedate) {
		this.hopedate = hopedate;
	}

	public TClassStudentCheck getParent() {
		return parent;
	}

	public void setParent(TClassStudentCheck parent) {
		this.parent = parent;
	}

	public String getShour() {
		String test = hopedate==null?"":DateUtils.formatDate(hopedate, "HH:mm:ss");
		if (StringUtils.isBlank(this.shour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.shour;
	}

	public void setShour(String shour) {
		this.shour = shour;
	}

	public String getSmin() {
		String test = hopedate==null?"":DateUtils.formatDate(hopedate, "HH:mm:ss");
		if (StringUtils.isBlank(this.smin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.smin;
	}

	public void setSmin(String smin) {
		this.smin = smin;
	}

	public String getTeactime() {
		if (tCourseTimetable != null && tCourseTimetable.getCourseDate()!=null && tCourseTimetable.getTeactime()!=null)
			return DateUtils.formatDate(tCourseTimetable.getCourseDate(), "yyyy-MM-dd") + "(" + tCourseTimetable.getTeactime() +")";
		return teactime;
	}

	public void setTeactime(String teactime) {
		this.teactime = teactime;
	}

	public String getTtid() {
		if (tCourseTimetable != null)
			return  tCourseTimetable.getId();
		else
			return ttid;
	}

	public void setTtid(String ttid) {
		this.ttid = ttid;
	}
  
}