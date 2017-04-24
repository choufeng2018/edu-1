/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.teaching.entity.TClassStudentCheck;

/**
 * 考勤记录管理DAO接口
 * @author fly
 * @version 2016-09-18
 */
@MyBatisDao
public interface TClassStudentCheckDao extends CrudDao<TClassStudentCheck> {

	public List<TClassStudentCheck> findListByTtid(TClassStudentCheck tClassStudentCheck);
	
	public List<TClassStudentCheck> findListForMissed(TClassStudentCheck tClassStudentCheck);
	
	public TClassStudentCheck findByParentId(String parentid);
	
	public TClassStudentCheck getByTtIdAndStuId(TClassStudentCheck tClassStudentCheck);
	
	public List<TClassStudentCheck> findPageForTran(TClassStudentCheck tClassStudentCheck);
	
	public List<TClassStudentCheck> findPageForMissed(TClassStudentCheck tClassStudentCheck);
	
	public void updateBkStatus(String id);
}