package com.palline.system.dao.imp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.palline.common.base.CommonBaseDao;
import com.palline.system.dao.UserDao;
import com.palline.system.entity.User;


@Repository
public class UserDaoImp extends CommonBaseDao<User, Integer> implements UserDao{
	private static final String nameSpace = "com.palline.system.entity.User.";
	@Override
	public User selectOneUser(User userEntity) {
		User user = (User) getSqlSession().selectOne(nameSpace+"getUserByNameAndPass",userEntity);
		return user;
	}

	@Override
	public User getUserById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getUserSelectRolesByUserId(User userEntity){
		List<Integer> list = super.getSqlSession().selectList(nameSpace+"getUserSelectRolesByUserId",userEntity);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getRoleIdsByUserId(User userEntity){
		List<Integer> list = super.getSqlSession().selectList(nameSpace+"getRoleIdsByUserId",userEntity);
		return list;
	}
	@Override
	public int updateUser(User user) {
		// TODO Auto-generated method stub
		return super.updateObject(nameSpace+"updateUser", user);
	}

	@Override
	public int deleteUserById(Integer id) {
		// TODO Auto-generated method stub
		return super.deleteObjectById(nameSpace+"delUser", id);
	}

	public int deleteUserRoleByUserId(Integer userId){
		return super.deleteObjectById(nameSpace+"deleteRoleUserByUserId", userId);
	}
	@Override
	public int insertUser(User userEntity) {
		// TODO Auto-generated method stub
		return super.saveObject(nameSpace+"saveUser", userEntity);
	}
	
	public int saveUserRole(User userEntity){
		return super.saveObject(nameSpace+"saveUserRole", userEntity);
	}

	@Override
	public List<User> queryUserListPage(User userEntity,int pageIndex,int pageSize) {
		// TODO Auto-generated method stub
		List<User> list = super.queryForList(nameSpace+"getUserList",userEntity,pageIndex,pageSize);
		return list;
	}

	public Long getUserPageTotal(User userEntity) {
		// TODO Auto-generated method stub
		Long count = super.getCount(nameSpace+"getUserTotal",userEntity);
		return count;
	}

}
