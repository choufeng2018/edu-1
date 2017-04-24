/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.iim.utils.DateUtil;
import com.jeeplus.modules.subject.dao.TCourseTimetableDao;
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.subject.entity.TCourseClassTime;
import com.jeeplus.modules.subject.entity.TCourseTimetable;
import com.jeeplus.modules.teaching.dao.TClassStudentCheckDao;
import com.jeeplus.modules.teaching.entity.TClassStudent;
import com.jeeplus.modules.teaching.entity.TClassStudentCheck;
import com.jeeplus.modules.teaching.service.TClassStudentService;

/**
 * 课程表Service
 * @author fly
 * @version 2016-09-15
 */
@Service
@Transactional(readOnly = true)
public class TCourseTimetableService extends CrudService<TCourseTimetableDao, TCourseTimetable> {
	@Autowired
	private TClassStudentCheckDao tClassStudentCheckDao;
	 
	public TCourseTimetable get(String id) {
		return super.get(id);
	}
	
	public List<TCourseTimetable> findList(TCourseTimetable tCourseTimetable) {
		return super.findList(tCourseTimetable);
	}
	
	public Page<TCourseTimetable> findPage(Page<TCourseTimetable> page, TCourseTimetable tCourseTimetable) {
		return super.findPage(page, tCourseTimetable);
	}
	
	// 补课
	public Page<TCourseTimetable> findPageForMissed(Page<TCourseTimetable> page, TCourseTimetable tCourseTimetable) {
		tCourseTimetable.setPage(page);
		page.setList(dao.findPageForMissed(tCourseTimetable));
		return page;
	}
		
	// 考勤
	public Page<TCourseTimetable> findPageForCheck(Page<TCourseTimetable> page, TCourseTimetable tCourseTimetable) {
		tCourseTimetable.setPage(page);
		page.setList(dao.findPageForCheck(tCourseTimetable));
		return page;
	}
	
	// 考勤
	public Page<TCourseTimetable> findPageForPay(Page<TCourseTimetable> page, TCourseTimetable tCourseTimetable) {
		tCourseTimetable.setPage(page);
		page.setList(dao.findPageForCheck(tCourseTimetable));
		return page;
	}
	 
	@Transactional(readOnly = false)
	public void save(TCourseTimetable tCourseTimetable) {
		super.save(tCourseTimetable);
	}
	
	@Transactional(readOnly = false)
	public void updateRoom(TCourseTimetable tCourseTimetable) {
		dao.updateRoom(tCourseTimetable);
	}
	
	@Transactional(readOnly = false)
	public void updateTime(TCourseTimetable tCourseTimetable) {
		dao.updateTime(tCourseTimetable);
	}
	
	@Transactional(readOnly = false)
	public void updatedelayTime(TCourseTimetable tCourseTimetable) {
		dao.updatedelayTime(tCourseTimetable);
	}
	
	@Transactional(readOnly = false)
	public void updateTeacher(TCourseTimetable tCourseTimetable) {
		dao.updateTeacher(tCourseTimetable);
	}
	
