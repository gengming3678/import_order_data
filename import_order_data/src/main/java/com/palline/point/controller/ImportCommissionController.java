package com.palline.point.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.palline.common.util.StringUtil;
import com.palline.system.entity.AccountInfo;
import com.palline.system.entity.AccountOprLog;
import com.palline.system.entity.Constants;
import com.palline.system.entity.Ouser;
import com.palline.system.service.imp.ImportSoServiceImpl;

/**
 * 刷会员共享分佣收入
 * @author gmm
 * @version 1.0
 */
@Controller
@RequestMapping("/ImportCommission")
public class ImportCommissionController {
	private static Logger logger=Logger.getLogger(ImportCommissionController.class); 
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name="jdbcTemplateOrder")
	private JdbcTemplate jdbcTemplateOrder;
	@Resource(name="jdbcTemplateOuser")
	private JdbcTemplate jdbcTemplateOuser;
	@Resource(name="jdbcTemplateFinance")
	private JdbcTemplate jdbcTemplateFinance;
	@Resource
	private ImportSoServiceImpl importSoServiceImpl;
	public Long count=108606071220l;
	public int lun=1;
	
	/**
	 * 刷会员共享分佣收入
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/raskImportCommissionData.do")
	public String raskImportCommissionData(HttpServletRequest request,Model model) {
		  Runnable runnable = new Runnable() {  
	            public void run() {  
	            	 java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                 String startDate = format1.format(new Date());
	                 logger.info("第"+lun+"轮分发给所有会员共享佣金");
	                 logger.info("开始时间："+startDate);
	                 raskCommissionData();
	                 String endDate = format1.format(new Date());
	                 logger.info("结束时间："+endDate);
	            }  
	        };  
	        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();  
	        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
	        service.scheduleAtFixedRate(runnable, 10, 60, TimeUnit.SECONDS);  
	        return "so/soList";
	}
	
	
	/**
	 * 刷会员共享分佣收入
	 * @param request
	 * @param model
	 * @return
	 */
	public void raskCommissionData() {
		try {
			logger.info("刷会员共享分佣收入开始>>>"+StringUtil.dateToString2(new Date()));
			List<String> ids=new ArrayList<String>();
			BigDecimal feeTotalBig=new BigDecimal("0");
			BigDecimal coutTotalBig=new BigDecimal("0");
			List<Map<String,Object>> feeTotalList = jdbcTemplateFinance.queryForList(" select sum(t.`缴款金额`) fee from sheet_all_share_money t  "); 
			if(feeTotalList.size()>0){
				feeTotalBig = (BigDecimal) feeTotalList.get(0).get("fee");
			}
			List<Map<String,Object>> coutlist = jdbcTemplateFinance.queryForList(" select count(t.id) count from sheet_all_share_money t  ");    
			if(coutlist.size()>0){
				Long coutTotal = (Long) coutlist.get(0).get("count");
				coutTotalBig=new BigDecimal(coutTotal);
			}
			BigDecimal fenYongBilie=new BigDecimal(0.03);//所有会员共享分佣3%
			BigDecimal balanceBig = feeTotalBig.multiply(fenYongBilie).divide(coutTotalBig, 2, RoundingMode.HALF_UP);
			//10000等于1块钱
			BigDecimal beishu=new BigDecimal(10000);
			BigDecimal balanceL = balanceBig.multiply(beishu);
			String suffix=String.valueOf(count);//12位，1201，日期编码每次导入依次递增
			//每次执行取300条数据；
			List<Map<String,Object>> list = jdbcTemplateFinance.queryForList(" select * from sheet_all_share_money  where 是否更新=3 order by id limit 300 ");         
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
			List<AccountOprLog> accountOprLogList=new ArrayList<AccountOprLog>();
			List<AccountInfo> accountInfoList=new ArrayList<AccountInfo>();
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=list.get(i);
				String id = ids.get(i);
				//获取tel这列数据,查询user_id
				String name = (String) map.get("姓名");
				String tel = (String) map.get("电话");
				String level = (String) map.get("现商户星级");
				//String tuiFee = (String) map.get("退款金额");
				//String fee = (String) map.get("实际金额");
				String idCardNum = (String) map.get("身份证号");
				Integer num = (Integer) map.get("id");
				
				//Integer type=2;
				//Integer type2=2;
				Long user_id=null;
				Long distributor_id=null;
				//先根据手机号去系统查；
				String sqlDistributor = "select user_id,id from distributor where mobile = '"+tel+"' ORDER BY registration_time desc limit 1";
			    List<Map<String,Object>> distributorList = jdbcTemplate.queryForList(sqlDistributor);
			    if(distributorList.size()>0){//手机号查得到；
			    	user_id = (Long) distributorList.get(0).get("user_id");
			    	distributor_id = (Long) distributorList.get(0).get("id");
			    	p++;
			    }else{
		    	    //2则再次根据身份证查找；
		    		String userIdCardSql = " select t.user_id from u_user_identity_card t where t.identity_card_number = CONCAT('@%^*',TO_BASE64(AES_ENCRYPT('"+idCardNum+"','1fi;qPa7utddahWy')))   ";
			    	List<Map<String,Object>> userIdCardList = jdbcTemplateOuser.queryForList(userIdCardSql);
			    	if(userIdCardList.size()>0){
			    		user_id = (Long) userIdCardList.get(0).get("user_id");
			    		p++;
			    	}else{
			    		k++;
			    		jdbcTemplateFinance.execute(" update sheet_all_share_money set 是否注册=2 , 是否更新=2 where id="+num+"   "); 
			    		continue;
			    	}
			    }
			    //Long levelProId = Constants.MapLevelId.mapLevel.get(level);
				//String levelProName = Constants.MapLevelName.mapLevelCh.get(level);
				String levelCode = Constants.MapLevelCode.mapLevelCode.get(level);
				Ouser ouser=new Ouser();
				ouser.setLevelCode(levelCode);
				ouser.setUserId(user_id);
				listLevelCode.add(ouser);
				if(distributor_id==null){
					String sqlDistributor2 = " select id from distributor where user_id = "+user_id+" ORDER BY registration_time desc limit 1 ";
				    List<Map<String,Object>> distributorList2 = jdbcTemplate.queryForList(sqlDistributor2);
				    if(distributorList2.size()>0){
				    	distributor_id = (Long) distributorList2.get(0).get("id");
				    }
				}
				//插入用户的 我的粉丝佣金类型110002/共享收入佣金类型110003
				List<Map<String,Object>>  listAccountInfo = jdbcTemplateFinance.queryForList("select  id , balance from  account_info where entity_id ="+distributor_id+"");
				if(listAccountInfo.size()>0){
					BigInteger infoId = (BigInteger) listAccountInfo.get(0).get("id");
					BigDecimal infoIdBig=new BigDecimal(infoId);
					//总表金额
					Long balance = (Long) listAccountInfo.get(0).get("balance");
					BigDecimal balanceBigF=new BigDecimal(balance);
					BigDecimal balanceFial = balanceBigF.add(balanceL);
					//总表
					AccountInfo accountInfo=new AccountInfo();
					accountInfo.setId(infoIdBig.longValue());
					accountInfo.setBalance(balanceFial);
					accountInfoList.add(accountInfo);
					//流水
					AccountOprLog accountOprLog=new AccountOprLog();
					accountOprLog.setId(new BigDecimal(id));
					accountOprLog.setInfoId(infoIdBig);
					accountOprLog.setDistributorId(distributor_id);
					accountOprLog.setBalanceL(balanceL);
					accountOprLog.setAfterAmount(balanceFial);
					accountOprLog.setType(110003);//共享分佣110003；粉丝分佣110002；
					accountOprLogList.add(accountOprLog);
				}
				jdbcTemplateFinance.execute(" update sheet_all_share_money set 是否注册=1 , 是否更新=1 where id="+num+" "); //纪录更新否，//纪录查到用户否
			}
			if(accountInfoList.size()>0){
				int infoNum = importSoServiceImpl.batchUpdateAccountInfo(accountInfoList);
				//共享类型佣金版本号version_no=20198888
				int logNum = importSoServiceImpl.batchInsertAccountOprLog(accountOprLogList);
				logger.info("刷会员共享分佣收入结束>>>批量插入流水 "+logNum+" 条数据！");
				logger.info("刷会员共享分佣收入结束>>>批量更新总表 "+infoNum+" 条数据！");
			}
			logger.info("刷会员共享分佣收入结束>>>找到user_id "+p+" 条数据！");
			logger.info("刷会员共享分佣收入结束>>>未找到user_id "+k+" 条数据！");
			logger.info("刷会员共享分佣收入结束>>>"+StringUtil.dateToString2(new Date()));
			count++;
			lun++;
		} catch (Exception e) {
			logger.info("异常信息>>>"+e.getMessage(),e);
		}
		
	}
	
	

	public Long getCount() {
		return count;
	}


	public void setCount(Long count) {
		this.count = count;
	}


	public int getLun() {
		return lun;
	}


	public void setLun(int lun) {
		this.lun = lun;
	}
	
	
	

}
