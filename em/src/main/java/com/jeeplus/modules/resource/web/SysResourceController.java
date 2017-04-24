/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.resource.web;

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
import com.jeeplus.modules.resource.entity.SysResource;
import com.jeeplus.modules.resource.service.SysResourceService;

/**
 * 系统资源Controller
 * @author fly
 * @version 2016-08-29
 */
@Controller
@RequestMapping(value = "${adminPath}/resource/sysResource")
public class SysResourceController extends BaseController {

	@Autowired
	private SysResourceService sysResourceService;
	
	@ModelAttribute
	public SysResource get(@RequestParam(required=false) String id) {
		SysResource entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysResourceService.get(id);
		}
		if (entity == null){
			entity = new SysResource();
		}
		return entity;
	}
	
	/**
	 * 系统资源列表页面
	 */
	@RequiresPermissions("resource:sysResource:list")
	@RequestMapping(value = {"list", ""})
	public String list(SysResource sysResource, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysResource> page = sysResourceService.findPage(new Page<SysResource>(request, response), sysResource); 
		model.addAttribute("page", page);
		return "modules/resource/sysResourceList";
	}
	
	/**
	 * 租户资源列表页面
	 */
	@RequiresPermissions("resource:sysResource:list")
	@RequestMapping(value = {"listSchoolResource", ""})
	public String listSchoolResource(SysResource sysResource, HttpServletRequest request, HttpServletResponse response, Model model, String schoolcode) {
		List<SysResource> srList = sysResourceService.findListBySchoolCode(schoolcode); 
		model.addAttribute("srList", srList);
		return "modules/resource/schoolResourceList";
	}

	/**
	 * 查看，增加，编辑系统资源表单页面
	 */
	@RequiresPermissions(value={"resource:sysResource:view","resource:sysResource:add","resource:sysResource:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SysResource sysResource, Model model) {
		model.addAttribute("sysResource", sysResource);
		return "modules/resource/sysResourceForm";
	}

	/**
	 * 保存系统资源
	 */
	@RequiresPermissions(value={"resource:sysResource:add","resource:sysResource:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SysResource sysResource, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sysResource)){
			return form(sysResource, model);
		}
		if(!sysResource.getIsNewRecord()){//编辑表单保存
			SysResource t = sysResourceService.get(sysResource.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(sysResource, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			sysResourceService.save(t);//保存
		}else{//新增表单保存
			sysResourceService.save(sysResource);//保存
		}
		addMessage(redirectAttributes, "保存系统资源成功");
		return "redirect:"+Global.getAdminPath()+"/resource/sysResource/list?repage";
	}
	
	/**
	 * 删除系统资源
	 */
	@RequiresPermissions("resource:sysResource:del")
	@RequestMapping(value = "delete")
	public String delete(SysResource sysResource, RedirectAttributes redirectAttributes) {
		int result = sysResourceService.deleteByLogic(sysResource);
		//sysResourceService.delete(sysResource);
		addMessage(redirectAttributes, result>0?"删除系统资源成功":"删除系统资源失败");
		return "redirect:"+Global.getAdminPath()+"/resource/sysResource/list?repage";
	}
	
	/**
	 * 批量删除系统资源
	 */
	@RequiresPermissions("resource:sysResource:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysResourceService.delete(sysResourceService.get(id));
		}
		addMessage(redirectAttributes, "删除系统资源成功");
		return "redirect:"+Global.getAdminPath()+"/resource/sysResource/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("resource:sysResource:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SysResource sysResource, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "系统资源"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SysResource> page = sysResourceService.findPage(new Page<SysResource>(request, response, -1), sysResource);
    		new ExportExcel("系统资源", SysResource.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出系统资源记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/resource/sysResource/list?repage";
    }

	/**
	 * 导入Excel数据
	 */
	@RequiresPermissions("resource:sysResource:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SysResource> list = ei.getDataList(SysResource.class);
			for (SysResource sysResource : list){
				try{
					sysResourceService.save(sysResource);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条系统资源记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条系统资源记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入系统资源失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/resource/sysResource/list?repage";
    }
	
	/**
	 * 下载导入系统资源数据模板
	 */
	@RequiresPermissions("resource:sysResource:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "系统资源数据导入模板.xlsx";
    		List<SysResource> list = Lists.newArrayList(); 
    		new ExportExcel("系统资源数据", SysResource.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/resource/sysResource/list?repage";
    }
	
	
	

}