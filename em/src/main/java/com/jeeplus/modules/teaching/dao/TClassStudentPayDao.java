/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.teaching.entity.TClassStudentPay;

/**
 * 缴费DAO接口
 * @author flying
 * @version 2017-01-18
 */
@MyBatisDao
public interface TClassStudentPayDao extends CrudDao<TClassStudentPay> {

	
}