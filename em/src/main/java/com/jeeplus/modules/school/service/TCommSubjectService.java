/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.school.entity.TCommSubject;
import com.jeeplus.modules.school.dao.TCommSubjectDao;

/**
 * 课程信息Service
 * @author fly
 * @version 2016-09-09
 */
@Service
@Transactional(readOnly = true)
public class TCommSubjectService extends CrudService<TCommSubjectDao, TCommSubject> {

	public TCommSubject get(String id) {
		return super.get(id);
	}
	
	public List<TCommSubject> findList(TCommSubject tCommSubject) {
		return super.findList(tCommSubject);
	}
	
	public Page<TCommSubject> findPage(Page<TCommSubject> page, TCommSubject tCommSubject) {
		return super.findPage(page, tCommSubject);
	}
	
	@Transactional(readOnly = false)
	public void save(TCommSubject tCommSubject) {
		super.save(tCommSubject);
	}
	
	@Transactional(readOnly = false)
	public void delete(TCommSubject tCommSubject) {
		super.delete(tCommSubject);
	}
	
	
	
	
}