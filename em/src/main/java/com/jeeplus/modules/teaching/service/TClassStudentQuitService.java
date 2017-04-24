/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.teaching.entity.TClassStudent;
import com.jeeplus.modules.teaching.entity.TClassStudentQuit;
import com.jeeplus.modules.teaching.dao.TClassStudentQuitDao;

/**
 * 学生退课管理Service
 * @author fly
 * @version 2016-09-28
 */
@Service
@Transactional(readOnly = true)
public class TClassStudentQuitService extends CrudService<TClassStudentQuitDao, TClassStudentQuit> {
	@Autowired
	private TClassStudentService tClassStudentService;
	
	public TClassStudentQuit get(String id) {
		return super.get(id);
	}
	
	public List<TClassStudentQuit> findList(TClassStudentQuit tClassStudentQuit) {
		return super.findList(tClassStudentQuit);
	}
	
	public Page<TClassStudentQuit> findPage(Page<TClassStudentQuit> page, TClassStudentQuit tClassStudentQuit) {
		return super.findPage(page, tClassStudentQuit);
	}
	
	@Transactional(readOnly = false)
	public void save(TClassStudentQuit tClassStudentQuit) {
		super.save(tClassStudentQuit);
		
		// 更新报名表状态，中途退学
		TClassStudent tClassStudent = tClassStudentQuit.gettClassStudent();
		tClassStudent.setStatus(3);
		tClassStudentService.updateStatus(tClassStudent);
	}
	
	@Transactional(readOnly = false)
	public void delete(TClassStudentQuit tClassStudentQuit) {
		super.delete(tClassStudentQuit);
	}
	
	
	
	
}