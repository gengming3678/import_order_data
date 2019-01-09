package com.palline.common.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
/**
 * Copyright (C), 2015-8-10, 上海蓬海涞讯数据技术有限公司
 * @author gmm
 * 公共继承类 
 * @version 1.0
 * @date 2015-8-10
 */
public abstract class CommonBaseService  {
	
	
	public String getJsonByListMapPage(List<Map<String, Object>> list, int pageIndex, int pageSize) {
		int fromIndex=(pageIndex-1)*pageSize;
		int toIndex=(pageIndex-1)*pageSize+pageSize;
		List<Map<String,Object>> curPageList = new ArrayList<Map<String,Object>>();
		if(toIndex>list.size()){//集合越界判断
			 toIndex = list.size();
			 curPageList = list.subList(fromIndex, toIndex);
		}else{
			 curPageList = list.subList(fromIndex, toIndex);
		}
		String curPageJson = JSON.toJSONString(curPageList);
		String curPageJsonPage = "{total:"+list.size()+",rows:"+curPageJson+"}";
		return curPageJsonPage;
	}
	
	
}
