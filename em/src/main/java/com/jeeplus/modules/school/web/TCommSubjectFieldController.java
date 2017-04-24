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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.school.entity.TCommSubjectField;
import com.jeeplus.modules.school.service.TCommSubjectFieldService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 学科分类Controller
 * @author fly
 * @version 2016-09-09
 */
@Controller
@RequestMapping(value = "${adminPath}/school/tCommSubjectField")
public class TCommSubjectFieldController extends BaseController {

	@Autowired
	private TCommSubjectFieldService tCommSubjectFieldService;
	
	@ModelAttribute
	public TCommSubjectField get(@RequestParam(required=false) String id) {
		TCommSubjectField entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tCommSubjectFieldService.get(id);
		}
		if (entity == null){
			entity = new TCommSubjectField();
		}
		return entity;
	}
	
	/**
	 * 学科分类列表页面
	 */
	@RequiresPermissions("school:tCommSubjectField:list")
	@RequestMapping(value = {"list", ""})
	public String list(TCommSubjectField tCommSubjectField, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCommSubjectField.getSchoolCode()==null || "".equals(tCommSubjectField.getSchoolCode()))
			tCommSubjectField.setSchoolCode(UserUtils.getUser().getSchool().getSchoolCode());
		if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
			tCommSubjectField.setCampus(UserUtils.getUser().getCompany());
		Page<TCommSubjectField> page = tCommSubjectFieldService.findPage(new Page<TCommSubjectField>(request, response), tCommSubjectField); 
		model.addAttribute("page", page);
		return "modules/school/tCommSubjectFieldList";
	}
	
	/**
	 * 学科分类列表页面
	 */
	@RequiresPermissions("school:tCommSubjectField:list")
	@RequestMapping(value = {"selectlist", ""})
	public String selectlist(TCommSubjectField tCommSubjectField, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCommSubjectField.getSchoolCode()==null || "".equals(tCommSubjectField.getSchoolCode()))
			tCommSubjectField.setSchoolCode(UserUtils.getUser().getSchool().getSchoolCode());
		Page<TCommSubjectField> page = tCommSubjectFieldService.findPage(new Page<TCommSubjectField>(request, response), tCommSubjectField); 
		model.addAttribute("page", page);
		return "modules/school/tCommSubjectFieldList";
	}

	/**
	 * 查看，增加，编辑学科分类表单页面
	 */
	@RequiresPermissions(value={"school:tCommSubjectField:view","school:tCommSubjectField:add","school:tCommSubjectField:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TCommSubjectField tCommSubjectField, Model model) {
		if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
		{
			tCommSubjectField.setCampus(UserUtils.getUser().getCompany());
		}
		model.addAttribute("tCommSubjectField", tCommSubjectField);
		return "modules/school/tCommSubjectFieldForm";
	}

	/**
	 * 保存学科分类
	 */
	@RequiresPermissions(value={"school:tCommSubjectField:add","school:tCommSubjectField:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TCommSubjectField tCommSubjectField, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (tCommSubjectField.getSchoolCode()==null || "".equals(tCommSubjectField.getSchoolCode()))
			tCommSubjectField.setSchoolCode(UserUtils.getUser().getSchool().getSchoolCode());
		if (!beanValidator(model, tCommSubjectField)){
			return form(tCommSubjectField, model);
		}
		if(!tCommSubjectField.getIsNewRecord()){//编辑表单保存
			TCommSubjectField t = tCommSubjectFieldService.get(tCommSubjectField.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tCommSubjectField, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tCommSubjectFieldService.save(t);//保存
		}else{//新增表单保存
			tCommSubjectFieldService.save(tCommSubjectField);//保存
		}
		addMessage(redirectAttributes, "保存学科分类成功");
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubjectField/list?repage";
	}
	
	/**
	 * 删除学科分类
	 */
	@RequiresPermissions("school:tCommSubjectField:del")
	@RequestMapping(value = "delete")
	public String delete(TCommSubjectField tCommSubjectField, RedirectAttributes redirectAttributes) {
		tCommSubjectFieldService.delete(tCommSubjectField);
		addMessage(redirectAttributes, "删除学科分类成功");
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubjectField/list?repage";
	}
	
	/**
	 * 批量删除学科分类
	 */
	@RequiresPermissions("school:tCommSubjectField:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tCommSubjectFieldService.delete(tCommSubjectFieldService.get(id));
		}
		addMessage(redirectAttributes, "删除学科分类成功");
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubjectField/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("school:tCommSubjectField:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TCommSubjectField tCommSubjectField, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学科分类"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TCommSubjectField> page = tCommSubjectFieldService.findPage(new Page<TCommSubjectField>(request, response, -1), tCommSubjectField);
    		new ExportExcel("学科分类", TCommSubjectField.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学科分类记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubjectField/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("school:tCommSubjectField:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TCommSubjectField> list = ei.getDataList(TCommSubjectField.class);
			for (TCommSubjectField tCommSubjectField : list){
				try{
					tCommSubjectFieldService.save(tCommSubjectField);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学科分类记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学科分类记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学科分类失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubjectField/list?repage";
    }
	
	/**
	 * 下载导入学科分类数据模板
	 */
	@RequiresPermissions("school:tCommSubjectField:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学科分类数据导入模板.xlsx";
    		List<TCommSubjectField> list = Lists.newArrayList(); 
    		new ExportExcel("学科分类数据", TCommSubjectField.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubjectField/list?repage";
    }
	
	
	

}