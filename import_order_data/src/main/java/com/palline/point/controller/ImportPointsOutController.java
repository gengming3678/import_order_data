package com.palline.point.controller;

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
 * 刷会员积分
 * @author gmm
 * @version 1.0
 */
@Controller
@RequestMapping("/ImportPointsOut")
public class ImportPointsOutController {
	private static Logger logger=Logger.getLogger(ImportPointsOutController.class); 
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name="jdbcTemplateOrder")
	private JdbcTemplate jdbcTemplateOrder;
	@Resource(name="jdbcTemplateOuser")
	private JdbcTemplate jdbcTemplateOuser;
	@Resource
	private ImportSoServiceImpl importSoServiceImpl;

	/**
	 * 刷会员积分
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/importPointsOutData.do")
	public String importPointsOutData(HttpServletRequest request,Model model) {
		try {
			logger.info("刷会员积分开始>>>"+StringUtil.dateToString2(new Date()));
			List<String> ids=new ArrayList<String>();
			String suffix="108606071229";//12位，1201，日期编码每次导入依次递增
			List<Map<String,Object>> list = jdbcTemplateOuser.queryForList("select * from sheet8 order by 序号  ");         
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
				Integer num = (Integer) map.get("序号");
				String name = (String) map.get("姓名");
				String tel = (String) map.get("电话");
				String level = (String) map.get("星级");
				String fee = (String) map.get("缴款金额");
				String idCardNum = (String) map.get("身份证号");
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
			    		jdbcTemplateOuser.execute("update sheet8 set 邮箱='"+type+"' where 序号="+num+""); 
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
				String type2="3";
				//外部用户专用收货人手机号-18717718990；/create_time 2018-11-26 16:21:13；
		    	String sqlAccountinfo =" SELECT * FROM account_info where user_id="+user_id+"  ";
				List<Map<String,Object>> accountinfoList = jdbcTemplateOuser.queryForList(sqlAccountinfo);
				if(accountinfoList.size()>0){
					Long aId = (Long) accountinfoList.get(0).get("id");
					if(level.equals("0")){//降为普通用户的，删除之前的订单流水积分；
			    		String sqlUpdate=" delete FROM  account_flow   where user_id="+user_id+" and action_type=4 ";
				    	jdbcTemplateOuser.execute(sqlUpdate);//删除积分流水表；
				    	type2="2";
			    	}else{
						String insertSql=" INSERT INTO account_flow (`id`, `user_id`, `action_type`, `account_id`, `trans_type`, `amount_action`, `amount_trans`, `amount_trans_balance`, `amount_deadline`, `freeze_action`, `freeze_trans`, `freeze_trans_balance`, `freeze_deadline`, `order_no`, `company_id`, `amount_trans_time`, `remark`, `is_available`, `is_deleted`, `version_no`, `create_userid`, `create_username`, `create_userip`, `create_usermac`, `create_time`, `create_time_db`, `server_ip`, `update_userid`, `update_username`, `update_userip`, `update_usermac`, `update_time`, `update_time_db`, `busi_type`, `ref_no`, `identity_id`, `outflow_id`, `platform_id`, `merchant_id`, `channel_type`) VALUES  ("+id+", "+user_id+", 4, "+aId+", 2, 1, "+fee+", NULL, NULL, NULL, NULL, NULL, NULL, NULL, 11, '2018-11-20 16:21:13', '下单送积分', 1, 0, 0, NULL, 'superadmin', NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', NULL, NULL, NULL, NULL, NULL, '2018-11-20 16:21:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)";
				    	jdbcTemplateOuser.execute(insertSql);//插入积分流水表；
				    	type2="1";
			    	}
			    	String sqlUpdateAccountInfo=" update account_info t1 set t1.amount_balance=amount_balance+"+fee+" where t1.user_id="+user_id+" ";
			    	jdbcTemplateOuser.execute(sqlUpdateAccountInfo);//修改积分总额表；
				}
			    jdbcTemplateOuser.execute(" update sheet8 set 邮箱='"+type+"' , 交款方式='"+type2+"' where 序号="+num+" ");   
			}
			logger.info("刷会员积分结束>>>成功插入 "+p+" 条数据！");
			logger.info("刷会员积分结束>>>失败插入 "+k+" 条数据！");
			logger.info("刷会员积分结束>>>"+StringUtil.dateToString2(new Date()));
		} catch (Exception e) {
			logger.info("异常信息>>>"+e.getMessage(),e);
			model.addAttribute("errorInfo", e.getMessage());
			return "so/soError";
		}
		
		return "so/soList";
	}
	
	

}
