/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 学科分类Entity
 * @author fly
 * @version 2016-09-09
 */
public class TCommSubjectField extends DataEntity<TCommSubjectField> {
	
	private static final long serialVersionUID = 1L;
	private String fieldCode;		// 课程编码
	private String fieldName;		// 课程名称
	private String schoolCode;		// 所属学校
	private Office campus;		// 所属校区
	
	public TCommSubjectField() {
		super();
	}

	public TCommSubjectField(String id){
		super(id);
	}

	@Length(min=0, max=30, message="课程编码长度必须介于 0 和 30 之间")
	@ExcelField(title="课程编码", align=2, sort=1)
	public String getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}
	
	@Length(min=0, max=30, message="课程名称长度必须介于 0 和 30 之间")
	@ExcelField(title="课程名称", align=2, sort=2)
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Length(min=0, max=64, message="所属学校长度必须介于 0 和 64 之间")
	@ExcelField(title="所属学校", align=2, sort=3)
	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	 
	@JsonIgnore
	@NotNull(message="校区不能为空")
	@ExcelField(title="所属校区", align=2, sort=4)
	public Office getCampus() {
		return campus;
	}

	public void setCampus(Office campus) {
		this.campus = campus;
	}
	
}