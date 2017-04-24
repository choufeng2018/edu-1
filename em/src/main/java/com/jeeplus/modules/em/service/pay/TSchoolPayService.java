/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.em.service.pay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.em.entity.pay.TSchoolPay;
import com.jeeplus.modules.em.service.school.TCommSchoolService;
import com.jeeplus.modules.em.dao.pay.TSchoolPayDao;

/**
 * 会员支付Service
 * @author fly
 * @version 2016-09-08
 */
@Service
@Transactional(readOnly = true)
public class TSchoolPayService extends CrudService<TSchoolPayDao, TSchoolPay> {
	@Autowired
	private TCommSchoolService tCommSchoolService;
	
	public TSchoolPay get(String id) {
		return super.get(id);
	}
	
	public List<TSchoolPay> findList(TSchoolPay tSchoolPay) {
		return super.findList(tSchoolPay);
	}
	
	public Page<TSchoolPay> findPage(Page<TSchoolPay> page, TSchoolPay tSchoolPay) {
		return super.findPage(page, tSchoolPay);
	}
	
	@Transactional(readOnly = false)
	public void save(TSchoolPay tSchoolPay) {
		super.save(tSchoolPay);
		
		tCommSchoolService.updateSchoolEndDate(tSchoolPay.getSchool(), tSchoolPay.getEndDate());
	}
	
	@Transactional(readOnly = false)
	public void delete(TSchoolPay tSchoolPay) {
		super.delete(tSchoolPay);
	}
	
	
	
	
}