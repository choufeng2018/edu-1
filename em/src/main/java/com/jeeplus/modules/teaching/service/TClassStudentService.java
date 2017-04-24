/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.modules.iim.utils.DateUtil;
import com.jeeplus.modules.school.entity.TSchoolStudent;
import com.jeeplus.modules.school.service.TSchoolStudentService;
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.subject.entity.TCourseClassTime;
import com.jeeplus.modules.subject.service.TCourseClassService;
import com.jeeplus.modules.subject.service.TCourseTimetableService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teaching.entity.TClassStudent;
import com.jeeplus.modules.teaching.dao.TClassStudentDao;

/**
 * 报名管理Service
 * @author fly
 * @version 2016-09-16
 */
@Service
@Transactional(readOnly = true)
public class TClassStudentService extends CrudService<TClassStudentDao, TClassStudent> {
	@Autowired
	private TCourseClassService tCourseClassService;
	
	@Autowired
	private TSchoolStudentService tSchoolStudentService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private TCourseTimetableService tCourseTimetableService;
	
	public TClassStudent get(String id) {
		TClassStudent tClassStudent = super.get(id);
		tClassStudent.setCourseclass(tCourseClassService.get(tClassStudent.getCourseclass().getId()));
		return tClassStudent;
	}
	
	public TClassStudent getByStudentIdAndClassId(TClassStudent tClassStudent) {
		return dao.getByStudentIdAndClassId(tClassStudent);
	}
	
	public List<TClassStudent> findList(TClassStudent tClassStudent) {
		return super.findList(tClassStudent);
	}
	
	public Page<TClassStudent> findPage(Page<TClassStudent> page, TClassStudent tClassStudent) {
		return super.findPage(page, tClassStudent);
	}
	
	public Page<TClassStudent> findPageByStatus(Page<TClassStudent> page, TClassStudent tClassStudent) {
		tClassStudent.setPage(page);
		return page.setList(dao.findListByStatus(tClassStudent));
	}
	
	public Page<TClassStudent> findPageByType(Page<TClassStudent> page, TClassStudent tClassStudent) {
		tClassStudent.setPage(page);
		return page.setList(dao.findListByType(tClassStudent)); 
	}
	
	public Page<TClassStudent> findPageForTran(Page<TClassStudent> page, TClassStudent tClassStudent) {
		tClassStudent.setPage(page);
		return page.setList(dao.findPageForTran(tClassStudent)); 
	}
	
	public Page<TClassStudent> findPageForLeave(Page<TClassStudent> page, TClassStudent tClassStudent) {
		tClassStudent.setPage(page);
		return page.setList(dao.findPageForLeave(tClassStudent)); 
	}
	
	public Page<TClassStudent> findPageForView(Page<TClassStudent> page, TClassStudent tClassStudent) {
		tClassStudent.setPage(page);
		return page.setList(dao.findPageForView(tClassStudent)); 
	}
	
