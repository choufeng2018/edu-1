/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.modules.subject.dao.TCourseClassDao;
import com.jeeplus.modules.subject.dao.TCourseClassTimeDao;
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.subject.entity.TCourseClassTime;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 课程方案管理Service
 * @author fly
 * @version 2016-09-13
 */
@Service
@Transactional(readOnly = true)
public class TCourseClassService extends CrudService<TCourseClassDao, TCourseClass> {
	@Autowired
	private TCourseTimetableService tCourseTimetableService;
	
	@Autowired
	private TCourseClassTimeDao tCourseClassTimeDao;
	
	public TCourseClass get(String id) {
		TCourseClass tCourseClass = super.get(id);
		tCourseClass.settCourseClassTimeList(tCourseClassTimeDao.findList(new TCourseClassTime(tCourseClass)));
		return tCourseClass;
	}
	
	public List<TCourseClass> findList(TCourseClass tCourseClass) {
		return super.findList(tCourseClass);
	}
	
	public List<TCourseClass> findListByIds(String ids) {
		return dao.findListByIds(ids);
	}
	
	/** 查找可报名班级信息 **/
	public Page<TCourseClass> findListForEnter(Page<TCourseClass> page, TCourseClass tCourseClass) {
		tCourseClass.setPage(page);
		return page.setList(dao.findListForEnter(tCourseClass));
	}
	
	public Page<TCourseClass> findPage(Page<TCourseClass> page, TCourseClass tCourseClass) {
		return super.findPage(page, tCourseClass);
	}
	 
	@Transactional(readOnly = false)
	public void save(TCourseClass tCourseClass) {
		super.save(tCourseClass);
	}
	 
	// 自定义ID 保存
	@Transactional(readOnly = false)
	public TCourseClass saveDefId(TCourseClass tCourseClass, boolean change) {
		if (tCourseClass.getIsNewRecord()){
			tCourseClass.setId(tCourseClass.getSchoolId()+DateUtils.getYear().substring(2, 4)+"1"+getCount(tCourseClass.getSchoolId()));
			User user = UserUtils.getUser();
			if (StringUtils.isNotBlank(user.getId())){
				tCourseClass.setCreateBy(user);
				tCourseClass.setUpdateBy(user);
			}
			tCourseClass.setUpdateDate(new Date());
			tCourseClass.setCreateDate(new Date());
			
			dao.insert(tCourseClass);
			saveDetail(tCourseClass);
			
			tCourseTimetableService.createTimtables(tCourseClass);
		} else {
			tCourseClass.preUpdate();
			User user = UserUtils.getUser();
			if (StringUtils.isNotBlank(user.getId())){
				tCourseClass.setUpdateBy(user);
			}
			tCourseClass.setUpdateDate(new Date());
			dao.update(tCourseClass);
			saveDetail(tCourseClass);
			
			// 修改记录，如果班级开课时间 和 上课时间都没变化，则不修改课程表信息
			if (change)
				tCourseTimetableService.createTimtables(tCourseClass);
		}
		return tCourseClass;
	}
	
	public void saveDetail(TCourseClass tCourseClass)
	{
		for (TCourseClassTime tCourseClassTime : tCourseClass.gettCourseClassTimeList()){
			if (tCourseClassTime.getId() == null){
				continue;
			}
			if (TCourseClassTime.DEL_FLAG_NORMAL.equals(tCourseClassTime.getDelFlag())){
				if (StringUtils.isBlank(tCourseClassTime.getId())){
					tCourseClassTime.setCsId(tCourseClass);
					tCourseClassTime.preInsert();
					tCourseClassTimeDao.insert(tCourseClassTime);
				}else{
					tCourseClassTime.preUpdate();
					tCourseClassTimeDao.update(tCourseClassTime);
				}
			}else{
				tCourseClassTimeDao.delete(tCourseClassTime);
			}
		}
	}
	
	public String getTeactime(TCourseClassTime tCourseClassTime)
	{
			String returnValue = "";
			if (tCourseClassTime.getWeek() == 1)
				returnValue = "周一";
			else if (tCourseClassTime.getWeek() == 2)
				returnValue = "周二";
			else if (tCourseClassTime.getWeek() == 3)
				returnValue = "周三";
			else if (tCourseClassTime.getWeek() == 4)
				returnValue = "周四";
			else if (tCourseClassTime.getWeek() == 5)
				returnValue = "周五";
			else if (tCourseClassTime.getWeek() == 6)
				returnValue = "周六";
			else if (tCourseClassTime.getWeek() == 7)
				returnValue = "周日"; 
			
			return returnValue + "(" + tCourseClassTime.getShour() + ":" + tCourseClassTime.getSmin() + " — "  + tCourseClassTime.getEhour() + ":" + tCourseClassTime.getEmin() + ")";
	 
	}
	
	public String getCount(String schoolId)
	{
		TCourseClass tcc1 = new TCourseClass();
		tcc1.setClassDesc(DateUtils.getYear());
		tcc1.setSchoolId(schoolId);
		tcc1 = dao.getCount(tcc1);
		if ((tcc1.getTotalStu()+1) < 10)
			return "00" + (tcc1.getTotalStu()+1);
		else if ((tcc1.getTotalStu()+1)<100)
			return "0" + (tcc1.getTotalStu()+1);
		else 
			return "" + tcc1.getTotalStu();
	}
	
	@Transactional(readOnly = false)
	public void delete(TCourseClass tCourseClass) {
		super.delete(tCourseClass);
		
		if (tCourseClass.getIscreate() !=null && tCourseClass.getIscreate() == 1)
			tCourseTimetableService.deleteByClassid(tCourseClass.getId());	
	}
	
	@Transactional(readOnly = false)
	public void updateIsCreate(TCourseClass tCourseClass) {
		dao.updateIsCreate(tCourseClass);
		
		tCourseTimetableService.createTimtables(tCourseClass);
	}
	
	@Transactional(readOnly = false)
	public void updateStatus(TCourseClass tCourseClass) {
		dao.updateStatus(tCourseClass);
	}
	
	@Transactional(readOnly = false)
	public void updateStuNum(TCourseClass tCourseClass) {
		dao.updateStuNum(tCourseClass);
	}
}