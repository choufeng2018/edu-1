/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.resource.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 系统资源Entity
 * @author fly
 * @version 2016-08-29
 */
public class SysResource extends DataEntity<SysResource> {
	
	private static final long serialVersionUID = 1L;
	private String menuid;		// 资源编码
	private String name;		// 资源名称
	private String rdesc;		// 资源描述
	private Integer level;		// 资源级别
	private Integer type;		// 资源类型（1 系统资源 2 租户资源）
	private Integer isPrice;		// 是否收费
	private Double price;		// 资源价格
	private String unit;		// 资源单位
	
	public SysResource() {
		super();
	}

	public SysResource(String id){
		super(id);
	}

	@Length(min=0, max=50, message="资源编码长度必须介于 0 和 50 之间")
	@ExcelField(title="资源编码", align=2, sort=1)
	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	
	@Length(min=0, max=50, message="资源名称长度必须介于 0 和 50 之间")
	@ExcelField(title="资源名称", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=500, message="资源描述长度必须介于 0 和 500 之间")
	@ExcelField(title="资源描述", align=2, sort=3)
	public String getRdesc() {
		return rdesc;
	}

	public void setRdesc(String rdesc) {
		this.rdesc = rdesc;
	}
	
	@ExcelField(title="资源级别", align=2, sort=4)
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	@ExcelField(title="资源类型（1 系统资源 2 租户资源）", align=2, sort=5)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@ExcelField(title="是否收费", dictType="is_resource", align=2, sort=6)
	public Integer getIsPrice() {
		return isPrice;
	}

	public void setIsPrice(Integer isPrice) {
		this.isPrice = isPrice;
	}
	
	@ExcelField(title="资源价格", align=2, sort=7)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Length(min=0, max=50, message="资源单位长度必须介于 0 和 50 之间")
	@ExcelField(title="资源单位", align=2, sort=8)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}