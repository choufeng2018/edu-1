/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.em.web.school;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.em.entity.school.TCommSchool;
import com.jeeplus.modules.em.service.school.TCommSchoolService;
import com.jeeplus.modules.schoollevel.entity.TCommSchoolLevel;
import com.jeeplus.modules.sys.dao.UserDao;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.tools.utils.UtilTools;

/**
 * 学校租户Controller
 * @author fly
 * @version 2016-08-28
 */
@Controller
@RequestMapping(value = "${adminPath}/em/school/tCommSchool")
public class TCommSchoolController extends BaseController {

	@Autowired
	private TCommSchoolService tCommSchoolService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private UserDao userDao;
	
	@ModelAttribute
	public TCommSchool get(@RequestParam(required=false) String id) {
		TCommSchool entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tCommSchoolService.get(id);
		}
		if (entity == null){
			entity = new TCommSchool();
		}
		return entity;
	}
	
	/**
	 * 学校列表页面
	 */
	@RequiresPermissions("em:school:tCommSchool:list")
	@RequestMapping(value = {"list", ""})
	public String list(TCommSchool tCommSchool, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TCommSchool> page = tCommSchoolService.findPage(new Page<TCommSchool>(request, response), tCommSchool); 
		model.addAttribute("page", page);
		return "modules/em/school/tCommSchoolList";
	}
	
	/**
	 * 学校列表页面
	 */
	@RequiresPermissions("em:school:tCommSchool:list")
	@RequestMapping(value = {"selectlist", ""})
	public String selectlist(TCommSchool tCommSchool, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TCommSchool> page = tCommSchoolService.findPage(new Page<TCommSchool>(request, response), tCommSchool); 
		model.addAttribute("page", page);
		return "modules/em/school/tCommSchoolSelectList";
	}

	/**
	 * 查看，增加，编辑学校表单页面
	 */
	@RequiresPermissions(value={"em:school:tCommSchool:view","em:school:tCommSchool:add","em:school:tCommSchool:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TCommSchool tCommSchool, Model model) {
		model.addAttribute("tCommSchool", tCommSchool);
		return "modules/em/school/tCommSchoolForm";
	}
	
	/**
	 * 机构编辑学校表单页面
	 */
	@RequiresPermissions(value={"em:school:tCommSchool:view","em:school:tCommSchool:add","em:school:tCommSchool:edit"},logical=Logical.OR)
	@RequestMapping(value = "formself")
	public String formself(TCommSchool tCommSchool, Model model) {
		model.addAttribute("tCommSchool", tCommSchool);
		return "modules/em/school/tCommSchoolFormSelf";
	}
	
	//@RequiresPermissions(value={"em:school:tCommSchool:view","em:school:tCommSchool:add","em:school:tCommSchool:edit"},logical=Logical.OR)
	@RequestMapping(value = "info")
	public String info(Office office, Model model) {
		User user = UserUtils.getUser();  
		model.addAttribute("tCommSchool", tCommSchoolService.get(user.getSchool().getId()));
		return "modules/em/school/schoolInfo";
	}

	/**
	 * 保存学校
	 */
	@RequiresPermissions(value={"em:school:tCommSchool:add","em:school:tCommSchool:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TCommSchool tCommSchool, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (tCommSchool.getIsNewRecord()) {
			tCommSchool.setSchoolStatus(1); //启用状态
			tCommSchool.setSchoolType(1);   //试用用户
			if (tCommSchool.gettSchoolLevel()==null || tCommSchool.gettSchoolLevel().getId()==null ||
					"".equals(tCommSchool.gettSchoolLevel().getId()))
			{
				TCommSchoolLevel tsl = new TCommSchoolLevel();
				tsl.setId("1");
				tCommSchool.settSchoolLevel(tsl); // 注册时默认初级会员
			}
			// 设置学校编码
			TCommSchool tParam = new TCommSchool();
			tParam.setCity(tCommSchool.getCity());
			tParam = tCommSchoolService.getCount(tParam);
			tCommSchool.setSchoolCode(tCommSchool.getCity().getId() + "" + DateUtils.getYearMonthDate() + UtilTools.getSeq(tParam.getSchoolStatus()+1));
			// 试用用户，设置有效期
			tCommSchool.setEndDate(DateUtils.addDays(new Date(), 7));
		}
		if (!beanValidator(model, tCommSchool)){
			return form(tCommSchool, model);
		}
		if(!tCommSchool.getIsNewRecord()){//编辑表单保存
			TCommSchool t = tCommSchoolService.get(tCommSchool.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tCommSchool, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tCommSchoolService.save(t);//保存
		}else{//新增表单保存
			tCommSchoolService.save(tCommSchool);//保存
			
			// 初始化资源
		}
		addMessage(redirectAttributes, "保存学校成功");
		return "redirect:"+Global.getAdminPath()+"/em/school/tCommSchool/list?repage";
	} 
	
	/**
	 * 保存学校
	 */
	@RequiresPermissions(value={"em:school:tCommSchool:add","em:school:tCommSchool:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveEdit")
	public String saveEdit(TCommSchool tCommSchool, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tCommSchool)){
			return form(tCommSchool, model);
		}
		if(!tCommSchool.getIsNewRecord()){//编辑表单保存
			TCommSchool t = tCommSchoolService.get(tCommSchool.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tCommSchool, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tCommSchoolService.save(t);//保存
		}
		addMessage(redirectAttributes, "保存学校成功");
		return "redirect:"+Global.getAdminPath()+"/em/school/tCommSchool/info";
	} 
	
	/**
	 * 删除学校
	 */
	@RequiresPermissions("em:school:tCommSchool:del")
	@RequestMapping(value = "delete")
	public String delete(TCommSchool tCommSchool, RedirectAttributes redirectAttributes) {
		tCommSchoolService.delete(tCommSchool);
		addMessage(redirectAttributes, "删除学校成功");
		return "redirect:"+Global.getAdminPath()+"/em/school/tCommSchool/list?repage";
	}
	
	/**
	 * 启用、禁用学校
	 */
	@RequiresPermissions("em:school:tCommSchool:enableanddisable")
	@RequestMapping(value = "enableAndDisable")
	public String enableAndDisable(TCommSchool tCommSchool, RedirectAttributes redirectAttributes) {
		tCommSchool.setSchoolStatus(tCommSchool.getSchoolStatus()==0?1:0);
		int result = tCommSchoolService.enableAndDisable(tCommSchool);
		addMessage(redirectAttributes, result==1?"状态更新成功":"状态更新失败");
		return "redirect:"+Global.getAdminPath()+"/em/school/tCommSchool/list?repage";
	}
	
	/**
	 * 學校證件显示编辑保存
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"em:school:tCommSchool:add","em:school:tCommSchool:edit"},logical=Logical.OR)
	@RequestMapping(value = "imageEdit")
	public String imageEdit(TCommSchool tCommSchool, boolean __ajax, HttpServletResponse response, Model model) {
		TCommSchool currentSchool = tCommSchoolService.get(UserUtils.getUser().getSchool().getId());
		if (StringUtils.isNotBlank(tCommSchool.getSchoolName())){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userInfo";
			}
			if(tCommSchool.getZjphoto() != null )
				currentSchool.setZjphoto(tCommSchool.getZjphoto());
			tCommSchoolService.save(currentSchool);
			if(__ajax){//手机访问
				AjaxJson j = new AjaxJson();
				j.setSuccess(true);
				j.setMsg("修改个人头像成功!");
				return renderString(response, j);
			}
			model.addAttribute("message", "保存用户信息成功");
			return "modules/sys/userInfo";
		}
		model.addAttribute("school", currentSchool);
		model.addAttribute("Global", new Global());
		return "modules/em/school/schoolImageEdit";
	}
	
	/**
	 * 用户头像显示编辑保存
	 * @param user
	 * @param model
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequiresPermissions(value={"em:school:tCommSchool:add","em:school:tCommSchool:edit"},logical=Logical.OR)
	@RequestMapping(value = "imageUpload")
	public String imageUpload( HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws IllegalStateException, IOException {
		TCommSchool currentSchool = tCommSchoolService.get(UserUtils.getUser().getSchool().getId());
		
		// 判断文件是否为空  
        if (!file.isEmpty()) {  
        	// 文件保存路径  
            String realPath = Global.USERFILES_BASE_URL + UserUtils.getPrincipal() + "/images/" ;
            // 转存文件  
            FileUtils.createDirectory(Global.getUserfilesBaseDir() + realPath);
            file.transferTo(new File( Global.getUserfilesBaseDir() + realPath + file.getOriginalFilename()));  
            currentSchool.setZjphoto(request.getContextPath()+realPath + file.getOriginalFilename());
            tCommSchoolService.save(currentSchool);
        }  

		return "modules/em/school/schoolImageEdit";
	}
	
	/**
	 * 批量删除学校
	 */
	@RequiresPermissions("em:school:tCommSchool:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tCommSchoolService.delete(tCommSchoolService.get(id));
		}
		addMessage(redirectAttributes, "删除学校成功");
		return "redirect:"+Global.getAdminPath()+"/em/school/tCommSchool/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("em:school:tCommSchool:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TCommSchool tCommSchool, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学校"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TCommSchool> page = tCommSchoolService.findPage(new Page<TCommSchool>(request, response, -1), tCommSchool);
    		new ExportExcel("学校", TCommSchool.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学校记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/em/school/tCommSchool/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("em:school:tCommSchool:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TCommSchool> list = ei.getDataList(TCommSchool.class);
			for (TCommSchool tCommSchool : list){
				try{
					tCommSchoolService.save(tCommSchool);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学校记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学校记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学校失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/em/school/tCommSchool/list?repage";
    }
	
	/**
	 * 下载导入学校数据模板
	 */
	@RequiresPermissions("em:school:tCommSchool:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学校数据导入模板.xlsx";
    		List<TCommSchool> list = Lists.newArrayList(); 
    		new ExportExcel("学校数据", TCommSchool.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/em/school/tCommSchool/list?repage";
    }
	
	/**
	 * 资源分配
	 * @param tel
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("em:school:tCommSchool:assign")
	@RequestMapping(value = "assign")
	public String assign(TCommSchool tCommSchool, Model model) {
		//model.addAttribute("role", role);
		model.addAttribute("menuList", systemService.findAllMenu());
		//model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/roleAuth";
	}
	
	/**
	 * 验证电话号码是否有效
	 * @param tel
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value={"em:school:tCommSchool:add","em:school:tCommSchool:edit"}, logical=Logical.OR)
	@RequestMapping(value = "checkTel")
	public boolean checkTel(String tel, String oldtel) {
		if (tel!=null && !"".equals(tel) && tel.equals(oldtel))
			return true;
		TCommSchool tParam = new TCommSchool();
		tParam.setTel(tel);
		tParam = tCommSchoolService.getCount(tParam);
		if (tParam.getSchoolStatus() == 0)
			return true;
		// 验证用户表中是否已存在该手机号码
		User user = userDao.findUniqueByProperty("mobile", tel);
	    if(user != null){
	    	return false;
	    }
		return false;
	}
	
	/**
	 * 验证邮箱是否已存在
	 * @param tel
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value={"em:school:tCommSchool:add","em:school:tCommSchool:edit"}, logical=Logical.OR)
	@RequestMapping(value = "checkEmail")
	public boolean checkEmail(String email, String oldemail) {
		if (email!=null && !"".equals(email) && email.equals(oldemail))
			return true;
		TCommSchool tParam = new TCommSchool();
		tParam.setEmail(email);
		tParam = tCommSchoolService.getCount(tParam);
		if (tParam.getSchoolStatus() == 0)
			return true;
		
		// 验证用户表中是否已存在该邮箱地址
		User user = userDao.findUniqueByProperty("email", email);
		if(user != null){
			return false;
		}
		return false;
	}
	
}