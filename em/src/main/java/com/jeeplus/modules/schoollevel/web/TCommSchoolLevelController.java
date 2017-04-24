/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.schoollevel.web;

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
import com.jeeplus.modules.schoollevel.entity.TCommSchoolLevel;
import com.jeeplus.modules.schoollevel.service.TCommSchoolLevelService;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 租户级别Controller
 * @author fly
 * @version 2016-09-06
 */
@Controller
@RequestMapping(value = "${adminPath}/schoollevel/tCommSchoolLevel")
public class TCommSchoolLevelController extends BaseController {
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private TCommSchoolLevelService tCommSchoolLevelService;
	
	@ModelAttribute
	public TCommSchoolLevel get(@RequestParam(required=false) String id) {
		TCommSchoolLevel entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tCommSchoolLevelService.get(id);
		}
		if (entity == null){
			entity = new TCommSchoolLevel();
		}
		return entity;
	}
	
	/**
	 * 租户等级列表页面
	 */
	@RequiresPermissions("schoollevel:tCommSchoolLevel:list")
	@RequestMapping(value = {"list", ""})
	public String list(TCommSchoolLevel tCommSchoolLevel, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TCommSchoolLevel> page = tCommSchoolLevelService.findPage(new Page<TCommSchoolLevel>(request, response), tCommSchoolLevel); 
		model.addAttribute("page", page);
		return "modules/schoollevel/tCommSchoolLevelList";
	}
	
	/**
	 * 租户等级列表页面
	 */
	@RequiresPermissions("schoollevel:tCommSchoolLevel:list")
	@RequestMapping(value = {"selectlist", ""})
	public String selectlist(TCommSchoolLevel tCommSchoolLevel, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TCommSchoolLevel> page = tCommSchoolLevelService.findPage(new Page<TCommSchoolLevel>(request, response, -1), tCommSchoolLevel); 
		model.addAttribute("page", page);
		return "modules/schoollevel/tCommSchoolLevelSelectList";
	}

	/**
	 * 查看，增加，编辑租户等级表单页面
	 */
	@RequiresPermissions(value={"schoollevel:tCommSchoolLevel:view","schoollevel:tCommSchoolLevel:add","schoollevel:tCommSchoolLevel:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TCommSchoolLevel tCommSchoolLevel, Model model) {
		model.addAttribute("tCommSchoolLevel", tCommSchoolLevel);
		return "modules/schoollevel/tCommSchoolLevelForm";
	}

	/**
	 * 保存租户等级
	 */
	@RequiresPermissions(value={"schoollevel:tCommSchoolLevel:add","schoollevel:tCommSchoolLevel:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TCommSchoolLevel tCommSchoolLevel, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tCommSchoolLevel)){
			return form(tCommSchoolLevel, model);
		}
		if(!tCommSchoolLevel.getIsNewRecord()){//编辑表单保存
			TCommSchoolLevel t = tCommSchoolLevelService.get(tCommSchoolLevel.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tCommSchoolLevel, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tCommSchoolLevelService.save(t);//保存
		}else{//新增表单保存
			tCommSchoolLevelService.save(tCommSchoolLevel);//保存
		}
		addMessage(redirectAttributes, "保存租户等级成功");
		return "redirect:"+Global.getAdminPath()+"/schoollevel/tCommSchoolLevel/list?repage";
	}
	
	/**
	 * 保存租户等级对于资源
	 */
	@RequiresPermissions(value={"schoollevel:tCommSchoolLevel:add","schoollevel:tCommSchoolLevel:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveResource")
	public String saveResource(TCommSchoolLevel tCommSchoolLevel, Model model, RedirectAttributes redirectAttributes) throws Exception{
		tCommSchoolLevelService.saveResource(tCommSchoolLevel);//保存
		addMessage(redirectAttributes, "资源配置成功");
		return "redirect:"+Global.getAdminPath()+"/schoollevel/tCommSchoolLevel/list?repage";
	}
	
	/**
	 * 删除租户等级
	 */
	@RequiresPermissions("schoollevel:tCommSchoolLevel:del")
	@RequestMapping(value = "delete")
	public String delete(TCommSchoolLevel tCommSchoolLevel, RedirectAttributes redirectAttributes) {
		tCommSchoolLevelService.delete(tCommSchoolLevel);
		addMessage(redirectAttributes, "删除租户等级成功");
		return "redirect:"+Global.getAdminPath()+"/schoollevel/tCommSchoolLevel/list?repage";
	}
	
	/**
	 * 批量删除租户等级
	 */
	@RequiresPermissions("schoollevel:tCommSchoolLevel:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tCommSchoolLevelService.delete(tCommSchoolLevelService.get(id));
		}
		addMessage(redirectAttributes, "删除租户等级成功");
		return "redirect:"+Global.getAdminPath()+"/schoollevel/tCommSchoolLevel/list?repage";
	}
	
	/**
	 * 租户等级资源分配
	 */
	@RequiresPermissions("schoollevel:tCommSchoolLevel:resource")
	@RequestMapping(value = "resource")
	public String resource(TCommSchoolLevel tCommSchoolLevel, Model model) {
		model.addAttribute("tCommSchoolLevel", tCommSchoolLevel);
		List<Menu> list = systemService.findAllMenu();
		
		model.addAttribute("menuList", list);
		return "modules/schoollevel/tCommSchoolResource";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("schoollevel:tCommSchoolLevel:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TCommSchoolLevel tCommSchoolLevel, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "租户等级"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TCommSchoolLevel> page = tCommSchoolLevelService.findPage(new Page<TCommSchoolLevel>(request, response, -1), tCommSchoolLevel);
    		new ExportExcel("租户等级", TCommSchoolLevel.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出租户等级记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/schoollevel/tCommSchoolLevel/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("schoollevel:tCommSchoolLevel:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TCommSchoolLevel> list = ei.getDataList(TCommSchoolLevel.class);
			for (TCommSchoolLevel tCommSchoolLevel : list){
				try{
					tCommSchoolLevelService.save(tCommSchoolLevel);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条租户等级记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条租户等级记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入租户等级失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/schoollevel/tCommSchoolLevel/list?repage";
    }
	
	/**
	 * 下载导入租户等级数据模板
	 */
	@RequiresPermissions("schoollevel:tCommSchoolLevel:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "租户等级数据导入模板.xlsx";
    		List<TCommSchoolLevel> list = Lists.newArrayList(); 
    		new ExportExcel("租户等级数据", TCommSchoolLevel.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/schoollevel/tCommSchoolLevel/list?repage";
    }
	
	
	

}