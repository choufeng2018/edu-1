/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.em.entity.pay;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.em.entity.school.TCommSchool;

/**
 * 会员支付Entity
 * @author fly
 * @version 2016-09-08
 */
public class TSchoolPay extends DataEntity<TSchoolPay> {
	
	private static final long serialVersionUID = 1L;
	//private String schoolCode;		// 所属学校
	private TCommSchool school;	// 归属部门
	private String campusCode;		// 所属校区
	private Date buyDate;		// 资源购买日期
	private Double payCount;		// 付费金额
	private Integer payType;		// 付费方式(现金、刷卡、移动支付、在线支付)
	private String payPeriod;		// 续费周期
	private Date begDate;		// 起始日期
	private Date endDate;		// 有效期
	
	public TSchoolPay() {
		super();
	}

	public TSchoolPay(String id){
		super(id);
	}
 
	@JsonIgnore
	@NotNull(message="会员不能为空")
	@ExcelField(title="所属学校", align=2, sort=1)
	public TCommSchool getSchool() {
		return school;
	}

	public void setSchool(TCommSchool school) {
		this.school = school;
	}
	
	@Length(min=0, max=11, message="所属校区长度必须介于 0 和 11 之间")
	@ExcelField(title="所属校区", align=2, sort=2)
	public String getCampusCode() {
		return campusCode;
	}

	public void setCampusCode(String campusCode) {
		this.campusCode = campusCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="资源购买日期", align=2, sort=3)
	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}
	
	@JsonIgnore
	@NotNull(message="付费金额不能为空")
	@ExcelField(title="付费金额", align=2, sort=4)
	public Double getPayCount() {
		return payCount;
	}

	public void setPayCount(Double payCount) {
		this.payCount = payCount;
	}
	
	@JsonFormat
	@NotNull(message="付费方式不能为空")
	@ExcelField(title="付费方式(现金、刷卡、移动支付、在线支付)", dictType="paytype", align=2, sort=5)
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	
	@Length(min=0, max=64, message="续费周期长度必须介于 0 和 64 之间")
	@ExcelField(title="续费周期", align=2, sort=6)
	public String getPayPeriod() {
		return payPeriod;
	}

	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="起始日期", align=2, sort=7)
	public Date getBegDate() {
		return begDate;
	}

	public void setBegDate(Date begDate) {
		this.begDate = begDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="有效期", align=2, sort=8)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}