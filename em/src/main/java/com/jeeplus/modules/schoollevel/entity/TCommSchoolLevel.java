/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.schoollevel.entity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Menu;

/**
 * 租户级别Entity
 * @author fly
 * @version 2016-09-06
 */
public class TCommSchoolLevel extends DataEntity<TCommSchoolLevel> {
	
	private static final long serialVersionUID = 1L;
	private String levelCode;		// 等级编码
	private String levelDesc;		// 等级描述
	private Double levelPrice;		// 费用价格（元/年）
	private List<Menu> menuList = Lists.newArrayList(); // 拥有菜单列表
	
	public TCommSchoolLevel() {
		super();
	}

	public TCommSchoolLevel(String id){
		super(id);
	}

	@Length(min=0, max=40, message="等级编码长度必须介于 0 和 40 之间")
	@ExcelField(title="等级编码", align=2, sort=1)
	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	
	@Length(min=0, max=50, message="等级描述长度必须介于 0 和 50 之间")
	@ExcelField(title="等级描述", align=2, sort=2)
	public String getLevelDesc() {
		return levelDesc;
	}

	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}
	
	@ExcelField(title="费用价格（元/年）", align=2, sort=3)
	public Double getLevelPrice() {
		return levelPrice;
	}

	public void setLevelPrice(Double levelPrice) {
		this.levelPrice = levelPrice;
	}
	
	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<String> getMenuIdList() {
		List<String> menuIdList = Lists.newArrayList();
		for (Menu menu : menuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}

	public void setMenuIdList(List<String> menuIdList) {
		menuList = Lists.newArrayList();
		for (String menuId : menuIdList) {
			Menu menu = new Menu();
			menu.setId(menuId);
			menuList.add(menu);
		}
	}

	public String getMenuIds() {
		return StringUtils.join(getMenuIdList(), ",");
	}
	
	public void setMenuIds(String menuIds) {
		menuList = Lists.newArrayList();
		if (menuIds != null){
			String[] ids = StringUtils.split(menuIds, ",");
			setMenuIdList(Lists.newArrayList(ids));
		}
	}
}