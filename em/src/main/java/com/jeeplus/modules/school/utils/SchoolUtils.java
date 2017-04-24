/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.utils;

import java.util.List;

import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.school.dao.TCommSubjectFieldDao;
import com.jeeplus.modules.school.entity.TCommSubjectField;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 用户工具类
 * @author jeeplus
 * @version 2013-12-05
 */
public class SchoolUtils {

	private static TCommSubjectFieldDao tCommSubjectFieldDao = SpringContextHolder.getBean(TCommSubjectFieldDao.class);
	 
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static List<TCommSubjectField> getByCampusId(){
		TCommSubjectField tCommSubjectField = new TCommSubjectField();
		tCommSubjectField.setSchoolCode(UserUtils.getUser().getSchool().getSchoolCode());
		if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
			tCommSubjectField.setCampus(UserUtils.getUser().getCompany());
		List<TCommSubjectField> list =tCommSubjectFieldDao.findAllList(tCommSubjectField);
		
		return list;
	}
	 
}
