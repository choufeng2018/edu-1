/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.subject.entity.TCourseTimetable;
import com.jeeplus.modules.subject.service.TCourseClassService;
import com.jeeplus.modules.subject.service.TCourseTimetableService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teaching.dao.TClassStudentPayDao;
import com.jeeplus.modules.teaching.entity.TClassStudent;
import com.jeeplus.modules.teaching.entity.TClassStudentPay;

/**
 * 缴费Service
 * @author flying
 * @version 2017-01-18
 */
@Service
@Transactional(readOnly = true)
public class TClassStudentPayService extends CrudService<TClassStudentPayDao, TClassStudentPay> {
	@Autowired
	private TClassStudentService tClassStudentService;
	
	@Autowired
	private TCourseClassService tCourseClassService;
	
	@Autowired
	private TCourseTimetableService tCourseTimetableService;
	
	public TClassStudentPay get(String id) {
		return super.get(id);
	}
	
	public List<TClassStudentPay> findList(TClassStudentPay tClassStudentPay) {
		return super.findList(tClassStudentPay);
	}
	
	public Page<TClassStudentPay> findPage(Page<TClassStudentPay> page, TClassStudentPay tClassStudentPay) {
		return super.findPage(page, tClassStudentPay);
	}
	
	@Transactional(readOnly = false)
	public void save(TClassStudentPay tClassStudentPay, HttpServletRequest request, HttpServletResponse response) {
		TClassStudent tcs = tClassStudentService.get(tClassStudentPay.getCs().getId());
		tClassStudentPay.setCampusId(tcs.getCampusId());
		tClassStudentPay.setSchoolId(tcs.getSchoolId());
		tClassStudentPay.setStudentId(Integer.parseInt(tcs.getStudent().getId()));
		tClassStudentPay.setCc(UserUtils.getUser());
		
		super.save(tClassStudentPay);
		
		//更新报名表信息
		tcs.setAmount(tcs.getAmount() + tClassStudentPay.getAmount());
		tcs.setZks(tcs.getZks() + tClassStudentPay.getZks());
		tcs.setBalance(tcs.getBalance() + tClassStudentPay.getAmount());
		tcs.setRenewcount(tcs.getRenewcount() + 1);
		tClassStudentService.save(tcs);
		
		// 更新课程表信息
		TCourseClass cc = tCourseClassService.get(tcs.getCourseclass().getId());
		tCourseTimetableService.createTimtablesForPay(new Page<TCourseTimetable>(request, response, cc.gettCourseClassTimeList().size()), cc, tClassStudentPay.getZks());
	}
	
	@Transactional(readOnly = false)
	public void delete(TClassStudentPay tClassStudentPay) {
		super.delete(tClassStudentPay);
	}
	
	
	
	
}