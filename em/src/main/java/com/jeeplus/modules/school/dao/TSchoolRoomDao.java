/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.school.entity.TSchoolRoom;

/**
 * 教室信息管理DAO接口
 * @author fly
 * @version 2016-09-09
 */
@MyBatisDao
public interface TSchoolRoomDao extends CrudDao<TSchoolRoom> {

	public void updatestatus(TSchoolRoom tSchoolRoom);
}