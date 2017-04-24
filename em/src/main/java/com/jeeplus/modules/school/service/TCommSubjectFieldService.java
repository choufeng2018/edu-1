/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.school.entity.TCommSubjectField;
import com.jeeplus.modules.school.dao.TCommSubjectFieldDao;

/**
 * 学科分类Service
 * @author fly
 * @version 2016-09-09
 */
@Service
@Transactional(readOnly = true)
public class TCommSubjectFieldService extends CrudService<TCommSubjectFieldDao, TCommSubjectField> {

	public TCommSubjectField get(String id) {
		return super.get(id);
	}
	
	public List<TCommSubjectField> findList(TCommSubjectField tCommSubjectField) {
		return super.findList(tCommSubjectField);
	}
	
	public Page<TCommSubjectField> findPage(Page<TCommSubjectField> page, TCommSubjectField tCommSubjectField) {
		return super.findPage(page, tCommSubjectField);
	}
	
	@Transactional(readOnly = false)
	public void save(TCommSubjectField tCommSubjectField) {
		super.save(tCommSubjectField);
	}
	
	@Transactional(readOnly = false)
	public void delete(TCommSubjectField tCommSubjectField) {
		super.delete(tCommSubjectField);
	}
	
	
	
	
}