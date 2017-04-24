/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单Entity
 * @author fly
 * @version 2017-03-11
 */
public class TestOrderDetail extends DataEntity<TestOrderDetail> {
	
	private static final long serialVersionUID = 1L;
	private TestOrder order;		// o_id 父类
	private Integer num;		// num
	private Double price;		// price
	private Date ordertime;		// ordertime
	
	public TestOrderDetail() {
		super();
	}

	public TestOrderDetail(String id){
		super(id);
	}

	public TestOrderDetail(TestOrder order){
		this.order = order;
	}

	@Length(min=0, max=11, message="o_id长度必须介于 0 和 11 之间")
	public TestOrder getOrder() {
		return order;
	}

	public void setOrder(TestOrder order) {
		this.order = order;
	}
	
	@ExcelField(title="num", align=2, sort=2)
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	@ExcelField(title="price", align=2, sort=3)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="ordertime", align=2, sort=4)
	public Date getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	
}