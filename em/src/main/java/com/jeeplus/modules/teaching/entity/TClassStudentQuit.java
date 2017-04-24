/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.entity;

import org.hibernate.validator.constraints.Length;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 学生退课管理Entity
 * @author fly
 * @version 2016-09-28
 */
public class TClassStudentQuit extends DataEntity<TClassStudentQuit> {
	
	private static final long serialVersionUID = 1L;
	private String schoolId;		// 学校ID
	private String campusId;		// 校区
	private TClassStudent tClassStudent;		// 课程班级编号
	//private String studentId;		// 学生ID
	//private String name;		// 学生姓名
	//private Double amount;		// 缴费总金额
	private Double perAmount;		// 单课时费用
	private Double backAmount;		// 退费金额
	//private Integer classAll;		// 总课时
	private Integer classComplete;		// 可用余额
	private Integer classRest;		// 剩余课时
	private Integer classMissed;		// 缺勤课时
	private Integer classLeave;		// 请假课时
	private Integer classAdd;		// 补课课时
	private User user;		// 课程顾问
	
	public TClassStudentQuit() {
		super();
	}

	public TClassStudentQuit(String id){
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
	 
	@ExcelField(title="课程班级", fieldType=TClassStudent.class, value="", align=2, sort=3)
	public TClassStudent gettClassStudent() {
		return tClassStudent;
	}

	public void settClassStudent(TClassStudent tClassStudent) {
		this.tClassStudent = tClassStudent;
	}
  
	@ExcelField(title="单课时费用", align=2, sort=7)
	public Double getPerAmount() {
		return perAmount;
	}

	public void setPerAmount(Double perAmount) {
		this.perAmount = perAmount;
	}
	
	@ExcelField(title="退费金额", align=2, sort=8)
	public Double getBackAmount() {
		return backAmount;
	}

	public void setBackAmount(Double backAmount) {
		this.backAmount = backAmount;
	}
	 
	@ExcelField(title="可用余额", align=2, sort=10)
	public Integer getClassComplete() {
		return classComplete;
	}

	public void setClassComplete(Integer classComplete) {
		this.classComplete = classComplete;
	}
	
	@ExcelField(title="剩余课时", align=2, sort=11)
	public Integer getClassRest() {
		return classRest;
	}

	public void setClassRest(Integer classRest) {
		this.classRest = classRest;
	}
	
	@ExcelField(title="缺勤课时", align=2, sort=12)
	public Integer getClassMissed() {
		return classMissed;
	}

	public void setClassMissed(Integer classMissed) {
		this.classMissed = classMissed;
	}
	
	@ExcelField(title="请假课时", align=2, sort=13)
	public Integer getClassLeave() {
		return classLeave;
	}

	public void setClassLeave(Integer classLeave) {
		this.classLeave = classLeave;
	}
	
	@ExcelField(title="补课课时", align=2, sort=14)
	public Integer getClassAdd() {
		return classAdd;
	}

	public void setClassAdd(Integer classAdd) {
		this.classAdd = classAdd;
	}
	 
	@ExcelField(title="课程顾问", fieldType=User.class, value="", align=2, sort=15)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}