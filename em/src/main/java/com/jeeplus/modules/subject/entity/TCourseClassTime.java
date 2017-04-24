/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 课程方案管理Entity
 * @author fly
 * @version 2017-01-22
 */
public class TCourseClassTime extends DataEntity<TCourseClassTime> {
	
	private static final long serialVersionUID = 1L;
	private TCourseClass csId;		// 课程编号 父类
	private Integer week;		// 周几
	private Date begintime;		// 开始时间
	private Date endtime;		// 结束时间
	
	private String shour;
	private String smin;
	private String ehour;
	private String emin;
	
	public TCourseClassTime() {
		super();
	}

	public TCourseClassTime(String id){
		super(id);
	}

	public TCourseClassTime(TCourseClass csId){
		this.csId = csId;
	}

	public TCourseClass getCsId() {
		return csId;
	}

	public void setCsId(TCourseClass csId) {
		this.csId = csId;
	}
	
	@ExcelField(title="周几", align=2, sort=2)
	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开始时间", align=2, sort=3)
	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=4)
	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
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
 
}