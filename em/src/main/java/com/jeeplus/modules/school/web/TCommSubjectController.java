/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.school.entity.TCommSubject;
import com.jeeplus.modules.school.entity.TCommSubjectField;
import com.jeeplus.modules.school.service.TCommSubjectFieldService;
import com.jeeplus.modules.school.service.TCommSubjectService;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 课程信息Controller
 * @author fly
 * @version 2016-09-09
 */
@Controller
@RequestMapping(value = "${adminPath}/school/tCommSubject")
public class TCommSubjectController extends BaseController {

	@Autowired
	private TCommSubjectService tCommSubjectService;
	
	@Autowired
	private TCommSubjectFieldService tCommSubjectFieldService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public TCommSubject get(@RequestParam(required=false) String id) {
		TCommSubject entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tCommSubjectService.get(id);
		}
		if (entity == null){
			entity = new TCommSubject();
		}
		return entity;
	}
	
	/**
	 * 课程信息列表页面
	 */
	@RequiresPermissions("school:tCommSubject:list")
	@RequestMapping(value = {"list", ""})
	public String list(TCommSubject tCommSubject, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (tCommSubject.getSchoolId()==null || "".equals(tCommSubject.getSchoolId()))
			tCommSubject.setSchoolId(UserUtils.getUser().getSchool().getId());
//		if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
//			tCommSubject.setCampus(UserUtils.getUser().getCompany());
//		Page<TCommSubject> page = tCommSubjectService.findPage(new Page<TCommSubject>(request, response), tCommSubject); 
//		model.addAttribute("page", page);
		
		List<TCommSubject> list = Lists.newArrayList();
		List<TCommSubject> sourcelist = tCommSubjectService.findList(tCommSubject);
		TCommSubject.sortList(list, sourcelist, TCommSubject.getRootId(), true);
        model.addAttribute("list", list);
		
		return "modules/school/tCommSubjectList";
	}

	/**
	 * 查看，增加，编辑课程信息表单页面
	 */
	@RequiresPermissions(value={"school:tCommSubject:view","school:tCommSubject:add","school:tCommSubject:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TCommSubject tCommSubject, Model model) {
		if (tCommSubject.getParent()!=null && tCommSubject.getParent().getId()!=null && !"".equals(tCommSubject.getParent().getId()))
		{
			tCommSubject.setParent(tCommSubjectService.get(tCommSubject.getParent().getId()));
		}
		model.addAttribute("tCommSubject", tCommSubject);
		return "modules/school/tCommSubjectForm";
	}
	
	/**
	 * 查看，增加，编辑课程信息表单页面
	 */
	@RequiresPermissions(value={"school:tCommSubject:view","school:tCommSubject:add","school:tCommSubject:edit"},logical=Logical.OR)
	@RequestMapping(value = "formFL")
	public String formFL(TCommSubject tCommSubject, Model model) {
		model.addAttribute("tCommSubject", tCommSubject);
		return "modules/school/tCommSubjectFLForm";
	}

	/**
	 * 保存课程信息
	 */
	@RequiresPermissions(value={"school:tCommSubject:add","school:tCommSubject:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TCommSubject tCommSubject, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (tCommSubject.getSchoolId()==null || "".equals(tCommSubject.getSchoolId()))
			tCommSubject.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (!beanValidator(model, tCommSubject)){
			return form(tCommSubject, model);
		}
		if(!tCommSubject.getIsNewRecord()){//编辑表单保存
			TCommSubject t = tCommSubjectService.get(tCommSubject.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tCommSubject, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tCommSubjectService.save(t);//保存
		}else{//新增表单保存
			tCommSubjectService.save(tCommSubject);//保存
		}
		addMessage(redirectAttributes, "保存课程信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubject/?repage";
	}
	
	/**
	 * 保存课程信息
	 */
	@RequiresPermissions(value={"school:tCommSubject:add","school:tCommSubject:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveFL")
	public String saveFL(TCommSubject tCommSubject, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (tCommSubject.getSchoolId()==null || "".equals(tCommSubject.getSchoolId()))
			tCommSubject.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (!beanValidator(model, tCommSubject)){
			return formFL(tCommSubject, model);
		}
		if(!tCommSubject.getIsNewRecord()){//编辑表单保存
			TCommSubject t = tCommSubjectService.get(tCommSubject.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tCommSubject, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tCommSubjectService.save(t);//保存
		}else{//新增表单保存
			TCommSubject parent = new TCommSubject();
			parent.setId("0");
			tCommSubject.setParent(parent);
			tCommSubjectService.save(tCommSubject);	//保存
		}
		addMessage(redirectAttributes, "保存课程信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubject/list?repage";
	}
	
	/**
	 * 删除课程信息
	 */
	@RequiresPermissions("school:tCommSubject:del")
	@RequestMapping(value = "delete")
	public String delete(TCommSubject tCommSubject, RedirectAttributes redirectAttributes) {
		tCommSubjectService.delete(tCommSubject);
		addMessage(redirectAttributes, "删除课程信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubject/?repage";
	}
	
	/**
	 * 批量删除课程信息
	 */
	@RequiresPermissions("school:tCommSubject:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tCommSubjectService.delete(tCommSubjectService.get(id));
		}
		addMessage(redirectAttributes, "删除课程信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubject/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("school:tCommSubject:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TCommSubject tCommSubject, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "课程信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TCommSubject> page = tCommSubjectService.findPage(new Page<TCommSubject>(request, response, -1), tCommSubject);
    		new ExportExcel("课程信息", TCommSubject.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出课程信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubject/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("school:tCommSubject:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TCommSubject> list = ei.getDataList(TCommSubject.class);
			for (TCommSubject tCommSubject : list){
				try{
					tCommSubjectService.save(tCommSubject);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条课程信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条课程信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入课程信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubject/?repage";
    }
	
	/**
	 * 下载导入课程信息数据模板
	 */
	@RequiresPermissions("school:tCommSubject:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "课程信息数据导入模板.xlsx";
    		List<TCommSubject> list = Lists.newArrayList(); 
    		new ExportExcel("课程信息数据", TCommSubject.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tCommSubject/?repage";
    }
	
	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	//@RequiresPermissions(value={"school:tCommSubject:view","school:tCommSubject:add","school:tCommSubject:edit"},logical=Logical.OR)
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String campus, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		
		TCommSubject tcs = new TCommSubject();
		tcs.setSchoolId(UserUtils.getUser().getSchool().getId());
		
		List<TCommSubject> list = tCommSubjectService.findList(tcs);
		 
		for (int i=0; i<list.size(); i++){
			TCommSubject e = list.get(i);
			if (true){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent().getId());
				map.put("pIds", e.getParent().getId());
				map.put("name", e.getSubjectName());
				if ("1".equals(e.getParent().getId()))
					map.put("isParent", true);
				mapList.add(map);
			}
		} 
		return mapList;
	}
	

}