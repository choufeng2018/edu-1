/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.entity;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 教师一对一教学执行时间配置Entity
 * @author flying
 * @version 2017-01-15
 */
public class TSchoolTeacherConf extends DataEntity<TSchoolTeacherConf> {
	
	private static final long serialVersionUID = 1L;
	private TSchoolTeacher teacher;		// 教师
	private Integer price;		// 课时价格
	private Integer cmin;		// 课时时长（分钟）
	private Integer rmin;		// 课间休息时长
	private Integer ism;		// 上午
	private Date mtime;		// 上午开始时间
	private Date emtime;		// 上午开始时间
	private Integer isa;		// 下午
	private Date atime;		// 下午开始时间
	private Date eatime;		// 下午开始时间
	private Integer isn;		// 晚上
	private Date ntime;		// 晚上开始时间
	private Date entime;		// 晚上开始时间
	private Integer stuCount;
	
	private Boolean mon;
	private Boolean tue;
	private Boolean wed;
	private Boolean thu;
	private Boolean fri;
	private Boolean sat;
	private Boolean sun;
	
	private String mtimeshour;
	private String mtimesmin;
	private String atimeehour;
	private String atimeemin;
	private String ntimeehour;
	private String ntimeemin;
	
	private String emtimeshour;
	private String emtimesmin;
	private String eatimeehour;
	private String eatimeemin;
	private String entimeehour;
	private String entimeemin;
	
	public TSchoolTeacherConf() {
		super();
	}

	public TSchoolTeacherConf(String id){
		super(id);
	}

	@NotNull(message="教师不能为空")
	@ExcelField(title="教师", align=2, sort=1)
	public TSchoolTeacher getTeacher() {
		return teacher;
	}

	public void setTeacher(TSchoolTeacher teacher) {
		this.teacher = teacher;
	}
 
	@NotNull(message="课时价格不能为空")
	@ExcelField(title="课时价格", align=2, sort=2)
	public Integer getPrice() {
		return price;
	}
 
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	@NotNull(message="课时时长（分钟）不能为空")
	@ExcelField(title="课时时长（分钟）", align=2, sort=3)
	public Integer getCmin() {
		return cmin;
	}

	public void setCmin(Integer cmin) {
		this.cmin = cmin;
	}
	
	@ExcelField(title="课间休息时长", align=2, sort=4)
	public Integer getRmin() {
		return rmin;
	}

	public void setRmin(Integer rmin) {
		this.rmin = rmin;
	}
	
	@ExcelField(title="上午", dictType="", align=2, sort=5)
	public Integer getIsm() {
		return ism;
	}

	public void setIsm(Integer ism) {
		this.ism = ism;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="上午开始时间", align=2, sort=6)
	public Date getMtime() {
		return mtime;
	}

