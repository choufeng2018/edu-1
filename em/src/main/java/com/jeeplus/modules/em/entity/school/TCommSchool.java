/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.em.entity.school;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.schoollevel.entity.TCommSchoolLevel;
import com.jeeplus.modules.sys.entity.Area;

/**
 * 学校租户Entity
 * @author fly
 * @version 2016-08-28
 */
public class TCommSchool extends DataEntity<TCommSchool> {
	
	private static final long serialVersionUID = 1L;
	private String schoolCode;		// 学校编码
	private String schoolName;		// 学校名称
	private Area province;		// 所在省
	private Area city;		// 所在地市
	private Area region;		// 所属区县
	private String address;		// 学校地址
	private String contact;
	private String tel;		// 联系电话
	private String email;		// 联系电话
	private String summary;		// 学校简介
	private String homepage;		// 学校主页
	private String zjphoto;
	private TCommSchoolLevel tSchoolLevel; //会员等级
	private Integer schoolType;		// 租户状态（1 试用  2 正式）
	private Integer schoolStatus;	// 租户状态（0 禁用  1 正常）
	private Date endDate;		// 有效期
	private String endDateStr;
	
	public TCommSchool() {
		super();
	}

	public TCommSchool(String id){
		super(id);
	}

	@Length(min=1, max=40, message="学校编码长度必须介于 1 和 40 之间")
	@ExcelField(title="学校编码", align=2, sort=1)
	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	
	@Length(min=1, max=100, message="学校名称长度必须介于 1 和 100 之间")
	@ExcelField(title="学校名称", align=2, sort=2)
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	@NotNull(message="所在省不能为空")
	@ExcelField(title="所在省", dictType="", align=2, sort=3)
	public Area getProvince() {
		return province;
	}

	public void setProvince(Area province) {
		this.province = province;
	}
	
	@NotNull(message="所在地市不能为空")
	@ExcelField(title="所在地市", dictType="", align=2, sort=4)
	public Area getCity() {
		return city;
	}

	public void setCity(Area city) {
		this.city = city;
	}
	
	@NotNull(message="会员等级不能为空")
	@ExcelField(title="会员等级", dictType="", align=2, sort=14)
	public TCommSchoolLevel gettSchoolLevel() {
		return tSchoolLevel;
	}

	public void settSchoolLevel(TCommSchoolLevel tSchoolLevel) {
		this.tSchoolLevel = tSchoolLevel;
	}

	@NotNull(message="所属区县不能为空")
	@ExcelField(title="所属区县", dictType="", align=2, sort=5)
	public Area getRegion() {
		return region;
	}

	public void setRegion(Area region) {
		this.region = region;
	}
	
	@Length(min=0, max=100, message="学校地址长度必须介于 0 和 100 之间")
	@ExcelField(title="学校地址", align=2, sort=6)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=40, message="联系人长度不能超过40 个字符")
	@ExcelField(title="联系人", align=2, sort=14)
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Length(min=11, max=11, message="联系电话必须为11位的手机号码")
	@ExcelField(title="联系电话", align=2, sort=7)
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	@Length(min=0, max=36, message="邮箱长度不能超过36位")
	@ExcelField(title="邮箱", align=2, sort=8)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=2000, message="学校简介长度必须介于 0 和 2000 之间")
	@ExcelField(title="学校简介", align=2, sort=9)
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@Length(min=0, max=40, message="学校主页长度必须介于 0 和 40 之间")
	@ExcelField(title="学校主页", align=2, sort=10)
	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	 
	public String getZjphoto() {
		return zjphoto;
	}

	public void setZjphoto(String zjphoto) {
		this.zjphoto = zjphoto;
	}

	@NotNull(message="租户类型不能为空")
	@ExcelField(title="租户类型", dictType="", align=2, sort=11)
	public Integer getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(Integer schoolType) {
		this.schoolType = schoolType;
	}

	@NotNull(message="租户状态不能为空")
	@ExcelField(title="租户状态（1 试用  2 正式）", dictType="", align=2, sort=12)
	public Integer getSchoolStatus() {
		return schoolStatus;
	}

	public void setSchoolStatus(Integer schoolStatus) {
		this.schoolStatus = schoolStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="有效期", align=2, sort=13)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEndDateStr() {
		return DateUtils.formatDate(endDate, null);
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	
}