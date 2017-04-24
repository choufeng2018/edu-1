/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.school.dao.TSchoolRoomDao;
import com.jeeplus.modules.school.entity.TSchoolRoom;

/**
 * 教室信息管理Service
 * @author fly
 * @version 2016-09-09
 */
@Service
@Transactional(readOnly = true)
public class TSchoolRoomService extends CrudService<TSchoolRoomDao, TSchoolRoom> {

	public TSchoolRoom get(String id) {
		return super.get(id);
	}
	
	public List<TSchoolRoom> findList(TSchoolRoom tSchoolRoom) {
		return super.findList(tSchoolRoom);
	}
	
	public Page<TSchoolRoom> findPage(Page<TSchoolRoom> page, TSchoolRoom tSchoolRoom) {
		return super.findPage(page, tSchoolRoom);
	}
	 
	@Transactional(readOnly = false)
	public void save(TSchoolRoom tSchoolRoom) {
		super.save(tSchoolRoom);
	}
	
	@Transactional(readOnly = false)
	public void delete(TSchoolRoom tSchoolRoom) {
		super.delete(tSchoolRoom);
	}
	
	@Transactional(readOnly = false)
	public void updatestatus(TSchoolRoom tSchoolRoom) {
		dao.updatestatus(tSchoolRoom);
	}
	
	
}