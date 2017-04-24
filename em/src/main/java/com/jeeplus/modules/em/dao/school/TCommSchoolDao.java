package com.jeeplus.modules.em.dao.school;
 
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.em.entity.school.TCommSchool;

/**
 * 学校租户DAO接口
 * @author fly
 * @version 2016-08-28
 */
@MyBatisDao
public interface TCommSchoolDao extends CrudDao<TCommSchool> {
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public TCommSchool getCount(TCommSchool tCommSchool);
	 
	/**
	 * 更新状态
	 * @param tel
	 * @return
	 */
	public int enableAndDisable(TCommSchool tCommSchool);
	
	/**
	 * 更新有效期
	 * @param tel
	 * @return
	 */
	public int updateSchoolEndDate(TCommSchool tCommSchool);
}