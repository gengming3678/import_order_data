package com.palline.common.base;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.dao.DataAccessException;
/**
 * Copyright (C), 2015-08-03, 上海蓬海涞讯数据技术有限公司
 * @author gmm
 * @version 1.0
 * @date 2015-08-03
 * @param <E>
 * @param <PK>
 */
public abstract class BaseDao <E, PK extends Serializable> extends SqlSessionDaoSupport implements Dao<E,PK>{
	
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public E getObjectById(String nameSpace,PK primaryKey) throws DataAccessException{
		E object = (E) super.getSqlSession().selectOne(nameSpace, primaryKey);
		
		return object; 
	}
	
	/**
	 * 查询总数
	 * @param entity
	 * @return
	 */
	public Long getCount(String nameSpace){
		return (Long)super.getSqlSession().selectOne(nameSpace);
	}
	
	/**
	 * 根据条件查询总数
	 * @param statementName
	 * @param param
	 * @return
	 */
	public Long getCount(String nameSpace,Object param){
		return (Long)super.getSqlSession().selectOne(nameSpace,param);
	}
	
	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteObjectById(String nameSpace,PK id) throws DataAccessException{
		return super.getSqlSession().delete(nameSpace,id);
	}
	
	/**
	 * 根据对象删除
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteObjectByObj(String nameSpace,E entity) throws DataAccessException{
		return super.getSqlSession().delete(nameSpace,entity);
	}
	
	/**
	 * 插入数据
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public int saveObject(String nameSpace,E entity) throws DataAccessException{
		return super.getSqlSession().insert(nameSpace, entity);
	}
	
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public int updateObject(String nameSpace,E entity) throws DataAccessException{
		return super.getSqlSession().update(nameSpace, entity);
	}
	
	
	/**
	 * 查询,指定statementName
	 * @param statementName
	 * @param param
	 * @return
	 */
	public Object queryForObject(String nameSpace, Object param){
		return super.getSqlSession().selectOne(nameSpace,param);
	}
	
	/**
	 * 查询
	 * @param statementName
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<E> queryForList(String nameSpace){
		return super.getSqlSession().selectList(nameSpace);
	}
	
	/**
	 * 查询,指定statementName
	 * @param statementName
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<E> queryForList(String nameSpace, Object param){
		return super.getSqlSession().selectList(nameSpace,param);
	}
	
	
	
	/**
	 * mybatis 分页
	 * @param nameSpace  
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<E> queryForList(String nameSpace,int pageIndex, int pageSize){
          RowBounds rowBounds = new RowBounds((pageIndex-1) * pageSize,pageSize);
          return  super.getSqlSession().selectList(nameSpace,null,rowBounds);
	}
	
	/**
	 * mybatis 分页
	 * @param nameSpace  
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<E> queryForList(String nameSpace, Object param,int pageIndex, int pageSize){
	     RowBounds rowBounds = new RowBounds((pageIndex-1) * pageSize,pageSize);
         return  super.getSqlSession().selectList(nameSpace, param, rowBounds);
	}
	
}