	public void setMtime(Date mtime) {
		this.mtime = mtime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEmtime() {
		return emtime;
	}

	public void setEmtime(Date emtime) {
		this.emtime = emtime;
	}

	@ExcelField(title="下午", dictType="", align=2, sort=7)
	public Integer getIsa() {
		return isa;
	}

	public void setIsa(Integer isa) {
		this.isa = isa;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="下午开始时间", align=2, sort=8)
	public Date getAtime() {
		return atime;
	}

	public void setAtime(Date atime) {
		this.atime = atime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEatime() {
		return eatime;
	}

	public void setEatime(Date eatime) {
		this.eatime = eatime;
	}

	@ExcelField(title="晚上", dictType="", align=2, sort=9)
	public Integer getIsn() {
		return isn;
	}

	public void setIsn(Integer isn) {
		this.isn = isn;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="晚上开始时间", align=2, sort=10)
	public Date getNtime() {
		return ntime;
	}

	public void setNtime(Date ntime) {
		this.ntime = ntime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEntime() {
		return entime;
	}

	public void setEntime(Date entime) {
		this.entime = entime;
	}

	public String getMtimeshour() {
		String test = mtime==null?"":DateUtils.formatDate(mtime, "HH:mm:ss");
		if (StringUtils.isBlank(this.mtimeshour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.mtimeshour;
	}

	public void setMtimeshour(String mtimeshour) { 
		this.mtimeshour = mtimeshour;
	}

	public String getMtimesmin() {
		String test = mtime==null?"":DateUtils.formatDate(mtime, "HH:mm:ss");
		if (StringUtils.isBlank(this.mtimesmin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.mtimesmin;
	}

	public void setMtimesmin(String mtimesmin) {
		this.mtimesmin = mtimesmin;
	}

	public String getAtimeehour() {
		String test = atime==null?"":DateUtils.formatDate(atime, "HH:mm:ss");
		if (StringUtils.isBlank(this.atimeehour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.atimeehour;
	}

	public void setAtimeehour(String atimeehour) {
		this.atimeehour = atimeehour;
	}

	public String getAtimeemin() {
		String test = atime==null?"":DateUtils.formatDate(atime, "HH:mm:ss");
		if (StringUtils.isBlank(this.atimeemin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.atimeemin;
	}

	public void setAtimeemin(String atimeemin) {
		this.atimeemin = atimeemin;
	}

	public String getNtimeehour() {
		String test = ntime==null?"":DateUtils.formatDate(ntime, "HH:mm:ss");
		if (StringUtils.isBlank(this.ntimeehour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.ntimeehour;
	}

	public void setNtimeehour(String ntimeehour) {
		this.ntimeehour = ntimeehour;
	}

	public String getNtimeemin() {
		String test = ntime==null?"":DateUtils.formatDate(ntime, "HH:mm:ss");
		if (StringUtils.isBlank(this.ntimeemin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.ntimeemin;
	}

	public void setNtimeemin(String ntimeemin) {
		this.ntimeemin = ntimeemin;
	}
	
	public String getEmtimeshour() {
		String test = emtime==null?"":DateUtils.formatDate(emtime, "HH:mm:ss");
		if (StringUtils.isBlank(this.emtimeshour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.emtimeshour;
	}

	public void setEmtimeshour(String emtimeshour) {
		this.emtimeshour = emtimeshour;
	}

	public String getEmtimesmin() { 
		String test = emtime==null?"":DateUtils.formatDate(emtime, "HH:mm:ss");
		if (StringUtils.isBlank(this.emtimesmin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.emtimesmin;
	}

	public void setEmtimesmin(String emtimesmin) {
		this.emtimesmin = emtimesmin;
	}

	public String getEatimeehour() { 
		String test = eatime==null?"":DateUtils.formatDate(eatime, "HH:mm:ss");
		if (StringUtils.isBlank(this.eatimeehour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.eatimeehour;
	}

	public void setEatimeehour(String eatimeehour) {
		this.eatimeehour = eatimeehour;
	}

	public String getEatimeemin() { 
		String test = eatime==null?"":DateUtils.formatDate(eatime, "HH:mm:ss");
		if (StringUtils.isBlank(this.eatimeemin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.eatimeemin;
	}

	public void setEatimeemin(String eatimeemin) {
		this.eatimeemin = eatimeemin;
	}

	public String getEntimeehour() { 
		String test = entime==null?"":DateUtils.formatDate(entime, "HH:mm:ss");
		if (StringUtils.isBlank(this.entimeehour))
			return test.equals("")?"":test.substring(0, 2);
		else 
			return this.entimeehour;
	}

	public void setEntimeehour(String entimeehour) {
		this.entimeehour = entimeehour;
	}

	public String getEntimeemin() { 
		String test = entime==null?"":DateUtils.formatDate(entime, "HH:mm:ss");
		if (StringUtils.isBlank(this.entimeemin))
			return test.equals("")?"":test.substring(3, 5);
		else 
			return this.entimeemin;
	}

	public void setEntimeemin(String entimeemin) {
		this.entimeemin = entimeemin;
	}

	public Integer getStuCount() {
		return stuCount;
	}

	public void setStuCount(Integer stuCount) {
		this.stuCount = stuCount;
	}

	public Boolean getMon() {
		return mon;
	}

	public void setMon(Boolean mon) {
		this.mon = mon;
	}

	public Boolean getTue() {
		return tue;
	}

	public void setTue(Boolean tue) {
		this.tue = tue;
	}

	public Boolean getWed() {
		return wed;
	}

	public void setWed(Boolean wed) {
		this.wed = wed;
	}

	public Boolean getThu() {
		return thu;
	}

	public void setThu(Boolean thu) {
		this.thu = thu;
	}

	public Boolean getFri() {
		return fri;
	}

	public void setFri(Boolean fri) {
		this.fri = fri;
	}

	public Boolean getSat() {
		return sat;
	}

	public void setSat(Boolean sat) {
		this.sat = sat;
	}

	public Boolean getSun() {
		return sun;
	}

	public void setSun(Boolean sun) {
		this.sun = sun;
	}
	
}