/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.subject.entity.TCourseClass;

/**
 * 课程方案管理DAO接口
 * @author fly
 * @version 2016-09-13
 */
@MyBatisDao
public interface TCourseClassDao extends CrudDao<TCourseClass> {

	public List<TCourseClass> findListByIds(String ids);
	
	public List<TCourseClass> findListForEnter(TCourseClass tCourseClass);
	
	public void updateIsCreate(TCourseClass tCourseClass);
	
	public void updateStatus(TCourseClass tCourseClass);
	
	public void updateStuNum(TCourseClass tCourseClass); 
	
	public TCourseClass getCount(TCourseClass tCourseClass);
}