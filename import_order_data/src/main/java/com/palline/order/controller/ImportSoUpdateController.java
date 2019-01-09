package com.palline.order.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.palline.common.util.StringUtil;
import com.palline.system.entity.Constants;
import com.palline.system.entity.Ouser;
import com.palline.system.service.imp.ImportSoServiceImpl;

/**
 * 更新退款补款操作的种子会员星级和订单金额
 * @author gmm
 * @version 1.0
 */
@Controller
@RequestMapping("/ImportSoUpdate")
public class ImportSoUpdateController {
	private static Logger logger=Logger.getLogger(ImportSoUpdateController.class); 
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name="jdbcTemplateOrder")
	private JdbcTemplate jdbcTemplateOrder;
	@Resource
	private ImportSoServiceImpl importSoServiceImpl;

	/**
	 * 更新退款补款操作的种子会员星级和订单金额
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateData.do")
	public String updateData(HttpServletRequest request,Model model) {
		try {
			logger.info("更新退款补款操作的种子会员星级和订单金额开始>>>"+StringUtil.dateToString2(new Date()));
			//List<String> ids=new ArrayList<String>();
			//String suffix="188708421219";//12位，1201，日期编码每次导入依次递增
			List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from sheet2 order by 序号 ");         
			/*for(int j=0;j<list.size();j++){
				String big=j+"";
				int leng=(big.trim()).length();
				if(leng==1){
					big=suffix+"000"+j;
				}else if(leng==2){
					big=suffix+"00"+j;
				}else if(leng==3){
					big=suffix+"0"+j;
				}
				ids.add(big);
			}*/
			List<Ouser> listLevelCode=new ArrayList<Ouser>();
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=list.get(i);
				//String id = ids.get(i);
				//获取tel这列数据,查询user_id
				String name = (String) map.get("姓名");
				String tel = (String) map.get("电话");
				String level = (String) map.get("现商户星级");
				String fee = (String) map.get("实际金额");
				
				String sqlDistributor = "select user_id from distributor where mobile = '"+tel+"' ORDER BY registration_time desc limit 1";
				Long user_id=null;
				try{
					 user_id = jdbcTemplate.queryForLong(sqlDistributor);
				}catch (EmptyResultDataAccessException e) {  
					logger.info(i+"未查询到user_id的姓名："+name+"  手机号:"+tel);
					continue;   
		        } 
				
				Long levelProId = Constants.MapLevelId.mapLevel.get(level);
				String levelProName = Constants.MapLevelName.mapLevelCh.get(level);
				String levelCode = Constants.MapLevelCode.mapLevelCode.get(level);
				
				Ouser ouser=new Ouser();
				ouser.setLevelCode(levelCode);
				ouser.setUserId(user_id);
				listLevelCode.add(ouser);
				//内部种子用户专用收货人手机号——17521282079；
				/*String sqlSo = " ("+id+", '"+id+"', '0', 1, "+user_id+", 1590058601000003, NULL, -1, 1872018091301, "+fee+", "+fee+", 0.00, 35, 1, 0, NULL, NULL, NULL, 0.00, 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '2018-11-20 16:52:00', 0, NULL, 1, NULL, 0, '10001', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 5, 0, 0, 0, NULL, NULL, 1086059000000411, '新闸路831号', '200135', 'tom', NULL, NULL, '17521282079', NULL, NULL, NULL, 10, '上海', 110, '上海市', NULL, NULL, 1140, '静安区', '', NULL, NULL, 0, NULL, 0.00, NULL, 0, NULL, 0, 1, 0, 0, 1870081300000047, '17521282079', NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', '127.0.0.1', NULL, '系统', NULL, NULL, '2018-11-20 16:52:00', NULL, NULL, 187, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, 'ody', NULL, NULL, NULL, 0, '', 0.00, 0.00, 0.00, NULL, 0, NULL, 1046014700000833, NULL, NULL, NULL, 35, 0, 0, '上海三少猩球贸易有限公司', NULL, NULL, 0, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL, NULL, NULL, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, NULL, 0.00, NULL, 1590058601000008, '三少猩球贸易有限公司', NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
				String sqlSoItem = "  ("+id+", NULL, 1, '"+id+"', "+user_id+", 1590058601000003, "+levelProId+", 1900084000000240, 1872018091301, 0.00, NULL, NULL, "+fee+", 1, '"+levelProName+"', NULL, 'https://cdn.oudianyun.com/dev/back-product-web/1542541241488_59.52936780076672_5ed25f84-0545-4f29-a211-b0d6c2ca0c25.png', NULL, 1, 0.00, 0.00, 0.00, NULL, 0, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, 1, NULL, 0.00, NULL, 0.00, NULL, NULL, 0.000, NULL, NULL, NULL, 0, 1, 0, 0, 1870081300000047, NULL, NULL, NULL, '2018-11-20 16:21:13', NULL, '127.0.0.1', NULL, NULL, NULL, NULL, '2018-11-20 16:21:13', NULL, NULL, 187, '181120537951367047', 0.00, NULL, NULL, NULL, NULL, NULL, '1900084100000002', NULL, '份', NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, 0.00, 0, NULL, NULL, NULL, NULL, NULL, 1900082400000089, '来伊份', NULL, 1900083602000056, '增值服务费', '1900080800000001-1900083602000056', 'B2C后台类目->增值服务费', NULL, NULL, NULL, 1, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, 90, 30, 1, 1, NULL, NULL, NULL);";
				*/
				//外部用户专用收货人手机号-18717718990；
				//String sqlSo = " ("+id+", '"+id+"', '0', 1, "+user_id+", 1590058601000003, NULL, -1, 1872018091301, "+fee+", "+fee+", 0.00, 35, 1, 0, NULL, NULL, NULL, 0.00, 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '2018-11-20 16:52:00', 0, NULL, 1, NULL, 0, '10001', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 5, 0, 0, 0, NULL, NULL, 1086059000000411, '新闸路831号', '200135', 'tom', NULL, NULL, '18717718990', NULL, NULL, NULL, 10, '上海', 110, '上海市', NULL, NULL, 1140, '静安区', '', NULL, NULL, 0, NULL, 0.00, NULL, 0, NULL, 0, 1, 0, 0, 1870081300000047, '17521282079', NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', '127.0.0.1', NULL, '系统', NULL, NULL, '2018-11-20 16:52:00', NULL, NULL, 187, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, 'ody', NULL, NULL, NULL, 0, '', 0.00, 0.00, 0.00, NULL, 0, NULL, 1046014700000833, NULL, NULL, NULL, 35, 0, 0, '上海三少猩球贸易有限公司', NULL, NULL, 0, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL, NULL, NULL, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, NULL, 0.00, NULL, 1590058601000008, '三少猩球贸易有限公司', NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
				//String sqlSoItem = "  ("+id+", NULL, 1, '"+id+"', "+user_id+", 1590058601000003, "+levelProId+", 1900084000000240, 1872018091301, 0.00, NULL, NULL, "+fee+", 1, '"+levelProName+"', NULL, 'https://cdn.oudianyun.com/dev/back-product-web/1542541241488_59.52936780076672_5ed25f84-0545-4f29-a211-b0d6c2ca0c25.png', NULL, 1, 0.00, 0.00, 0.00, NULL, 0, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, 1, NULL, 0.00, NULL, 0.00, NULL, NULL, 0.000, NULL, NULL, NULL, 0, 1, 0, 0, 1870081300000047, NULL, NULL, NULL, '2018-11-20 16:21:13', NULL, '127.0.0.1', NULL, NULL, NULL, NULL, '2018-11-20 16:21:13', NULL, NULL, 187, '181120537951367047', 0.00, NULL, NULL, NULL, NULL, NULL, '1900084100000002', NULL, '份', NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, 0.00, 0, NULL, NULL, NULL, NULL, NULL, 1900082400000089, '来伊份', NULL, 1900083602000056, '增值服务费', '1900080800000001-1900083602000056', 'B2C后台类目->增值服务费', NULL, NULL, NULL, 1, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, 90, 30, 1, 1, NULL, NULL, NULL);";
				String sqlSo2="";
				String sqlSoItem2="";
				if(level.equals("0")){//0代表降为普通用户，删除订单，
					 sqlSo2 = " delete  FROM  `order`.`so`   where user_id="+user_id+" and create_time ='2018-11-20 16:21:13' ";
					 sqlSoItem2 = " delete  FROM    `order`.`so_item` where user_id="+user_id+" and create_time ='2018-11-20 16:21:13'  ";
				}else{
					 sqlSo2 = " UPDATE  `order`.`so`  SET order_amount="+fee+", product_amount="+fee+" where user_id="+user_id+" and create_time ='2018-11-20 16:21:13' ";
					 sqlSoItem2 = " UPDATE  `order`.`so_item` SET product_id="+levelProId+",product_price_final="+fee+",product_cname='"+levelProName+"' where user_id="+user_id+" and create_time ='2018-11-20 16:21:13'  ";
				}
				jdbcTemplateOrder.execute(sqlSo2);//插入订单
				jdbcTemplateOrder.execute(sqlSoItem2);//插入订单详情
				if(i==(list.size()-1)){
					//更新用户星级
					int length = importSoServiceImpl.batchUpdateOuserLevelCode(listLevelCode);       
					logger.info("批量更新用户星级数量>>>："+length+"&&&用户id:"+listLevelCode.toArray());
				}
			}
			//logger.info("执行sql语句打印》》》》"+sqlSo);
			logger.info("更新退款补款操作的种子会员星级和订单金额结束>>>"+StringUtil.dateToString2(new Date()));
		} catch (Exception e) {
			logger.info("异常信息>>>"+e.getMessage(),e);
			model.addAttribute("errorInfo", e.getMessage());
			return "so/soError";
		}
		
		return "so/soList";
	}
	
	

}
