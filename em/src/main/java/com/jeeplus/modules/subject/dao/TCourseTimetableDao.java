/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.subject.entity.TCourseTimetable;

/**
 * 课程表DAO接口
 * @author fly
 * @version 2016-09-15
 */
@MyBatisDao
public interface TCourseTimetableDao extends CrudDao<TCourseTimetable> {

	public void deleteByClassid(String classid) ;
	
	public List<TCourseTimetable> findPageForCheck(TCourseTimetable tCourseTimetable);
	
	public List<TCourseTimetable> findPageForMissed(TCourseTimetable tCourseTimetable);
	
	public List<TCourseTimetable> findListforpay(TCourseTimetable tCourseTimetable);
	
	public List<TCourseTimetable> findListIsComplete(TCourseTimetable tCourseTimetable);
	
	public void updateStatus(TCourseTimetable tCourseTimetable);
	
	public void updateRoom(TCourseTimetable tCourseTimetable);
	
	public void updateTime(TCourseTimetable tCourseTimetable);
	
	public void updatedelayTime(TCourseTimetable tCourseTimetable);
	
	public void updateTeacher(TCourseTimetable tCourseTimetable);
}