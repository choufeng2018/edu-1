/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.resource.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.resource.entity.SysResource;

/**
 * 系统资源DAO接口
 * @author fly
 * @version 2016-08-29
 */
@MyBatisDao
public interface SysResourceDao extends CrudDao<SysResource> {
	/**
	 * 查询列表数据
	 * @param menuid
	 * @return
	 */
	public SysResource getByMenuid(String menuid);
	
	/**
	 * 查询列表数据
	 * @param menuid
	 * @return
	 */
	public List<SysResource> findListBySchoolCode(String schoolcode);
}