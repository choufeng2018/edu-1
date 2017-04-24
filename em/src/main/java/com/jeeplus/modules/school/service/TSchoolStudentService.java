/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.school.entity.TSchoolStudent;
import com.jeeplus.modules.school.dao.TSchoolStudentDao;

/**
 * 学生信息管理Service
 * @author fly
 * @version 2016-09-12
 */
@Service
@Transactional(readOnly = true)
public class TSchoolStudentService extends CrudService<TSchoolStudentDao, TSchoolStudent> {

	public TSchoolStudent get(String id) {
		return super.get(id);
	}
	
	public List<TSchoolStudent> findList(TSchoolStudent tSchoolStudent) {
		return super.findList(tSchoolStudent);
	}
	
	public Page<TSchoolStudent> findPage(Page<TSchoolStudent> page, TSchoolStudent tSchoolStudent) {
		return super.findPage(page, tSchoolStudent);
	}
	
	@Transactional(readOnly = false)
	public void save(TSchoolStudent tSchoolStudent) {
		super.save(tSchoolStudent);
	}
	
	@Transactional(readOnly = false)
	public void delete(TSchoolStudent tSchoolStudent) {
		super.delete(tSchoolStudent);
	}
	 
	public void updateStatus(TSchoolStudent tSchoolStudent) {
		dao.updateStatus(tSchoolStudent);
	}
	
	
}