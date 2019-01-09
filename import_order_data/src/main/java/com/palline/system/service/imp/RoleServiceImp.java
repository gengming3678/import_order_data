package com.palline.system.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palline.system.dao.imp.RoleDaoImp;
import com.palline.system.entity.KeyValueCheck;
import com.palline.system.entity.Role;
import com.palline.system.service.RoleService;

@Service
public class RoleServiceImp implements RoleService{

	@Autowired
	private RoleDaoImp roleDaoImp;

	@Override
	public List<Role> queryRoleListPage(Role role, int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		return roleDaoImp.queryRoleListPage(role,pageIndex,pageSize);
	}

	public List<Role> queryRoleList(Role role){
		return roleDaoImp.queryRoleList(role);
	}
	@Override
	public Long getRolePageTotal(Role role) {
		// TODO Auto-generated method stub
		return roleDaoImp.getRolePageTotal(role);
	}



	@Override
	public int updateRole(Role role) {
		// TODO Auto-generated method stub
		return roleDaoImp.updateRole(role);
	}

	@Override
	public int deleteRoleById(Integer id) {
		// TODO Auto-generated method stub
		return roleDaoImp.deleteRoleById(id);
	}

	public int deleteRoleNodeByRoleId(Integer roleId){
		return roleDaoImp.deleteRoleNodeByRoleId(roleId);
	}
	
	public int deleteRoleDeptByRoleId(Integer roleId){
		return roleDaoImp.deleteRoleDeptByRoleId(roleId);
	}
	@Override
	public int insertRole(Role role) {
		// TODO Auto-generated method stub
		return roleDaoImp.insertRole(role);
	}
	
	public int insertRoleDept(List<Map<String,Integer>> list){
		return roleDaoImp.insertRoleDept(list);
	}

	public List<Role> getSelectDeptListByRoleId(Role role){
		return roleDaoImp.getSelectDeptListByRoleId(role);
	}
	
	public List<KeyValueCheck> getOneLevelSelectDeptList(Role role){
		return roleDaoImp.getOneLevelSelectDeptList(role);
	}
}
