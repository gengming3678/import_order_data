package com.palline.common.base;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
/**
 * Copyright (C), 2015-8-10, 上海蓬海涞讯数据技术有限公司
 * @author gmm
 * 公共继承类 
 * @version 1.0
 * @date 2015-8-10
 */
public abstract class CommonBaseDao <E,PK extends Serializable> extends BaseDao<E, Serializable>{
	
	@Resource(name="sqlSessionFactory")
	private SqlSessionFactory sqlSessionFactory;


	@PostConstruct  
    public void injectSqlMapClient() {   
        super.setSqlSessionFactory(sqlSessionFactory);
    }
}
