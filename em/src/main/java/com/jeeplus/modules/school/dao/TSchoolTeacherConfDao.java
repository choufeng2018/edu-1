/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.school.entity.TSchoolTeacherConf;

/**
 * 教师一对一教学执行时间配置DAO接口
 * @author flying
 * @version 2017-01-15
 */
@MyBatisDao
public interface TSchoolTeacherConfDao extends CrudDao<TSchoolTeacherConf> {

	public TSchoolTeacherConf getByTid(TSchoolTeacherConf tSchoolTeacherConf);
}