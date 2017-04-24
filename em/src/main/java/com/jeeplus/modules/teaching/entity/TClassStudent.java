/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.school.entity.TSchoolStudent;
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.sys.entity.User;

/**
 * 报名管理Entity
 * @author fly
 * @version 2016-09-16
 */
public class TClassStudent extends DataEntity<TClassStudent> {
	
	private static final long serialVersionUID = 1L;
	private String schoolId;		// 学校ID
	private String campusId;		// 校区
	private TCourseClass courseclass;		// 课程班级编号
	private TSchoolStudent student;		// 学生ID
	private Integer ispay;		// 是否已缴费
	private Integer paytype;		// 缴费方式
	private Double amount;		// 缴费总金额
	private Double balance;		// 可用余额
	private Integer status;		// 状态(正常, 退学)
	private Integer zks;
	private Integer ywcks;
	private User cc;
	private TClassStudent parent;
	private Integer leavecount;
	private Integer renewcount;
	
	private List<TClassStudentCheck> tClassStudentCheckList = Lists.newArrayList();		// 子表列表
	
	public TClassStudent() {
		super();
	}

	public TClassStudent(String id){
		super(id);
	}

	@Length(min=0, max=30, message="学校ID长度必须介于 0 和 30 之间")
	@ExcelField(title="学校ID", align=2, sort=1)
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	@Length(min=0, max=64, message="校区长度必须介于 0 和 64 之间")
	@ExcelField(title="校区", align=2, sort=2)
	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	 
	@JsonIgnore
	@NotNull(message="课程班级不能为空")
	@ExcelField(title="课程班级编号", align=2, sort=3)
	public TCourseClass getCourseclass() {
		return courseclass;
	}

	public void setCourseclass(TCourseClass courseclass) {
		this.courseclass = courseclass;
	}
	 
	@JsonIgnore
	@NotNull(message="课程班级不能为空")
	@ExcelField(title="学生ID", align=2, sort=4)
	public TSchoolStudent getStudent() {
		return student;
	}

	public void setStudent(TSchoolStudent student) {
		this.student = student;
	}
	
	@ExcelField(title="是否已缴费", dictType="yes_no", align=2, sort=6)
	public Integer getIspay() {
		return ispay;
	}

	public void setIspay(Integer ispay) {
		this.ispay = ispay;
	}
	 
	@ExcelField(title="缴费方式", dictType="pay_type", align=2, sort=6)
	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	@ExcelField(title="缴费总金额", align=2, sort=7)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@ExcelField(title="可用余额", align=2, sort=8)
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	@ExcelField(title="状态", dictType="enter_status", align=2, sort=9)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getZks() {
		return zks;
	}

	public void setZks(Integer zks) {
		this.zks = zks;
	}

	public Integer getYwcks() {
		return ywcks;
	}

	public void setYwcks(Integer ywcks) {
		this.ywcks = ywcks;
	}

	@JsonIgnore
	@NotNull(message="课程顾问不能为空")
	@ExcelField(title="课程顾问", align=2, sort=10)
	public User getCc() {
		return cc;
	}

	public void setCc(User cc) {
		this.cc = cc;
	}
	
	@JsonIgnore
	public TClassStudent getParent() {
		return parent;
	}

	public void setParent(TClassStudent parent) {
		this.parent = parent;
	}

	public Integer getLeavecount() {
		return leavecount;
	}

	public void setLeavecount(Integer leavecount) {
		this.leavecount = leavecount;
	}

	public Integer getRenewcount() {
		return renewcount;
	}

	public void setRenewcount(Integer renewcount) {
		this.renewcount = renewcount;
	}

	public List<TClassStudentCheck> gettClassStudentCheckList() {
		return tClassStudentCheckList;
	}

	public void settClassStudentCheckList(
			List<TClassStudentCheck> tClassStudentCheckList) {
		this.tClassStudentCheckList = tClassStudentCheckList;
	}
	
	
}