	@Transactional(readOnly = false)
	public void check(TCourseTimetable tCourseTimetable, TCourseClassService tCourseClassService, TClassStudentService tClassStudentService) {
		// 保存考勤数据
		String ywcStr = "";
		for (TClassStudentCheck tClassStudentCheck : tCourseTimetable.gettClassStudentCheckList()) {
//			if (tClassStudentCheck.getId() == null){
//				continue;
//			}
			if (StringUtils.isBlank(tClassStudentCheck.getId())){
				tClassStudentCheck.settCourseTimetable(tCourseTimetable);
				tClassStudentCheck.preInsert();
				tClassStudentCheckDao.insert(tClassStudentCheck);
				if (tClassStudentCheck.getStatus() == 1)
					ywcStr += tClassStudentCheck.getStudentId() + ",";
			} else {
				tClassStudentCheck.preUpdate();
				tClassStudentCheckDao.update(tClassStudentCheck);
			}
		}
		// 更新课程表中记录的完成状态，考勤完成代表本堂课完成
		if (tCourseTimetable.getStatus() != 2)
		{
			tCourseTimetable.setStatus(2);
			dao.updateStatus(tCourseTimetable);
		}
		
		// 更新学生已完成课程数
		if (!"".equals(ywcStr))
		{
			TClassStudent tClassStudent = new TClassStudent();
			tClassStudent.getSqlMap().put("dsf", " a.student_id in ("+ywcStr.substring(0, ywcStr.lastIndexOf(","))+")");
			tClassStudentService.updateYwc(tClassStudent);
		}
		
		// 更新该课程状态 （3：已开课   4：已完成）
		TCourseClass tcourseclass = tCourseClassService.get(tCourseTimetable.getCourseclass().getId());
		if (tcourseclass.getStatus() == 2)
		{
			tcourseclass.setStatus(3);
			tCourseClassService.updateStatus(tcourseclass);
		} else if (tcourseclass.getStatus() == 3) {
			tCourseTimetable.setStatus(2);
			List<TCourseTimetable> list = dao.findListIsComplete(tCourseTimetable);
			if (list.size() == tcourseclass.getClassHour())
			{
				tcourseclass.setStatus(4);
				tCourseClassService.updateStatus(tcourseclass);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TCourseTimetable tCourseTimetable) {
		super.delete(tCourseTimetable);
	}
	
	@Transactional(readOnly = false)
	public void deleteByClassid(String classid) {
		dao.deleteByClassid(classid);
	}
	
	/** 创建课表，供排课时调用，不单独起事务控制 **/
	public void createTimtables(TCourseClass tCourseClass) {
		// 创建课表（先删除已有）
		if ((tCourseClass.getClassType()==2 && tCourseClass.getStatus()==1) || (tCourseClass.getClassType()==1 && tCourseClass.getStatus()==2))
		{
			dao.deleteByClassid(tCourseClass.getId());
			if (tCourseClass.getIscreate() == 1 && tCourseClass.gettCourseClassTimeList().size()>0) {
				List<Date> templist = getWeekAfterDate(tCourseClass.getBeginDate(), tCourseClass.gettCourseClassTimeList(), tCourseClass.getClassHour()); 
				for (int i=0; i<templist.size(); i++)
				{
					TCourseTimetable tCourseTimetable = new TCourseTimetable();
					tCourseTimetable.setSchoolId(tCourseClass.getSchoolId());
					tCourseTimetable.setCampusId(tCourseClass.getCampus().getId());
					tCourseTimetable.setSubject(tCourseClass.getSubject());
					tCourseTimetable.setCourseclass(tCourseClass);
					
					tCourseTimetable.setCourseDate(templist.get(i));
					tCourseTimetable.setBeginTime(templist.get(i));
					tCourseTimetable.setEndTime(DateUtils.parseDate(DateUtil.long2string(DateUtil.string2long(DateUtils.formatDateTime(templist.get(i)))+tCourseClass.getClassMin()*60)));
					
					tCourseTimetable.setRoom(tCourseClass.getRoom());
					tCourseTimetable.setType(1);
					tCourseTimetable.setStatus(1);
					tCourseTimetable.setTeacher(tCourseClass.getTeacher());
					this.save(tCourseTimetable);
				} 
			}
		}
	}
	
	/** 创建课表，供续费调用，不单独起事务控制 **/
	public void createTimtablesForPay(Page<TCourseTimetable> page, TCourseClass tCourseClass, int zks) {
		TCourseTimetable tCourseTimetable = new TCourseTimetable();
		tCourseTimetable.setCourseclass(tCourseClass);
		tCourseTimetable.setPage(page);
		List<TCourseTimetable> list = dao.findListforpay(tCourseTimetable);
		
		int ind = list.size() - 1;
		for (int i=0; i<zks; i++)
		{
			if (ind<0) 
				ind += list.size();
			TCourseTimetable tCT = list.get(ind);
			long starttime = DateUtil.string2long(DateUtils.formatDateTime(tCT.getCourseDate()));
			tCT.setCourseDate(DateUtils.parseDate(DateUtil.long2string( starttime+7*24*60*60) ));
			tCT.setBeginTime(DateUtils.parseDate(DateUtils.formatDate(tCT.getCourseDate(), "yyyy-MM-dd") + " " + tCT.getShour() + ":" + tCT.getSmin() + ":00"));
			tCT.setEndTime(DateUtils.parseDate(DateUtils.formatDate(tCT.getCourseDate(), "yyyy-MM-dd") + " " + tCT.getEhour() + ":" + tCT.getEmin() + ":00"));
			
			tCT.setType(1);
			tCT.setStatus(1);
			tCT.setId("");
		 
			super.save(tCT);
			
			ind = ind - 1;
		} 
	}

	private static List<Date> getWeekAfterDate(Date inputDate, List<TCourseClassTime> weeklist, int count)
	{
		List<Date> list = Lists.newArrayList();
		
		Calendar cDate = Calendar.getInstance();
		cDate.setFirstDayOfWeek(Calendar.MONDAY);
		cDate.setTime(inputDate);
		 
		// 排序
		List<TCourseClassTime> templist = Lists.newArrayList();
		int indx = 0; // 记录初始数字下标位置
		int b = weeklist.size();
		for (int i=0; i<b; i++) {
			indx = 0;
			for (int j=0; j<weeklist.size(); j++)
			{
				if (weeklist.get(j).getWeek() < weeklist.get(indx).getWeek())
				{
					indx = j;
				}
			}
			templist.add(weeklist.get(indx));
			weeklist.remove(indx);
		}
		
		// 定位开始时间从周几算起
		int tempweek = cDate.get(Calendar.DAY_OF_WEEK)-1;
		tempweek = tempweek==0?7:tempweek;
		indx = 0;
		Date beginDate = null;
		if (templist.get(0).getWeek() - tempweek >= 0 || tempweek == 7) {
			beginDate = DateUtils.addDays(inputDate, templist.get(0).getWeek() - tempweek);
		} else if (templist.size() == 1){
			beginDate = DateUtils.addDays(inputDate, templist.get(0).getWeek() - tempweek + 7);
		} else {
			for (int i=0; i<templist.size()-1; i++) {
				if (tempweek > templist.get(i).getWeek() && tempweek <= templist.get(i+1).getWeek())
				{
					indx = i+1;
					beginDate = DateUtils.addDays(inputDate, templist.get(indx).getWeek() - tempweek);
					break;
				}
			}
		}
		
		int k=1, d_value = 0, cou = templist.size(), next_ind = 0, last_ind = indx;
		try {
			list.add(DateUtils.parseDate(DateUtils.formatDate(beginDate, "yyyy-MM-dd") + " " + templist.get(indx).getShour() + ":" + templist.get(indx).getSmin() + ":00", "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		while (k<count) {
			next_ind = (indx+k) % cou;
			d_value = templist.get(next_ind).getWeek() - templist.get(last_ind).getWeek();
			if (d_value<=0) d_value += 7;
			last_ind = next_ind;
			beginDate = DateUtils.addDays(beginDate, d_value);
			try {
				list.add(DateUtils.parseDate(DateUtils.formatDate(beginDate, "yyyy-MM-dd") + " " + templist.get(next_ind).getShour() + ":" + templist.get(next_ind).getSmin() + ":00", "yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			k++;
		}
		
		return list;
	}
}