	@Transactional(readOnly = false)
	public void save(TClassStudent tClassStudent, String quiteid, boolean change) {
		// 如果是新报名表
		if (tClassStudent.getIsNewRecord())
		{
			// 大班制报名（更新人数），小班制报名（创建班级，创建课表）
			TCourseClass tcc = null;
			if (tClassStudent.getCourseclass().getClassType()==2) {
				tcc = tCourseClassService.get(tClassStudent.getCourseclass().getId());
				tcc.setStuNum(tcc.getStuNum() + 1);
				tCourseClassService.updateStuNum(tcc);
			} else if (tClassStudent.getCourseclass().getClassType()==1) {
				tcc = tClassStudent.getCourseclass();
				tcc.setId("");
				tcc.setClassCode(DateUtils.getYear()+DateUtils.getMonth()+DateUtils.getDay()+tClassStudent.getStudent().getId());
				//tcc.setClassDesc(tcc.getSubject().getSubjectName() + "1对1教学【" + tcc.getTeacher().getName() + " 教 " + tClassStudent.getStudent().getName() + "】");
				tcc.setClassDesc(tcc.getSubject().getSubjectName() + "1对1");
				tcc.setSchoolId(tClassStudent.getSchoolId());
				tcc.setCampus(officeService.get(tClassStudent.getCampusId()));
				tcc.setTotalStu(1);
				tcc.setStuNum(1);
				tcc.setClassType(1);	// 小班制
				tcc.setStatus(2); 		// 状态：开始招生
				tcc.setIscreate(1);
				tcc.setClassHour(tClassStudent.getZks()); // 课次
				tcc.setCost(tClassStudent.getAmount());
				tcc.setChargeType(2);
				tcc = tCourseClassService.saveDefId(tcc, true);
				// 同时创建课表
				//tCourseTimetableService.createTimtables(tcc);
			}
			tClassStudent.setCourseclass(tcc);
		} else {
			TCourseClass tcc = tCourseClassService.get(tClassStudent.getCourseclass().getId());
			if (tcc.getClassType()==1)
			{
				tcc.setClassDesc(tcc.getSubject().getSubjectName() + "1对1教学【" + tcc.getTeacher().getName() + " 教 " + tClassStudent.getStudent().getName() + "】");
				tcc.setClassHour(tClassStudent.getZks()); // 课次
				tcc.setCost(tClassStudent.getAmount());
				tcc = tCourseClassService.saveDefId(tcc, change);
			}
		}
		
		// 如果是转班则更新原报名表状态为 中途转班，更新原班级相关状态信息
		if (quiteid != null && !"".equals(quiteid))
		{ 
			TClassStudent ts = get(quiteid);
			ts.setStatus(4); // 中途转班
			super.save(ts);
			tClassStudent.setParent(ts);
			
			// 如果原班级是大班，则要班级人数减1; 如果是小班，则更新状态为已完成
			TCourseClass tcTemp = tCourseClassService.get(ts.getCourseclass().getId());
			if (tcTemp.getClassType()==2 && tcTemp.getStuNum() > 0) {
				tcTemp.setStuNum(tcTemp.getStuNum() - 1);
				tCourseClassService.updateStuNum(tcTemp);
			} else if (tcTemp.getClassType()==1) {
				tcTemp.setStatus(4); 
				tCourseClassService.delete(tcTemp);
			} 
		} else if (tClassStudent.getIsNewRecord()){
			// 非转班报名，新增报名表时需更新学生状态为 在册 （不管之前是等级、毕业、退学）
			TSchoolStudent tTSchoolStudent = tSchoolStudentService.get(tClassStudent.getStudent().getId());
			if (tTSchoolStudent.getStatus() != 2) {
				tTSchoolStudent.setStatus(2);
				tSchoolStudentService.updateStatus(tTSchoolStudent);
			}
		}
		
		// 保存新报名表
		super.save(tClassStudent);
	}
	
	@Transactional(readOnly = false)
	public void updateStatus(TClassStudent tClassStudent) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			tClassStudent.setUpdateBy(user);
		}
		tClassStudent.setUpdateDate(new Date());
		dao.updateStatus(tClassStudent);
		
		// 取消报名：2 大班制 ：减少报名学生；1  小班制 ：取消班级
		if (tClassStudent.getStatus()==2)
		{
			// 更新已报名学生数增加1
			TCourseClass tcTemp = tCourseClassService.get(tClassStudent.getCourseclass().getId());
			if (tcTemp.getClassType() == 1) {
				tcTemp.setStatus(5);
				tCourseClassService.updateStatus(tcTemp);
			} else if (tcTemp.getClassType() == 2) {
				if (tcTemp.getStuNum() > 0) {
					tcTemp.setStuNum(tcTemp.getStuNum() - 1);
					tCourseClassService.updateStuNum(tcTemp);
				}
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void updateRenewCount(TClassStudent tClassStudent)
	{
		dao.updateRenewCount(tClassStudent);
	}
	
	@Transactional(readOnly = false)
	public void updateYwc(TClassStudent tClassStudent) {
		dao.updateYwc(tClassStudent);
	}
	
	@Transactional(readOnly = false)
	public void delete(TClassStudent tClassStudent) {
		super.delete(tClassStudent);
		
		// 取消报名：2 大班制 ：减少报名学生；1  小班制 ：逻辑删除班级
		if (tClassStudent.getDelFlag().equals("1"))
		{
			// 更新已报名学生数增加1
			TCourseClass tcTemp = tCourseClassService.get(tClassStudent.getCourseclass().getId());
			if (tcTemp.getClassType() == 1) {
				tCourseClassService.delete(tcTemp);
			} else if (tcTemp.getClassType() == 2) {
				if (tcTemp.getStuNum() > 0) {
					tcTemp.setStuNum(tcTemp.getStuNum() - 1);
					tCourseClassService.updateStuNum(tcTemp);
				}
			}
		}
	
	}
	
	@Transactional(readOnly = false)
	public void pause(TClassStudent tClassStudent) {
		dao.updateStatus(tClassStudent);
	} 
}