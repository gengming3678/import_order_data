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
import com.palline.system.service.imp.ImportSoServiceImpl;

/**
 * 导入会员上下家关系
 * @author gmm
 * @version 1.0
 */
@Controller
@RequestMapping("/ImportSoOutUpDown")
public class ImportSoOutUpDownController {
	private static Logger logger=Logger.getLogger(ImportSoOutUpDownController.class); 
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name="jdbcTemplateOrder")
	private JdbcTemplate jdbcTemplateOrder;
	@Resource(name="jdbcTemplateOuser")
	private JdbcTemplate jdbcTemplateOuser;
	@Resource
	private ImportSoServiceImpl importSoServiceImpl;

	/**
	 * 导入会员上下家关系
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/importOutUpDownData.do")
	public String importOutUpDownData(HttpServletRequest request,Model model) {
		try {
			logger.info("导入会员上下家关系开始>>>"+StringUtil.dateToString2(new Date()));
			List<String> ids=new ArrayList<String>();
			String suffix="188721181225";//12位，1201，日期编码每次导入依次递增
			List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from sheet3 order by 星级 desc");         
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
			
			Map<String,Long> listDistributorId = importSoServiceImpl.getDistributorIdByUserId(list);
			int p=0;
			//int k=0;
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=list.get(i);
				//String id = ids.get(i);
				//获取tel这列数据,查询user_id
				String tel = (String) map.get("电话");
				//String level = (String) map.get("星级");
				//String fee = (String) map.get("缴款金额");
				String idCardNum = (String) map.get("身份证号");
				Integer num = (Integer) map.get("序号");
				
				String name = (String) map.get("姓名");
				String upName = (String) map.get("乔丽尔上家");
				
				String type="1";
				Long user_id=null;
				//先根据手机号去系统查；
				String sqlDistributor = "select user_id from distributor where mobile = '"+tel+"' ORDER BY registration_time desc limit 1";
			    List<Map<String,Object>> distributorList = jdbcTemplate.queryForList(sqlDistributor);
			    if(distributorList.size()>0){//根据手机号查得到；
			    	type="1";
			    	user_id = (Long) distributorList.get(0).get("user_id");
			    }else{
			    	//2则再次根据身份证查找；
		    		String userIdCardSql = " select t.user_id from u_user_identity_card t where t.identity_card_number = CONCAT('@%^*',TO_BASE64(AES_ENCRYPT('"+idCardNum+"','1fi;qPa7utddahWy')))  ORDER BY create_time desc ";
			    	List<Map<String,Object>> userIdCardList = jdbcTemplateOuser.queryForList(userIdCardSql);
			    	if(userIdCardList.size()>0){
			    		user_id = (Long) userIdCardList.get(0).get("user_id");
			    		type="1";
			    	}else{
			    		type="2";
			    		jdbcTemplate.execute("update sheet3 set 邮箱='2' where 序号="+num+""); 
			    		continue;
			    	}
			    }
				
			    //String sqlSo2="";
			    //String sqlSoItem2="";
			    //更新上下家关系
			    //首先更新上家是三少猩球
			    //再次更新其它
			    List<Map<String,Object>>  curUserInfoList = jdbcTemplate.queryForList("select id,path,parent_id from distributor where user_id="+user_id+" ORDER BY registration_time desc limit 1");
			    if(curUserInfoList.size()>0){
			    	Long idL = (Long) curUserInfoList.get(0).get("id");
				    if(upName.equals("三少猩球")){//上家是平台
				    	//String updateSql=" update distributor  set path='/"+idL+"/',parent_id=null where id= "+idL;
				    	String updateSql=" update distributor  set parent_id=null where id= "+idL;
				    	jdbcTemplate.execute(updateSql);
				    	p++;
				    }else{
				    	Long parentIdL = listDistributorId.get(upName);
				    	if(parentIdL==null){//再次归平台（和财务确认过这样做）
				    		//String updateSql=" update distributor  set path='/"+idL+"/',parent_id=null where id= "+idL;
				    		String updateSql=" update distributor  set parent_id=null where id= "+idL;
					    	jdbcTemplate.execute(updateSql);
					    	p++;
				    	}else{//归个人
				    		//String updateSql=" update distributor  set path='/"+parentIdL+"/"+idL+"/',parent_id="+parentIdL+" where id= "+idL;
				    		String updateSql=" update distributor  set parent_id="+parentIdL+" where id= "+idL;
					    	jdbcTemplate.execute(updateSql);
					    	p++;
				    	}
				    }
				    jdbcTemplate.execute("update sheet3 set 邮箱='1' where 序号="+num+"");   
			    }
			    
	
				//内部种子用户专用收货人手机号——17521282079；
				/*String sqlSo = " ("+id+", '"+id+"', '0', 1, "+user_id+", 1590058601000003, NULL, -1, 1872018091301, "+fee+", "+fee+", 0.00, 35, 1, 0, NULL, NULL, NULL, 0.00, 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '2018-11-20 16:52:00', 0, NULL, 1, NULL, 0, '10001', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 5, 0, 0, 0, NULL, NULL, 1086059000000411, '新闸路831号', '200135', 'tom', NULL, NULL, '17521282079', NULL, NULL, NULL, 10, '上海', 110, '上海市', NULL, NULL, 1140, '静安区', '', NULL, NULL, 0, NULL, 0.00, NULL, 0, NULL, 0, 1, 0, 0, 1870081300000047, '17521282079', NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', '127.0.0.1', NULL, '系统', NULL, NULL, '2018-11-20 16:52:00', NULL, NULL, 187, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, 'ody', NULL, NULL, NULL, 0, '', 0.00, 0.00, 0.00, NULL, 0, NULL, 1046014700000833, NULL, NULL, NULL, 35, 0, 0, '上海三少猩球贸易有限公司', NULL, NULL, 0, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL, NULL, NULL, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, NULL, 0.00, NULL, 1590058601000008, '三少猩球贸易有限公司', NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
				String sqlSoItem = "  ("+id+", NULL, 1, '"+id+"', "+user_id+", 1590058601000003, "+levelProId+", 1900084000000240, 1872018091301, 0.00, NULL, NULL, "+fee+", 1, '"+levelProName+"', NULL, 'https://cdn.oudianyun.com/dev/back-product-web/1542541241488_59.52936780076672_5ed25f84-0545-4f29-a211-b0d6c2ca0c25.png', NULL, 1, 0.00, 0.00, 0.00, NULL, 0, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, 1, NULL, 0.00, NULL, 0.00, NULL, NULL, 0.000, NULL, NULL, NULL, 0, 1, 0, 0, 1870081300000047, NULL, NULL, NULL, '2018-11-20 16:21:13', NULL, '127.0.0.1', NULL, NULL, NULL, NULL, '2018-11-20 16:21:13', NULL, NULL, 187, '181120537951367047', 0.00, NULL, NULL, NULL, NULL, NULL, '1900084100000002', NULL, '份', NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, 0.00, 0, NULL, NULL, NULL, NULL, NULL, 1900082400000089, '来伊份', NULL, 1900083602000056, '增值服务费', '1900080800000001-1900083602000056', 'B2C后台类目->增值服务费', NULL, NULL, NULL, 1, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, 90, 30, 1, 1, NULL, NULL, NULL);";
				*/
				//外部用户专用收货人手机号-18717718990；
				//String sqlSo = " ("+id+", '"+id+"', '0', 1, "+user_id+", 1590058601000003, NULL, -1, 1872018091301, "+fee+", "+fee+", 0.00, 35, 1, 0, NULL, NULL, NULL, 0.00, 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '2018-11-20 16:52:00', 0, NULL, 1, NULL, 0, '10001', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 5, 0, 0, 0, NULL, NULL, 1086059000000411, '新闸路831号', '200135', 'tom', NULL, NULL, '18717718990', NULL, NULL, NULL, 10, '上海', 110, '上海市', NULL, NULL, 1140, '静安区', '', NULL, NULL, 0, NULL, 0.00, NULL, 0, NULL, 0, 1, 0, 0, 1870081300000047, '17521282079', NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', '127.0.0.1', NULL, '系统', NULL, NULL, '2018-11-20 16:52:00', NULL, NULL, 187, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, 'ody', NULL, NULL, NULL, 0, '', 0.00, 0.00, 0.00, NULL, 0, NULL, 1046014700000833, NULL, NULL, NULL, 35, 0, 0, '上海三少猩球贸易有限公司', NULL, NULL, 0, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL, NULL, NULL, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, NULL, 0.00, NULL, 1590058601000008, '三少猩球贸易有限公司', NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
				//String sqlSoItem = "  ("+id+", NULL, 1, '"+id+"', "+user_id+", 1590058601000003, "+levelProId+", 1900084000000240, 1872018091301, 0.00, NULL, NULL, "+fee+", 1, '"+levelProName+"', NULL, 'https://cdn.oudianyun.com/dev/back-product-web/1542541241488_59.52936780076672_5ed25f84-0545-4f29-a211-b0d6c2ca0c25.png', NULL, 1, 0.00, 0.00, 0.00, NULL, 0, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, 1, NULL, 0.00, NULL, 0.00, NULL, NULL, 0.000, NULL, NULL, NULL, 0, 1, 0, 0, 1870081300000047, NULL, NULL, NULL, '2018-11-20 16:21:13', NULL, '127.0.0.1', NULL, NULL, NULL, NULL, '2018-11-20 16:21:13', NULL, NULL, 187, '181120537951367047', 0.00, NULL, NULL, NULL, NULL, NULL, '1900084100000002', NULL, '份', NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, 0.00, 0, NULL, NULL, NULL, NULL, NULL, 1900082400000089, '来伊份', NULL, 1900083602000056, '增值服务费', '1900080800000001-1900083602000056', 'B2C后台类目->增值服务费', NULL, NULL, NULL, 1, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, 90, 30, 1, 1, NULL, NULL, NULL);";	
			}
			logger.info("导入会员上下家关系结束>>>成功插入 "+p+" 条数据！");
			logger.info("导入会员上下家关系结束>>>"+StringUtil.dateToString2(new Date()));
		} catch (Exception e) {
			logger.info("异常信息>>>"+e.getMessage(),e);
			model.addAttribute("errorInfo", e.getMessage());
			return "so/soError";
		}
		
		return "so/soList";
	}
	
	

}
