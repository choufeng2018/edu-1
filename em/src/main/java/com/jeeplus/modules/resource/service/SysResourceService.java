/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.resource.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.resource.entity.SysResource;
import com.jeeplus.modules.resource.dao.SysResourceDao;

/**
 * 系统资源Service
 * @author fly
 * @version 2016-08-29
 */
@Service
@Transactional(readOnly = true)
public class SysResourceService extends CrudService<SysResourceDao, SysResource> {

	public SysResource get(String id) {
		return super.get(id);
	}
	
	@Transactional(readOnly = false)
	public SysResource getByMenuid(String menuid) {
		return dao.getByMenuid(menuid);
	}
	
	public List<SysResource> findList(SysResource sysResource) {
		return super.findList(sysResource);
	}
	
	public List<SysResource> findListBySchoolCode(String schoolcode) {
		return dao.findListBySchoolCode(schoolcode);
	}
	
	public Page<SysResource> findPage(Page<SysResource> page, SysResource sysResource) {
		return super.findPage(page, sysResource);
	}
	
	@Transactional(readOnly = false)
	public void save(SysResource sysResource) {
		super.save(sysResource);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysResource sysResource) {
		super.delete(sysResource);
	}
	
	@Transactional(readOnly = false)
	public int deleteByLogic(SysResource sysResource) {
		return dao.deleteByLogic(sysResource);
	}
	
	
}