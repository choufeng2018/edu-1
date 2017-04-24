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

/**
 * 教师信息管理Entity
 * @author fly
 * @version 2016-09-12
 */
public class TSchoolTeacher extends DataEntity<TSchoolTeacher> {
	
	private static final long serialVersionUID = 1L;
	private String schoolId;		// 所属学校
	private Office campus;		// 所属校区
	private String number;		// 工号
	private String name;		// 姓名
	private String account;		// 账号
	private String password;		// 密码
	private Integer level;		// 级别
	private Integer sex;		// 性别
	private String age;		// 年龄
	private Integer type;		// 类型（1 全职、2 兼职）
	private Integer status;		// 状态（1 在职、2 离职）
	private Integer isoto;		
	private Date birthday;
	private String wx;		// 微信号
	private String email;		// 邮箱
	private String tel;		// 联系电话
	private TCommSubject course;		// 课程
	private Integer price;
	private Integer cmin;
	
	public TSchoolTeacher() {
		super();
	}

	public TSchoolTeacher(String id){
		super(id);
	}

	@Length(min=0, max=30, message="所属学校长度必须介于 0 和 30 之间")
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	@JsonIgnore
	@NotNull(message="校区不能为空")
	@ExcelField(title="所属校区", align=2, sort=13, value="campus.name")
	public Office getCampus() {
		return campus;
	}

	public void setCampus(Office campus) {
		this.campus = campus;
	}
	
	@Length(min=0, max=30, message="工号长度必须介于 0 和 30 之间")
	@ExcelField(title="工号", align=2, sort=12)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	@Length(min=0, max=30, message="姓名长度必须介于 0 和 30 之间")
	@ExcelField(title="姓名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=30, message="账号长度必须介于 0 和 30 之间")
	@ExcelField(title="账号", align=2, sort=8)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@Length(min=0, max=48, message="密码长度必须介于 0 和 48 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@ExcelField(title="级别", dictType="teacher_level", align=2, sort=11)
	public Integer getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	@ExcelField(title="性别", dictType="sex", align=2, sort=2)
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@ExcelField(title="状态", dictType="teacher_status", align=2, sort=10)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsoto() {
		return isoto;
	}

	public void setIsoto(Integer isoto) {
		this.isoto = isoto;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="出生日期", align=2, sort=4)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Length(min=0, max=30, message="年龄长度必须介于 0 和 30 之间")
	@ExcelField(title="年龄", align=2, sort=3)
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	@ExcelField(title="类型", dictType="teacher_type", align=2, sort=9)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@Length(min=0, max=30, message="微信号长度必须介于 0 和 30 之间")
	@ExcelField(title="微信号", align=2, sort=10)
	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}
	
	@Length(min=0, max=30, message="邮箱长度必须介于 0 和 30 之间")
	@ExcelField(title="邮箱", align=2, sort=7)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=30, message="联系电话长度必须介于 0 和 30 之间")
	@ExcelField(title="联系电话", align=2, sort=5)
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@JsonIgnore
	@NotNull(message="专业不能为空")
	@ExcelField(title="专业", align=2, sort=6, value="course.subjectName")
	public TCommSubject getCourse() {
		return course;
	}

	public void setCourse(TCommSubject course) {
		this.course = course;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getCmin() {
		return cmin;
	}

	public void setCmin(Integer cmin) {
		this.cmin = cmin;
	}
	
	
}