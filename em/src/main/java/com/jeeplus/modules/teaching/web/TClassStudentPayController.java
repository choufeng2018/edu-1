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
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teaching.entity.TClassStudent;
import com.jeeplus.modules.teaching.entity.TClassStudentPay;
import com.jeeplus.modules.teaching.service.TClassStudentPayService;
import com.jeeplus.modules.teaching.service.TClassStudentService;

/**
 * 缴费Controller
 * @author flying
 * @version 2017-01-18
 */
@Controller
@RequestMapping(value = "${adminPath}/teaching/tClassStudentPay")
public class TClassStudentPayController extends BaseController {

	@Autowired
	private TClassStudentPayService tClassStudentPayService;
	
	@Autowired
	private TClassStudentService tClassStudentService;
	
	@ModelAttribute
	public TClassStudentPay get(@RequestParam(required=false) String id) {
		TClassStudentPay entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tClassStudentPayService.get(id);
		}
		if (entity == null){
			entity = new TClassStudentPay();
		}
		return entity;
	}
	
	/**
	 * 缴费列表页面
	 */
	@RequiresPermissions("teaching:tClassStudentPay:list")
	@RequestMapping(value = {"list", ""})
	public String list(TClassStudentPay tClassStudentPay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TClassStudentPay> page = tClassStudentPayService.findPage(new Page<TClassStudentPay>(request, response), tClassStudentPay); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentPayList";
	}
	
	/**
	 * 缴费列表页面
	 */
	@RequiresPermissions("teaching:tClassStudentPay:list")
	@RequestMapping(value = {"querylist", ""})
	public String querylist(TClassStudentPay tClassStudentPay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TClassStudentPay> page = tClassStudentPayService.findPage(new Page<TClassStudentPay>(request, response), tClassStudentPay); 
		model.addAttribute("page", page);
		return "modules/teaching/tClassStudentPayQueryList";
	}

	/**
	 * 查看，增加，编辑缴费表单页面
	 */
	@RequiresPermissions(value={"teaching:tClassStudentPay:view","teaching:tClassStudentPay:add","teaching:tClassStudentPay:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TClassStudentPay tClassStudentPay, Model model) {
		tClassStudentPay.setCc(UserUtils.getUser());
		tClassStudentPay.setCs(tClassStudentService.get(tClassStudentPay.getCs().getId()));
		model.addAttribute("tClassStudentPay", tClassStudentPay);
		return "modules/teaching/tClassStudentPayForm";
	}

	/**
	 * 保存缴费
	 */
	@RequiresPermissions(value={"teaching:tClassStudentPay:add","teaching:tClassStudentPay:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TClassStudentPay tClassStudentPay, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tClassStudentPay)){
			return form(tClassStudentPay, model);
		}
		if(!tClassStudentPay.getIsNewRecord()){//编辑表单保存
			TClassStudentPay t = tClassStudentPayService.get(tClassStudentPay.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tClassStudentPay, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tClassStudentPayService.save(t);//保存
		}else{//新增表单保存
			
			tClassStudentPayService.save(tClassStudentPay, request, response);//保存
		}
		addMessage(redirectAttributes, "缴费成功");
		
		//return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentPay/?repage";
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudent/renewlist?repage";
	}
	
	/**
	 * 删除缴费
	 */
	@RequiresPermissions("teaching:tClassStudentPay:del")
	@RequestMapping(value = "delete")
	public String delete(TClassStudentPay tClassStudentPay, RedirectAttributes redirectAttributes) {
		tClassStudentPayService.delete(tClassStudentPay);
		addMessage(redirectAttributes, "删除缴费成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentPay/?repage";
	}
	
	/**
	 * 批量删除缴费
	 */
	@RequiresPermissions("teaching:tClassStudentPay:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tClassStudentPayService.delete(tClassStudentPayService.get(id));
		}
		addMessage(redirectAttributes, "删除缴费成功");
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentPay/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("teaching:tClassStudentPay:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TClassStudentPay tClassStudentPay, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "缴费"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TClassStudentPay> page = tClassStudentPayService.findPage(new Page<TClassStudentPay>(request, response, -1), tClassStudentPay);
    		new ExportExcel("缴费", TClassStudentPay.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出缴费记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentPay/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("teaching:tClassStudentPay:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TClassStudentPay> list = ei.getDataList(TClassStudentPay.class);
			for (TClassStudentPay tClassStudentPay : list){
				try{
					tClassStudentPayService.save(tClassStudentPay);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条缴费记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条缴费记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入缴费失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentPay/?repage";
    }
	
	/**
	 * 下载导入缴费数据模板
	 */
	@RequiresPermissions("teaching:tClassStudentPay:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "缴费数据导入模板.xlsx";
    		List<TClassStudentPay> list = Lists.newArrayList(); 
    		new ExportExcel("缴费数据", TClassStudentPay.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/teaching/tClassStudentPay/?repage";
    }
	
	
	

}