/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.em.web.pay;

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
import com.jeeplus.modules.em.entity.pay.TSchoolPay;
import com.jeeplus.modules.em.service.pay.TSchoolPayService;

/**
 * 会员支付Controller
 * @author fly
 * @version 2016-09-08
 */
@Controller
@RequestMapping(value = "${adminPath}/em/pay/tSchoolPay")
public class TSchoolPayController extends BaseController {

	@Autowired
	private TSchoolPayService tSchoolPayService;
	
	@ModelAttribute
	public TSchoolPay get(@RequestParam(required=false) String id) {
		TSchoolPay entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSchoolPayService.get(id);
		}
		if (entity == null){
			entity = new TSchoolPay();
		}
		return entity;
	}
	
	/**
	 * 支付列表页面
	 */
	@RequiresPermissions("em:pay:tSchoolPay:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSchoolPay tSchoolPay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TSchoolPay> page = tSchoolPayService.findPage(new Page<TSchoolPay>(request, response), tSchoolPay); 
		model.addAttribute("page", page);
		return "modules/em/pay/tSchoolPayList";
	}

	/**
	 * 查看，增加，编辑支付表单页面
	 */
	@RequiresPermissions(value={"em:pay:tSchoolPay:view","em:pay:tSchoolPay:add","em:pay:tSchoolPay:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSchoolPay tSchoolPay, Model model) {
		tSchoolPay.setBuyDate(new Date());
		model.addAttribute("tSchoolPay", tSchoolPay);
		return "modules/em/pay/tSchoolPayForm";
	}

	/**
	 * 保存支付
	 */
	@RequiresPermissions(value={"em:pay:tSchoolPay:add","em:pay:tSchoolPay:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSchoolPay tSchoolPay, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tSchoolPay)){
			return form(tSchoolPay, model);
		}
		if(!tSchoolPay.getIsNewRecord()){//编辑表单保存
			TSchoolPay t = tSchoolPayService.get(tSchoolPay.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSchoolPay, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tSchoolPayService.save(t);//保存
		}else{//新增表单保存
			tSchoolPayService.save(tSchoolPay);//保存
		}
		addMessage(redirectAttributes, "保存支付成功");
		return "redirect:"+Global.getAdminPath()+"/em/pay/tSchoolPay/?repage";
	}
	
	/**
	 * 删除支付
	 */
	@RequiresPermissions("em:pay:tSchoolPay:del")
	@RequestMapping(value = "delete")
	public String delete(TSchoolPay tSchoolPay, RedirectAttributes redirectAttributes) {
		tSchoolPayService.delete(tSchoolPay);
		addMessage(redirectAttributes, "删除支付成功");
		return "redirect:"+Global.getAdminPath()+"/em/pay/tSchoolPay/?repage";
	}
	
	/**
	 * 批量删除支付
	 */
	@RequiresPermissions("em:pay:tSchoolPay:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tSchoolPayService.delete(tSchoolPayService.get(id));
		}
		addMessage(redirectAttributes, "删除支付成功");
		return "redirect:"+Global.getAdminPath()+"/em/pay/tSchoolPay/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("em:pay:tSchoolPay:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSchoolPay tSchoolPay, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "支付"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSchoolPay> page = tSchoolPayService.findPage(new Page<TSchoolPay>(request, response, -1), tSchoolPay);
    		new ExportExcel("支付", TSchoolPay.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出支付记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/em/pay/tSchoolPay/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("em:pay:tSchoolPay:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSchoolPay> list = ei.getDataList(TSchoolPay.class);
			for (TSchoolPay tSchoolPay : list){
				try{
					tSchoolPayService.save(tSchoolPay);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条支付记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条支付记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入支付失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/em/pay/tSchoolPay/?repage";
    }
	
	/**
	 * 下载导入支付数据模板
	 */
	@RequiresPermissions("em:pay:tSchoolPay:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "支付数据导入模板.xlsx";
    		List<TSchoolPay> list = Lists.newArrayList(); 
    		new ExportExcel("支付数据", TSchoolPay.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/em/pay/tSchoolPay/?repage";
    }
	
	
	

}