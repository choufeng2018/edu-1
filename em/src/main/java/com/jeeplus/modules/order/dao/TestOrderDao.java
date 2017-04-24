/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.order.entity.TestOrder;

/**
 * 订单DAO接口
 * @author fly
 * @version 2017-03-11
 */
@MyBatisDao
public interface TestOrderDao extends CrudDao<TestOrder> {

	
}