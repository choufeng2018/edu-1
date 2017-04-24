/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 课程信息Entity
 * @author fly
 * @version 2016-09-09
 */
public class TCommSubject extends DataEntity<TCommSubject> {
	
	private static final long serialVersionUID = 1L;
	private String subjectCode;		// 课程编码
	private String subjectName;		// 课程名称
	private TCommSubject parent;	// 课程类型
	private String schoolId;		// 所属学校
	private Office campus;		// 所属校区
	private String subjectDesc;		// 课程描述
	
	public TCommSubject() {
		super();
	}

	public TCommSubject(String id){
		super(id);
	}

	@Length(min=0, max=30, message="课程编码长度必须介于 0 和 30 之间")
	@ExcelField(title="课程编码", align=2, sort=1)
	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	@Length(min=0, max=30, message="课程名称长度必须介于 0 和 30 之间")
	@ExcelField(title="课程名称", align=2, sort=2)
	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	 
	public TCommSubject getParent() {
		return parent;
	}

	public void setParent(TCommSubject parent) {
		this.parent = parent;
	}

	@Length(min=0, max=11, message="所属学校长度必须介于 0 和 11 之间")
	@ExcelField(title="所属学校", align=2, sort=4)
	public String getSchoolId() {
		return schoolId;
	}
 
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	 
	@JsonIgnore
	@ExcelField(title="所属校区", align=2, sort=5)
	public Office getCampus() {
		return campus;
	}

	public void setCampus(Office campus) {
		this.campus = campus;
	}
	
	@Length(min=0, max=500, message="课程描述长度必须介于 0 和 200 之间")
	@ExcelField(title="课程描述", align=2, sort=6)
	public String getSubjectDesc() {
		return subjectDesc;
	}

	public void setSubjectDesc(String subjectDesc) {
		this.subjectDesc = subjectDesc;
	}
	
	@JsonIgnore
	public static String getRootId(){
		return "0";
	}
	
	@JsonIgnore
	public static void sortList(List<TCommSubject> list, List<TCommSubject> sourcelist, String parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			TCommSubject e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null && e.getParent().getId().equals(parentId)){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						TCommSubject child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getId()!=null && child.getParent().getId().equals(e.getId())){
							sortList(list, sourcelist, e.getId(), true);
							break;
						}
					}
				}
			}
		}
	}
}