/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.school.entity.TSchoolTeacher;
import com.jeeplus.modules.school.dao.TSchoolTeacherDao;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 教师信息管理Service
 * @author fly
 * @version 2016-09-12
 */
@Service
@Transactional(readOnly = true)
public class TSchoolTeacherService extends CrudService<TSchoolTeacherDao, TSchoolTeacher> {

	public TSchoolTeacher get(String id) {
		return super.get(id);
	}
	
	public List<TSchoolTeacher> findList(TSchoolTeacher tSchoolTeacher) {
		return super.findList(tSchoolTeacher);
	}
	
	public Page<TSchoolTeacher> findPage(Page<TSchoolTeacher> page, TSchoolTeacher tSchoolTeacher) {
		return super.findPage(page, tSchoolTeacher);
	}
	
	@Transactional(readOnly = false)
	public void save(TSchoolTeacher tSchoolTeacher) {
		super.save(tSchoolTeacher);
	}
	
	@Transactional(readOnly = false)
	public void delete(TSchoolTeacher tSchoolTeacher) {
		super.delete(tSchoolTeacher);
	}
	
	/**
	 * 根据tel获取用户
	 * @param tel
	 * @return
	 */
	public TSchoolTeacher getUserByTel(TSchoolTeacher tSchoolTeacher) {
		return dao.getUserByTel(tSchoolTeacher);
	}
	
	/**
	 * 根据tel获取用户
	 * @param tel
	 * @return
	 */
	public TSchoolTeacher getUserByAccount(TSchoolTeacher tSchoolTeacher) {
		return dao.getUserByTel(tSchoolTeacher);
	}
}