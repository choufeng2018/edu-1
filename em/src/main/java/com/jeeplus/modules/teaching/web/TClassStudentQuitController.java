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
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.subject.entity.TCourseTimetable;
import com.jeeplus.modules.subject.service.TCourseClassService;
import com.jeeplus.modules.subject.service.TCourseTimetableService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teaching.entity.TClassStudent;
import com.jeeplus.modules.teaching.entity.TClassStudentCheck;
import com.jeeplus.modules.teaching.entity.TClassStudentQuit;
import com.jeeplus.modules.teaching.service.TClassStudentCheckService;
import com.jeeplus.modules.teaching.service.TClassStudentQuitService;
import com.jeeplus.modules.teaching.service.TClassStudentService;

/**
 * 学生退课管理Controller
 * @author fly
 * @version 2016-09-28
 */
@Controller
@RequestMapping(value = "${adminPath}/teaching/tClassStudentQuit")
public class TClassStudentQuitController extends BaseController {

	@Autowired
	private TClassStudentQuitService tClassStudentQuitService;
	
	@Autowired
	private TClassStudentService tClassStudentService;
	
	@Autowired
	private TClassStudentCheckService tClassStudentCheckService;
	
	@Autowired
	private TCourseTimetableService tCourseTimetableService;
	
	@Autowired
	private TCourseClassService tCourseClassService;
	
