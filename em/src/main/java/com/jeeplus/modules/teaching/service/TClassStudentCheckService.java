/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.teaching.dao.TClassStudentCheckDao;
import com.jeeplus.modules.teaching.entity.TClassStudent;
import com.jeeplus.modules.teaching.entity.TClassStudentCheck;

/**
 * 考勤记录管理Service
 * @author fly
 * @version 2016-09-18
 */
@Service
@Transactional(readOnly = true)
public class TClassStudentCheckService extends CrudService<TClassStudentCheckDao, TClassStudentCheck> {

	public TClassStudentCheck get(String id) {
		return super.get(id);
	}
	
	public TClassStudentCheck getByParentId(String parentid) {
		return dao.findByParentId(parentid);
	}
	
	public TClassStudentCheck getByTtIdAndStuId(TClassStudentCheck tClassStudentCheck) {
		return dao.getByTtIdAndStuId(tClassStudentCheck);
	}
	
	public List<TClassStudentCheck> findList(TClassStudentCheck tClassStudentCheck) {
		return super.findList(tClassStudentCheck);
	}
	
	public Page<TClassStudentCheck> findPage(Page<TClassStudentCheck> page, TClassStudentCheck tClassStudentCheck) {
		return super.findPage(page, tClassStudentCheck);
	}
	
	public List<TClassStudentCheck> findListByTtid(TClassStudentCheck tClassStudentCheck) {
		return dao.findListByTtid(tClassStudentCheck);
	}
	
	public List<TClassStudentCheck> findListForMissed(TClassStudentCheck tClassStudentCheck) {
		return dao.findListForMissed(tClassStudentCheck);
	}
	
	public Page<TClassStudentCheck> findPageForMissed(Page<TClassStudentCheck> page, TClassStudentCheck tClassStudentCheck) {
		tClassStudentCheck.setPage(page);
		return page.setList(dao.findPageForMissed(tClassStudentCheck)); 
	}
	
	public Page<TClassStudentCheck> findPageForTran(Page<TClassStudentCheck> page, TClassStudentCheck tClassStudentCheck) {
		tClassStudentCheck.setPage(page);
		return page.setList(dao.findPageForTran(tClassStudentCheck)); 
	}
	
	@Transactional(readOnly = false)
	public void save(TClassStudentCheck tClassStudentCheck) {
		if (tClassStudentCheck.getType() == 2 && tClassStudentCheck.getIsNewRecord())
		{
			dao.updateBkStatus(tClassStudentCheck.getParent().getId());
		}
		
		super.save(tClassStudentCheck); 
	}
	
	@Transactional(readOnly = false)
	public void save1(TClassStudent tClassStudent) 
	{
		for (int i=0; i<tClassStudent.gettClassStudentCheckList().size(); i++)
		{
			TClassStudentCheck tClassStudentCheck = tClassStudent.gettClassStudentCheckList().get(i);
			if (tClassStudentCheck.getStatus() != null && tClassStudentCheck.getStatus() == 3)
			{
				tClassStudentCheck.setStudentId(tClassStudent.getStudent().getId());
				tClassStudentCheck.setStudentName(tClassStudent.getStudent().getName());
				tClassStudentCheck.setType(1);
				super.save(tClassStudentCheck); 
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TClassStudentCheck tClassStudentCheck) {
		super.delete(tClassStudentCheck);
	}
	
}