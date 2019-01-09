package com.palline.common.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
/**
 * Copyright (C), 2015-8-10, 上海蓬海涞讯数据技术有限公司
 * @author gmm
 * @version 1.0
 * @date 2015-8-10
 * @param <E>
 * @param <PK>
 */
public interface Dao <E,PK extends Serializable>{
	
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public E getObjectById(String nameSpace,PK id) throws DataAccessException;
	
	/**
	 * 
	 * 查询总数
	 * @param entity
	 * @return
	 */
	public Long getCount(String nameSpace);
	
	/**
	 * 根据条件查询总数
	 * @param statementName
	 * @param param
	 * @return
	 */
	public Long getCount(String nameSpace,Object param);
	
	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteObjectById(String nameSpace,PK id) throws DataAccessException;
	
	/**
	 * 根据对象删除
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteObjectByObj(String nameSpace,E entity) throws DataAccessException ;
	
	/**
	 * 插入数据
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public int saveObject(String nameSpace,E entity) throws DataAccessException;
	
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public int updateObject(String nameSpace,E entity) throws DataAccessException;

	/** 
	 * 
	 * 根据id检查是否插入或是更新数据 
	 */
	//public void saveOrUpdateObject(E entity) throws DataAccessException;

	/**
	 * 查询
	 * @param statementName
	 * @param param
	 * @return
	 */
	public List<E> queryForList(String nameSpace);
	
	/**
	 * 查询,指定statementName
	 * @param statementName
	 * @param param
	 * @return
	 */
	public List<E> queryForList(String nameSpace, Object param);
	

}
