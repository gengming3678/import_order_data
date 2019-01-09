package com.palline.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.palline.common.util.Constant;
import com.palline.common.util.JsonUtil;
import com.palline.common.util.PageData;
import com.palline.common.util.StringUtil;
import com.palline.common.util.TipInfo;
import com.palline.system.entity.KeyValueCheck;
import com.palline.system.entity.Role;
import com.palline.system.entity.User;
import com.palline.system.service.imp.RoleServiceImp;
import com.palline.system.service.imp.UserServiceImp;

/**
 * @author gmm
 * @version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {	
	@Resource
	private UserServiceImp userServiceImp;
	
	@Resource
	private RoleServiceImp roleServiceImp;
	
	/**
	 * 用户导航
	 **/

	@RequestMapping(value = "/list.do")
	public String list(Model model) {
		return "user/userList";
	}
	
	/**
	 * 用户维护拉取数据
	 **/
	@ResponseBody
	@RequestMapping(value = "/getJsonUsers.do")
	public PageData<User> getJsonUsers(@RequestParam("page") Integer pageIndex,@RequestParam("findName") String findName,HttpServletResponse response, HttpServletRequest request) {
		User user = new User();
		user.setName(StringUtil.strToUtf8(findName));
		user.setRealName(StringUtil.strToUtf8(findName));
		Long total = userServiceImp.getUserPageTotal(user);
		List<User> list = userServiceImp.queryUserListPage(user, pageIndex, Constant.PAGE_SIZE);
		return new PageData<User>(total,list);
	}
	
	/**
	 * 用户新增数据
	 **/
	@ResponseBody
	@RequestMapping(value = "/add.do",method=RequestMethod.POST)
	public Object add(@RequestParam("json") String json) {
		String jsonNew = JsonUtil.replaceAllOnOff(json);
		User user=JSON.parseObject(jsonNew,User.class);
		int result = userServiceImp.insertUser(user);
		if(result>0){
			return new TipInfo(Constant.SUCCESS,"操作成功!");
		}else{
			return new TipInfo(Constant.FAILTURE,"操作失败!");
		}
	}
	
	/**
	 * 用户修改数据
	 **/
	@ResponseBody
	@RequestMapping(value = "/edit.do",method=RequestMethod.POST)
	public Object edit(@RequestParam("json") String json) {
		User user=JSON.parseObject(json,User.class);
		int result = userServiceImp.updateUser(user);
		if(result>0){
			return new TipInfo(Constant.SUCCESS,"操作成功!");
		}else{
			return new TipInfo(Constant.FAILTURE,"操作失败!");
		}
	}
	
	/**
	 * 用户删除数据
	 **/
	@ResponseBody
	@RequestMapping(value = "/delete.do",method=RequestMethod.POST)
	public Object delete(@RequestParam("id") Integer id) {
		int result = userServiceImp.deleteUserById(id);
		if(result>0){
			return new TipInfo(Constant.SUCCESS,"操作成功!");
		}else{
			return new TipInfo(Constant.FAILTURE,"操作失败!");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUserSelectRolesByUserId.do")
	public List<KeyValueCheck> getUserSelectRolesByUserId(@RequestParam("userId") Integer userId,
			HttpServletResponse response, HttpServletRequest request,Model model) {
		User userEntity =new User();
		userEntity.setId(userId);
		List<Integer>  listCheck = userServiceImp.getUserSelectRolesByUserId(userEntity);
		
		List<Role> roleList = roleServiceImp.queryRoleList(null);
		List<KeyValueCheck> keyValueCheckList=new ArrayList<KeyValueCheck>();
		for (Role role:roleList) {
			KeyValueCheck keyValueCheck=new KeyValueCheck();
			keyValueCheck.setId(String.valueOf(role.getRoleId()));
			keyValueCheck.setText(role.getRoleName());
			keyValueCheck.setLeaf(true);
			keyValueCheck.setExpanded(true);
			keyValueCheck.setChecked(listCheck.contains(role.getRoleId())?true:false);
			keyValueCheckList.add(keyValueCheck);
		}
		return keyValueCheckList;
	}
	
	/**
	 * 角色菜单数据保存
	 **/
	@ResponseBody
	@RequestMapping(value = "/saveRoleUser.do",method=RequestMethod.POST)
	public Object saveRoleUser(@RequestParam("json") String json,@RequestParam("userId") Integer userId) {
		String[] roleIds = json.split(",");
		 userServiceImp.deleteUserRoleByUserId(userId);
		 for (String roleId:roleIds) {
			 User user=new User();
			 user.setId(userId);
			 user.setRoleId(Integer.valueOf(roleId));
			 userServiceImp.saveUserRole(user);
		}
		return new TipInfo(Constant.FAILTURE,"操作成功!");
	}

}
