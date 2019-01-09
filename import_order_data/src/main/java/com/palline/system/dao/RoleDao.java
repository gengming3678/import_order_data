package com.palline.system.dao;

import java.util.List;
import java.util.Map;

import com.palline.system.entity.KeyValueCheck;
import com.palline.system.entity.Role;

/**
 * Copyright (C), 2015-8-10, 上海蓬海涞讯数据技术有限公司
 * @author gmm
 * @version 1.0
 * @date 2015-8-10
 */
public interface RoleDao {


	public List<Role> queryRoleListPage(Role role,int pageIndex,int pageSize);
	
	public List<Role> queryRoleList(Role role);
	
	public Long getRolePageTotal(Role role);
	
	public int updateRole(Role role);
	
	public int deleteRoleById(Integer id);
	
	public int deleteRoleNodeByRoleId(Integer roleId);
	
	public int deleteRoleDeptByRoleId(Integer roleId);
	
	
	public int insertRole(Role role);
	
	public int insertRoleDept(List<Map<String,Integer>> list);
	
	public List<Role> getSelectDeptListByRoleId(Role role);
	
	public List<KeyValueCheck> getOneLevelSelectDeptList(Role role);
	
	
}
