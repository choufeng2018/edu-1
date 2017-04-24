/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.dao;

import java.util.List;
 
import com.jeeplus.common.persistence.TreeDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 机构DAO接口
 * @author jeeplus
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
	public Office getByCode(String code);
	
	/**
	 * 找到所有子节点
	 * @param entity
	 * @return
	 */
	public List<Office> findListByPid(Office entity);
	
	/**
	 * 找到目标节点及所有子节点
	 * @param entity
	 * @return
	 */
	public List<Office> findListDeptByPid(Office entity);
}
