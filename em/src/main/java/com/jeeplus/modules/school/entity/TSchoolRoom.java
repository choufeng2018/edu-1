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
import com.jeeplus.modules.em.entity.school.TCommSchool;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 教室信息管理Entity
 * @author fly
 * @version 2016-09-09
 */
public class TSchoolRoom extends DataEntity<TSchoolRoom> {
	
	private static final long serialVersionUID = 1L;
	private String roomCode;		// 教室编码
	private String roomDesc;		// 教室名称
	private TCommSchool school;		// 所属学校
	private Office campus;
	private String useDesc;		// 教室用途描述
	private Integer useType;
	private TCommSubject course;		// 学科
	private Integer status;
	private List<String> campidStr;
	private String sqlMapStr;
	
	public TSchoolRoom() {
		super();
	}

	public TSchoolRoom(String id){
		super(id);
	}

	@Length(min=0, max=64, message="教室编码长度必须介于 0 和 64 之间")
	@ExcelField(title="教室编码", align=2, sort=1)
	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	
	@Length(min=0, max=64, message="教室名称长度必须介于 0 和 64 之间")
	@ExcelField(title="教室名称", align=2, sort=2)
	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}
	
	@JsonIgnore
	@NotNull(message="学校不能为空")
	@ExcelField(title="所属学校", align=2, sort=3)
	public TCommSchool getSchool() {
		return school;
	}

	public void setSchool(TCommSchool school) {
		this.school = school;
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

	@Length(min=0, max=200, message="教室用途描述长度必须介于 0 和 200 之间")
	@ExcelField(title="教室用途描述", align=2, sort=5)
	public String getUseDesc() {
		return useDesc;
	}

	public void setUseDesc(String useDesc) {
		this.useDesc = useDesc;
	}
 
	@ExcelField(title="教室用途描述", align=2, sort=6)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<String> getCampidStr() {
		return campidStr;
	}

	public void setCampidStr(List<String> campidStr) {
		this.campidStr = campidStr;
	}

	public String getSqlMapStr() {
		return sqlMapStr;
	}

	public void setSqlMapStr(String sqlMapStr) {
		this.sqlMapStr = sqlMapStr;
	}

	public Integer getUseType() {
		return useType;
	}

	public void setUseType(Integer useType) {
		this.useType = useType;
	}

	@JsonIgnore  
	public TCommSubject getCourse() {
		return course;
	}

	public void setCourse(TCommSubject course) {
		this.course = course;
	}
}