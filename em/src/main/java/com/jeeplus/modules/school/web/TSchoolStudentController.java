/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.web;

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
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 学生信息管理Controller
 * @author fly
 * @version 2016-09-12
 */
@Controller
@RequestMapping(value = "${adminPath}/school/tSchoolStudent")
public class TSchoolStudentController extends BaseController {

	@Autowired
	private TSchoolStudentService tSchoolStudentService;
	
	@ModelAttribute
	public TSchoolStudent get(@RequestParam(required=false) String id) {
		TSchoolStudent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSchoolStudentService.get(id);
		}
		if (entity == null){
			entity = new TSchoolStudent();
		}
		return entity;
	}
	
	/**
	 * 学生信息列表页面
	 * [分校的只能看本校的]
	 */
	@RequiresPermissions("school:tSchoolStudent:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSchoolStudent tSchoolStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tSchoolStudent.getSchoolId()==null || "".equals(tSchoolStudent.getSchoolId()))
		{
			tSchoolStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		}
		if (tSchoolStudent.getCampus()==null || tSchoolStudent.getCampus().getId()==null || "".equals(tSchoolStudent.getCampus().getId()))
		{
			if (UserUtils.getUser().getCompany().getParentId().equals("0"))
				tSchoolStudent.setCampus(new Office());
			else
				tSchoolStudent.setCampus(UserUtils.getUser().getCompany());
		}
		Page<TSchoolStudent> page = tSchoolStudentService.findPage(new Page<TSchoolStudent>(request, response), tSchoolStudent); 
		model.addAttribute("page", page);
		model.addAttribute("isparent", UserUtils.getUserIsParent());
		return "modules/school/tSchoolStudentList";
	}
	
	/**
	 * 学生选择列表页面
	 */
	@RequiresPermissions("school:tSchoolStudent:list")
	@RequestMapping(value = {"selectlist", ""})
	public String selectlist(TSchoolStudent tSchoolStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tSchoolStudent.getSchoolId() ==null || "".equals(tSchoolStudent.getSchoolId()))
			tSchoolStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		Page<TSchoolStudent> page = tSchoolStudentService.findPage(new Page<TSchoolStudent>(request, response), tSchoolStudent); 
		model.addAttribute("page", page);
		return "modules/school/tSchoolStudentSelectList";
	}

	/**
	 * 查看，增加，编辑学生信息表单页面
	 */
	@RequiresPermissions(value={"school:tSchoolStudent:view","school:tSchoolStudent:add","school:tSchoolStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSchoolStudent tSchoolStudent, Model model) {
		if ("".equals(tSchoolStudent.getId()))
			tSchoolStudent.setCc(UserUtils.getUser());
		if (tSchoolStudent.getSchoolId() == null || "".equals(tSchoolStudent.getSchoolId()))
			tSchoolStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (tSchoolStudent.getCampus()==null || tSchoolStudent.getCampus().getId().equals("") || tSchoolStudent.getCampus().getId() == null)
		{
			if (UserUtils.getUser().getCompany().getParentId().equals("0"))
				model.addAttribute("message", "您非分校管理人员，无权添加学生信息！");
			else {
				tSchoolStudent.setCampus(UserUtils.getUser().getCompany());
				tSchoolStudent.setCc(UserUtils.getUser());
			}
		}
		model.addAttribute("tSchoolStudent", tSchoolStudent);
		return "modules/school/tSchoolStudentForm";
	}

	/**
	 * 保存学生信息
	 */
	@RequiresPermissions(value={"school:tSchoolStudent:add","school:tSchoolStudent:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSchoolStudent tSchoolStudent, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (tSchoolStudent.getStatus()==null || tSchoolStudent.getStatus()==0)
		{
			tSchoolStudent.setStatus(1);
		}
		if (!beanValidator(model, tSchoolStudent)){
			return form(tSchoolStudent, model);
		}
		if(!tSchoolStudent.getIsNewRecord()){//编辑表单保存
			TSchoolStudent t = tSchoolStudentService.get(tSchoolStudent.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSchoolStudent, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tSchoolStudentService.save(t);//保存
		}else{//新增表单保存
			tSchoolStudentService.save(tSchoolStudent);//保存
		}
		addMessage(redirectAttributes, "保存学生信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolStudent/list?repage";
	}
	
	/**
	 * 删除学生信息
	 */
	@RequiresPermissions("school:tSchoolStudent:del")
	@RequestMapping(value = "delete")
	public String delete(TSchoolStudent tSchoolStudent, RedirectAttributes redirectAttributes) {
		tSchoolStudentService.delete(tSchoolStudent);
		addMessage(redirectAttributes, "删除学生信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolStudent/list?repage";
	}
	
	/**
	 * 批量删除学生信息
	 */
	@RequiresPermissions("school:tSchoolStudent:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tSchoolStudentService.delete(tSchoolStudentService.get(id));
		}
		addMessage(redirectAttributes, "删除学生信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolStudent/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("school:tSchoolStudent:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSchoolStudent tSchoolStudent, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            if (tSchoolStudent.getSchoolId()==null || "".equals(tSchoolStudent.getSchoolId()))
    		{
    			tSchoolStudent.setSchoolId(UserUtils.getUser().getSchool().getId());
    		}
    		if (tSchoolStudent.getCampus()==null || tSchoolStudent.getCampus().getId()==null || "".equals(tSchoolStudent.getCampus().getId()))
    		{
    			if (UserUtils.getUser().getCompany().getParentId().equals("0"))
    				tSchoolStudent.setCampus(new Office());
    			else
    				tSchoolStudent.setCampus(UserUtils.getUser().getCompany());
    		}
            Page<TSchoolStudent> page = tSchoolStudentService.findPage(new Page<TSchoolStudent>(request, response, -1), tSchoolStudent);
    		new ExportExcel("学生信息", TSchoolStudent.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学生信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolStudent/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("school:tSchoolStudent:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSchoolStudent> list = ei.getDataList(TSchoolStudent.class);
			for (TSchoolStudent tSchoolStudent : list){
				try{
					tSchoolStudentService.save(tSchoolStudent);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学生信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学生信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学生信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolStudent/list?repage";
    }
	
	/**
	 * 下载导入学生信息数据模板
	 */
	@RequiresPermissions("school:tSchoolStudent:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学生信息数据导入模板.xlsx";
    		List<TSchoolStudent> list = Lists.newArrayList(); 
    		new ExportExcel("学生信息数据", TSchoolStudent.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolStudent/list?repage";
    }
	
	
	

}