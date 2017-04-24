/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.teaching.entity.TClassStudentQuit;

/**
 * 学生退课管理DAO接口
 * @author fly
 * @version 2016-09-28
 */
@MyBatisDao
public interface TClassStudentQuitDao extends CrudDao<TClassStudentQuit> {

	
}