package com.jeeplus.modules.em.service.school;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.em.dao.school.TCommSchoolDao;
import com.jeeplus.modules.em.entity.school.TCommSchool;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;

/**
 * 学校租户Service
 * @author fly
 * @version 2016-08-28
 */
@Service
@Transactional(readOnly = true)
public class TCommSchoolService extends CrudService<TCommSchoolDao, TCommSchool> {
	
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SystemService systemService;
	
	public TCommSchool get(String id) {
		return super.get(id);
	}
	
	public List<TCommSchool> findList(TCommSchool tCommSchool) {
		return super.findList(tCommSchool);
	}
	
	public TCommSchool getCount(TCommSchool tCommSchool) {
		return dao.getCount(tCommSchool);
	}
	
	public Page<TCommSchool> findPage(Page<TCommSchool> page, TCommSchool tCommSchool) {
		return super.findPage(page, tCommSchool);
	}
	
	@Transactional(readOnly = false)
	public void save(TCommSchool tCommSchool) {
		boolean tag = "".equals(tCommSchool.getId());
		super.save(tCommSchool);
		
		if (tag)
		{
			// 新增公司
			Office office = new Office(), parent = new Office();
			office.setName(tCommSchool.getSchoolName());
			office.setSchoolCode(tCommSchool.getSchoolCode());
			office.setType("1");
			office.setGrade("1");
			parent.setId("0");
			office.setParent(parent);
			office.setParentIds("0,");
			office.setSort(10);
			office.setUseable("1");
			office.setCode("001");
			office.setPhone(tCommSchool.getTel());
			office.setAddress(tCommSchool.getAddress());
			office.setArea(tCommSchool.getProvince());
			officeService.save(office);
			
			// 新增总部校区
			Office office1 = new Office();
			office1.setName("总部校区");
			office1.setSchoolCode(tCommSchool.getSchoolCode());
			office1.setType("1");
			office1.setGrade("1");
			office1.setParent(office);
			office1.setParentIds("0,"+office.getId()+",");
			office1.setSort(11);
			office1.setUseable("1");
			office1.setCode("001001");
			office1.setPhone(tCommSchool.getTel());
			office1.setAddress(tCommSchool.getAddress());
			office1.setArea(tCommSchool.getProvince());
			officeService.save(office1);
			 
			// 新增管理员账号
			User user = new User();
			user.setLoginName(tCommSchool.getTel());
			user.setMobile(tCommSchool.getTel());
			user.setPassword(SystemService.entryptPassword(tCommSchool.getTel()));
			user.setName("管理员");
			user.setNo("000");
			user.setCompany(office);
			user.setSchool(tCommSchool);
			user.setLoginFlag("1");
			user.setIsSysAdmin("1");// 超级管理员
			systemService.saveUserAsSchool(user);
			
			// 预分配角色信息
			List<Role> list = systemService.findTempRole(new Role());
			for (int i=0; i<list.size(); i++)
			{
				Role roleT = systemService.getTempRole(list.get(i).getId());
				roleT.setId("");
				roleT.setRoleType("security-role");
				roleT.setSchoolCode(tCommSchool.getSchoolCode());
				roleT.setDataScope("1");
				roleT.setSysData("1");
				roleT.setUseable("1");
				roleT.setCreateBy(user);
				systemService.saveRole(roleT);
			}
//			
//			// 新增角色组（管理员）
//			Role role = new Role();
//			role.setEnname("system");
//			role.setRoleType("security-role");
//			role.setSchoolCode(tCommSchool.getSchoolCode());
//			role.setName("总部系统管理员");
//			role.setDataScope("1");
//			role.setSysData("1");
//			role.setUseable("1");
//			role.setCreateBy(user);
//			systemService.saveRole(role);
//			
//			Role role1 = new Role();
//			role1.setEnname("JXZL");
//			role1.setRoleType("security-role");
//			role1.setSchoolCode(tCommSchool.getSchoolCode());
//			role1.setName("教学助理");
//			role1.setDataScope("1");
//			role1.setSysData("1");
//			role1.setUseable("1");
//			role1.setCreateBy(user);
//			systemService.saveRole(role1);
//			
//			Role role2 = new Role();
//			role2.setEnname("FXGLY");
//			role2.setRoleType("security-role");
//			role2.setSchoolCode(tCommSchool.getSchoolCode());
//			role2.setName("分校管理员");
//			role2.setDataScope("1");
//			role2.setSysData("1");
//			role2.setUseable("1");
//			role2.setCreateBy(user);
//			systemService.saveRole(role2);
//			
//			Role role3 = new Role();
//			role3.setEnname("GLY");
//			role3.setRoleType("security-role");
//			role3.setSchoolCode(tCommSchool.getSchoolCode());
//			role3.setName("分校管理员");
//			role3.setDataScope("1");
//			role3.setSysData("1");
//			role3.setUseable("1");
//			role3.setCreateBy(user);
//			systemService.saveRole(role3);
			
			// 初始化资源
			// 角色资源分配
			// 角色人员添加
		}
	}
	
	@Transactional(readOnly = false)
	public void updateSchoolEndDate(TCommSchool tCommSchool, Date enddate) {
		tCommSchool.setEndDate(enddate);
		dao.updateSchoolEndDate(tCommSchool);
	}
	
	@Transactional(readOnly = false)
	public void delete(TCommSchool tCommSchool) {
		super.delete(tCommSchool);
	}
	
	@Transactional(readOnly = false)
	public int enableAndDisable(TCommSchool tCommSchool) {
		return dao.enableAndDisable(tCommSchool);
	}
	
	
}