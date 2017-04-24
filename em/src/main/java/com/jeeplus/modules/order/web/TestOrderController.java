/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.web;

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
import com.jeeplus.modules.order.entity.TestOrder;
import com.jeeplus.modules.order.service.TestOrderService;

/**
 * 订单Controller
 * @author fly
 * @version 2017-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/order/testOrder")
public class TestOrderController extends BaseController {

	@Autowired
	private TestOrderService testOrderService;
	
	@ModelAttribute
	public TestOrder get(@RequestParam(required=false) String id) {
		TestOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testOrderService.get(id);
		}
		if (entity == null){
			entity = new TestOrder();
		}
		return entity;
	}
	
	/**
	 * 订单列表页面
	 */
	@RequiresPermissions("order:testOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list(TestOrder testOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TestOrder> page = testOrderService.findPage(new Page<TestOrder>(request, response), testOrder); 
		model.addAttribute("page", page);
		return "modules/order/testOrderList";
	}

	/**
	 * 查看，增加，编辑订单表单页面
	 */
	@RequiresPermissions(value={"order:testOrder:view","order:testOrder:add","order:testOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TestOrder testOrder, Model model) {
		model.addAttribute("testOrder", testOrder);
		return "modules/order/testOrderForm";
	}

	/**
	 * 保存订单
	 */
	@RequiresPermissions(value={"order:testOrder:add","order:testOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TestOrder testOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, testOrder)){
			return form(testOrder, model);
		}
		if(!testOrder.getIsNewRecord()){//编辑表单保存
			TestOrder t = testOrderService.get(testOrder.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(testOrder, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			testOrderService.save(t);//保存
		}else{//新增表单保存
			testOrderService.save(testOrder);//保存
		}
		addMessage(redirectAttributes, "保存订单成功");
		return "redirect:"+Global.getAdminPath()+"/order/testOrder/?repage";
	}
	
	/**
	 * 删除订单
	 */
	@RequiresPermissions("order:testOrder:del")
	@RequestMapping(value = "delete")
	public String delete(TestOrder testOrder, RedirectAttributes redirectAttributes) {
		testOrderService.delete(testOrder);
		addMessage(redirectAttributes, "删除订单成功");
		return "redirect:"+Global.getAdminPath()+"/order/testOrder/?repage";
	}
	
	/**
	 * 批量删除订单
	 */
	@RequiresPermissions("order:testOrder:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testOrderService.delete(testOrderService.get(id));
		}
		addMessage(redirectAttributes, "删除订单成功");
		return "redirect:"+Global.getAdminPath()+"/order/testOrder/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("order:testOrder:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TestOrder testOrder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TestOrder> page = testOrderService.findPage(new Page<TestOrder>(request, response, -1), testOrder);
    		new ExportExcel("订单", TestOrder.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出订单记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/order/testOrder/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("order:testOrder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestOrder> list = ei.getDataList(TestOrder.class);
			for (TestOrder testOrder : list){
				try{
					testOrderService.save(testOrder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条订单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入订单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/order/testOrder/?repage";
    }
	
	/**
	 * 下载导入订单数据模板
	 */
	@RequiresPermissions("order:testOrder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单数据导入模板.xlsx";
    		List<TestOrder> list = Lists.newArrayList(); 
    		new ExportExcel("订单数据", TestOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/order/testOrder/?repage";
    }
	
	
	

}