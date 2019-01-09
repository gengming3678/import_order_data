package com.palline.system.dao.imp;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.palline.common.base.CommonBaseDao;
import com.palline.system.dao.RoleDao;
import com.palline.system.entity.KeyValueCheck;
import com.palline.system.entity.Role;


@Repository
public class RoleDaoImp extends CommonBaseDao<Role, Integer> implements RoleDao{
	private static final String nameSpace = "com.palline.system.entity.Role.";


	@Override
	public int updateRole(Role role) {
		// TODO Auto-generated method stub
		return super.updateObject(nameSpace+"updateRole", role);
	}

	@Override
	public int deleteRoleById(Integer id) {
		// TODO Auto-generated method stub
		return super.deleteObjectById(nameSpace+"delRole", id);
	}
	
	public int deleteRoleNodeByRoleId(Integer roleId) {
		// TODO Auto-generated method stub
		return super.deleteObjectById(nameSpace+"deleteRoleNodeByRoleId", roleId);
	}
	public int deleteRoleDeptByRoleId(Integer roleId){
		return super.deleteObjectById(nameSpace+"deleteRoleDeptByRoleId", roleId);
	}
	@Override
	public int insertRole(Role role) {
		// TODO Auto-generated method stub
		return super.saveObject(nameSpace+"saveRole", role);
	}
	public int insertRoleDept(List<Map<String,Integer>> list){
		return super.getSqlSession().insert(nameSpace+"saveRoleDept", list);
	}
	@Override
	public List<Role> queryRoleListPage(Role role,int pageIndex,int pageSize) {
		// TODO Auto-generated method stub
		List<Role> list = super.queryForList(nameSpace+"getRoleList",role,pageIndex,pageSize);
		return list;
	}
	
	public List<Role> queryRoleList(Role role){
		List<Role> list = super.queryForList(nameSpace+"getAllRoleList",role);
		return list;
	}

	@Override
	public Long getRolePageTotal(Role role) {
		// TODO Auto-generated method stub
		Long count = super.getCount(nameSpace+"getRoleTotal",role);
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getSelectDeptListByRoleId(Role role){
		List<Role> list = super.getSqlSession().selectList(nameSpace+"getSelectDeptListByRoleId",role);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<KeyValueCheck> getOneLevelSelectDeptList(Role role){
		List<KeyValueCheck> list = super.getSqlSession().selectList(nameSpace+"getOneLevelSelectDeptList",role);
		return list;
	}
}
