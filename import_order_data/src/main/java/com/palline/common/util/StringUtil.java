package com.palline.common.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 字符操作
 * @author gmm
 *
 */
public class StringUtil {

	public static String strToUtf8(String str) {
		String strNew = null;
		try {
			strNew = new String(str.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strNew;
	}
	
	public static boolean isBlank(String str) {
		if(str==null || str.equals("") || str.equals("null")){
			return true;
		}else{
			return false;
		}
	}
	
	
	/** 
     * 定义分割常量 （#在集合中的含义是每个元素的分割，|主要用于map类型的集合用于key与value中的分割） 
     */  
    private static final String SEP1 = "#";  
    private static final String SEP2 = "|";
    private static final String SEP3 = ",";
  
    /** 
     * List转换String 
     *  
     * @param list 
     *            :需要转换的List 
     * @return String转换后的字符串 
     */  
    public static String ListToString(List<?> list) {  
        StringBuffer sb = new StringBuffer();  
        if (list != null && list.size() > 0) {  
            for (int i = 0; i < list.size(); i++) {  
                if (list.get(i) == null || list.get(i) == "" ) {  
                    continue;  
                }  
                // 如果值是list类型则调用自己  
                if (list.get(i) instanceof List) {  
                    sb.append(ListToString((List<?>) list.get(i)));  
                } else if (list.get(i) instanceof Map) {  
                    sb.append(MapToString((Map<?, ?>) list.get(i)));    
                } else {  
                    sb.append(list.get(i));  
                }  
                if(list.size()!=(i+1)){
                	sb.append(SEP3);  
                }
                
            }  
        }  
        return sb.toString();  
    }  
  
    /** 
     * Map转换String 
     *  
     * @param map 
     *            :需要转换的Map 
     * @return String转换后的字符串 
     */  
    public static String MapToString(Map<?, ?> map) {  
        StringBuffer sb = new StringBuffer();  
        // 遍历map  
        for (Object obj : map.keySet()) {  
            if (obj == null) {  
                continue;  
            }  
            Object key = obj;  
            Object value = map.get(key);  
            if (value instanceof List<?>) {  
                sb.append(key.toString() + SEP1 + ListToString((List<?>) value));  
                sb.append(SEP2);  
            } else if (value instanceof Map<?, ?>) {  
                sb.append(key.toString() + SEP1  
                        + MapToString((Map<?, ?>) value));  
                sb.append(SEP2);  
            } else {  
                sb.append(key.toString() + SEP1 + value.toString());  
                sb.append(SEP2);  
            }  
        }  
        return sb.toString();  
    }  
  
    /** 
     * String转换Map 
     *  
     * @param mapText 
     *            :需要转换的字符串 
     * @param KeySeparator 
     *            :字符串中的分隔符每一个key与value中的分割 
     * @param ElementSeparator 
     *            :字符串中每个元素的分割 
     * @return Map<?,?> 
     */  
    public static Map<String, Object> StringToMap(String mapText) {  
  
        if (mapText == null || mapText.equals("")) {  
            return null;  
        }  
        mapText = mapText.substring(1);  
  
       // mapText = EspUtils.DecodeBase64(mapText);  
  
        Map<String, Object> map = new HashMap<String, Object>();  
        String[] text = mapText.split("\\" + SEP2); // 转换为数组  
        for (String str : text) {  
            String[] keyText = str.split(SEP1); // 转换key与value的数组  
            if (keyText.length < 1) {  
                continue;  
            }  
            String key = keyText[0]; // key  
            String value = keyText[1]; // value  
            if (value.charAt(0) == 'M') {  
                Map<?, ?> map1 = StringToMap(value);  
                map.put(key, map1);  
            } else if (value.charAt(0) == 'L') {  
                List<?> list = StringToList(value);  
                map.put(key, list);  
            } else {  
                map.put(key, value);  
            }  
        }  
        return map;  
    }  
  
    /** 
     * String转换List 
     *  
     * @param listText 
     *            :需要转换的文本 
     * @return List<?> 
     */  
    public static List<Object> StringToList(String listText) {  
        if (listText == null || listText.equals("")) {  
            return null;  
        }  
        listText = listText.substring(1);  
  
        //listText = EspUtils.DecodeBase64(listText);  
  
        List<Object> list = new ArrayList<Object>();  
        String[] text = listText.split(SEP1);  
        for (String str : text) {  
            if (str.charAt(0) == 'M') {  
                Map<?, ?> map = StringToMap(str);  
                list.add(map);  
            } else if (str.charAt(0) == 'L') {  
                List<?> lists = StringToList(str);  
                list.add(lists);  
            } else {  
                list.add(str);  
            }  
        }  
        return list;  
    }
    
    /**
     * yyyy-MM-dd 
     */
    public static String dateToString(Date time){ 
        SimpleDateFormat formatter; 
        formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
        String ctime = formatter.format(time); 
        return ctime; 
    } 
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String dateToString2(Date time){ 
        SimpleDateFormat formatter; 
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
        String ctime = formatter.format(time); 
        return ctime; 
    }
    
    /**
     * 
     * @param time
     * @param formatter
     * @return
     */
    public static Date stringToDate(String time,String formatter){ 
    	SimpleDateFormat    sdf = new SimpleDateFormat (formatter);
    	Date ctime=null;
		try {
			ctime = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
    	return ctime;
    }
    
    /**
     * 
     * @param time
     * @param formatter
     * @return
     */
    public static String startDateOfMonth(){ 
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
    	//获取当前月第一天：
        Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String first = format.format(c.getTime());
        return first;
    }
    
    /**
     * 
     * @param time
     * @param formatter
     * @return
     */
    public static String endDateOfMonth(){ 
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
    	  //获取当前月最后一天
        Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        String last = format.format(ca.getTime());
        //System.out.println("===============last:"+last);
        return last;
    }
    
    /**
     * 
     * @param time
     * @param formatter
     * @return
     */
    public static String getCurMonth(){  
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
        return df.format(new Date());
    }
    
    /**
     * 
     * @param time
     * @param formatter
     * @return
     */
    public static String getCurYear(){  
        SimpleDateFormat df = new SimpleDateFormat("yyyy");//设置日期格式
        return df.format(new Date());
    }
    
    
    /**
     * 
     * @return
     */
    public static String[] get20Years(){  
    	Integer curYear = Integer.valueOf(StringUtil.getCurYear())+1;
    	String[] strYears = new String[10];
        for (int i = 0; i < 10; i++) {
        	strYears[i]=String.valueOf(curYear-i);
		}
        return strYears;
    }
    
    /**
     * 
     * @param time
     * @param formatter
     * @return
     */
    public static String[] getCurYearMonths(String year){  
        String[] strMonths = new String[12];
        strMonths[0]=year+"-01";
        strMonths[1]=year+"-02";
        strMonths[2]=year+"-03";
        strMonths[3]=year+"-04";
        strMonths[4]=year+"-05";
        strMonths[5]=year+"-06";
        strMonths[6]=year+"-07";
        strMonths[7]=year+"-08";
        strMonths[8]=year+"-09";
        strMonths[9]=year+"-10";
        strMonths[10]=year+"-11";
        strMonths[11]=year+"-12";
        return strMonths;
    }

    /**
     * 
     * @param time
     * @param formatter
     * @return
     */
    public static String getCurDate(){  
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }
    
	public static String getRandomNum() {
		return String.valueOf(Math.random());
	}
    
	
}
