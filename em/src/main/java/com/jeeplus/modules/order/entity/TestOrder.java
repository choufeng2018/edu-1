/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.entity;

import org.hibernate.validator.constraints.Length;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单Entity
 * @author fly
 * @version 2017-03-11
 */
public class TestOrder extends DataEntity<TestOrder> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// code
	private Double price;		// price
	private String custom;		// custom
	private List<TestOrderDetail> testOrderDetailList = Lists.newArrayList();		// 子表列表
	
	public TestOrder() {
		super();
	}

	public TestOrder(String id){
		super(id);
	}

	@Length(min=0, max=255, message="code长度必须介于 0 和 255 之间")
	@ExcelField(title="code", align=2, sort=1)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="price", align=2, sort=2)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Length(min=0, max=255, message="custom长度必须介于 0 和 255 之间")
	@ExcelField(title="custom", align=2, sort=3)
	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}
	
	public List<TestOrderDetail> getTestOrderDetailList() {
		return testOrderDetailList;
	}

	public void setTestOrderDetailList(List<TestOrderDetail> testOrderDetailList) {
		this.testOrderDetailList = testOrderDetailList;
	}
}