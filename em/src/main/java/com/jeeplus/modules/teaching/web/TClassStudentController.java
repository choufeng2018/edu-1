/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.web;

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
import com.jeeplus.modules.school.entity.TSchoolStudent;
import com.jeeplus.modules.school.service.TSchoolStudentService;
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.subject.entity.TCourseClassTime;
import com.jeeplus.modules.subject.entity.TCourseTimetable;
import com.jeeplus.modules.subject.service.TCourseClassService;
import com.jeeplus.modules.subject.service.TCourseTimetableService;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teaching.entity.TClassStudent;
import com.jeeplus.modules.teaching.service.TClassStudentCheckService;
import com.jeeplus.modules.teaching.service.TClassStudentService;

/**
 * 报名管理Controller
 * @author fly
 * @version 2016-09-16
 */
@Controller
@RequestMapping(value = "${adminPath}/teaching/tClassStudent")
public class TClassStudentController extends BaseController {

	@Autowired
	private TClassStudentService tClassStudentService;
	
	@Autowired
	private TCourseClassService tCourseClassService;
	
	@Autowired
	private TClassStudentCheckService tClassStudentCheckService;
	
	@Autowired
	private TCourseTimetableService tCourseTimetableService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private TSchoolStudentService tSchoolStudentService;
	
