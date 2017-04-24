/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.school.entity.TSchoolRoom;
import com.jeeplus.modules.school.service.TSchoolRoomService;
import com.jeeplus.modules.subject.dao.TCourseClassTimeDao;
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.subject.entity.TCourseTimetable;
import com.jeeplus.modules.subject.entity.TDayTime;
import com.jeeplus.modules.subject.service.TCourseClassService;
import com.jeeplus.modules.subject.service.TCourseTimetableService;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teaching.entity.TClassStudentCheck;
import com.jeeplus.modules.teaching.service.TClassStudentCheckService;
import com.jeeplus.modules.teaching.service.TClassStudentService;

/**
 * 课程表Controller
 * @author fly
 * @version 2016-09-15
 */
@Controller
@RequestMapping(value = "${adminPath}/subject/tCourseTimetable")
public class TCourseTimetableController extends BaseController {

	@Autowired
	private TCourseTimetableService tCourseTimetableService;
	
	@Autowired
	private TClassStudentCheckService tClassStudentCheckService;
	
	@Autowired
	private TSchoolRoomService tSchoolRoomService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private TCourseClassService tCourseClassService;
	
	@Autowired
	private TClassStudentService tClassStudentService;
	
	@ModelAttribute
	public TCourseTimetable get(@RequestParam(required=false) String id) {
		TCourseTimetable entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tCourseTimetableService.get(id);
		}
		if (entity == null){
			entity = new TCourseTimetable();
		}
		return entity;
	}
	
	/**
	 * 课程表列表页面
	 */
	@RequiresPermissions("subject:tCourseTimetable:list")
	@RequestMapping(value = {"list", ""})
	public String list(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0"))
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (tCourseTimetable.getCampusId()==null || "".equals(tCourseTimetable.getCampusId()))
		{
			if (UserUtils.getUser().getCompany().getParentId().equals("0")) {
				tCourseTimetable.setCampusId("");
				Page<TCourseTimetable> page = new Page(); 
				model.addAttribute("page", page);
				model.addAttribute("message", "请选择校区");
				return "modules/subject/tCourseTimetableList";
			} else {
				tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
				tCourseTimetable.setCampusName(UserUtils.getUser().getCompany().getName());
			}
		}
		if (tCourseTimetable.getCourseDate()==null)
			//tCourseTimetable.setCourseDate(new Date());
			tCourseTimetable.getSqlMap().put("dsf", " AND date(a.course_date) >= '"+ DateUtils.getDate() +"'");
		Page<TCourseTimetable> page = tCourseTimetableService.findPage(new Page<TCourseTimetable>(request, response), tCourseTimetable); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseTimetableList";
	}
	
	/**
	 * 课程表列表页面
	 */
	@RequiresPermissions("subject:tCourseTimetable:list")
	@RequestMapping(value = {"newlist", ""})
	public String newlist(String datestr, TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		TSchoolRoom tSchoolRoom = new TSchoolRoom();
		model.addAttribute("isparent", UserUtils.getUserIsParent());
		
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0")) {
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
			tSchoolRoom.setSchool(UserUtils.getUser().getSchool());
		}
		if (tCourseTimetable.getCampusId()==null || "".equals(tCourseTimetable.getCampusId()))
		{
			if (UserUtils.getUser().getCompany().getParentId().equals("0")) {
				tCourseTimetable.setCampusId("");
				Page<TCourseTimetable> page = new Page(); 
				model.addAttribute("page", page);
				model.addAttribute("message", "请选择校区"); 
				return "modules/subject/tCourseTimetableWeekList";
			} else {
				tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
				tCourseTimetable.setCampusName(UserUtils.getUser().getCompany().getName());
				tSchoolRoom.setCampus(UserUtils.getUser().getCompany());
			}
		} else {
			tCourseTimetable.setCampusName(officeService.get(tCourseTimetable.getCampusId()).getName());
		}
		if (datestr==null || datestr.equals(""))
			tCourseTimetable.setCourseDate(new Date());
		else
			tCourseTimetable.setCourseDate(DateUtils.parseDate(datestr));
		
		// 本周日期
		List<TDayTime> dayList = DateUtils.getWeekList(tCourseTimetable.getCourseDate());
		model.addAttribute("dayList", dayList);
		
		// 上课时间
		List<TDayTime> timeList = DateUtils.getTimeList2();
		 
		// 所有教室
		DateUtils.getWeek();
		List<TSchoolRoom> roomList = tSchoolRoomService.findList(tSchoolRoom);
		model.addAttribute("roomList", roomList);
		
		// 本周所有课程
		//tCourseTimetable.getSqlMap().put("dsf", " AND (date(a.course_date) >= '"+ DateUtils.getBeginOfWeek() +"' and date(a.course_date)<= '"+DateUtils.getEndOfWeek()+"')");
		Page<TCourseTimetable> page = tCourseTimetableService.findPage(new Page<TCourseTimetable>(request, response, -1), tCourseTimetable); 
		List<TCourseTimetable> tableList = page.getList();
		List<List<TCourseTimetable>> tList = new ArrayList<List<TCourseTimetable>>();
		
		for (int i=0; i<timeList.size(); i++)
		{
			List<List<TCourseTimetable>> tempList = new ArrayList<List<TCourseTimetable>>();
			List<TCourseTimetable> temp = new ArrayList<TCourseTimetable>();

			TDayTime tTDayTime = timeList.get(i);
			int size = 0;
			//循环教室
			for (int k=0; k<roomList.size(); k++)
			{
				// 循环课程  结果是按时间升序
				for (int j=0; j<tableList.size(); j++)
				{
					TCourseTimetable ttt = tableList.get(j);
					if (ttt.getRoom().getId().equals(roomList.get(k).getId()))
					{
						int shour = Integer.parseInt(ttt.getShour());
						if (shour >= tTDayTime.getBegin()  && shour <= tTDayTime.getEnd())
							temp.add(ttt);
					}
				} 
				if (temp.size() > size) size = temp.size();
				tempList.add(temp);
				temp = new ArrayList<TCourseTimetable>();
			}
			
			tTDayTime.setTtList(tempList);
			tTDayTime.setSize(size);
		 
		}
		model.addAttribute("timeList", timeList); 
		
		/**
		//循环时间
		for (int i=0; i<timeList.size(); i++)
		{
			List<TCourseTimetable> temp = new ArrayList<TCourseTimetable>();
			TCourseTimetable t1 = null;
			TDayTime tTDayTime = timeList.get(i);
			String timeStr = tTDayTime.getTimeString();
			timeStr = timeStr.substring(0, timeStr.lastIndexOf(":"));
			//循环教室
			for (int k=0; k<roomList.size(); k++)
			{
				// 循环课程
				TCourseTimetable tttt = null;
				for (int j=0; j<tableList.size(); j++)
				{
					TCourseTimetable ttt = tableList.get(j);
					if ((Integer.parseInt(ttt.getShour()) == Integer.parseInt(timeStr)) && (ttt.getRoom().getId().equals(roomList.get(k).getId())))
					{
						tttt = ttt;
						tableList.remove(j);
						break;
					}
				}
				if (tttt==null) tttt = new TCourseTimetable();
				temp.add(tttt);
			}
			tTDayTime.settList(temp);
		}
		model.addAttribute("timeList", timeList); 
		**/
		
		return "modules/subject/tCourseTimetableWeekList";
	}
	
	/**
	 * 课程表列表页面
	 */
	@RequiresPermissions("subject:tCourseTimetable:list")
	@RequestMapping(value = {"selectlist", ""})
	public String selectlist(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0"))
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (tCourseTimetable.getCampusId()==null || "".equals(tCourseTimetable.getCampusId()))
		{
			if (UserUtils.getUser().getCompany().getParentId().equals("0")) {
				model.addAttribute("message", "请选择校区");
				return "modules/subject/tCourseTimetableSelectList";
			} else
				tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		 
		if (tCourseTimetable.getCourseDate()==null)
			tCourseTimetable.getSqlMap().put("dsf", " AND date(a.course_date) >= date_add(date('"+ DateUtils.getDate() +"'), interval -7 day)");
		Page<TCourseTimetable> page = tCourseTimetableService.findPage(new Page<TCourseTimetable>(request, response, 30), tCourseTimetable); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseTimetableSelectList";
	}
	
	/**
	 * 课程表列表页面
	 */
	@RequestMapping(value = {"selectlistformissed", ""})
	public String selectlistformissed(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model, String classid) {
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0"))
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (tCourseTimetable.getCampusId()==null || "".equals(tCourseTimetable.getCampusId()))
		{
			if (UserUtils.getUser().getCompany().getParentId().equals("0")) {
				model.addAttribute("message", "您非小区管理人员，无权操作补课设置！");
				return "modules/subject/tCourseTimetableSelectForMissedList";
			} else
				tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		
		/** 获取待补课的学科类型 **/
		if (classid!=null && !"".equals(classid))
		{
			TCourseClass tTCourseClass = tCourseClassService.get(classid);
			tCourseTimetable.setSubject(tTCourseClass.getSubject());
		}
		
		if (tCourseTimetable.getCourseclass()==null || tCourseTimetable.getCourseclass().getId()==null || tCourseTimetable.getCourseclass().getId().equals(""))
		{
			model.addAttribute("message", "请课程");
			return "modules/subject/tCourseTimetableSelectForMissedList";
		}
		 
		if (tCourseTimetable.getCourseDate()==null)
			tCourseTimetable.getSqlMap().put("dsf", " AND date(a.course_date) >= date_add(date('"+ DateUtils.getDate() +"'), interval -7 day)");
		Page<TCourseTimetable> page = tCourseTimetableService.findPageForMissed(new Page<TCourseTimetable>(request, response, 30), tCourseTimetable); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseTimetableSelectForMissedList";
	}
	
	/**
	 * 课程表列表请假选择页面
	 */
	@RequiresPermissions("subject:tCourseTimetable:list")
	@RequestMapping(value = {"selectforleavelist", ""})
	public String selectforleavelist(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0"))
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (tCourseTimetable.getCampusId()==null || "".equals(tCourseTimetable.getCampusId()))
		{
			if (UserUtils.getUser().getCompany().getParentId().equals("0")) {
				model.addAttribute("message", "请选择校区");
				return "modules/subject/tCourseTimetableSelectList";
			} else
				tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		tCourseTimetable.setStatus(1);
		if (tCourseTimetable.getCourseDate()==null)
			tCourseTimetable.getSqlMap().put("dsf", " AND date(a.course_date) >= date_add(date('"+ DateUtils.getDate() +"'), interval -7 day)");
		
		Page<TCourseTimetable> page = tCourseTimetableService.findPage(new Page<TCourseTimetable>(request, response, -1), tCourseTimetable); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseTimetableSelectForLeaveList";
	}
	
	/**
	 * 课程表列表页面
	 */
	@RequiresPermissions("subject:tCourseTimetable:list")
	@RequestMapping(value = {"listByDay", ""})
	public String listByDay(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0"))
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		// 以下初始化查询时如果是分校管理人员，则默认查询本小区的，如果是总公司人员，默认查询结果为空
		
		if (!UserUtils.getUser().getCompany().getParentId().equals("0")) {
			tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
			
			tCourseTimetable.setCourseDate(DateUtils.parseDate(DateUtils.getDate()));// 默认获取当天所有课程
			List todayList = tCourseTimetableService.findList(tCourseTimetable);
			model.addAttribute("todayList", todayList);
			model.addAttribute("today", DateUtils.getDate());
			
			tCourseTimetable.setCourseDate(DateUtils.parseDate(DateUtils.getYesterday()));// 默认获取昨天所有课程
			List yesterdayList = tCourseTimetableService.findList(tCourseTimetable);
			model.addAttribute("yesterdayList", yesterdayList);
			model.addAttribute("yesterday", DateUtils.getYesterday());
			
			tCourseTimetable.setCourseDate(DateUtils.parseDate(DateUtils.getTomorrow()));// 默认获取前天所有课程
			List tomorrowList = tCourseTimetableService.findList(tCourseTimetable);
			model.addAttribute("tomorrowList", tomorrowList);
			model.addAttribute("tomorrow", DateUtils.getTomorrow());
		} else {
			tCourseTimetable.setCampusId("");
			model.addAttribute("message", "您非校区管理人员，无权进行考勤");
		}
		
		return "modules/subject/tCourseTimetableListByDay";
	}
	
	/**
	 * 课程调整页面
	 */
	@RequiresPermissions("subject:tCourseTimetable:list")
	@RequestMapping(value = {"adjustlist", ""})
	public String adjustlist(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0"))
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (tCourseTimetable.getCampusId()==null || "".equals(tCourseTimetable.getCampusId()))
		{
			if (UserUtils.getUser().getCompany().getParentId().equals("0"))
				tCourseTimetable.setCampusId("");
			else {
				tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
				tCourseTimetable.setCampusName(UserUtils.getUser().getCompany().getName());
			}
		}
		if (tCourseTimetable.getCourseDate()==null)
			tCourseTimetable.getSqlMap().put("dsf", " AND date(a.course_date) >= '"+ DateUtils.getDate() +"'");
		tCourseTimetable.setStatus(1);
		Page<TCourseTimetable> page = tCourseTimetableService.findPage(new Page<TCourseTimetable>(request, response), tCourseTimetable); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseTimetableAdjustList";
	}
	
	/**
	 * 代课管理页面
	 */
	@RequiresPermissions("subject:tCourseTimetable:list")
	@RequestMapping(value = {"substitutelist", ""})
	public String substitutelist(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0"))
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (tCourseTimetable.getCampusId()==null || "".equals(tCourseTimetable.getCampusId()))
		{
			if (UserUtils.getUser().getCompany().getParentId().equals("0"))
				tCourseTimetable.setCampusId("");
			else {
				tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
				tCourseTimetable.setCampusName(UserUtils.getUser().getCompany().getName());
			}
		}
		if (tCourseTimetable.getCourseDate()==null)
			tCourseTimetable.getSqlMap().put("dsf", " AND date(a.course_date) >= '"+ DateUtils.getDate() +"'");
		tCourseTimetable.setStatus(1);
		Page<TCourseTimetable> page = tCourseTimetableService.findPage(new Page<TCourseTimetable>(request, response), tCourseTimetable); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseTimetableSubstituteList";
	}
	 
	/**
	 * 课程表列表页面
	 */
	@RequiresPermissions("subject:tCourseTimetable:list")
	@RequestMapping(value = {"subjectlist", ""})
	public String subjectlist(String ccid, TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		TCourseClass courseclass = new TCourseClass();
		courseclass.setId(ccid);
		tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		tCourseTimetable.setCourseclass(courseclass);
		Page<TCourseTimetable> page = tCourseTimetableService.findPage(new Page<TCourseTimetable>(request, response), tCourseTimetable); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseTimetableSubjectList";
	}
	
	/**
	 * 课程表列表页面
	 */
	@RequiresPermissions("subject:tCourseTimetable:list")
	@RequestMapping(value = {"editlist", ""})
	public String editlist(String ccid, TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		TCourseClass courseclass = new TCourseClass();
		courseclass.setId(ccid);
		tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		tCourseTimetable.setCourseclass(courseclass);
		Page<TCourseTimetable> page = tCourseTimetableService.findPage(new Page<TCourseTimetable>(request, response), tCourseTimetable); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseTimetableEditList";
	}
	 
	/**
	 * 请假列表
	 */
	@RequiresPermissions("subject:tCourseTimetable:checklist")
	@RequestMapping(value = {"leavelist", ""})
	public String leavelist(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0"))
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		// 以下初始化查询时如果是分校管理人员，则默认查询本小区的，如果是总公司人员，默认查询结果为空

		if (tCourseTimetable.getCampusId()==null || "".equals(tCourseTimetable.getCampusId()))
		{
			if (!UserUtils.getUser().getCompany().getParentId().equals("0")){
				tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
				tCourseTimetable.setCampusName(UserUtils.getUser().getCompany().getName());
			} else 
				tCourseTimetable.setCampusId("");
		}
		
		Page<TCourseTimetable> page = tCourseTimetableService.findPage(new Page<TCourseTimetable>(request, response, -1), tCourseTimetable); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseTimetableLeaveList";
	}
	 
	/**
	 * 考勤列表
	 */
	@RequiresPermissions("subject:tCourseTimetable:checklist")
	@RequestMapping(value = {"checklist", ""})
	public String checklist(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0"))
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		// 以下初始化查询时如果是分校管理人员，则默认查询本小区的，如果是总公司人员，默认查询结果为空

		if (tCourseTimetable.getCampusId()==null || "".equals(tCourseTimetable.getCampusId()))
		{
			if (!UserUtils.getUser().getCompany().getParentId().equals("0")){
				tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
				tCourseTimetable.setCampusName(UserUtils.getUser().getCompany().getName());
			} else 
				tCourseTimetable.setCampusId("");
		}
		if (tCourseTimetable.getCourseDate() == null)
		{
			tCourseTimetable.setCourseDate(DateUtils.parseDate(DateUtils.getDate()));// 默认获取当天所有课程
		}
		Page<TCourseTimetable> page = tCourseTimetableService.findPageForCheck(new Page<TCourseTimetable>(request, response), tCourseTimetable); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseTimetableCheckList";
	}
	
	/**
	 * 考勤列表
	 */
	@RequiresPermissions("subject:tCourseTimetable:checklist")
	@RequestMapping(value = {"daychecklist", ""})
	public String daychecklist(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCourseTimetable.getSchoolId()==null || tCourseTimetable.getSchoolId().equals("0"))
			tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		// 以下初始化查询时如果是分校管理人员，则默认查询本小区的，如果是总公司人员，默认查询结果为空

		if (!UserUtils.getUser().getCompany().getParentId().equals("0")) {
			tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
			
			tCourseTimetable.setCourseDate(DateUtils.parseDate(DateUtils.getDate()));// 默认获取当天所有课程
			//List todayList = tCourseTimetableService.findList(tCourseTimetable);
			Page<TCourseTimetable> todayList = tCourseTimetableService.findPageForCheck(new Page<TCourseTimetable>(request, response, -1), tCourseTimetable); 
			model.addAttribute("todayList", todayList.getList());
			model.addAttribute("today", DateUtils.getDate());
			
			tCourseTimetable.setCourseDate(DateUtils.parseDate(DateUtils.getYesterday()));// 默认获取当天所有课程
			//List yesterdayList = tCourseTimetableService.findList(tCourseTimetable);
			Page<TCourseTimetable> yesterdayList = tCourseTimetableService.findPageForCheck(new Page<TCourseTimetable>(request, response, -1), tCourseTimetable); 
			model.addAttribute("yesterdayList", yesterdayList.getList());
			model.addAttribute("yesterday", DateUtils.getYesterday());
			
			tCourseTimetable.setCourseDate(DateUtils.parseDate(DateUtils.getTomorrow()));// 默认获取当天所有课程
			//List tomorrowList = tCourseTimetableService.findList(tCourseTimetable);
			Page<TCourseTimetable> tomorrowList = tCourseTimetableService.findPageForCheck(new Page<TCourseTimetable>(request, response, -1), tCourseTimetable); 
			model.addAttribute("tomorrowList", tomorrowList.getList());
			model.addAttribute("tomorrow", DateUtils.getTomorrow());
		} else {
			tCourseTimetable.setCampusId("");
			model.addAttribute("message", "您非校区管理人员，无权进行考勤");
		}
		
		return "modules/subject/tCourseTimetableCheckByDayList";
	}
	 
	/**
	 * 查看，增加，编辑课程表表单页面
	 */
	@RequiresPermissions(value={"subject:tCourseTimetable:view","subject:tCourseTimetable:add","subject:tCourseTimetable:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TCourseTimetable tCourseTimetable, Model model) {
		model.addAttribute("tCourseTimetable", tCourseTimetable);
		return "modules/subject/tCourseTimetableForm";
	}
	
	/**
	 * 更改时间表单页面
	 */
	@RequiresPermissions(value={"subject:tCourseTimetable:view","subject:tCourseTimetable:add","subject:tCourseTimetable:edit"},logical=Logical.OR)
	@RequestMapping(value = "timeform")
	public String timeform(String source, TCourseTimetable tCourseTimetable, Model model) {
		model.addAttribute("source", source==null?"":source);
		model.addAttribute("tCourseTimetable", tCourseTimetable);
		return "modules/subject/tCourseTimetableTimeForm";
	}
	
	/**
	 * 整体后延
	 */
	@RequiresPermissions(value={"subject:tCourseTimetable:view","subject:tCourseTimetable:add","subject:tCourseTimetable:edit"},logical=Logical.OR)
	@RequestMapping(value = "delayform")
	public String delayform(String source, TCourseTimetable tCourseTimetable, Model model) {
		tCourseTimetable.setShour("0");
		model.addAttribute("source", source==null?"":source);
		model.addAttribute("tCourseTimetable", tCourseTimetable);
		return "modules/subject/tCourseTimetableDelayForm";
	}
	
	/**
	 * 更改教室表单页面
	 */
	@RequiresPermissions(value={"subject:tCourseTimetable:view","subject:tCourseTimetable:add","subject:tCourseTimetable:edit"},logical=Logical.OR)
	@RequestMapping(value = "roomform")
	public String roomform(String source, TCourseTimetable tCourseTimetable, Model model) {
		model.addAttribute("source", source==null?"":source);
		model.addAttribute("tCourseTimetable", tCourseTimetable);
		return "modules/subject/tCourseTimetableRoomForm";
	}
	
	/**
	 * 更改教师表单页面
	 */
	@RequiresPermissions(value={"subject:tCourseTimetable:view","subject:tCourseTimetable:add","subject:tCourseTimetable:edit"},logical=Logical.OR)
	@RequestMapping(value = "teacherform")
	public String teacherform(String source, TCourseTimetable tCourseTimetable, Model model) {
		model.addAttribute("source", source==null?"":source);
		model.addAttribute("tCourseTimetable", tCourseTimetable);
		return "modules/subject/tCourseTimetableTeacherForm";
	}
	
	/**
	 * 查看，增加，编辑课程表表单页面
	 */
	@RequiresPermissions(value={"subject:tCourseTimetable:view","subject:tCourseTimetable:add","subject:tCourseTimetable:edit"},logical=Logical.OR)
	@RequestMapping(value = "checkform")
	public String checkform(String source, TCourseTimetable tCourseTimetable, Model model) {
		model.addAttribute("tCourseTimetable", tCourseTimetable);
		model.addAttribute("source", source);
		
		TClassStudentCheck tClassStudentCheck = new TClassStudentCheck();
		tClassStudentCheck.settCourseTimetable(tCourseTimetable);
		
		List<TClassStudentCheck> tClassStudentCheckList = null;
		
		// 已完成
		if (tCourseTimetable.getStatus() == 2)
		{
			tClassStudentCheckList = tClassStudentCheckService.findList(tClassStudentCheck); 
		} else { // 未完成
			tClassStudentCheckList = tClassStudentCheckService.findListByTtid(tClassStudentCheck); 
		}
		for (TClassStudentCheck tss : tClassStudentCheckList)
		{
			if (tss.getStatus() == null)
				tss.setStatus(1);
		}
		tCourseTimetable.settClassStudentCheckList(tClassStudentCheckList);
		return "modules/subject/tCourseTimetableCheckForm";
	}

	/**
	 * 保存课程表
	 */
	@RequiresPermissions(value={"subject:tCourseTimetable:add","subject:tCourseTimetable:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(String opertype, String source, TCourseTimetable tCourseTimetable, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (opertype==null || "".equals(opertype))
		{
			if (!beanValidator(model, tCourseTimetable)){
				return form(tCourseTimetable, model);
			}
		}
		if ("updateroom".equals(opertype))
		{
			tCourseTimetable.setType(4);
			tCourseTimetableService.save(tCourseTimetable);//保存
			addMessage(redirectAttributes, "教室调整成功");
		} else if ("updatetime".equals(opertype))
		{
			tCourseTimetable.setBeginTime(DateUtils.parseDate("2000-01-01 "+tCourseTimetable.getShour()+":"+tCourseTimetable.getSmin()+":00"));
			tCourseTimetable.setEndTime(DateUtils.parseDate("2000-01-01 "+tCourseTimetable.getEhour()+":"+tCourseTimetable.getEmin()+":00"));
			tCourseTimetable.setType(5);
			tCourseTimetableService.save(tCourseTimetable);//保存
			addMessage(redirectAttributes, "时间更改成功");
		} else if ("delay".equals(opertype))
		{
			tCourseTimetable.setType(5);
			tCourseTimetable.setShour((Integer.parseInt(tCourseTimetable.getShour()) * 7) + "");
			tCourseTimetableService.updatedelayTime(tCourseTimetable);//保存
			addMessage(redirectAttributes, "时间更改成功");
		} else if ("updateteacher".equals(opertype))
		{
			tCourseTimetable.setType(6);
			tCourseTimetableService.save(tCourseTimetable);//保存
			addMessage(redirectAttributes, "教师代课设置成功");
		} else {
			if(!tCourseTimetable.getIsNewRecord()){//编辑表单保存
				TCourseTimetable t = tCourseTimetableService.get(tCourseTimetable.getId());//从数据库取出记录的值
				MyBeanUtils.copyBeanNotNull2Bean(tCourseTimetable, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
				tCourseTimetableService.save(t);//保存
			} else {//新增表单保存
				tCourseTimetableService.save(tCourseTimetable);//保存
			}
			addMessage(redirectAttributes, "保存课程表成功");
		}
		if (source.equals("adjust")) {
			return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/adjustlist?repage";
		} else if (source.equals("substitute")) {
			return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/substitutelist?repage";
		} else if (source.equals("editlist")) {
			return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/editlist?ccid="+tCourseTimetable.getCourseclass().getId()+"&repage";
		} else 
			return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/list?repage";
	}
	
	/**
	 * 保存考勤数据
	 * 正式考勤打卡页面结果数据保存
	 */
	@RequiresPermissions(value={"subject:tCourseTimetable:add","subject:tCourseTimetable:edit"},logical=Logical.OR)
	@RequestMapping(value = "check")
	public String check(String source, TCourseTimetable tCourseTimetable, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tCourseTimetable)){
			return form(tCourseTimetable, model);
		}
		tCourseTimetableService.check(tCourseTimetable, tCourseClassService, tClassStudentService);
		addMessage(redirectAttributes, "考勤成功");
		
		if ("new".equals(source))
			return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/daychecklist?repage";
		else
			return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/checklist?repage&coursedate="+DateUtils.formatDate(tCourseTimetable.getCourseDate(), "yyyy-MM-dd");
	}
	
	/**
	 * 删除课程表
	 */
	@RequiresPermissions("subject:tCourseTimetable:del")
	@RequestMapping(value = "delete")
	public String delete(TCourseTimetable tCourseTimetable, RedirectAttributes redirectAttributes) {
		tCourseTimetableService.delete(tCourseTimetable);
		addMessage(redirectAttributes, "删除课程表成功");
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/list?repage";
	}
	
	/**
	 * 批量删除课程表
	 */
	@RequiresPermissions("subject:tCourseTimetable:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tCourseTimetableService.delete(tCourseTimetableService.get(id));
		}
		addMessage(redirectAttributes, "删除课程表成功");
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("subject:tCourseTimetable:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TCourseTimetable tCourseTimetable, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "课程表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TCourseTimetable> page = tCourseTimetableService.findPage(new Page<TCourseTimetable>(request, response, -1), tCourseTimetable);
    		new ExportExcel("课程表", TCourseTimetable.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出课程表记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("subject:tCourseTimetable:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TCourseTimetable> list = ei.getDataList(TCourseTimetable.class);
			for (TCourseTimetable tCourseTimetable : list){
				try{
					tCourseTimetableService.save(tCourseTimetable);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条课程表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条课程表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入课程表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/list?repage";
    }
	
	/**
	 * 下载导入课程表数据模板
	 */
	@RequiresPermissions("subject:tCourseTimetable:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "课程表数据导入模板.xlsx";
    		List<TCourseTimetable> list = Lists.newArrayList(); 
    		new ExportExcel("课程表数据", TCourseTimetable.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseTimetable/list?repage";
    }
	
	
	

}