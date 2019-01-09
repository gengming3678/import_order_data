package com.palline.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
/**
 * JSON数据格式和java对象以及集合框架转化操作
 * Copyright (C), 2015-8-10, 上海蓬海涞讯数据技术有限公司
 * @author gmm
 * @version 1.0
 * @date 2015-8-10
 */
public class JsonUtil {
	public static void main(String[] args) {
		//method1();
		//method2();
		//method3();
		//method4();
	}
	
	public static String replaceAllOnOff(String json) {
		return json.replaceAll("'leaf':'on'", "'leaf':'true'").replaceAll("'leaf':'off'", "'leaf':'false'");
	}
	
	/**
	 * 0 Json
	 */
	public static void serialJsonFromObject(){
		Person person = new Person("1","fastjson",1);
		//bean 转化为 json
		String jsonString = JSON.toJSONString(person);
		System.out.println(jsonString);
	}
	
	/**
	 * 1 Person
	 */
	public static void serialObjectFromObjJson(){
		Person person = new Person("1","fastjson",1);
		//bean 转化为 json
		String jsonString = JSON.toJSONString(person);
		System.out.println(jsonString);
		//json 转化为 bean  == parseObject
		Person person2 =JSON.parseObject(jsonString,Person.class);
		System.out.println(person2.toString());
		
	}
	/**
	 * 2 List<Person>
	 */
	public static void serialObjectFromListObj(){

		Person person1 = new Person("1","fastjson1",1);
		Person person2 = new Person("2","fastjson2",2);
		List<Person> persons = new ArrayList<Person>();
		persons.add(person1);
		persons.add(person2);
		//list<Bean> 转化为 json
		String jsonString = JSON.toJSONString(persons);
		System.out.println("aaaa"+jsonString);
		
		//json转化为 list<Bean>  == parseArray
		List<Person> persons2 = JSON.parseArray(jsonString,Person.class);
		System.out.println("person1"+persons2.get(0).toString());
		System.out.println("person2"+persons2.get(1).toString());

	}
	
	/**
	 * 3 list<String>
	 */
	public static void serialObjectFromListStr(){
		List<String> list = new ArrayList<String>();
		list.add("fastjson1");
		list.add("fastjson2");
		list.add("fastjson3");
		//list<String> 转化为 json
		String jsonString = JSON.toJSONString(list);
		System.out.println("json"+jsonString);
		
		//  json转化为list<String>
		List<String> list2 = JSON.parseObject(jsonString,new TypeReference<List<String>>(){}); 
		System.out.println(list2.get(0)+"::"+list2.get(1)+"::"+list2.get(2));
		

	}
	/**
	 * 4 list<Map> 
	 */
	public static void serialObjectFromListMap(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		Map<String,Object> map2 = new HashMap<String,Object>();
		map2.put("key1", 1);
		map2.put("key2", 2);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		list.add(map2);
		//map转化为json
		String jsonString = JSON.toJSONString(list);
		//json转化为List<Map<String,Object>>
		List<Map<String,Object>> list2 = JSON.parseObject(jsonString,new TypeReference<List<Map<String,Object>>>(){});
		
		System.out.println("map1"+list2.get(0).get("key1"));
		System.out.println("map2"+list2.get(0).get("key2"));
		System.out.println("map3"+list2.get(1).get("key1"));
		System.out.println("map4"+list2.get(1).get("key2"));
	}
	
}
