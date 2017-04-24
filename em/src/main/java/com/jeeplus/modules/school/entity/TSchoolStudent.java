/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

/**
 * 学生信息管理Entity
 * @author fly
 * @version 2016-09-12
 */
public class TSchoolStudent extends DataEntity<TSchoolStudent> {
	
	private static final long serialVersionUID = 1L;
	private String schoolId;		// 所属学校
	private Office campus;		// 所属校区
	private String account;		// 账号
	private String name;		// 姓名
	private String password;		// 密码
	private Date birthday;		// 出生年月
	private Integer sex;		// 性别
	private Integer age;
	private String wx;		// 微信号
	private String email;		// 邮箱
	private String tel;		// 联系电话
	private Integer status;		// 状态
	private String subjectsid;
	private String subjects;
	private Date intenTime;		// 意向报名时间
	private User cc;
	
	public TSchoolStudent() {
		super();
	}

	public TSchoolStudent(String id){
		super(id);
	}

	@Length(min=0, max=30, message="所属学校长度必须介于 0 和 30 之间")
	//@ExcelField(title="所属学校", align=2, sort=1)
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	@JsonIgnore
	@NotNull(message="校区不能为空")
	//@ExcelField(title="所属校区", align=2, sort=2)
	public Office getCampus() {
		return campus;
	}

	public void setCampus(Office campus) {
		this.campus = campus;
	}
	
	@Length(min=0, max=30, message="账号长度必须介于 0 和 30 之间")
	@ExcelField(title="账号", align=2, sort=8)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@Length(min=0, max=30, message="姓名长度必须介于 0 和 30 之间")
	@ExcelField(title="姓名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=48, message="密码长度必须介于 0 和 48 之间")
	//@ExcelField(title="密码", align=2, sort=5)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="出生年月", align=2, sort=4)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@ExcelField(title="性别", dictType="sex", align=2, sort=2)
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	@ExcelField(title="年龄", align=2, sort=3)
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Length(min=0, max=20, message="微信号长度必须介于 0 和 20 之间")
	@ExcelField(title="微信号", align=2, sort=8)
	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}
	
	@Length(min=0, max=20, message="邮箱长度必须介于 0 和 20 之间")
	@ExcelField(title="邮箱", align=2, sort=7)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=15, message="联系电话长度必须介于 0 和 15 之间")
	@ExcelField(title="联系电话", align=2, sort=5)
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	@ExcelField(title="状态", dictType="student_status", align=2, sort=9)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Length(min=0, max=200, message="长度不能超过200")
	@ExcelField(title="意向学科", align=2, sort=6)
	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String getSubjectsid() {
		return subjectsid;
	}

	public void setSubjectsid(String subjectsid) {
		this.subjectsid = subjectsid;
	}

	@JsonIgnore
	@NotNull(message="课程顾问不能为空")
	@ExcelField(title="课程顾问", align=2, sort=10, value="cc.name")
	public User getCc() {
		return cc;
	}

	public void setCc(User cc) {
		this.cc = cc;
	}

	public Date getIntenTime() {
		return intenTime;
	}

	public void setIntenTime(Date intenTime) {
		this.intenTime = intenTime;
	}
	
	
}