	@ModelAttribute
	public TClassStudent get(@RequestParam(required=false) String id) {
		TClassStudent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tClassStudentService.get(id);
		}
		if (entity == null){
			entity = new TClassStudent();
		}
		return entity;
	}
	
	/**
	 * 【学员报名】报名列表页面
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"list", ""})
	public String list(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		setSchoolAndCampus(tClassStudent);
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看报名表数据！");
		} 
		Page<TClassStudent> page = tClassStudentService.findPage(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentList";
	}
	
	/**
	 * 【学员报名】报名列表页面（1对1）
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"minilist", ""})
	public String minilist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		setSchoolAndCampus(tClassStudent);
		if (tClassStudent.getCourseclass() == null)
			tClassStudent.setCourseclass(new TCourseClass());
		tClassStudent.getCourseclass().setClassType(1);
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看报名表数据！");
		} 
		Page<TClassStudent> page = tClassStudentService.findPage(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentminiList";
	}
	
	/**
	 * 【学员报名】报名列表页面(大班制)
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"biglist", ""})
	public String biglist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		setSchoolAndCampus(tClassStudent);
		if (tClassStudent.getCourseclass() == null)
			tClassStudent.setCourseclass(new TCourseClass());
		tClassStudent.getCourseclass().setClassType(2);
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看报名表数据！");
		} 
		Page<TClassStudent> page = tClassStudentService.findPage(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentbigList";
	}
	/**
	 * 学生基础信息界面，续费列表页面
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"renewlist", ""})
	public String renewlist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tClassStudent.getSchoolId()==null || "".equals(tClassStudent.getSchoolId()))
			tClassStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看报名表数据！");
			tClassStudent.setCampusId("0");
		} else {
			tClassStudent.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		
		// 续费只针对1对1教学（或者计次收费的课程）且课程尚未结束
		if (tClassStudent.getCourseclass()==null)
			tClassStudent.setCourseclass(new TCourseClass());
		tClassStudent.getCourseclass().setClassType(1);
		tClassStudent.setStatus(1);
		
		Page<TClassStudent> page = tClassStudentService.findPage(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentRenewList";
	}
	
	/**
	 * 学生基础信息界面，查看报名列表页面
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"viewlist", ""})
	public String viewlist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tClassStudent.getStudent()==null || tClassStudent.getStudent().getId()==null || "".equals(tClassStudent.getStudent().getId()))
		{
			addMessage(model, "请选择学生！");
		} else {
			Page<TClassStudent> page = tClassStudentService.findPageForView(new Page<TClassStudent>(request, response, -1), tClassStudent); 
			model.addAttribute("page", page);
		}
		return "modules/teaching/tClassStudentViewList";
	}
	
	/**
	 * 退课报名列表页面
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"quitlist", ""})
	public String quitlist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tClassStudent.getSchoolId()==null || "".equals(tClassStudent.getSchoolId()))
			tClassStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看报名表数据！");
			tClassStudent.setCampusId("0");
		} else {
			tClassStudent.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		tClassStudent.setStatus(1);
		Page<TClassStudent> page = tClassStudentService.findPageByType(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentToQuitList";
	}
	
	/**
	 * 学生课程冻结
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"pauselist", ""})
	public String pauselist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tClassStudent.getSchoolId()==null || "".equals(tClassStudent.getSchoolId()))
			tClassStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看报名表数据！");
			tClassStudent.setCampusId("0");
		} else {
			tClassStudent.setCampusId(UserUtils.getUser().getCompany().getId());
		}
	 
		tClassStudent.getSqlMap().put("dsf", " AND a.status in (1, 5) ");
		Page<TClassStudent> page = tClassStudentService.findPageByStatus(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentPauseList";
	}
	
	/**
	 * 【OK】
	 * 学生转班列表页面
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"switchlist", ""})
	public String switchlist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model, String source) {
		if (tClassStudent.getSchoolId()==null || "".equals(tClassStudent.getSchoolId()))
			tClassStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看报名表数据！");
			tClassStudent.setCampusId("0");
		} else {
			tClassStudent.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		tClassStudent.setStatus(1);
		Page<TClassStudent> page = tClassStudentService.findPage(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		model.addAttribute("source", (source==null||source.equals(""))?"switch":source);
		return "modules/teaching/tClassStudentSwitchList";
	}
	
	/**
	 * 转班历史记录
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"hisSwitchlist", ""})
	public String hisSwitchlist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tClassStudent.getSchoolId()==null || "".equals(tClassStudent.getSchoolId()))
			tClassStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看转班历史记录数据！");
			tClassStudent.setCampusId("0");
			return "modules/teaching/tClassStudentSwitchList";
		} else {
			tClassStudent.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		tClassStudent.setStatus(4); // 中途转班记录
		Page<TClassStudent> page = tClassStudentService.findPageForTran(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudenthisSwitchList";
	}
	
	/**
	 * 报名学生列表页面
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"stulist", ""})
	public String stulist(String ccid, TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		TCourseClass tCourseClass = tCourseClassService.get(ccid);
		tClassStudent.setSchoolId(tCourseClass.getSchoolId());
		tClassStudent.setCampusId(tCourseClass.getCampus().getId());
		tClassStudent.setCourseclass(tCourseClass);
		
		Page<TClassStudent> page = tClassStudentService.findPage(new Page<TClassStudent>(request, response, 30), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentEnterList";
	}
	
	/**
	 * 请假列表页面(一对多)
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"leavelist", ""})
	public String leavelist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tClassStudent.getSchoolId()==null || "".equals(tClassStudent.getSchoolId()))
			tClassStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看请假数据！");
			tClassStudent.setCampusId("0");
			return "modules/teaching/tClassStudentLeaveList";
		} else {
			tClassStudent.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		tClassStudent.setStatus(1);
		tClassStudent.getSqlMap().put("dsf", " aa.leavecount>0 and ");
		Page<TClassStudent> page = tClassStudentService.findPageForLeave(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentLeaveList";
	}
	
	/**
	 * 请假列表页面(一对1)
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"oneleavelist", ""})
	public String oneleavelist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tClassStudent.getSchoolId()==null || "".equals(tClassStudent.getSchoolId()))
			tClassStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看请假数据！");
			tClassStudent.setCampusId("0");
			return "modules/teaching/tClassStudentLeaveList";
		} else {
			tClassStudent.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		tClassStudent.setStatus(1);
		tClassStudent.getSqlMap().put("dsf", " aa.leavecount>0 and ");
		Page<TClassStudent> page = tClassStudentService.findPageForLeave(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentLeaveList";
	}
	
	/**
	 * 待请假列表页面
	 */
	@RequiresPermissions("teaching:tClassStudent:list")
	@RequestMapping(value = {"todoleavelist", ""})
	public String todoleavelist(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tClassStudent.getSchoolId()==null || "".equals(tClassStudent.getSchoolId()))
			tClassStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (UserUtils.getUserIsParent())
		{
			addMessage(model, "您非校区管理人员，无权查看请假数据！");
			tClassStudent.setCampusId("0");
			return "modules/teaching/tClassStudentLeaveTodoList";
		} else {
			tClassStudent.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		tClassStudent.setStatus(1);
		if (tClassStudent.getCourseclass() == null)
			tClassStudent.setCourseclass(new TCourseClass());
		//整合1对1  1对多 请假功能
		//tClassStudent.getCourseclass().setClassType(2);
		Page<TClassStudent> page = tClassStudentService.findPageForLeave(new Page<TClassStudent>(request, response), tClassStudent); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentLeaveTodoList";
	}

	/**
	 * 【OK】跳转 修改 报名表单页面
	 */
	@RequiresPermissions(value={"teaching:tClassStudent:view","teaching:tClassStudent:add","teaching:tClassStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TClassStudent tClassStudent, Model model) {
		model.addAttribute("tClassStudent", tClassStudent);
		return "modules/teaching/tClassStudentForm";
	}
	
	/**
	 * 【OK】 跳转到转班填报详情页面
	 */
	@RequiresPermissions(value={"teaching:tClassStudent:view","teaching:tClassStudent:add","teaching:tClassStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "switchform")
	public String switchform(TClassStudent tClassStudent, Model model, String type, String source) {
		model.addAttribute("tClassStudent", tClassStudent);
		TCourseClass tCourseClass = tCourseClassService.get(tClassStudent.getCourseclass().getId());
		tClassStudent.setCourseclass(tCourseClass);
		
		// 已完成课时
		TCourseTimetable tCourseTimetable = new TCourseTimetable();
		tCourseTimetable.setCourseclass(tCourseClass);
		tCourseTimetable.setStatus(2);
		List<TCourseTimetable> list = tCourseTimetableService.findList(tCourseTimetable);
		model.addAttribute("iscomplete", list.size());
		// 结余费用
		//double remaincost = (tCourseClass.getClassHour()-list.size())*tCourseClass.getPercost();
		//model.addAttribute("remaincost", tClassStudent.getBalance());
		
		TClassStudent tClassStudentNew = new TClassStudent();
		tClassStudentNew.setParent(tClassStudent);
		tClassStudentNew.setSchoolId(tClassStudent.getSchoolId());
		tClassStudentNew.setCampusId(tClassStudent.getCampusId());
		tClassStudentNew.setCc(UserUtils.getUser());
		tClassStudentNew.setStudent(tClassStudent.getStudent());
		tClassStudentNew.setAmount(tClassStudent.getBalance());
		tClassStudentNew.setBalance(tClassStudent.getBalance());
		tClassStudentNew.setIspay(1);
		tClassStudentNew.setStatus(1);
		tClassStudentNew.setRemarks("学生中途转班，部分费用【"+tClassStudent.getBalance()+"】是从 【" + tClassStudent.getCourseclass().getClassDesc() + "】结余过来");
		if (type.equals("1")) {
			TCourseClass tcc = new TCourseClass();
			//tcc.gettCourseClassTimeList().add(new TCourseClassTime());
			tcc.setSchoolId(tClassStudent.getSchoolId());
			tClassStudentNew.setCourseclass(tcc);
		}
		model.addAttribute("tClassStudentNew", tClassStudentNew);
		model.addAttribute("source", source);
		
		if (type.equals("1")) 
			return "modules/teaching/tClassStudentSwitchSmForm";
		else
			return "modules/teaching/tClassStudentSwitchForm";
	}
	
	/**
	 * 【OK】  学生列表页面 跳转报名表单页面(1对多大班制教学)
	 */
	@RequiresPermissions(value={"teaching:tClassStudent:add"},logical=Logical.OR)
	@RequestMapping(value = "enterform")
	public String enterform(TClassStudent tClassStudent, Model model, String ccid, String source) {
		TCourseClass tCourseClass = null;
		if (ccid != null && !"".equals(ccid))
			tCourseClass = tCourseClassService.get(ccid);
		if (tClassStudent.getStudent()!=null && tClassStudent.getStudent().getId()!=null && !"".equals(tClassStudent.getStudent().getId()))
		{
			tClassStudent.setStudent(tSchoolStudentService.get(tClassStudent.getStudent().getId()));
		}
		if (tCourseClass != null)
		{
			tClassStudent.setSchoolId(tCourseClass.getSchoolId());
			tClassStudent.setCampusId(tCourseClass.getCampus().getId());
			tClassStudent.setAmount(tCourseClass.getCost());
		} else {
			tClassStudent = setSchoolAndCampus(tClassStudent);
			tCourseClass = new TCourseClass();
		}
		tClassStudent.setCourseclass(tCourseClass);
		tClassStudent.setCc(UserUtils.getUser());
		tClassStudent.setStatus(1);
		
		model.addAttribute("tClassStudent", tClassStudent);
		model.addAttribute("source", source);
		
		return "modules/teaching/tClassStudentEnterForm";
	}
	
	/**
	 * 【OK】 学生列表界面 跳转报名表单页面(1对1小班制教学)
	 * 暂时 废弃
	 */
	@RequiresPermissions(value={"teaching:tClassStudent:add"},logical=Logical.OR)
	@RequestMapping(value = "enterformSm")
	public String enterformSm(TClassStudent tClassStudent, Model model) {
		if (tClassStudent.getStudent()!=null && tClassStudent.getStudent().getId()!=null && !"".equals(tClassStudent.getStudent().getId()))
		{
			tClassStudent.setStudent(tSchoolStudentService.get(tClassStudent.getStudent().getId()));
		}
		setSchoolAndCampus(tClassStudent);
		tClassStudent.setCc(UserUtils.getUser());
		tClassStudent.setStatus(1);
		TCourseClass tcc = new TCourseClass();
		tcc.setSchoolId(tClassStudent.getSchoolId());
		if (tClassStudent.getCampusId() != null && !"".equals(tClassStudent.getCampusId()))
			tcc.setCampus(officeService.get(tClassStudent.getCampusId()));
		tClassStudent.setCourseclass(tcc);
		
		model.addAttribute("tClassStudent", tClassStudent);
		return "modules/teaching/tClassStudentEnterSmForm";
	}
	
	/**
	 * 【OK】 跳转新增报名表单页面(1对1小班制 新增报名)
	 */
	@RequiresPermissions(value={"teaching:tClassStudent:view","teaching:tClassStudent:add","teaching:tClassStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "enterformSmW")
	public String enterformSmW(TClassStudent tClassStudent, Model model, String source) {
		if (tClassStudent.getStudent()!=null && tClassStudent.getStudent().getId()!=null && !"".equals(tClassStudent.getStudent().getId()))
		{
			tClassStudent.setStudent(tSchoolStudentService.get(tClassStudent.getStudent().getId()));
		}
		setSchoolAndCampus(tClassStudent);
		tClassStudent.setCc(UserUtils.getUser());
		tClassStudent.setStatus(1);
		TCourseClass tcc = null;
		if (tClassStudent.getCourseclass()==null || tClassStudent.getCourseclass().getId()==null || "".equals(tClassStudent.getCourseclass().getId()))
		{
			tcc = new TCourseClass();
			tcc.setSchoolId(tClassStudent.getSchoolId());
			if (tClassStudent.getCampusId() != null && !"".equals(tClassStudent.getCampusId()))
				tcc.setCampus(officeService.get(tClassStudent.getCampusId()));
		} else {
			tcc = tCourseClassService.get(tClassStudent.getCourseclass().getId());
		}
		tClassStudent.setCourseclass(tcc);
		
		model.addAttribute("tClassStudent", tClassStudent);
		model.addAttribute("source", source);
		return "modules/teaching/tClassStudentEnterSmWForm";
	}
	
	/**
	 * 【OK】跳转 新增报名表单页面 (1对多大班制 新增报名) 
	 * 废弃
	 
	@RequiresPermissions(value={"teaching:tClassStudent:view","teaching:tClassStudent:add","teaching:tClassStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "enterformW")
	public String enterformW(String ccid, TClassStudent tClassStudent, Model model) {
		TCourseClass tCourseClass = null;
		if (ccid!=null && !"".equals(ccid))
			tCourseClass = tCourseClassService.get(ccid);
		if (tCourseClass != null)
		{
			tClassStudent.setSchoolId(tCourseClass.getSchoolId());
			tClassStudent.setCampusId(tCourseClass.getCampus().getId());
			tClassStudent.setAmount(tCourseClass.getCost());
		} else {
			setSchoolAndCampus(tClassStudent);
			tCourseClass = new TCourseClass();
		}
		tClassStudent.setCourseclass(tCourseClass);
		tClassStudent.setCc(UserUtils.getUser());
		tClassStudent.setStatus(1);
		
		model.addAttribute("tClassStudent", tClassStudent);
		return "modules/teaching/tClassStudentEnterWForm";
	}*/
	 
	/**
	 * 【OK】 学生列表界面  保存新增报名（大班制）
	 */
	@RequiresPermissions(value={"teaching:tClassStudent:add","teaching:tClassStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "entersave")
	public String entersave(TClassStudent tClassStudent, Model model, RedirectAttributes redirectAttributes, String source) throws Exception{
		if (!beanValidator(model, tClassStudent)){
			return form(tClassStudent, model);
		}
		
		if (!tClassStudent.getIsNewRecord()){//编辑表单保存
			TClassStudent t = tClassStudentService.get(tClassStudent.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tClassStudent, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tClassStudentService.save(t);//保存
			addMessage(redirectAttributes, "修改报名信息成功！");
		} else {//新增表单保存
			if (tClassStudentService.getByStudentIdAndClassId(tClassStudent) != null)
			{
				addMessage(redirectAttributes, "报名失败，此学生已报名，不能重复报名！");
			} else {
				tClassStudent.setYwcks(0);
				tClassStudent.getCourseclass().setClassType(2);
				tClassStudent.setBalance(tClassStudent.getAmount());
				tClassStudentService.save(tClassStudent, null, false);//保存
				
				addMessage(redirectAttributes, "报名成功！");
			}
		}
		
		if ("studentlist".equals(source))
			return "redirect:"+Global.getAdminPath()+"/school/tSchoolStudent/list?repage";
		else if ("classstudentlist".equals(source))
			return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/biglist?repage";
		else return "";
	}

	/**
	 * 【OK】 
	 * 学员转班 大班制保存界面
	 * 修改报名表保存
	 */
	@RequiresPermissions(value={"teaching:tClassStudent:add","teaching:tClassStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TClassStudent tClassStudent, Model model, RedirectAttributes redirectAttributes, String quiteid, String classid, String source) throws Exception{
		if (!beanValidator(model, tClassStudent)){
			return form(tClassStudent, model);
		}
		
		if (quiteid == null || "".equals(quiteid))
		{
			addMessage(redirectAttributes, "请选择待转班报名表！");
		} else if (tClassStudentService.getByStudentIdAndClassId(tClassStudent) != null)
		{
			addMessage(redirectAttributes, "报名失败，此学生已报名，不能重复报名！");
		} else {
			tClassStudent.getCourseclass().setClassType(2);
			tClassStudent.setBalance(tClassStudent.getAmount());
			tClassStudentService.save(tClassStudent, quiteid, false);
			addMessage(redirectAttributes, "报名成功！");
		}
		
		// 转班生成的报名表，更新原班级信息
		if (quiteid != null && !"".equals(quiteid))
		{ 
			return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/switchlist?repage";
		}
		else if ("hisswitch".equals(source))
			return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/hisSwitchlist?repage";
				
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/list?repage";
	}
	
	/**
	 * 【OK】 (1对1小班制)
	 * 学生转班界面  保存报名
	 * 学生列表界面  保存报名
	 */
	@RequiresPermissions(value={"teaching:tClassStudent:add","teaching:tClassStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "entersaveSm")
	public String entersaveSm(TClassStudent tClassStudent, Model model, RedirectAttributes redirectAttributes, String quiteid, String classid) throws Exception{
		if (!beanValidator(model, tClassStudent)){
			return form(tClassStudent, model);
		}
	 
		if (tClassStudentService.getByStudentIdAndClassId(tClassStudent) != null)
		{
			addMessage(redirectAttributes, "报名失败，此学生已报名，不能重复报名！");
		} else {
			tClassStudent.getCourseclass().setClassType(1);
			
			tClassStudent.setBalance(tClassStudent.getAmount()); 
			tClassStudentService.save(tClassStudent, quiteid, true);	
			 
			addMessage(redirectAttributes, "报名成功！");
		}
		
		// 转班生成的报名表，更新原班级信息
		if (quiteid != null && !"".equals(quiteid))
		{ 
			return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/switchlist?repage";
		}
		
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolStudent/list?repage";
	}
	 
	/**
	 * 【OK】
	 *  新增报名保存（大班制）
	 *  废弃
	 
	@RequiresPermissions(value={"teaching:tClassStudent:add","teaching:tClassStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "entersaveW")
	public String entersaveW(TClassStudent tClassStudent, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tClassStudent)){
			return form(tClassStudent, model);
		}
		
		if (!tClassStudent.getIsNewRecord()){//编辑表单保存
			TClassStudent t = tClassStudentService.get(tClassStudent.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tClassStudent, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tClassStudentService.save(t);//保存
			addMessage(redirectAttributes, "修改报名信息成功！");
		} else {//新增表单保存
			if (tClassStudentService.getByStudentIdAndClassId(tClassStudent) != null)
			{
				addMessage(redirectAttributes, "报名失败，此学生已报名，不能重复报名！");
			} else {
				tClassStudent.setBalance(tClassStudent.getAmount());
				tClassStudent.setYwcks(0);
				tClassStudentService.save(tClassStudent, null, false);//保存
				
				addMessage(redirectAttributes, "报名成功！");
			}
		}
		
		//return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/list?repage";
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/biglist?repage";
	}
	*/
	/**
	 * 【OK】 
	 * 保存 新增报名(1对1 小班制)
	 */
	@RequiresPermissions(value={"teaching:tClassStudent:add","teaching:tClassStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "entersaveSmW")
	public String entersaveSmW(TClassStudent tClassStudent, Model model, RedirectAttributes redirectAttributes, String source, String quiteid) throws Exception{
		if (!beanValidator(model, tClassStudent)){
			return form(tClassStudent, model);
		}
		
		String backString = "";
		if ("classstudent".equals(source)) 
			backString = "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/minilist?repage";
		else if ("studentlist".equals(source))
			backString = "redirect:"+Global.getAdminPath()+"/school/tSchoolStudent/list?repage";
		else if ("switch".equals(source))
			backString = "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/switchlist?repage";
		else if ("hisswitch".equals(source))
			backString = "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/hisSwitchlist?repage";
		
		String teactime = "", slotdesc="";
		if (tClassStudent.getCourseclass().gettCourseClassTimeList().size()<1 || tClassStudent.getCourseclass().gettCourseClassTimeList().size()>7)
		{
			addMessage(model, "请设置正确的时间！");
			return backString;
		}
		for (TCourseClassTime tCourseClassTime : tClassStudent.getCourseclass().gettCourseClassTimeList())
		{
			if ("".equals(tCourseClassTime.getWeek()) || "".equals(tCourseClassTime.getShour()) || "".equals(tCourseClassTime.getSmin()))
			{
				addMessage(model, "请设置正确的时间！");
				return backString;
			}
			tCourseClassTime.setBegintime(DateUtils.parseDate("2000-01-01 "+tCourseClassTime.getShour()+":"+tCourseClassTime.getSmin()+":00"));
			tCourseClassTime.setEndtime(DateUtils.addMinutes(tCourseClassTime.getBegintime(), tClassStudent.getCourseclass().getClassMin()));
			teactime += ("".equals(teactime)?"":",") + DateUtils.getWeekStringByWeek(tCourseClassTime.getWeek()) + 
					"(" + tCourseClassTime.getShour()+":"+tCourseClassTime.getSmin() + "-"+ DateUtils.formatDate(tCourseClassTime.getEndtime(), "HH:mm:ss").substring(0, 5) +")";
			int hour = Integer.parseInt(tCourseClassTime.getShour());
			slotdesc += ("".equals(slotdesc)?"":",") + (hour<12?"上午":(hour<18?"下午":"晚上"));
		}
		tClassStudent.getCourseclass().setTeactime(teactime);
		tClassStudent.getCourseclass().setSlotdesc(slotdesc);
	
		boolean change = false;
		if (!tClassStudent.getIsNewRecord()){//编辑表单保存
			TClassStudent t = tClassStudentService.get(tClassStudent.getId());//从数据库取出记录的值
			if (t.getCourseclass().gettCourseClassTimeList().size() != tClassStudent.getCourseclass().gettCourseClassTimeList().size())
				change = true;
			else 
			{
				for (int i=0; i<tClassStudent.getCourseclass().gettCourseClassTimeList().size(); i++)
				{
					if (tClassStudent.getCourseclass().gettCourseClassTimeList().get(i).getWeek() != t.getCourseclass().gettCourseClassTimeList().get(i).getWeek()
						|| tClassStudent.getCourseclass().gettCourseClassTimeList().get(i).getShour() != t.getCourseclass().gettCourseClassTimeList().get(i).getShour()
						|| tClassStudent.getCourseclass().gettCourseClassTimeList().get(i).getSmin() != t.getCourseclass().gettCourseClassTimeList().get(i).getSmin())
					{
						change = true;
						break;
					}
				}
			}
			
			MyBeanUtils.copyBeanNotNull2Bean(tClassStudent, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			// 保存班级
			tClassStudentService.save(t, quiteid, change);//保存
			addMessage(redirectAttributes, "修改报名信息成功！");
		} else {//新增表单保存
			// 保存报名表
			tClassStudent.setBalance(tClassStudent.getAmount());
			tClassStudent.setYwcks(0);
			tClassStudent.getCourseclass().setClassType(1);
			tClassStudentService.save(tClassStudent, quiteid, true);	//保存
			
			addMessage(redirectAttributes, "报名成功！");
		}
		
		return backString;
	}
	
	/**
	 * 删除报名
	 */
	@RequiresPermissions("teaching:tClassStudent:del")
	@RequestMapping(value = "delete")
	public String delete(TClassStudent tClassStudent, RedirectAttributes redirectAttributes) {
		tClassStudentService.delete(tClassStudent);
		
		addMessage(redirectAttributes, "删除报名成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/list?repage";
	}
	
	/**
	 * 课程冻结
	 */
	@RequiresPermissions("teaching:tClassStudent:pause")
	@RequestMapping(value = "pause")
	public String pause(TClassStudent tClassStudent, RedirectAttributes redirectAttributes) {
		if (tClassStudent.getStatus() == 1)
			tClassStudent.setStatus(5);
		else if (tClassStudent.getStatus() == 5)
			tClassStudent.setStatus(1);
		else {
			addMessage(redirectAttributes, "此报名表当前状态不能冻结");
			return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/pauselist?repage";
		}
		tClassStudentService.pause(tClassStudent);
		addMessage(redirectAttributes, tClassStudent.getStatus()==5?"冻结":"解封"+"成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/pauselist?repage";
	}
	
	/**
	 * 批量删除报名
	 */
	@RequiresPermissions("teaching:tClassStudent:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tClassStudentService.delete(tClassStudentService.get(id));
		}
		addMessage(redirectAttributes, "删除报名成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/list?repage";
	}
	
	/**
	 * 取消报名
	 */
	@RequiresPermissions("teaching:tClassStudent:del")
	@RequestMapping(value = "cancle")
	public String cancle(TClassStudent tClassStudent, RedirectAttributes redirectAttributes) {
		tClassStudent.setStatus(2);
		tClassStudentService.updateStatus(tClassStudent);
		 	
		addMessage(redirectAttributes, "取消报名成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("teaching:tClassStudent:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TClassStudent tClassStudent, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			if (tClassStudent.getSchoolId()==null || "".equals(tClassStudent.getSchoolId()))
				tClassStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
			if (!UserUtils.getUserIsParent())
			{
				tClassStudent.setCampusId(UserUtils.getUser().getCompany().getId());
			}
			
            String fileName = "报名" + DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TClassStudent> page = tClassStudentService.findPage(new Page<TClassStudent>(request, response, -1), tClassStudent);
    		new ExportExcel("报名", TClassStudent.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出报名记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/list?repage";
    }

	/**
	 * 导入Excel数据
	 */
	@RequiresPermissions("teaching:tClassStudent:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TClassStudent> list = ei.getDataList(TClassStudent.class);
			for (TClassStudent tClassStudent : list){
				try{
					tClassStudentService.save(tClassStudent);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条报名记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条报名记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入报名失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/list?repage";
    }
	
	/**
	 * 下载导入报名数据模板
	 */
	@RequiresPermissions("teaching:tClassStudent:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "报名数据导入模板.xlsx";
    		List<TClassStudent> list = Lists.newArrayList(); 
    		new ExportExcel("报名数据", TClassStudent.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/list?repage";
    }
	
	public TClassStudent setSchoolAndCampus(TClassStudent tClassStudent)
	{
		if (tClassStudent.getSchoolId()==null || "".equals(tClassStudent.getSchoolId()))
			tClassStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (!UserUtils.getUserIsParent())
		{
			if (tClassStudent.getCampusId()==null || "".equals(tClassStudent.getCampusId()))
				tClassStudent.setCampusId(UserUtils.getUser().getCompany().getId());
			else
				tClassStudent.setCampusId("");
		} else {
			tClassStudent.setCampusId("");
		}
		return tClassStudent;
	}
	

}