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
import com.jeeplus.modules.school.entity.TSchoolRoom;
import com.jeeplus.modules.school.service.TCommSubjectService;
import com.jeeplus.modules.school.service.TSchoolRoomService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 教室信息管理Controller
 * @author fly
 * @version 2016-09-09
 */
@Controller
@RequestMapping(value = "${adminPath}/school/tSchoolRoom")
public class TSchoolRoomController extends BaseController {

	@Autowired
	private TSchoolRoomService tSchoolRoomService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private TCommSubjectService tCommSubjectService;
	
	@ModelAttribute
	public TSchoolRoom get(@RequestParam(required=false) String id) {
		TSchoolRoom entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSchoolRoomService.get(id);
		}
		if (entity == null){
			entity = new TSchoolRoom();
		}
		return entity;
	}
	
	/**
	 * 教室列表页面
	 */
	@RequiresPermissions("school:tSchoolRoom:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSchoolRoom tSchoolRoom, HttpServletRequest request, HttpServletResponse response, Model model) {
		String sqlMap = "";
		if (tSchoolRoom.getSchool()==null || tSchoolRoom.getSchool().getSchoolCode()==null || "".equals(tSchoolRoom.getSchool().getSchoolCode()))
			tSchoolRoom.setSchool(UserUtils.getUser().getSchool());
		if (tSchoolRoom.getCampus()==null || tSchoolRoom.getCampus().getId()==null || "".equals(tSchoolRoom.getCampus().getId()))
		{
			if (!UserUtils.getUserIsParent())
			{
				tSchoolRoom.setCampus(UserUtils.getUser().getCompany());
			}
		} 
//		if (tSchoolRoom.getCampus()!=null && tSchoolRoom.getCampus().getId()!=null && !"".equals(tSchoolRoom.getCampus().getId()))
//		{
//			sqlMap = "AND a.campus_id in ('"+ tSchoolRoom.getCampus().getId() +"')";
//		} else if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
//		{
//			Office o = UserUtils.getUser().getCompany();
//			sqlMap = "AND a.campus_id in ('"+ o.getId() +"', '"+o.getParentId()+"')";
//		}
//		tSchoolRoom.setSqlMapStr(sqlMap);
		Page<TSchoolRoom> page = tSchoolRoomService.findPage(new Page<TSchoolRoom>(request, response), tSchoolRoom); 
		model.addAttribute("page", page);
		model.addAttribute("isparent", UserUtils.getUserIsParent());
		return "modules/school/tSchoolRoomList";
	}

	/**
	 * 查看，增加，编辑教室表单页面
	 */
	@RequiresPermissions(value={"school:tSchoolRoom:view","school:tSchoolRoom:add","school:tSchoolRoom:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSchoolRoom tSchoolRoom, Model model) {
		if (tSchoolRoom.getSchool()==null || tSchoolRoom.getSchool().getId()==null || "".equals(tSchoolRoom.getSchool().getId()))
			tSchoolRoom.setSchool(UserUtils.getUser().getSchool());
		if (!UserUtils.getUserIsParent())
		{
			tSchoolRoom.setCampus(UserUtils.getUser().getCompany());
		}
		model.addAttribute("tSchoolRoom", tSchoolRoom);
		return "modules/school/tSchoolRoomForm";
	}

	/**
	 * 保存教室
	 */
	@RequiresPermissions(value={"school:tSchoolRoom:add","school:tSchoolRoom:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSchoolRoom tSchoolRoom, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (tSchoolRoom.getSchool()==null || tSchoolRoom.getSchool().getSchoolCode()==null || "".equals(tSchoolRoom.getSchool().getSchoolCode()))
			tSchoolRoom.setSchool(UserUtils.getUser().getSchool());
		if (!beanValidator(model, tSchoolRoom)){
			return form(tSchoolRoom, model);
		}
		if (tSchoolRoom.getUseType()==2 && (tSchoolRoom.getCourse()==null || tSchoolRoom.getCourse().getId() == null || "".equals(tSchoolRoom.getCourse().getId())))
		{
			addMessage(redirectAttributes, "保存失败：专用教室请先选择用途学科！");
		} else {
			if (tSchoolRoom.getCourse()!=null && (tSchoolRoom.getCourse().getId()==null || "".equals(tSchoolRoom.getCourse().getId())))
				tSchoolRoom.getCourse().setId("0");
			if (tSchoolRoom.getUseType()==1 && tSchoolRoom.getCourse()!=null && !"0".equals(tSchoolRoom.getId()))
			{
				tSchoolRoom.getCourse().setId("0");
			}
			if (!tSchoolRoom.getIsNewRecord()) {//编辑表单保存
				TSchoolRoom t = tSchoolRoomService.get(tSchoolRoom.getId());//从数据库取出记录的值
				MyBeanUtils.copyBeanNotNull2Bean(tSchoolRoom, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
				tSchoolRoomService.save(t);//保存
			} else {//新增表单保存
				tSchoolRoomService.save(tSchoolRoom);//保存
			}
			addMessage(redirectAttributes, "保存教室成功");
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolRoom/list?repage";
	}
	
	/**
	 * 删除教室
	 */
	@RequiresPermissions("school:tSchoolRoom:del")
	@RequestMapping(value = "delete")
	public String delete(TSchoolRoom tSchoolRoom, RedirectAttributes redirectAttributes) {
		tSchoolRoomService.delete(tSchoolRoom);
		addMessage(redirectAttributes, "删除教室成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolRoom/list?repage";
	}
	
	/**
	 * 状态更新教室
	 */
	@RequiresPermissions("school:tSchoolRoom:edit")
	@RequestMapping(value = "ustatus")
	public String ustatus(TSchoolRoom tSchoolRoom, RedirectAttributes redirectAttributes) {
		if(tSchoolRoom.getStatus()==0) tSchoolRoom.setStatus(1);
		else tSchoolRoom.setStatus(0);
		tSchoolRoomService.updatestatus(tSchoolRoom);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolRoom/list?repage";
	}
	
	/**
	 * 批量删除教室
	 */
	@RequiresPermissions("school:tSchoolRoom:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tSchoolRoomService.delete(tSchoolRoomService.get(id));
		}
		addMessage(redirectAttributes, "删除教室成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolRoom/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("school:tSchoolRoom:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSchoolRoom tSchoolRoom, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "教室"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSchoolRoom> page = tSchoolRoomService.findPage(new Page<TSchoolRoom>(request, response, -1), tSchoolRoom);
    		new ExportExcel("教室", TSchoolRoom.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出教室记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolRoom/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("school:tSchoolRoom:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSchoolRoom> list = ei.getDataList(TSchoolRoom.class);
			for (TSchoolRoom tSchoolRoom : list){
				try{
					tSchoolRoomService.save(tSchoolRoom);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条教室记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条教室记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入教室失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolRoom/list?repage";
    }
	
	/**
	 * 下载导入教室数据模板
	 */
	@RequiresPermissions("school:tSchoolRoom:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "教室数据导入模板.xlsx";
    		List<TSchoolRoom> list = Lists.newArrayList(); 
    		new ExportExcel("教室数据", TSchoolRoom.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolRoom/list?repage";
    }
	
	/**
	 * 获取机构JSON数据。
	 * @param campus 校区的ID
	 * @param response
	 * @return
	 */
	//@RequiresPermissions(value={"school:tSchoolTeacher:view","school:tSchoolTeacher:add","school:tSchoolTeacher:edit"},logical=Logical.OR)
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String campus, @RequestParam(required=false) String subject, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		
		TSchoolRoom tsRoom = new TSchoolRoom();
		Office o = new Office();
		o.setId(campus);
		tsRoom.setCampus(o);
		tsRoom.setSchool(UserUtils.getUser().getSchool());
		if (subject!=null && !"".equals(subject))
		{
			TCommSubject ts = tCommSubjectService.get(subject);
			String sql = " and (a.use_type=1 or (a.use_type=2 and a.use_subject in ("+ts.getId() +","+ts.getParent().getId()+"))) ";
			tsRoom.getSqlMap().put("dsf", sql);
		}
		//tsRoom.getCourse().setSubjectCode(subject);
		List<TSchoolRoom> list = tSchoolRoomService.findList(tsRoom);
		for (int i=0; i<list.size(); i++){
			TSchoolRoom e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "C"+e.getCampus().getId());
			map.put("pIds", e.getId());
			map.put("name", e.getRoomDesc() + "(" +(e.getUseType()==1?"公用教室":e.getCourse().getSubjectName()) + ")");
			mapList.add(map);
		} 
		
		o = officeService.get(campus);
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", "C"+o.getId());
		map.put("pId", "0");
		map.put("pIds", "0");
		map.put("name", o.getName());
		map.put("isParent", true);
		mapList.add(map);
		 
		return mapList;
	}
	

}