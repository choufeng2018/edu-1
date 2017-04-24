/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.schoollevel.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.schoollevel.entity.TCommSchoolLevel;

/**
 * 租户级别DAO接口
 * @author fly
 * @version 2016-09-06
 */
@MyBatisDao
public interface TCommSchoolLevelDao extends CrudDao<TCommSchoolLevel> {

	public void saveResource(TCommSchoolLevel tCommSchoolLevel);
	
	public void deleteLevelMenu(TCommSchoolLevel tCommSchoolLevel);
	
}