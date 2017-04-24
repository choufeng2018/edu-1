/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.school.entity.TSchoolTeacher; 

/**
 * 教师信息管理DAO接口
 * @author fly
 * @version 2016-09-12
 */
@MyBatisDao
public interface TSchoolTeacherDao extends CrudDao<TSchoolTeacher> {
	public TSchoolTeacher getUserByTel(TSchoolTeacher tSchoolTeacher) ;
	
	public TSchoolTeacher getUserByAccount(TSchoolTeacher tSchoolTeacher) ;
}