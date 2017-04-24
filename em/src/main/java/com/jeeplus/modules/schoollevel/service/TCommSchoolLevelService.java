/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.schoollevel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.schoollevel.entity.TCommSchoolLevel;
import com.jeeplus.modules.schoollevel.dao.TCommSchoolLevelDao;

/**
 * 租户级别Service
 * @author fly
 * @version 2016-09-06
 */
@Service
@Transactional(readOnly = true)
public class TCommSchoolLevelService extends CrudService<TCommSchoolLevelDao, TCommSchoolLevel> {

	public TCommSchoolLevel get(String id) {
		return super.get(id);
	}
	
	public List<TCommSchoolLevel> findList(TCommSchoolLevel tCommSchoolLevel) {
		return super.findList(tCommSchoolLevel);
	}
	
	public Page<TCommSchoolLevel> findPage(Page<TCommSchoolLevel> page, TCommSchoolLevel tCommSchoolLevel) {
		return super.findPage(page, tCommSchoolLevel);
	}
	
	@Transactional(readOnly = false)
	public void save(TCommSchoolLevel tCommSchoolLevel) {
		super.save(tCommSchoolLevel);
	}
	
	@Transactional(readOnly = false)
	public void saveResource(TCommSchoolLevel tCommSchoolLevel) {
		dao.deleteLevelMenu(tCommSchoolLevel);
		if (tCommSchoolLevel.getMenuList().size() > 0){
			dao.saveResource(tCommSchoolLevel);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TCommSchoolLevel tCommSchoolLevel) {
		super.delete(tCommSchoolLevel);
	} 
}