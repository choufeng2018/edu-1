/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.school.entity.TSchoolTeacherConf;
import com.jeeplus.modules.subject.entity.TCourseClassTime;

/**
 * 课程方案管理DAO接口
 * @author fly
 * @version 2017-01-22
 */
@MyBatisDao
public interface TCourseClassTimeDao extends CrudDao<TCourseClassTime> {

	public List<TCourseClassTime> findListForTeac(TCourseClassTime tCourseClassTime);
}