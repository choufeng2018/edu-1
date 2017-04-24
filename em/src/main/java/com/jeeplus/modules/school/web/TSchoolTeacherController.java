/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.web;

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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.school.entity.TCommSubject;
import com.jeeplus.modules.school.entity.TSchoolTeacher;
import com.jeeplus.modules.school.service.TCommSubjectService;
import com.jeeplus.modules.school.service.TSchoolTeacherService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 教师信息管理Controller
 * @author fly
 * @version 2016-09-12
 */
@Controller
@RequestMapping(value = "${adminPath}/school/tSchoolTeacher")
public class TSchoolTeacherController extends BaseController {

	@Autowired
	private TSchoolTeacherService tSchoolTeacherService;
	
	@Autowired
	private TCommSubjectService tCommSubjectService;
	
	@ModelAttribute
	public TSchoolTeacher get(@RequestParam(required=false) String id) {
		TSchoolTeacher entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSchoolTeacherService.get(id);
		}
		if (entity == null){
			entity = new TSchoolTeacher();
		}
		return entity;
	}
	
	/**
	 * 教师信息列表页面
	 */
	@RequiresPermissions("school:tSchoolTeacher:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSchoolTeacher tSchoolTeacher, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		tSchoolTeacher = setSchoolAndCampus(tSchoolTeacher);
//		String sqlMap = "";
//		if (tSchoolTeacher.getCampus()!=null && tSchoolTeacher.getCampus().getId()!=null && !"".equals(tSchoolTeacher.getCampus().getId()))
//		{
//			sqlMap = "AND a.campus_id in ('"+ tSchoolTeacher.getCampus().getId() +"')";
//		} else if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
//		{
//			Office o = UserUtils.getUser().getCompany();
//			sqlMap = "AND a.campus_id in ('"+ o.getId() +"', '"+o.getParentId()+"')";
//		}
//		tSchoolTeacher.getSqlMap().put("sqlmap", sqlMap);
		Page<TSchoolTeacher> page = tSchoolTeacherService.findPage(new Page<TSchoolTeacher>(request, response), tSchoolTeacher); 
		model.addAttribute("page", page);
		model.addAttribute("isparent", UserUtils.getUserIsParent());
		return "modules/school/tSchoolTeacherList";
	}
	
	/**
	 * 教师信息列表页面
	 */ 
	@RequestMapping(value = {"selectlist", ""})
	public String selectlist(@RequestParam(required=false) String subject, TSchoolTeacher tSchoolTeacher, HttpServletRequest request, HttpServletResponse response, Model model) {
		/**
		tSchoolTeacher = setSchoolAndCampus(tSchoolTeacher); 
		tSchoolTeacher.setIsoto(1);
		if (subject!=null && !"".equals(subject)) {
			if (tSchoolTeacher.getCourse()==null)
				tSchoolTeacher.setCourse(new TCommSubject());
			tSchoolTeacher.getCourse().setId(subject);
		}
		Page<TSchoolTeacher> page = tSchoolTeacherService.findPage(new Page<TSchoolTeacher>(request, response, -1), tSchoolTeacher); 
		model.addAttribute("page", page);
		model.addAttribute("isparent", UserUtils.getUserIsParent());
		**/
		model.addAttribute("subject", subject);
		//return "modules/school/tSchoolTeacherSelectList";
		return "modules/school/tSchoolTeacherIndex";
	}

	/**
	 * 查看，增加，编辑教师信息表单页面
	 */
	@RequiresPermissions(value={"school:tSchoolTeacher:view","school:tSchoolTeacher:add","school:tSchoolTeacher:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSchoolTeacher tSchoolTeacher, Model model) {
		if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
		{
			tSchoolTeacher.setCampus(UserUtils.getUser().getCompany());
		}
		model.addAttribute("tSchoolTeacher", tSchoolTeacher);
		return "modules/school/tSchoolTeacherForm";
	}

	/**
	 * 保存教师信息
	 */
	@RequiresPermissions(value={"school:tSchoolTeacher:add","school:tSchoolTeacher:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSchoolTeacher tSchoolTeacher, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tSchoolTeacher)){
			return form(tSchoolTeacher, model);
		}
		if (tSchoolTeacher.getSchoolId() == null || "".equals(tSchoolTeacher.getSchoolId()))
			tSchoolTeacher.setSchoolId(UserUtils.getUser().getSchool().getId());
		if(!tSchoolTeacher.getIsNewRecord()){//编辑表单保存
			TSchoolTeacher t = tSchoolTeacherService.get(tSchoolTeacher.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSchoolTeacher, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tSchoolTeacherService.save(t);//保存
		}else{//新增表单保存
			tSchoolTeacherService.save(tSchoolTeacher);//保存
		}
		addMessage(redirectAttributes, "保存教师信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacher/list?repage";
	}
	
	/**
	 * 删除教师信息
	 */
	@RequiresPermissions("school:tSchoolTeacher:del")
	@RequestMapping(value = "delete")
	public String delete(TSchoolTeacher tSchoolTeacher, RedirectAttributes redirectAttributes) {
		tSchoolTeacherService.delete(tSchoolTeacher);
		addMessage(redirectAttributes, "删除教师信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacher/list?repage";
	}
	
	/**
	 * 批量删除教师信息
	 */
	@RequiresPermissions("school:tSchoolTeacher:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tSchoolTeacherService.delete(tSchoolTeacherService.get(id));
		}
		addMessage(redirectAttributes, "删除教师信息成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacher/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("school:tSchoolTeacher:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSchoolTeacher tSchoolTeacher, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			tSchoolTeacher = setSchoolAndCampus(tSchoolTeacher);
			
            String fileName = "教师信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSchoolTeacher> page = tSchoolTeacherService.findPage(new Page<TSchoolTeacher>(request, response, -1), tSchoolTeacher);
    		new ExportExcel("教师信息", TSchoolTeacher.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出教师信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacher/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("school:tSchoolTeacher:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSchoolTeacher> list = ei.getDataList(TSchoolTeacher.class);
			for (TSchoolTeacher tSchoolTeacher : list){
				try{
					tSchoolTeacherService.save(tSchoolTeacher);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条教师信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条教师信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入教师信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacher/list?repage";
    }
	
	/**
	 * 下载导入教师信息数据模板
	 */
	@RequiresPermissions("school:tSchoolTeacher:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "教师信息数据导入模板.xlsx";
    		List<TSchoolTeacher> list = Lists.newArrayList(); 
    		new ExportExcel("教师信息数据", TSchoolTeacher.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacher/list?repage";
    }
	
	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	//@RequiresPermissions()
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String subject, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		
		TSchoolTeacher tsTeacher = new TSchoolTeacher();
		TCommSubject course = new TCommSubject();
		course.setId(subject);
		tsTeacher.setCourse(course);
		tsTeacher.setSchoolId(UserUtils.getUser().getSchool().getId());
		String sqlMap = "";
		if (UserUtils.getUser().getCompany().getParentId().equals("0"))
		{
		} else {
			Office o = UserUtils.getUser().getCompany();
			sqlMap = " AND a.campus_id in ('"+ o.getId() +"', '"+o.getParentId()+"')";
		}
		tsTeacher.getSqlMap().put("sqlmap", sqlMap);
			
		List<TSchoolTeacher> list = tSchoolTeacherService.findList(tsTeacher);
		for (int i=0; i<list.size(); i++){
			TSchoolTeacher e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "F"+e.getCourse().getId());
			map.put("pIds", e.getPrice()+","+e.getCmin());
			map.put("name", e.getName());
			map.put("parice", e.getPrice());
			mapList.add(map);
		} 
		
		TCommSubject ts = tCommSubjectService.get(subject);
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", "F"+ts.getId());
		map.put("pId", "0");
		map.put("pIds", "0");
		map.put("name", ts.getSubjectName()); 
		map.put("isParent", true);
		mapList.add(map);
		
		return mapList;
	}
	
	/**
	 * 验证电话是否有效
	 * @param tel
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value={"school:tSchoolTeacher:add","school:tSchoolTeacher:edit"},logical=Logical.OR)
	@RequestMapping(value = "checkMobile")
	public String checkMobile(String oldtel, String tel) {
		TSchoolTeacher tsTeacher = new TSchoolTeacher();
		tsTeacher.setTel(tel);
		tsTeacher.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (tel != null && tel.equals(oldtel)) {
			return "true";
		} else if (tel !=null && tSchoolTeacherService.getUserByTel(tsTeacher) == null) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 验证登录名是否有效
	 * @param oldAccount
	 * @param account
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value={"sys:user:add","sys:user:edit"},logical=Logical.OR)
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldAccount, String account) {
		TSchoolTeacher tsTeacher = new TSchoolTeacher();
		tsTeacher.setAccount(account);
		if (account !=null && account.equals(oldAccount)) {
			return "true";
		} else if (account !=null && tSchoolTeacherService.getUserByAccount(tsTeacher) == null) {
			return "true";
		}
		return "false";
	}
	
	public static TSchoolTeacher setSchoolAndCampus(TSchoolTeacher tSchoolTeacher)
	{
		if (tSchoolTeacher.getSchoolId()==null || "".equals(tSchoolTeacher.getSchoolId()))
			tSchoolTeacher.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
		{
			if (tSchoolTeacher.getCampus()==null || "".equals(tSchoolTeacher.getCampus().getId())|| tSchoolTeacher.getCampus().getId()==null)
				tSchoolTeacher.setCampus(UserUtils.getUser().getCompany());
			//else
				//tCourseClass.setCampus(new Office(""));
		}
		return tSchoolTeacher;
	}
}