	@ModelAttribute
	public TClassStudentQuit get(@RequestParam(required=false) String id) {
		TClassStudentQuit entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tClassStudentQuitService.get(id);
		}
		if (entity == null){
			entity = new TClassStudentQuit();
		}
		return entity;
	}
	
	/**
	 * 退课列表页面
	 */
	@RequiresPermissions("teaching:tClassStudentQuit:list")
	@RequestMapping(value = {"list", ""})
	public String list(TClassStudentQuit tClassStudentQuit, HttpServletRequest request, HttpServletResponse response, Model model) {
		tClassStudentQuit.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (UserUtils.getUser().getCompany().getParentId().equals("0"))
		{
			tClassStudentQuit.setCampusId("");
			model.addAttribute("message", "非分校区管理人员，无权查看退订表！");
		} else {
			tClassStudentQuit.setCampusId(UserUtils.getUser().getCompany().getId());
		}
		Page<TClassStudentQuit> page = tClassStudentQuitService.findPage(new Page<TClassStudentQuit>(request, response), tClassStudentQuit); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentQuitList";
	}

	/**
	 * 查看，增加，编辑退课表单页面
	 */
	@RequiresPermissions(value={"teaching:tClassStudentQuit:view","teaching:tClassStudentQuit:add","teaching:tClassStudentQuit:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TClassStudentQuit tClassStudentQuit, Model model) {
		model.addAttribute("tClassStudentQuit", tClassStudentQuit);
		return "modules/teaching/tClassStudentQuitForm";
	}
	
	/**
	 * 查看，增加，编辑退课表单页面
	 */
	@RequiresPermissions(value={"teaching:tClassStudentQuit:view","teaching:tClassStudentQuit:add","teaching:tClassStudentQuit:edit"},logical=Logical.OR)
	@RequestMapping(value = "quitform")
	public String quitform(String csid, TClassStudentQuit tClassStudentQuit, Model model) {
		TClassStudent tClassStudent = tClassStudentService.get(csid);
		TCourseClass tCourseClass = tCourseClassService.get(tClassStudent.getCourseclass().getId());
		tClassStudent.setCourseclass(tCourseClass);
		// 课程助理
		tClassStudentQuit.setUser(tClassStudent.getCc());
		// 已完成课时
		TCourseTimetable tCourseTimetable = new TCourseTimetable();
		tCourseTimetable.setCourseclass(tCourseClass);
		tCourseTimetable.setStatus(2);
		List<TCourseTimetable> list = tCourseTimetableService.findList(tCourseTimetable);
		tClassStudentQuit.setClassComplete(list.size());
		// 剩余课时
		tClassStudentQuit.setClassRest(tCourseClass.getClassHour() - list.size());
		// 单课时费用
		tClassStudentQuit.setPerAmount(tCourseClass.getPercost());
		
		// 获取该生考勤记录
		TClassStudentCheck tClassStudentCheck = new TClassStudentCheck();
		tClassStudentCheck.setStudentId(tClassStudent.getId());
		tClassStudentCheck.settCourseTimetable(tCourseTimetable);
		List<TClassStudentCheck> tList = tClassStudentCheckService.findList(tClassStudentCheck);
		int qj=0, qq=0;
		for (int i=0; i<tList.size(); i++)
		{
			TClassStudentCheck tcsc = tList.get(i);
			if (tcsc.getStatus() == 2) qq++;
			else if (tcsc.getStatus() == 3) qj++;
		}
		// 请假课时
		tClassStudentQuit.setClassLeave(qj);
		// 缺勤课时
		tClassStudentQuit.setClassMissed(qq);
		// 补课课时
		
		// 设置课程, 学校, 校区
		tClassStudentQuit.settClassStudent(tClassStudent);
		tClassStudentQuit.setSchoolId(tClassStudent.getSchoolId());
		tClassStudentQuit.setCampusId(tClassStudent.getCampusId());
		
		model.addAttribute("tClassStudentQuit", tClassStudentQuit);
		return "modules/teaching/tClassStudentQuitForm";
	}

	/**
	 * 保存退课
	 */
	@RequiresPermissions(value={"teaching:tClassStudentQuit:add","teaching:tClassStudentQuit:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TClassStudentQuit tClassStudentQuit, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tClassStudentQuit)){
			return form(tClassStudentQuit, model);
		}
		if(!tClassStudentQuit.getIsNewRecord()){//编辑表单保存
			TClassStudentQuit t = tClassStudentQuitService.get(tClassStudentQuit.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tClassStudentQuit, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tClassStudentQuitService.save(t);//保存
		} else {//新增表单保存
			tClassStudentQuitService.save(tClassStudentQuit);//保存
		}
		
		addMessage(redirectAttributes, "退课操作成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentQuit/list?repage";
		//return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/quitlist?repage";
	}
	
	/**
	 * 删除退课
	 */
	@RequiresPermissions("teaching:tClassStudentQuit:del")
	@RequestMapping(value = "delete")
	public String delete(TClassStudentQuit tClassStudentQuit, RedirectAttributes redirectAttributes) {
		tClassStudentQuitService.delete(tClassStudentQuit);
		addMessage(redirectAttributes, "删除退课成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentQuit/list?repage";
	}
	
	/**
	 * 批量删除退课
	 */
	@RequiresPermissions("teaching:tClassStudentQuit:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tClassStudentQuitService.delete(tClassStudentQuitService.get(id));
		}
		addMessage(redirectAttributes, "删除退课成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentQuit/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("teaching:tClassStudentQuit:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TClassStudentQuit tClassStudentQuit, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "退课"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TClassStudentQuit> page = tClassStudentQuitService.findPage(new Page<TClassStudentQuit>(request, response, -1), tClassStudentQuit);
    		new ExportExcel("退课", TClassStudentQuit.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出退课记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentQuit/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("teaching:tClassStudentQuit:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TClassStudentQuit> list = ei.getDataList(TClassStudentQuit.class);
			for (TClassStudentQuit tClassStudentQuit : list){
				try{
					tClassStudentQuitService.save(tClassStudentQuit);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条退课记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条退课记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入退课失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentQuit/list?repage";
    }
	
	/**
	 * 下载导入退课数据模板
	 */
	@RequiresPermissions("teaching:tClassStudentQuit:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "退课数据导入模板.xlsx";
    		List<TClassStudentQuit> list = Lists.newArrayList(); 
    		new ExportExcel("退课数据", TClassStudentQuit.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentQuit/list?repage";
    }
	
	
	

}