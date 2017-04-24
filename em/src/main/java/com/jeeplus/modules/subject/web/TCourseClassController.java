/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.subject.web;

import java.util.ArrayList;
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
import com.jeeplus.modules.iim.utils.DateUtil;
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.subject.entity.TCourseClassTime;
import com.jeeplus.modules.subject.entity.TCourseTimetable;
import com.jeeplus.modules.subject.entity.TDayTime;
import com.jeeplus.modules.subject.service.TCourseClassService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 课程方案管理Controller
 * @author fly
 * @version 2016-09-13
 */
@Controller
@RequestMapping(value = "${adminPath}/subject/tCourseClass")
public class TCourseClassController extends BaseController {

	@Autowired
	private TCourseClassService tCourseClassService; 
	
	@ModelAttribute
	public TCourseClass get(@RequestParam(required=false) String id) {
		TCourseClass entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tCourseClassService.get(id);
		}
		if (entity == null){
			entity = new TCourseClass();
			entity.gettCourseClassTimeList().add(new TCourseClassTime());
		}
		return entity;
	}
	
	/**
	 * 课程方案列表页面
	 */
	@RequiresPermissions("subject:tCourseClass:list")
	@RequestMapping(value = {"list", ""})
	public String list(TCourseClass tCourseClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		tCourseClass = setSchoolAndCampus(tCourseClass);
		tCourseClass.setClassType(2);
		
		Page<TCourseClass> page = tCourseClassService.findPage(new Page<TCourseClass>(request, response), tCourseClass); 
		model.addAttribute("page", page);
		model.addAttribute("isparent", UserUtils.getUserIsParent());
		return "modules/subject/tCourseClassList";
	}
	
	/**
	 * 课程方案列表页面
	 */
	@RequiresPermissions("subject:tCourseClass:list")
	@RequestMapping(value = {"minilist", ""})
	public String minilist(TCourseClass tCourseClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		tCourseClass = setSchoolAndCampus(tCourseClass);
		tCourseClass.setClassType(1);
		
		Page<TCourseClass> page = tCourseClassService.findPage(new Page<TCourseClass>(request, response), tCourseClass); 
		model.addAttribute("page", page);
		model.addAttribute("isparent", UserUtils.getUserIsParent());
		return "modules/subject/tCourseClassMiniList";
	}
	
	/**
	 * 班级整体升级
	 */
	@RequiresPermissions("subject:tCourseClass:list")
	@RequestMapping(value = {"uplist", ""})
	public String uplist(TCourseClass tCourseClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		tCourseClass = setSchoolAndCampus(tCourseClass);
		tCourseClass.setClassType(2);
		
		Page<TCourseClass> page = tCourseClassService.findPage(new Page<TCourseClass>(request, response), tCourseClass); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseClassUpList";
	}
	
	/**
	 * 教师查看课程安排情况
	 */
	@RequiresPermissions("subject:tCourseClass:list")
	@RequestMapping(value = {"teacherCourseList", ""})
	public String teacherCourseList(TCourseClass tCourseClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		tCourseClass = setSchoolAndCampus(tCourseClass);
		
		Page<TCourseClass> pageT = new Page<TCourseClass>(request, response, -1);
		pageT.setOrderBy(" begin_date desc ");
		Page<TCourseClass> page = tCourseClassService.findPage(pageT, tCourseClass); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseClassTeacherList";
	}
	

	/**
	 * 教师课程表
	 */
	@RequiresPermissions("subject:tCourseClass:list")
	@RequestMapping(value = {"teacherlist", ""})
	public String teacherlist(String datestr, TCourseClass tCourseClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		// 上课时间
		List<TDayTime> timeList = DateUtils.getTimeList2();
		
		// 所有课程
		tCourseClass = setSchoolAndCampus(tCourseClass);
		Page<TCourseClass> pageT = new Page<TCourseClass>(request, response, -1);
		pageT.setOrderBy(" begin_date desc ");
		Page<TCourseClass> page = tCourseClassService.findPage(pageT, tCourseClass); 
		List<TCourseClass> resultList = page.getList();
		
		for (int i=0; i<timeList.size(); i++)
		{
			List<List<TCourseClass>> tempList = new ArrayList<List<TCourseClass>>();
			List<TCourseClass> temp = new ArrayList<TCourseClass>();

			TDayTime tTDayTime = timeList.get(i);
			int size = 0; 
			// 循环课程  结果是按时间升序
			for (int k=1; k<8; k++){
				for (int j=0; j<resultList.size(); j++)
				{
					TCourseClass ttt = tCourseClassService.get(resultList.get(j).getId());
					for (TCourseClassTime tct : ttt.gettCourseClassTimeList())
					{
						if (tct.getWeek() == k )
						{
							int shour = Integer.parseInt(ttt.getShour());
							if (shour >= tTDayTime.getBegin()  && shour <= tTDayTime.getEnd())
								temp.add(ttt);
						}
					}
				} 
				
				tempList.add(temp);
				temp = new ArrayList<TCourseClass>();
			}
			
			
			tTDayTime.setTctList(tempList);
		 
		}
		model.addAttribute("timeList", timeList); 
		 
		return "modules/subject/tCourseTimetableWeekTeacherList";
	}
	
	
	/**
	 * 课程方案选择列表页面
	 */
	@RequestMapping(value = {"selectlist", ""})
	public String selectlist(TCourseClass tCourseClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		tCourseClass = setSchoolAndCampus(tCourseClass);
		tCourseClass.setClassType(2);
		
		Page<TCourseClass> page = tCourseClassService.findListForEnter(new Page<TCourseClass>(request, response), tCourseClass); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseClassSelectList";
	}
	
	/**
	 * 课程方案选择列表页面
	 */
	@RequestMapping(value = {"selectlistformissed", ""})
	public String selectlistformissed(TCourseClass tCourseClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		tCourseClass = setSchoolAndCampus(tCourseClass);
		tCourseClass.setClassType(2);
		
		Page<TCourseClass> page = tCourseClassService.findListForEnter(new Page<TCourseClass>(request, response), tCourseClass); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseClassSelectForMissedList";
	}
	
	/**
	 * 课程报名列表页面
	 */
	@RequiresPermissions("subject:tCourseClass:enterlist")
	@RequestMapping(value = {"enterlist", ""})
	public String enterlist(TCourseClass tCourseClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		tCourseClass = setSchoolAndCampus(tCourseClass);
		
		if (tCourseClass.getClassType()==null || tCourseClass.getClassType()==0)
			tCourseClass.setClassType(2);
		
		Page<TCourseClass> page = tCourseClassService.findListForEnter(new Page<TCourseClass>(request, response, 30), tCourseClass); 
		model.addAttribute("page", page);
		return "modules/subject/tCourseClassEnterList";
	}

	/**
	 * 查看，增加，编辑课程方案表单页面
	 */
	@RequiresPermissions(value={"subject:tCourseClass:view","subject:tCourseClass:add","subject:tCourseClass:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TCourseClass tCourseClass, Model model) {
		tCourseClass = setSchoolAndCampus(tCourseClass);
		if (tCourseClass.getCampus()==null || "".equals(tCourseClass.getCampus().getId())|| tCourseClass.getCampus().getId()==null)
		{
			addMessage(model, "您非小区管理人员，无权进行课程编排！");
		}
		
		model.addAttribute("isparent", UserUtils.getUserIsParent());
		model.addAttribute("tCourseClass", tCourseClass);
		return "modules/subject/tCourseClassForm";
	}
	
	/**
	 * 打开报名表
	 */
	@RequiresPermissions(value={"subject:tCourseClass:enterlist"},logical=Logical.OR)
	@RequestMapping(value = "enterform")
	public String enterform(TCourseClass tCourseClass, Model model) {
		tCourseClass = setSchoolAndCampus(tCourseClass);
		
		model.addAttribute("tCourseClass", tCourseClass);
		return "modules/subject/tCourseClassForm";
	}

	/**
	 * 保存课程方案
	 */
	@RequiresPermissions(value={"subject:tCourseClass:add","subject:tCourseClass:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TCourseClass tCourseClass, Model model, RedirectAttributes redirectAttributes) throws Exception{
		//tCourseClass.setBegintime(DateUtils.parseDate("2000-01-01 "+tCourseClass.getShour()+":"+tCourseClass.getSmin()+":00"));
		//tCourseClass.setEndtime(DateUtils.parseDate(DateUtil.long2string(DateUtil.string2long(DateUtils.formatDateTime(tCourseClass.getBegintime()))+tCourseClass.getClassMin()*60)));
		//tCourseClass.setEndtime(DateUtils.parseDate("2000-01-01 "+tCourseClass.getEhour()+":"+tCourseClass.getEmin()+":00"));
		if (tCourseClass.getSchoolId()==null || "".equals(tCourseClass.getSchoolId()))
			tCourseClass.setSchoolId(UserUtils.getUser().getSchool().getId());
		String teactime = "", slotdesc="";
		for (TCourseClassTime tCourseClassTime : tCourseClass.gettCourseClassTimeList())
		{
			if ("".equals(tCourseClassTime.getWeek()) || "".equals(tCourseClassTime.getShour()) || "".equals(tCourseClassTime.getSmin()))
			{
				addMessage(model, "请设置正确的时间！");
				return "redirect:"+Global.getAdminPath()+"/subject/tCourseClass/list?repage";
			}
			tCourseClassTime.setBegintime(DateUtils.parseDate("2000-01-01 "+tCourseClassTime.getShour()+":"+tCourseClassTime.getSmin()+":00"));
			tCourseClassTime.setEndtime(DateUtils.addMinutes(tCourseClassTime.getBegintime(), tCourseClass.getClassMin()));
			teactime += ("".equals(teactime)?"":",") + DateUtils.getWeekStringByWeek(tCourseClassTime.getWeek()) + 
					"(" + tCourseClassTime.getShour()+":"+tCourseClassTime.getSmin() + "-"+ DateUtils.formatDate(tCourseClassTime.getEndtime(), "HH:mm:ss").substring(0, 5) +")";
			int hour = Integer.parseInt(tCourseClassTime.getShour());
			slotdesc += ("".equals(slotdesc)?"":",") + (hour<12?"上午":(hour<18?"下午":"晚上"));
		}
		tCourseClass.setTeactime(teactime);
		tCourseClass.setSlotdesc(slotdesc);
		
		if (!beanValidator(model, tCourseClass)){
			return "redirect:"+Global.getAdminPath()+"/subject/tCourseClass/list?repage";
		}
		// 计算单课时费用
		boolean change = false;
		if (tCourseClass.getCost() > 0 && tCourseClass.getClassHour()>0)
			tCourseClass.setPercost(tCourseClass.getCost()/tCourseClass.getClassHour());
		if (!tCourseClass.getIsNewRecord()) {//编辑表单保存
			TCourseClass t = tCourseClassService.get(tCourseClass.getId());//从数据库取出记录的值
			if (t.gettCourseClassTimeList().size() != tCourseClass.gettCourseClassTimeList().size())
				change = true;
			else 
			{
				for (int i=0; i<tCourseClass.gettCourseClassTimeList().size(); i++)
				{
					if (tCourseClass.gettCourseClassTimeList().get(i).getWeek() != t.gettCourseClassTimeList().get(i).getWeek()
						|| tCourseClass.gettCourseClassTimeList().get(i).getShour() != t.gettCourseClassTimeList().get(i).getShour()
						|| tCourseClass.gettCourseClassTimeList().get(i).getSmin() != t.gettCourseClassTimeList().get(i).getSmin())
					{
						change = true;
						break;
					}
				}
			}
			
			MyBeanUtils.copyBeanNotNull2Bean(tCourseClass, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			
			tCourseClassService.saveDefId(t, change);//保存
		} else { //新增表单保存
			tCourseClass.setClassType(2);//默认创建1对多班级
			tCourseClass.setStuNum(0);
			tCourseClass.setIscreate(1); // 新增默认没创建课程表
			tCourseClass.setStatus(1);
			tCourseClassService.saveDefId(tCourseClass, true);//保存
		}
		addMessage(redirectAttributes, "保存课程方案成功");
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseClass/list?repage";
	}
	
	/**
	 * 删除课程方案
	 */
	@RequiresPermissions("subject:tCourseClass:del")
	@RequestMapping(value = "delete")
	public String delete(TCourseClass tCourseClass, RedirectAttributes redirectAttributes) {
		if (tCourseClass.getStatus() > 1) {
			addMessage(redirectAttributes, "当前课程状态已不允许删除课程信息！");
		} else if (tCourseClass.getStuNum() == 0) {
			tCourseClassService.delete(tCourseClass);
			
			addMessage(redirectAttributes, "删除课程方案成功");
		} else {
			addMessage(redirectAttributes, "已有学生报名，不能删除该课程，请核查！");
		}
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseClass/list?repage";
	}
	
	/**
	 * 审核课程方案
	 */
	@RequiresPermissions("subject:tCourseClass:check")
	@RequestMapping(value = "check")
	public String check(TCourseClass tCourseClass, RedirectAttributes redirectAttributes) {
		if (tCourseClass != null && tCourseClass.getStatus()==1) {
			tCourseClass.setStatus(2);
			tCourseClassService.updateStatus(tCourseClass);
		}
		addMessage(redirectAttributes, "课程方案审核成功，进入开始招生状态");
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseClass/list?repage";
	}
	
	/**
	 * 批量删除课程方案
	 */
	@RequiresPermissions("subject:tCourseClass:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tCourseClassService.delete(tCourseClassService.get(id));
		}
		addMessage(redirectAttributes, "删除课程方案成功");
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseClass/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("subject:tCourseClass:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TCourseClass tCourseClass, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			tCourseClass = setSchoolAndCampus(tCourseClass);
            String fileName = "课程方案"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TCourseClass> page = tCourseClassService.findPage(new Page<TCourseClass>(request, response, -1), tCourseClass);
    		new ExportExcel("课程方案", TCourseClass.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出课程方案记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseClass/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("subject:tCourseClass:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TCourseClass> list = ei.getDataList(TCourseClass.class);
			for (TCourseClass tCourseClass : list){
				try{
					tCourseClassService.save(tCourseClass);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条课程方案记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条课程方案记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入课程方案失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseClass/list?repage";
    }
	
	/**
	 * 下载导入课程方案数据模板
	 */
	@RequiresPermissions("subject:tCourseClass:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "课程方案数据导入模板.xlsx";
    		List<TCourseClass> list = Lists.newArrayList(); 
    		new ExportExcel("课程方案数据", TCourseClass.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseClass/list?repage";
    }
	
	/**
	 * 创建课程表
	 */
	@RequiresPermissions(value={"subject:tCourseTimetable:view","subject:tCourseTimetable:add","subject:tCourseTimetable:edit"},logical=Logical.OR)
	@RequestMapping(value = "createTimeTable")
	public String createTimeTable(String ids, RedirectAttributes redirectAttributes) {
		List<TCourseClass> tccList = tCourseClassService.findListByIds(ids);
		//String idArray[] = ids.split(",");
		for(TCourseClass tcc : tccList)
		{
			if (tcc.getIscreate() == null || tcc.getIscreate() == 0)
			{
				tcc.setIscreate(1);
				tCourseClassService.updateIsCreate(tcc);
			}
		}
		addMessage(redirectAttributes, "创建课程表成功");
		return "redirect:"+Global.getAdminPath()+"/subject/tCourseClass/list?repage";
	}
	
	public TCourseClass setSchoolAndCampus(TCourseClass tCourseClass)
	{
		if (tCourseClass.getSchoolId()==null || "".equals(tCourseClass.getSchoolId()))
			tCourseClass.setSchoolId(UserUtils.getUser().getSchool().getId());
		if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
		{
			if (tCourseClass.getCampus()==null || "".equals(tCourseClass.getCampus().getId())|| tCourseClass.getCampus().getId()==null)
				tCourseClass.setCampus(UserUtils.getUser().getCompany());
			//else
				//tCourseClass.setCampus(new Office(""));
		}
		return tCourseClass;
	}
	
	@RequestMapping(value = "dateselect") 
	public String dateselect(TCourseClass tCourseClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/subject/tCourseClassDateSelectList";
	}

}