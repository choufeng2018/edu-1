/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.order.entity.TestOrder;
import com.jeeplus.modules.order.dao.TestOrderDao;
import com.jeeplus.modules.order.entity.TestOrderDetail;
import com.jeeplus.modules.order.dao.TestOrderDetailDao;

/**
 * 订单Service
 * @author fly
 * @version 2017-03-11
 */
@Service
@Transactional(readOnly = true)
public class TestOrderService extends CrudService<TestOrderDao, TestOrder> {

	@Autowired
	private TestOrderDetailDao testOrderDetailDao;
	
	public TestOrder get(String id) {
		TestOrder testOrder = super.get(id);
		testOrder.setTestOrderDetailList(testOrderDetailDao.findList(new TestOrderDetail(testOrder)));
		return testOrder;
	}
	
	public List<TestOrder> findList(TestOrder testOrder) {
		return super.findList(testOrder);
	}
	
	public Page<TestOrder> findPage(Page<TestOrder> page, TestOrder testOrder) {
		return super.findPage(page, testOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(TestOrder testOrder) {
		super.save(testOrder);
		for (TestOrderDetail testOrderDetail : testOrder.getTestOrderDetailList()){
			if (testOrderDetail.getId() == null){
				continue;
			}
			if (TestOrderDetail.DEL_FLAG_NORMAL.equals(testOrderDetail.getDelFlag())){
				if (StringUtils.isBlank(testOrderDetail.getId())){
					testOrderDetail.setOrder(testOrder);
					testOrderDetail.preInsert();
					testOrderDetailDao.insert(testOrderDetail);
				}else{
					testOrderDetail.preUpdate();
					testOrderDetailDao.update(testOrderDetail);
				}
			}else{
				testOrderDetailDao.delete(testOrderDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TestOrder testOrder) {
		super.delete(testOrder);
		testOrderDetailDao.delete(new TestOrderDetail(testOrder));
	}
	
}