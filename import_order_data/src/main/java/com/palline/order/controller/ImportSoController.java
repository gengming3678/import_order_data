package com.palline.order.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.palline.common.util.StringUtil;
import com.palline.system.entity.Constants;
import com.palline.system.entity.Ouser;
import com.palline.system.service.imp.ImportSoServiceImpl;

/**
 * 导入种子会员订单和更新星级
 * @author gmm
 * @version 1.0
 */
@Controller
@RequestMapping("/ImportSo")
public class ImportSoController {
	private static Logger logger=Logger.getLogger(ImportSoController.class); 
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name="jdbcTemplateOrder")
	private JdbcTemplate jdbcTemplateOrder;
	@Resource(name="jdbcTemplateOuser")
	private JdbcTemplate jdbcTemplateOuser;
	@Resource
	private ImportSoServiceImpl importSoServiceImpl;

	/**
	 * 导入种子会员订单和更新星级
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/importData.do")
	public String importData(HttpServletRequest request,Model model) {
		try {
			logger.info("导入种子会员订单和更新星级开始>>>"+StringUtil.dateToString2(new Date()));
			List<String> ids=new ArrayList<String>();
			String suffix="188708421225";//12位，1201，日期编码每次导入依次递增
			List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from sheet1 order by 星级 desc");         
			for(int j=0;j<list.size();j++){
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
			}
			List<Ouser> listLevelCode=new ArrayList<Ouser>();
			int p=0;
			int k=0;
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=list.get(i);
				String id = ids.get(i);
				//获取tel这列数据,查询user_id
				String name = (String) map.get("姓名");
				String idCardNum = (String) map.get("身份证号码");
				String tel = (String) map.get("电话");
				String level = (String) map.get("星级");
				String fee = (String) map.get("缴款金额");
				Integer num = (Integer) map.get("序号");
				String type="1";
				Long user_id=null;
				//先根据手机号去系统查；
				String sqlDistributor = "select user_id from distributor where mobile = '"+tel+"' ORDER BY registration_time desc limit 1";
			    List<Map<String,Object>> distributorList = jdbcTemplate.queryForList(sqlDistributor);
			    if(distributorList.size()>0){//手机号查得到；
			    	type="1";
			    	user_id = (Long) distributorList.get(0).get("user_id");
			    	p++;
			    }else{  
			    	//2则再次根据身份证查找；
		    		String userIdCardSql = " select t.user_id from u_user_identity_card t where t.identity_card_number = CONCAT('@%^*',TO_BASE64(AES_ENCRYPT('"+idCardNum+"','1fi;qPa7utddahWy')))   ";
			    	List<Map<String,Object>> userIdCardList = jdbcTemplateOuser.queryForList(userIdCardSql);
			    	if(userIdCardList.size()>0){
			    		user_id = (Long) userIdCardList.get(0).get("user_id");
			    		type="3";
			    		p++;
			    	}else{
			    		type="4";
			    		k++;
			    		jdbcTemplate.execute("update sheet1 set 邮箱='"+type+"' where 序号="+num+""); 
			    		continue;
			    	}
			    }  
				Long levelProId = Constants.MapLevelId.mapLevel.get(level);
				String levelProName = Constants.MapLevelName.mapLevelCh.get(level);
				String levelCode = Constants.MapLevelCode.mapLevelCode.get(level);
				Ouser ouser=new Ouser();
				ouser.setLevelCode(levelCode);
				ouser.setUserId(user_id);
				listLevelCode.add(ouser);
				//select * from so t where t.order_status='35' and merchant_id=1590058601000003 and warehouse_id=1872018091301 order by id 718条
				//select * from so t where t.good_receiver_mobile in ('17521282079','18717718990') 718条
				//内部种子用户专用收货人手机号——17521282079 /create_time 2018-11-20 16:21:13；
				String sqlSo = " ("+id+", '"+id+"', '0', 1, "+user_id+", 1590058601000003, NULL, -1, 1872018091301, "+fee+", "+fee+", 0.00, 35, 1, 0, NULL, '2018-11-20 16:21:13', NULL, 0.00, 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '2018-11-20 16:21:13', 0, NULL, 1, NULL, 0, '10001', '10', '2018-11-20 16:21:13', '2018-11-20 16:21:13', NULL, NULL, NULL, NULL, NULL, 0, 5, 0, 0, 0, NULL, NULL, 1086059000000411, '新闸路831号', '200135', 'tom', NULL, NULL, '17521282079', NULL, NULL, NULL, 10, '上海', 110, '上海市', NULL, NULL, 1140, '静安区', '', '2018-11-20 16:21:13', '2018-11-20 16:21:13', 0, NULL, 0.00, NULL, 0, NULL, 0, 1, 0, 0, 1870081300000047, '17521282079', NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', '127.0.0.1', NULL, '系统', NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', NULL, 187, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, 'ody', NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', 0, '', 0.00, 0.00, 0.00, NULL, 0, NULL, 1046014700000833, NULL, NULL, NULL, 35, 0, 0, '上海三少猩球贸易有限公司', NULL, '2018-11-20 16:21:13', 0, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL, NULL, NULL, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, NULL, 0.00, NULL, 1590058601000008, '三少猩球贸易有限公司', NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
				String sqlSoItem = " ("+id+", NULL, 1, '"+id+"', "+user_id+", 1590058601000003, "+levelProId+", 1900084000000240, 1872018091301, 0.00, NULL, NULL, "+fee+", 1, '"+levelProName+"', NULL, 'https://cdn.oudianyun.com/dev/back-product-web/1542541241488_59.52936780076672_5ed25f84-0545-4f29-a211-b0d6c2ca0c25.png', NULL, 1, 0.00, 0.00, 0.00, NULL, 0, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, 1, NULL, 0.00, NULL, 0.00, NULL, NULL, 0.000, NULL, NULL, NULL, 0, 1, 0, 0, 1870081300000047, NULL, NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', '127.0.0.1', NULL, NULL, NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', NULL, 187, '181120537951367047', 0.00, NULL, NULL, NULL, NULL, NULL, '1900084100000002', NULL, '份', NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, 0.00, 0, NULL, NULL, NULL, NULL, NULL, 1900082400000089, '来伊份', NULL, 1900083602000056, '增值服务费', '1900080800000001-1900083602000056', 'B2C后台类目->增值服务费', NULL, NULL, NULL, 1, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, 90, 30, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);";

				String sqlSo2 = " INSERT INTO `order`.`so` VALUES "+sqlSo;
				String sqlSoItem2 = " INSERT INTO `order`.`so_item` VALUES "+sqlSoItem;
				jdbcTemplateOrder.execute(sqlSo2);//插入订单
				jdbcTemplateOrder.execute(sqlSoItem2);//插入订单详情
				//更改星级；
				String sqlLevel="update uc_user_membership_level t set t.membership_level_code='"+levelCode+"' where t.entity_id="+user_id+"";
				jdbcTemplateOuser.execute(sqlLevel);
				jdbcTemplate.execute("update sheet1 set 邮箱='"+type+"' where 序号="+num+""); 
				/*if(i==(list.size()-1)){
					//更新用户星级
					int length = importSoServiceImpl.batchUpdateOuserLevelCode(listLevelCode);       
					logger.info("批量更新用户星级数量>>>："+length+"&&&用户id:"+listLevelCode.toString());
				}*/
			}
			logger.info("导入星级用户订单数据结束>>>成功插入 "+p+" 条数据！");
			logger.info("导入星级用户订单数据结束>>>失败插入 "+k+" 条数据！");
			logger.info("导入种子会员订单和更新星级结束>>>"+StringUtil.dateToString2(new Date()));
		} catch (Exception e) {
			logger.info("异常信息>>>"+e.getMessage(),e);
			model.addAttribute("errorInfo", e.getMessage());
			return "so/soError";
		}
		
		return "so/soList";
	}
	
	

}
