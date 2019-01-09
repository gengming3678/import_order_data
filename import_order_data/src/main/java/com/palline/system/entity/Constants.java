package com.palline.system.entity;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	
	
	/**
	 * 星级——佣金比例
	 * @author jorrel04
	 *
	 */
	public static class LevelAccout{
		public final static Map<Long,Double> mapLevelAccoutBiLi=new HashMap<Long,Double>();
		static{
			mapLevelAccoutBiLi.put(8l, 0.49);//0.7
			mapLevelAccoutBiLi.put(7l, 0.42);//0.6
			mapLevelAccoutBiLi.put(6l, 0.35);//0.5
			mapLevelAccoutBiLi.put(5l, 0.28);//0.4
			mapLevelAccoutBiLi.put(3l, 0.21);//0.3
			mapLevelAccoutBiLi.put(2l, 0.14);//0.2
			mapLevelAccoutBiLi.put(1l, 0.07);//0.1
		}
	}
	
	/**
	 * 星级——积分比例
	 * @author jorrel04
	 *
	 */
	public static class LevelPoint{
		//所有分佣比例，积分和佣金3/7开
		public final static Map<Long,Double> mapLevelPointBiLi=new HashMap<Long,Double>();
		static{
			mapLevelPointBiLi.put(8l, 0.21);//0.7
			mapLevelPointBiLi.put(7l, 0.18);//0.6
			mapLevelPointBiLi.put(6l, 0.15);//0.5
			mapLevelPointBiLi.put(5l, 0.12);//0.4
			mapLevelPointBiLi.put(3l, 0.09);//0.3
			mapLevelPointBiLi.put(2l, 0.06);//0.2
			mapLevelPointBiLi.put(1l, 0.03);//0.1
		}
	}
	
	
	/**
	 * 星级Id映射
	 * @author jorrel04
	 *
	 */
	public static class MapLevelId{
		//所有分佣比例，积分和佣金3/7开
		public final static Map<String,Long> mapLevel=new HashMap<String,Long>();
		static{
			mapLevel.put("8", 1014084200000109l);
			mapLevel.put("7", 1015084200000072l);
			mapLevel.put("6", 1015084200000060l);
			mapLevel.put("5", 1015084200000037l);
			mapLevel.put("3", 1015084200000042l);
			mapLevel.put("2", 1015084200000027l);
			mapLevel.put("1", 1014084200000055l);
		}
	}
	
	
	/**
	 * 星级名称映射
	 * @author jorrel04
	 *
	 */
	public static class MapLevelName{
		//所有分佣比例，积分和佣金3/7开
		public final static Map<String,String> mapLevelCh=new HashMap<String,String>();
		static{		
			mapLevelCh.put("8", "八星会员");
			mapLevelCh.put("7", "七星会员");
			mapLevelCh.put("6", "六星会员");
			mapLevelCh.put("5", "五星会员");
			mapLevelCh.put("3", "三星会员");
			mapLevelCh.put("2", "二星会员");
			mapLevelCh.put("1", "一星会员");
		}
	}
	
	/**
	 * 星级编码映射
	 * @author jorrel04
	 *
	 */
	public static class MapLevelCode{
		//所有分佣比例，积分和佣金3/7开
		public final static Map<String,String> mapLevelCode=new HashMap<String,String>();
		static{		
			mapLevelCode.put("8", "008");//8星
			mapLevelCode.put("7", "007");//7星
			mapLevelCode.put("6", "006");//6星
			mapLevelCode.put("5", "005");//5星
			mapLevelCode.put("3", "004");//3星
			mapLevelCode.put("2", "003");//2星
			mapLevelCode.put("1", "002");//1星
			mapLevelCode.put("0", "001");//普通用户
		}
	}
	


}
