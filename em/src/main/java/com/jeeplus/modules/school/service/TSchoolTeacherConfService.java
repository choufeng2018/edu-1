/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.school.dao.TSchoolTeacherConfDao;
import com.jeeplus.modules.school.entity.TSchoolTeacherConf;
import com.jeeplus.modules.subject.dao.TCourseClassTimeDao;
import com.jeeplus.modules.subject.entity.TCourseClassTime;

/**
 * 教师一对一教学执行时间配置Service
 * @author flying
 * @version 2017-01-15
 */
@Service
@Transactional(readOnly = true)
public class TSchoolTeacherConfService extends CrudService<TSchoolTeacherConfDao, TSchoolTeacherConf> {

	@Autowired
	private TCourseClassTimeDao tCourseClassTimeDao;
	
	public TSchoolTeacherConf get(String id) {
		return super.get(id);
	}
	
	public TSchoolTeacherConf getByTid(TSchoolTeacherConf tSchoolTeacherConf) {
		return dao.getByTid(tSchoolTeacherConf);
	}
	
	public List<TSchoolTeacherConf> findList(TSchoolTeacherConf tSchoolTeacherConf) {
		return super.findList(tSchoolTeacherConf);
	}
	
	public List<TCourseClassTime> findListForTeac(TCourseClassTime tCourseClassTime) {
		return tCourseClassTimeDao.findListForTeac(tCourseClassTime);
	}
	
	public Page<TSchoolTeacherConf> findPage(Page<TSchoolTeacherConf> page, TSchoolTeacherConf tSchoolTeacherConf) {
		return super.findPage(page, tSchoolTeacherConf);
	}
	
	@Transactional(readOnly = false)
	public void save(TSchoolTeacherConf tSchoolTeacherConf) {
		super.save(tSchoolTeacherConf);
	}
	
	@Transactional(readOnly = false)
	public void delete(TSchoolTeacherConf tSchoolTeacherConf) {
		super.delete(tSchoolTeacherConf);
	}
	
	
	
	
}