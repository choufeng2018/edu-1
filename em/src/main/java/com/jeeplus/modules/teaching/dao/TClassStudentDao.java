/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.teaching.entity.TClassStudent;

/**
 * 报名管理DAO接口
 * @author fly
 * @version 2016-09-16
 */
@MyBatisDao
public interface TClassStudentDao extends CrudDao<TClassStudent> {

	public TClassStudent getByStudentIdAndClassId(TClassStudent tClassStudent);
	
	public List<TClassStudent> findListByType(TClassStudent tClassStudent);
	
	public List<TClassStudent> findListByStatus(TClassStudent tClassStudent);
	
	public List<TClassStudent> findPageForTran(TClassStudent tClassStudent);
	
	public List<TClassStudent> findPageForLeave(TClassStudent tClassStudent);
	
	public List<TClassStudent> findPageForView(TClassStudent tClassStudent);
	
	public void updateStatus(TClassStudent tClassStudent);
	
	public void updateRenewCount(TClassStudent tClassStudent);
	
	public void updateYwc(TClassStudent tClassStudent);
}