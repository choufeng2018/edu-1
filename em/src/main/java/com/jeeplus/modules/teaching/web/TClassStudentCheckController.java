/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teaching.web;

import java.util.ArrayList;
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
import com.jeeplus.modules.school.service.TSchoolStudentService;
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.subject.entity.TCourseTimetable;
import com.jeeplus.modules.subject.service.TCourseClassService;
import com.jeeplus.modules.subject.service.TCourseTimetableService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teaching.entity.TClassStudent;
import com.jeeplus.modules.teaching.entity.TClassStudentCheck;
import com.jeeplus.modules.teaching.service.TClassStudentCheckService;

/**
 * 考勤记录管理Controller
 * @author fly
 * @version 2016-09-18
 */
@Controller
@RequestMapping(value = "${adminPath}/teaching/tClassStudentCheck")
public class TClassStudentCheckController extends BaseController {

	@Autowired
	private TClassStudentCheckService tClassStudentCheckService;
	
	@Autowired
	private TCourseClassService tCourseClassService;
	
	@Autowired
	private TSchoolStudentService tSchoolStudentService;
	
	@Autowired
	private TCourseTimetableService tCourseTimetableService;
	
	@ModelAttribute
	public TClassStudentCheck get(@RequestParam(required=false) String id) {
		TClassStudentCheck entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tClassStudentCheckService.get(id);
		}
		if (entity == null){
			entity = new TClassStudentCheck();
		}
		return entity;
	}
	 
	/**
	 * 考勤列表页面
	 */
	@RequiresPermissions("teaching:tClassStudentCheck:list")
	@RequestMapping(value = {"list", ""})
	public String list(TClassStudentCheck tClassStudentCheck, HttpServletRequest request, HttpServletResponse response, Model model) {
		TCourseTimetable tt = tClassStudentCheck.gettCourseTimetable();
		if (tt == null)
			tt = new TCourseTimetable();
		if (tt.getSchoolId()==null || "".equals(tt.getSchoolId()))
			tt.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
			tt.setCampusId(UserUtils.getUser().getCompany().getId());
		
		tClassStudentCheck.settCourseTimetable(tt);
		tClassStudentCheck.getSqlMap().put("dsf", " AND date(tt.course_date) <= '"+ DateUtils.getDate() +"' ");
			
		Page<TClassStudentCheck> page = tClassStudentCheckService.findPage(new Page<TClassStudentCheck>(request, response), tClassStudentCheck); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentCheckList";
	}
	
	/**
	 * 考勤列表页面
	 */
	@RequiresPermissions("teaching:tClassStudentCheck:checklist")
	@RequestMapping(value = {"checklist", ""})
	public String checklist(TClassStudentCheck tClassStudentCheck, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TClassStudentCheck> page = tClassStudentCheckService.findPage(new Page<TClassStudentCheck>(request, response), tClassStudentCheck); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentCheckCheckList";
	}
	
	/**
	 * 查看个人考勤列表页面
	 */
	@RequiresPermissions("teaching:tClassStudentCheck:list")
	@RequestMapping(value = {"stuChecklist", ""})
	public String stuChecklist(TClassStudentCheck tClassStudentCheck, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TClassStudentCheck> page = tClassStudentCheckService.findPageForTran(new Page<TClassStudentCheck>(request, response), tClassStudentCheck); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentCheckStuCheckList";
	}
	
	/**
	 * 查看个人考勤列表页面
	 */
	@RequiresPermissions("teaching:tClassStudentCheck:list")
	@RequestMapping(value = {"stuCheckLeavelist", ""})
	public String stuCheckLeavelist(TClassStudentCheck tClassStudentCheck, HttpServletRequest request, HttpServletResponse response, Model model) {
		tClassStudentCheck.setStatus(3);
		Page<TClassStudentCheck> page = tClassStudentCheckService.findPageForTran(new Page<TClassStudentCheck>(request, response), tClassStudentCheck); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentCheckStuLeaveList";
	}
	 
	/**
	 * 补课列表页面
	 */
	@RequiresPermissions("teaching:tClassStudentCheck:list")
	@RequestMapping(value = {"missedlist", ""})
	public String missedlist(TClassStudentCheck tClassStudentCheck, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		TCourseTimetable tCourseTimetable = new TCourseTimetable();
		tCourseTimetable.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
			tCourseTimetable.setCampusId(UserUtils.getUser().getCompany().getId());
		else // 总部管理者过滤数据
			tCourseTimetable.setCampusId("");
	
		TCourseClass courseclass = new TCourseClass();
		courseclass.setClassType(2);
		tCourseTimetable.setCourseclass(courseclass);
		
		tClassStudentCheck.setStatus(3); // 只查请假的
		tClassStudentCheck.setCanmakeup(1);
		tClassStudentCheck.settCourseTimetable(tCourseTimetable);
		Page<TClassStudentCheck> page = tClassStudentCheckService.findPageForMissed(new Page<TClassStudentCheck>(request, response), tClassStudentCheck); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentCheckMissedList";
	}

	/**
	 * 查看，增加，编辑考勤表单页面
	 */
	@RequiresPermissions(value={"teaching:tClassStudentCheck:view","teaching:tClassStudentCheck:add","teaching:tClassStudentCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TClassStudentCheck tClassStudentCheck, Model model) {
		model.addAttribute("tClassStudentCheck", tClassStudentCheck);
		return "modules/teaching/tClassStudentCheckForm";
	}
	
	/**
	 * 查看，增加，编辑考勤表单页面
	 */
	@RequiresPermissions(value={"teaching:tClassStudentCheck:view","teaching:tClassStudentCheck:add","teaching:tClassStudentCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "leaveform")
	public String leaveform(TClassStudentCheck tClassStudentCheck, Model model) {
		tClassStudentCheck.gettCourseTimetable().setCourseclass(tCourseClassService.get(tClassStudentCheck.gettCourseTimetable().getCourseclass().getId()));
		tClassStudentCheck.setStudentName(tSchoolStudentService.get(tClassStudentCheck.getStudentId()).getName());
		tClassStudentCheck.setStatus(3);
		model.addAttribute("tClassStudentCheck", tClassStudentCheck);
		
		TClassStudent tcs = new TClassStudent();
		tcs.setCourseclass(tCourseClassService.get(tClassStudentCheck.gettCourseTimetable().getCourseclass().getId()));
		tcs.setStudent(tSchoolStudentService.get(tClassStudentCheck.getStudentId()));
		 
		//TCourseTimetable tCourseTimetable = tClassStudentCheck.gettCourseTimetable();
		//tCourseTimetable.setStatus(1); //未完成
		//List<TCourseTimetable> ttlist = tCourseTimetableService.findList(tCourseTimetable);
		List<TClassStudentCheck> ttlist = new ArrayList<>();
		ttlist = tClassStudentCheckService.findListForMissed(tClassStudentCheck);
		
		tcs.settClassStudentCheckList(ttlist);
		System.out.println(ttlist.get(0).gettCourseTimetable().getTeactime());
		model.addAttribute("tClassStudent", tcs);
		
//		if (tClassStudentCheck.gettCourseTimetable().getCourseclass().getClassType() == 1)
//			return "modules/teaching/tClassStudentCheckLeaveMiniForm";
//		else 
			return "modules/teaching/tClassStudentCheckLeaveForm";
	}

	/**
	 * 查看，增加，编辑考勤表单页面
	 */
	@RequiresPermissions(value={"teaching:tClassStudentCheck:view","teaching:tClassStudentCheck:add","teaching:tClassStudentCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "bkform")
	public String bkform(TClassStudentCheck tClassStudentCheck, Model model) {
		if (tClassStudentCheck!=null && tClassStudentCheck.getParent()!=null && !"".equals(tClassStudentCheck.getParent().getId()))
		{
			TClassStudentCheck tC = tClassStudentCheckService.getByParentId(tClassStudentCheck.getParent().getId());
			if (tC != null)
				tClassStudentCheck = tC;
			
			tClassStudentCheck.setParent(tClassStudentCheckService.get(tClassStudentCheck.getParent().getId()));
		}
		model.addAttribute("tClassStudentCheck", tClassStudentCheck);
		return "modules/teaching/tClassStudentCheckBkForm";
	}
	
	/**
	 * 保存考勤
	 * */
	@RequiresPermissions(value={"teaching:tClassStudentCheck:add","teaching:tClassStudentCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TClassStudentCheck tClassStudentCheck, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tClassStudentCheck)){
			return form(tClassStudentCheck, model);
		}
		if(!tClassStudentCheck.getIsNewRecord()){//编辑表单保存
			TClassStudentCheck t = tClassStudentCheckService.get(tClassStudentCheck.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tClassStudentCheck, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tClassStudentCheckService.save(t);//保存
		}else{//新增表单保存
			tClassStudentCheckService.save(tClassStudentCheck);//保存
		}
		addMessage(redirectAttributes, "保存考勤成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentCheck/list?repage";
	}
	 
	/**
	 * 保存请假
	 * */
	@RequiresPermissions(value={"teaching:tClassStudentCheck:add","teaching:tClassStudentCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "leavesave")
	public String leavesave(TClassStudentCheck tClassStudentCheck, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tClassStudentCheck)){
			return form(tClassStudentCheck, model);
		}
		
		String ttid = tClassStudentCheck.gettCourseTimetable().getId();
		String []tid = ttid.split(",");
		for (int i=0; i<tid.length; i++)
		{
			TCourseTimetable tCourseTimetable = new TCourseTimetable();
			tCourseTimetable.setId(tid[i]);
			tClassStudentCheck.settCourseTimetable(tCourseTimetable);
			TClassStudentCheck t = tClassStudentCheckService.getByTtIdAndStuId(tClassStudentCheck);//从数据库取出记录的值
			if (t != null){ // 更新请假记录
				t.settCourseTimetable(tClassStudentCheck.gettCourseTimetable());
				tClassStudentCheckService.save(t); //保存
			} else {//新增表单保存
				tClassStudentCheck.setId("");
				tClassStudentCheck.setType(1);
				tClassStudentCheckService.save(tClassStudentCheck);//保存
			}
		}
		addMessage(redirectAttributes, "保存请假成功，课堂考情完成后会更新正式请假次数");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/leavelist?repage";
	}
	
	/**
	 * 保存请假
	 * */
	@RequiresPermissions(value={"teaching:tClassStudentCheck:add","teaching:tClassStudentCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "leavesave1")
	public String leavesave1(TClassStudent tClassStudent, Model model, RedirectAttributes redirectAttributes) throws Exception{
		

		tClassStudentCheckService.save1(tClassStudent);
		
		addMessage(redirectAttributes, "保存请假成功，课堂考情完成后会更新正式请假次数");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/leavelist?repage";
	}
	
	/**
	 * 保存考勤
	 * */
	@RequiresPermissions(value={"teaching:tClassStudentCheck:add","teaching:tClassStudentCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "bksave")
	public String bksave(TClassStudentCheck tClassStudentCheck, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (tClassStudentCheck.getParent()!=null && !"".equals(tClassStudentCheck.getParent().getId()))
		{
			TClassStudentCheck parent= tClassStudentCheckService.get(tClassStudentCheck.getParent().getId());
			tClassStudentCheck.setStudentId(parent.getStudentId());
			tClassStudentCheck.setStudentName(parent.getStudentName());
		} else if (tClassStudentCheck.gettCourseTimetable()==null || tClassStudentCheck.gettCourseTimetable().getId().equals(""))
		{
			addMessage(redirectAttributes, "补课设置失败");
			return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentCheck/missedlist?repage";
		}
		if (tClassStudentCheck.getType() == null || tClassStudentCheck.getType() == 0)
		{
			tClassStudentCheck.setType(2); //学生类型：补课
		}
		
		if(!tClassStudentCheck.getIsNewRecord()){//编辑表单保存
			TClassStudentCheck t = tClassStudentCheckService.get(tClassStudentCheck.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tClassStudentCheck, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tClassStudentCheckService.save(t);//保存
		}else{//新增表单保存
			tClassStudentCheckService.save(tClassStudentCheck);//保存
		}
		addMessage(redirectAttributes, "补课设置成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentCheck/missedlist?repage";
	}
	
	/**
	 * 删除考勤
	 */
	@RequiresPermissions("teaching:tClassStudentCheck:del")
	@RequestMapping(value = "delete")
	public String delete(TClassStudentCheck tClassStudentCheck, RedirectAttributes redirectAttributes) {
		tClassStudentCheckService.delete(tClassStudentCheck);
		addMessage(redirectAttributes, "删除考勤成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentCheck/list?repage";
	}
	
	/**
	 * 批量删除考勤
	 */
	@RequiresPermissions("teaching:tClassStudentCheck:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tClassStudentCheckService.delete(tClassStudentCheckService.get(id));
		}
		addMessage(redirectAttributes, "删除考勤成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentCheck/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("teaching:tClassStudentCheck:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TClassStudentCheck tClassStudentCheck, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "考勤"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TClassStudentCheck> page = tClassStudentCheckService.findPage(new Page<TClassStudentCheck>(request, response, -1), tClassStudentCheck);
    		new ExportExcel("考勤", TClassStudentCheck.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出考勤记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentCheck/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("teaching:tClassStudentCheck:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TClassStudentCheck> list = ei.getDataList(TClassStudentCheck.class);
			for (TClassStudentCheck tClassStudentCheck : list){
				try{
					tClassStudentCheckService.save(tClassStudentCheck);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条考勤记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条考勤记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入考勤失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentCheck/list?repage";
    }
	
	/**
	 * 下载导入考勤数据模板
	 */
	@RequiresPermissions("teaching:tClassStudentCheck:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "考勤数据导入模板.xlsx";
    		List<TClassStudentCheck> list = Lists.newArrayList(); 
    		new ExportExcel("考勤数据", TClassStudentCheck.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentCheck/list?repage";
    }
	
}