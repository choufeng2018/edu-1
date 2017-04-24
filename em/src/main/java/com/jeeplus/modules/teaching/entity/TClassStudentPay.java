/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.User;

/**
 * 缴费Entity
 * @author flying
 * @version 2017-01-18
 */
public class TClassStudentPay extends DataEntity<TClassStudentPay> {
	
	private static final long serialVersionUID = 1L;
	private String schoolId;		// 学校ID
	private String campusId;		// 校区
	private TClassStudent cs;		// 课程班级编号
	private Integer studentId;		// 学生ID
	private Integer paytype;		// 付款方式
	private Integer zks;		// 总课时数
	private Integer ywcks;		// 已完成课时数
	private Double amount;		// 缴费总金额
	private Double balance;		// 可用余额
	private User cc;
	
	public TClassStudentPay() {
		super();
	}

	public TClassStudentPay(String id){
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
	
	@ExcelField(title="课程班级编号", align=2, sort=3)
	public TClassStudent getCs() {
		return cs;
	}

	public void setCs(TClassStudent cs) {
		this.cs = cs;
	}
	
	@ExcelField(title="学生ID", align=2, sort=4)
	public Integer getStudentId() {
		return studentId;
	}
 
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	
	@ExcelField(title="付款方式", align=2, sort=5)
	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}
	
	@ExcelField(title="总课时数", align=2, sort=6)
	public Integer getZks() {
		return zks;
	}

	public void setZks(Integer zks) {
		this.zks = zks;
	}
	
	@ExcelField(title="已完成课时数", align=2, sort=7)
	public Integer getYwcks() {
		return ywcks;
	}

	public void setYwcks(Integer ywcks) {
		this.ywcks = ywcks;
	}
	
	@ExcelField(title="缴费总金额", align=2, sort=8)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@ExcelField(title="可用余额", align=2, sort=9)
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public User getCc() {
		return cc;
	}

	public void setCc(User cc) {
		this.cc = cc;
	}
	
}