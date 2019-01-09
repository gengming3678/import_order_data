package com.palline.system.service;

import java.util.List;

import com.palline.system.entity.User;

/**
 * Copyright (C), 2015-8-10, 上海蓬海涞讯数据技术有限公司
 * @author gmm
 * @version 1.0
 * @date 2015-8-10
 */
public interface UserService {

	public User selectOneUser(User userEntity);
	
	public List<User> queryUserListPage(User userEntity,int pageIndex,int pageSize);
	
	public List<Integer> getUserSelectRolesByUserId(User userEntity);
	
	public List<Integer> getRoleIdsByUserId(User userEntity);
	
	public Long getUserPageTotal(User userEntity);
	
	public User getUserById(Integer id);
	
	public int updateUser(User userEntity);
	
	public int deleteUserById(Integer id);
	
	public int deleteUserRoleByUserId(Integer userId);
	
	public int insertUser(User userEntity);
	
	public int saveUserRole(User userEntity);
	
}
