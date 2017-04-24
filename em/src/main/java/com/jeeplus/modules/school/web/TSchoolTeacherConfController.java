/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.web;

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
import com.jeeplus.modules.school.entity.TSchoolTeacher;
import com.jeeplus.modules.school.entity.TSchoolTeacherConf;
import com.jeeplus.modules.school.service.TSchoolTeacherConfService;
import com.jeeplus.modules.school.service.TSchoolTeacherService;
import com.jeeplus.modules.subject.dao.TCourseClassTimeDao;
import com.jeeplus.modules.subject.entity.TCourseClass;
import com.jeeplus.modules.subject.entity.TCourseClassTime;
import com.jeeplus.modules.subject.entity.TCourseTimetable;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 教师一对一教学执行时间配置Controller
 * @author flying
 * @version 2017-01-15
 */
@Controller
@RequestMapping(value = "${adminPath}/school/tSchoolTeacherConf")
public class TSchoolTeacherConfController extends BaseController {

	@Autowired
	private TSchoolTeacherConfService tSchoolTeacherConfService;
	
	@Autowired
	private TSchoolTeacherService tSchoolTeacherService;
	
	@ModelAttribute
	public TSchoolTeacherConf get(@RequestParam(required=false) String id) {
		TSchoolTeacherConf entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSchoolTeacherConfService.get(id);
		}
		if (entity == null){
			entity = new TSchoolTeacherConf();
		}
		return entity;
	}
	
	/**
	 * 排课列表页面
	 */
	@RequiresPermissions("school:tSchoolTeacherConf:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSchoolTeacherConf tSchoolTeacherConf, HttpServletRequest request, HttpServletResponse response, Model model) {
		setSchoolAndCampus(tSchoolTeacherConf);
		Page<TSchoolTeacherConf> page = tSchoolTeacherConfService.findPage(new Page<TSchoolTeacherConf>(request, response), tSchoolTeacherConf); 
		model.addAttribute("page", page);
		return "modules/school/tSchoolTeacherConfList";
	}
	
	/**
	 * 排课列表页面
	 */
	//@RequiresPermissions("school:tSchoolTeacherConf:list")
	@RequestMapping(value = {"view", ""})
	public String view(TSchoolTeacherConf tSchoolTeacherConf, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		model.addAttribute("tSchoolTeacherConf", tSchoolTeacherConf);
		
		List <TCourseTimetable> timeList = new ArrayList<TCourseTimetable>();
		
		if (tSchoolTeacherConf.getId()==null)
		{
			TSchoolTeacherConf t = tSchoolTeacherConfService.getByTid(tSchoolTeacherConf);
			if (t != null)
				tSchoolTeacherConf = t;
			else
			{
				model.addAttribute("timeList", timeList);
				return "modules/school/tSchoolTeacherConfViewList";
			}
		}
		
		TCourseTimetable tt1 = null;
		String beginTime = "", endtime = ""; 
		if (tSchoolTeacherConf.getIsm()!=null && tSchoolTeacherConf.getIsm()==1)
		{
			do {
				tt1 = new TCourseTimetable();
				if ("".equals(beginTime))
					beginTime = "2000-01-01 " + tSchoolTeacherConf.getMtimeshour() + ":" + tSchoolTeacherConf.getMtimesmin() + ":00";
				endtime = DateUtil.long2string(DateUtil.string2long(beginTime) + tSchoolTeacherConf.getCmin()*60);
				
				tt1.setBeginTime(DateUtils.parseDate(beginTime));
				tt1.setEndTime(DateUtils.parseDate(endtime));
				
				TCourseClassTime tCourseClassTime = new TCourseClassTime();
				tCourseClassTime.setBegintime(DateUtils.parseDate(beginTime));
				tCourseClassTime.setEndtime(DateUtils.parseDate(endtime));
				TCourseClass tcc = new TCourseClass();
				tcc.setTeacher(tSchoolTeacherConf.getTeacher());
				tCourseClassTime.setCsId(tcc);
				List<TCourseClassTime> ltList = tSchoolTeacherConfService.findListForTeac(tCourseClassTime);
				for (TCourseClassTime tct : ltList)
				{
					if (tct.getWeek()==1) {tt1.setDay1(true); break;}
					else if (tct.getWeek()==2) {tt1.setDay2(true); break;}
					else if (tct.getWeek()==3) {tt1.setDay3(true); break;}
					else if (tct.getWeek()==4) {tt1.setDay4(true); break;}
					else if (tct.getWeek()==5) {tt1.setDay5(true); break;}
					else if (tct.getWeek()==6) {tt1.setDay6(true); break;}
					else if (tct.getWeek()==7) {tt1.setDay7(true);}
				}
				
				timeList.add(tt1);
				beginTime = DateUtil.long2string(DateUtil.string2long(endtime) + tSchoolTeacherConf.getRmin()*60);
			} while ( DateUtil.string2long("2000-01-01 12:00:00") > DateUtil.string2long(beginTime));
		}
		beginTime = ""; 
		if (tSchoolTeacherConf.getIsa()!=null && tSchoolTeacherConf.getIsa()==1)
		{
			do {
				tt1 = new TCourseTimetable();
				String beginTimeTemp = "2000-01-01 " + tSchoolTeacherConf.getAtimeehour() + ":" + tSchoolTeacherConf.getAtimeemin() + ":00";
				if ("".equals(beginTime))
					if (!"".equals(endtime) && DateUtil.string2long(endtime) > DateUtil.string2long(beginTimeTemp))
						beginTime = endtime;
					else
						beginTime = beginTimeTemp;
				endtime = DateUtil.long2string(DateUtil.string2long(beginTime) + tSchoolTeacherConf.getCmin()*60);
				
				tt1.setBeginTime(DateUtils.parseDate(beginTime));
				tt1.setEndTime(DateUtils.parseDate(endtime));
				
				TCourseClassTime tCourseClassTime = new TCourseClassTime();
				tCourseClassTime.setBegintime(DateUtils.parseDate(beginTime));
				tCourseClassTime.setEndtime(DateUtils.parseDate(endtime));
				TCourseClass tcc = new TCourseClass();
				tcc.setTeacher(tSchoolTeacherConf.getTeacher());
				tCourseClassTime.setCsId(tcc);
				List<TCourseClassTime> ltList = tSchoolTeacherConfService.findListForTeac(tCourseClassTime);
				for (TCourseClassTime tct : ltList)
				{
					if (tct.getWeek()==1) tt1.setDay1(true);
					else if (tct.getWeek()==2) tt1.setDay2(true);
					else if (tct.getWeek()==3) tt1.setDay3(true);
					else if (tct.getWeek()==4) tt1.setDay4(true);
					else if (tct.getWeek()==5) tt1.setDay5(true);
					else if (tct.getWeek()==6) tt1.setDay6(true);
					else if (tct.getWeek()==7) tt1.setDay7(true);
				}
				
				timeList.add(tt1);
				beginTime = DateUtil.long2string(DateUtil.string2long(endtime) + tSchoolTeacherConf.getRmin()*60);
			} while ( DateUtil.string2long("2000-01-01 19:00:00") > DateUtil.string2long(beginTime));
		}
		beginTime = "";
		if (tSchoolTeacherConf.getIsn()!=null && tSchoolTeacherConf.getIsn()==1)
		{
			do {
				tt1 = new TCourseTimetable();
				String beginTimeTemp = "2000-01-01 " + tSchoolTeacherConf.getNtimeehour() + ":" + tSchoolTeacherConf.getNtimeemin() + ":00";
				if ("".equals(beginTime))
					if (!"".equals(endtime) && DateUtil.string2long(endtime) > DateUtil.string2long(beginTimeTemp))
						beginTime = endtime;
					else
						beginTime = beginTimeTemp;
				endtime = DateUtil.long2string(DateUtil.string2long(beginTime) + tSchoolTeacherConf.getCmin()*60);
				
				tt1.setBeginTime(DateUtils.parseDate(beginTime));
				tt1.setEndTime(DateUtils.parseDate(endtime));
				
				TCourseClassTime tCourseClassTime = new TCourseClassTime();
				tCourseClassTime.setBegintime(DateUtils.parseDate(beginTime));
				tCourseClassTime.setEndtime(DateUtils.parseDate(endtime));
				TCourseClass tcc = new TCourseClass();
				tcc.setTeacher(tSchoolTeacherConf.getTeacher());
				tCourseClassTime.setCsId(tcc);
				List<TCourseClassTime> ltList = tSchoolTeacherConfService.findListForTeac(tCourseClassTime);
				for (TCourseClassTime tct : ltList)
				{
					if (tct.getWeek()==1) tt1.setDay1(true);
					else if (tct.getWeek()==2) tt1.setDay2(true);
					else if (tct.getWeek()==3) tt1.setDay3(true);
					else if (tct.getWeek()==4) tt1.setDay4(true);
					else if (tct.getWeek()==5) tt1.setDay5(true);
					else if (tct.getWeek()==6) tt1.setDay6(true);
					else if (tct.getWeek()==7) tt1.setDay7(true);
				}
				
				timeList.add(tt1);
				beginTime = DateUtil.long2string(DateUtil.string2long(endtime) + tSchoolTeacherConf.getRmin()*60);
			} while ( DateUtil.string2long("2000-01-01 22:00:00") > DateUtil.string2long(beginTime));
		}
		
		model.addAttribute("timeList", timeList);
		return "modules/school/tSchoolTeacherConfViewList";
	}

	/**
	 * 查看，增加，编辑排课表单页面
	 */
	@RequiresPermissions(value={"school:tSchoolTeacherConf:view","school:tSchoolTeacherConf:add","school:tSchoolTeacherConf:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSchoolTeacherConf tSchoolTeacherConf, Model model) {
		if (tSchoolTeacherConf.getId()==null || "".equals(tSchoolTeacherConf.getId()))
		{
			TSchoolTeacherConf t = tSchoolTeacherConfService.getByTid(tSchoolTeacherConf);
			if (t == null)
				tSchoolTeacherConf.setTeacher(tSchoolTeacherService.get(tSchoolTeacherConf.getTeacher().getId()));
			else 
				tSchoolTeacherConf = t;
		}
		model.addAttribute("tSchoolTeacherConf", tSchoolTeacherConf);
		return "modules/school/tSchoolTeacherConfForm";
	}
	
	/**
	 * 查看，增加，编辑排课表单页面
	 */
	@RequiresPermissions(value={"school:tSchoolTeacherConf:view","school:tSchoolTeacherConf:add","school:tSchoolTeacherConf:edit"},logical=Logical.OR)
	@RequestMapping(value = "teacform")
	public String teacform(TSchoolTeacherConf tSchoolTeacherConf, Model model) {
		TSchoolTeacherConf tSchoolTeacherConf1 = tSchoolTeacherConfService.getByTid(tSchoolTeacherConf);
		if (tSchoolTeacherConf1 != null)
			tSchoolTeacherConf = tSchoolTeacherConf1;
			
		model.addAttribute("tSchoolTeacherConf", tSchoolTeacherConf);
		return "modules/school/tSchoolTeacherConfForm";
	}

	/**
	 * 保存排课
	 */
	@RequiresPermissions(value={"school:tSchoolTeacherConf:add","school:tSchoolTeacherConf:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSchoolTeacherConf tSchoolTeacherConf, Model model, RedirectAttributes redirectAttributes) throws Exception{
		tSchoolTeacherConf.setMtime(DateUtils.parseDate("2000-01-01 "+tSchoolTeacherConf.getMtimeshour()+":"+tSchoolTeacherConf.getMtimesmin()+":00"));
		tSchoolTeacherConf.setAtime(DateUtils.parseDate("2000-01-01 "+tSchoolTeacherConf.getAtimeehour()+":"+tSchoolTeacherConf.getAtimeemin()+":00"));
		tSchoolTeacherConf.setNtime(DateUtils.parseDate("2000-01-01 "+tSchoolTeacherConf.getNtimeehour()+":"+tSchoolTeacherConf.getNtimeemin()+":00"));
		
		tSchoolTeacherConf.setEmtime(DateUtils.parseDate("2000-01-01 "+tSchoolTeacherConf.getEmtimeshour()+":"+tSchoolTeacherConf.getEmtimesmin()+":00"));
		tSchoolTeacherConf.setEatime(DateUtils.parseDate("2000-01-01 "+tSchoolTeacherConf.getEatimeehour()+":"+tSchoolTeacherConf.getEatimeemin()+":00"));
		tSchoolTeacherConf.setEntime(DateUtils.parseDate("2000-01-01 "+tSchoolTeacherConf.getEntimeehour()+":"+tSchoolTeacherConf.getEntimeemin()+":00"));
		
		if (!beanValidator(model, tSchoolTeacherConf)){
			return form(tSchoolTeacherConf, model);
		}
		if(!tSchoolTeacherConf.getIsNewRecord()){//编辑表单保存
			TSchoolTeacherConf t = tSchoolTeacherConfService.get(tSchoolTeacherConf.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSchoolTeacherConf, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tSchoolTeacherConfService.save(t);//保存
		}else{//新增表单保存
			tSchoolTeacherConfService.save(tSchoolTeacherConf);//保存
		}
		addMessage(redirectAttributes, "排课成功");
		//return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacherConf/?repage";
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacher/list?repage";
	}
	
	/**
	 * 删除排课
	 */
	@RequiresPermissions("school:tSchoolTeacherConf:del")
	@RequestMapping(value = "delete")
	public String delete(TSchoolTeacherConf tSchoolTeacherConf, RedirectAttributes redirectAttributes) {
		tSchoolTeacherConfService.delete(tSchoolTeacherConf);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacherConf/?repage";
	}
	
	/**
	 * 批量删除排课
	 */
	@RequiresPermissions("school:tSchoolTeacherConf:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tSchoolTeacherConfService.delete(tSchoolTeacherConfService.get(id));
		}
		addMessage(redirectAttributes, "删除排课成功");
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacherConf/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("school:tSchoolTeacherConf:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSchoolTeacherConf tSchoolTeacherConf, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "排课"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSchoolTeacherConf> page = tSchoolTeacherConfService.findPage(new Page<TSchoolTeacherConf>(request, response, -1), tSchoolTeacherConf);
    		new ExportExcel("排课", TSchoolTeacherConf.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出排课记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacherConf/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("school:tSchoolTeacherConf:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSchoolTeacherConf> list = ei.getDataList(TSchoolTeacherConf.class);
			for (TSchoolTeacherConf tSchoolTeacherConf : list){
				try{
					tSchoolTeacherConfService.save(tSchoolTeacherConf);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条排课记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条排课记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入排课失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacherConf/?repage";
    }
	
	/**
	 * 下载导入排课数据模板
	 */
	@RequiresPermissions("school:tSchoolTeacherConf:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "排课数据导入模板.xlsx";
    		List<TSchoolTeacherConf> list = Lists.newArrayList(); 
    		new ExportExcel("排课数据", TSchoolTeacherConf.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/school/tSchoolTeacherConf/?repage";
    }
	
	public static TSchoolTeacherConf setSchoolAndCampus(TSchoolTeacherConf tSchoolTeacherConf)
	{
		if (tSchoolTeacherConf.getTeacher()==null)
			tSchoolTeacherConf.setTeacher(new TSchoolTeacher());
		if (tSchoolTeacherConf.getTeacher().getSchoolId()==null || "".equals(tSchoolTeacherConf.getTeacher().getSchoolId()))
			tSchoolTeacherConf.getTeacher().setSchoolId(UserUtils.getUser().getSchool().getId());
		if (!UserUtils.getUser().getCompany().getParentId().equals("0"))
		{
			if (tSchoolTeacherConf.getTeacher().getCampus()==null || "".equals(tSchoolTeacherConf.getTeacher().getCampus().getId())|| tSchoolTeacherConf.getTeacher().getCampus().getId()==null)
				tSchoolTeacherConf.getTeacher().setCampus(UserUtils.getUser().getCompany());
			//else
				//tCourseClass.setCampus(new Office(""));
		} else{
			if (tSchoolTeacherConf.getTeacher().getCampus()==null)
				tSchoolTeacherConf.getTeacher().setCampus(new Office());
		}
		return tSchoolTeacherConf;
	}
	

}