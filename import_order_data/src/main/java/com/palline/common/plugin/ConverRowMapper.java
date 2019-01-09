package com.palline.common.plugin;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

@SuppressWarnings("rawtypes")
public class ConverRowMapper implements ParameterizedRowMapper{

	 //默认已经执行rs.next(),可以直接取数据
	 public Map<String,Object> mapRow(ResultSet rs, int index) throws SQLException {
		 Map<String,Object> map = new HashMap<String,Object>();
		 ResultSetMetaData m=rs.getMetaData();
		 int columns=m.getColumnCount();
		   //显示表格内容
		   while(rs.next()){
		    for(int i=1;i<=columns;i++){//存放列名，和值
		     map.put(m.getColumnName(i), rs.getString(i));
		    }
		   }
	  return map;
	}

}
