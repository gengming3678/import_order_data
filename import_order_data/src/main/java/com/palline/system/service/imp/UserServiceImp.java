package com.palline.system.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palline.system.dao.imp.UserDaoImp;
import com.palline.system.entity.User;
import com.palline.system.service.UserService;

@Service
public class UserServiceImp implements UserService{

	@Autowired
	private UserDaoImp userDaoImp;
	@Override
	public User selectOneUser(User userEntity) {
		// TODO Auto-generated method stub
		return userDaoImp.selectOneUser(userEntity);
	}

	@Override
	public List<User> queryUserListPage(User userEntity,
			int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		return userDaoImp.queryUserListPage(userEntity,pageIndex,pageSize);
	}

	@Override
	public Long getUserPageTotal(User userEntity) {
		// TODO Auto-generated method stub
		return userDaoImp.getUserPageTotal(userEntity);
	}

	@Override
	public User getUserById(Integer id) {
		// TODO Auto-generated method stub
		return userDaoImp.getUserById(id);
	}

	@Override
	public int updateUser(User userEntity) {
		// TODO Auto-generated method stub
		return userDaoImp.updateUser(userEntity);
	}

	@Override
	public int deleteUserById(Integer id) {
		// TODO Auto-generated method stub
		return userDaoImp.deleteUserById(id);
	}
	
	public int deleteUserRoleByUserId(Integer userId){
		return userDaoImp.deleteUserRoleByUserId(userId);
	}

	@Override
	public int insertUser(User userEntity) {
		// TODO Auto-generated method stub
		return userDaoImp.insertUser(userEntity);
	}
	
	public int saveUserRole(User userEntity){
		return userDaoImp.saveUserRole(userEntity);
	}

	public List<Integer> getUserSelectRolesByUserId(User userEntity){
		return userDaoImp.getUserSelectRolesByUserId(userEntity);
	}
	
	public List<Integer> getRoleIdsByUserId(User userEntity){
		return userDaoImp.getRoleIdsByUserId(userEntity);
	}
	
}
