package com.palline.point.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.palline.common.util.StringUtil;
import com.palline.system.entity.AccountInfo;
import com.palline.system.entity.AccountOprLog;
import com.palline.system.entity.Constants;
import com.palline.system.entity.Points;
import com.palline.system.entity.PointsLog;
import com.palline.system.service.imp.ImportSoServiceImpl;

/**
 * 发给上家/平台佣金
 * @author gmm
 * @version 1.0
 */
@Controller
@RequestMapping("/ImportCommissionUpDown")
public class ImportCommissionUpDownController {
	private static Logger logger=Logger.getLogger(ImportCommissionUpDownController.class); 
	
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
	public Long count=108606190102l;
	public int lun=1;
	
	/**
	 * 发给上家和平台佣金
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/raskImportCommissionUpDownData.do")
	public String raskImportCommissionUpDownData(HttpServletRequest request,Model model) {
		  Runnable runnable = new Runnable() {  
	            public void run() {  
	            	 java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                 String startDate = format1.format(new Date());
	                 logger.info("第"+lun+"轮分发给上家佣金");
	                 logger.info("开始时间："+startDate);
	                 importCommissionUpDownData();
	                 String endDate = format1.format(new Date());
	                 logger.info("结束时间："+endDate);
	            }  
	        };  
	        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();  
	        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
	        service.scheduleAtFixedRate(runnable, 10, 60*60, TimeUnit.SECONDS);  
	        return "so/soList";
	}
	
	/**
	 * 发给上家和平台佣金
	 * 必须先导入中间表之一  sheet_up_share_money_index  （上家是平台或会员，分发相应比例佣金和积分的引导表）
	 * 必须先导入中间表之二  sheet_up_share_money （上家是平台或会员，分发相应比例佣金和积分的会员表）
	 * @return
	 */
	public String importCommissionUpDownData() {
		try {
			logger.info("发给上家和平台佣金开始>>>"+StringUtil.dateToString2(new Date()));
			List<String> ids=new ArrayList<String>();
			String suffix=String.valueOf(count);//12位，1201，日期编码每次导入依次递增
			//
			String runOne = jdbcTemplateFinance.queryForObject(" select `批次名称` from sheet_up_share_money_index where 状态=1  ORDER BY id limit 1 ",String.class);   
			//每次只执行一批一个excel;
			List<Map<String,Object>> list = jdbcTemplateFinance.queryForList(" select * from sheet_up_share_money  where 批次='"+runOne+"' order by id  ");         
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
			//获取下家名称和DistributorId对应关系（下家名称，分销商表Id）；
			List<Map<String,Long>> listDistributorIdList = importSoServiceImpl.getCommissionUpDownDistributorIdByUserId(list);
			Map<String,Long> listDistributorId=listDistributorIdList.get(0);//当前用户名和分销商表distributor_Id映射关系
			Map<String,Long> userIds=listDistributorIdList.get(1);//当前用户名和user_Id映射关系
			//获取下家名称和星级对应关系（下家名称，星级)sheet_up_share_money；
			Map<String,Long> levelByUpNames = listDistributorIdList.get(2);
			int p=0;
			int k=0;
			int h=0;
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=list.get(i);
				String id = ids.get(i);
				//获取tel这列数据,查询user_id
				String tel = (String) map.get("电话");
				Integer level = (Integer) map.get("商户星级");
				Integer feeInt = (Integer) map.get("金额（元）");
				String idCardNum = (String) map.get("身份证号");
				Integer num = (Integer) map.get("id");
				String name = (String) map.get("姓名");
				String upName = (String) map.get("乔丽尔上家");
				
				BigDecimal fee=new BigDecimal(feeInt);
				Long user_id=null;
				Long distributor_id=null;
				//1先根据手机号去系统查；
				String sqlDistributor = "select id,user_id from distributor where mobile = '"+tel+"' ORDER BY registration_time desc limit 1";
			    List<Map<String,Object>> distributorList = jdbcTemplate.queryForList(sqlDistributor);
			    if(distributorList.size()>0){//根据手机号查得到；
			    	p++;
			    	user_id = (Long) distributorList.get(0).get("user_id");
			    	distributor_id = (Long) distributorList.get(0).get("id");
			    }else{
			    	//2则再次根据身份证查找；
		    		String userIdCardSql = " select t.user_id from u_user_identity_card t where t.identity_card_number = CONCAT('@%^*',TO_BASE64(AES_ENCRYPT('"+idCardNum+"','1fi;qPa7utddahWy')))  ORDER BY create_time desc ";
			    	List<Map<String,Object>> userIdCardList = jdbcTemplateOuser.queryForList(userIdCardSql);
			    	if(userIdCardList.size()>0){
			    		user_id = (Long) userIdCardList.get(0).get("user_id");
			    		p++;
			    	}else{
			    		k++;
			    		jdbcTemplateFinance.execute(" update sheet_up_share_money set 是否注册=2,是否更新=2  where id="+num+" "); 
			    		continue;
			    	}
			    }
			    //当前用户的分销商id
			    if(distributor_id==null){//部分根据人名或身份证号查到的，再次执行查询；找到分销商Id
					String sqlDistributor2 = " select id from distributor where user_id = "+user_id+" ORDER BY registration_time desc limit 1 ";
				    List<Map<String,Object>> distributorList2 = jdbcTemplate.queryForList(sqlDistributor2);
				    if(distributorList2.size()>0){
				    	distributor_id = (Long) distributorList2.get(0).get("id");
				    }
				}
			    //首先更新上家是三少猩球
			    //再次更新其它
			    Long parentIdL=null;
			    if(upName.equals("三少猩球")){//上家是平台|平台全拿佣金;
			    	//上家是平台分佣金；
			    	fenPlatAccout(user_id,id,parentIdL,distributor_id,fee);
			    	h++;
			    }else{
			    	parentIdL = listDistributorId.get(upName);//上家的分销商id
			    	if(parentIdL==null){//再次归平台（和财务确认过这样做）
			    		//上家是平台分佣金；
			    		fenPlatAccout(user_id,id,parentIdL,distributor_id,fee);
			    		h++;
			    	}else{//归个人
			    		Long user_id_up=userIds.get(upName);//获取上家user_id
			    		//上家是会员分佣金
			    		fenPersonAccout(user_id_up,id,parentIdL,distributor_id,fee,upName,levelByUpNames);
			    		//上家是会员分积分
			    		fenPersonPoint(user_id_up,id,parentIdL,distributor_id,fee,upName,levelByUpNames);
			    	}
			    }
				jdbcTemplateFinance.execute(" update sheet_up_share_money set 是否注册=1,是否更新=1 where id="+num+" "); //纪录更新否，//纪录查到用户否
			}
			logger.info("发给上家和平台佣金结束>>>成功插入 "+p+" 条数据！");
			logger.info("发给上家和平台佣金结束>>>失败插入 "+k+" 条数据！");
			logger.info("发给上家和平台佣金结束>>>"+StringUtil.dateToString2(new Date()));
			jdbcTemplateFinance.execute(" update sheet_up_share_money_index set 状态=2 where 批次名称='"+runOne+"' "); //纪录更新否，//纪录查到用户否
			count++;
			lun++;
			//最后批量插入上家是会员的佣金和积分的总表和明细表，以及上家是平台的佣金总表和明细表
			//第一个：上家是会员的批量分发佣金和积分
			//1-1真正批量插入上家该拿到下家缴款金额的分佣佣金的70%
			/*if(accountOprLogList.size()>0){
				int accoutLogNum = importSoServiceImpl.batchInsertAccountOprLog(accountOprLogList);
				int accoutInfoNum = importSoServiceImpl.batchUpdateAccountInfo(accountInfoList);
				logger.info("上家是会员分发70%佣金>>>批量插入流水 "+accoutLogNum+" 条数据！");
				//logger.info("上家是会员分发70%佣金>>>批量更新总表 "+accoutInfoNum+" 条数据！");
			}
			//1-2真正批量插入上家该拿到下家缴款金额的分佣佣金30%是要兑换成积分100:1
			if(pointsLogList.size()>0){
				int pointLogNum = importSoServiceImpl.batchInsertPointsLog(pointsLogList);
				int pointInfoNum = importSoServiceImpl.batchUpdatePoints(pointsList);
				logger.info("上家是会员分发30%积分>>>批量插入流水 "+pointLogNum+" 条数据！");
				//logger.info("上家是会员分发30%积分>>>批量更新总表 "+pointInfoNum+" 条数据！");
			}
			logger.info("上家是平台分发佣金>>>批量插入流水 "+h+" 条数据！");
			//logger.info("上家是平台分发佣金>>>批量更新总表 "+h+" 条数据！");
			//第二个：上家是平台的批量分发佣金
			if(platCommissionLogList.size()>0){
				int platLogNum = importSoServiceImpl.batchPlatInsertAccountOprLog(platCommissionLogList);
				int platInfoNum = importSoServiceImpl.batchPlatUpdateAccountInfo(platCommissionList);
				logger.info("刷上下家平台佣金结束>>>批量更新总表 "+platInfoNum+" 条数据！");
				logger.info("刷上下家平台佣金结束>>>批量插入流水 "+platLogNum+" 条数据！");
			}*/
		} catch (Exception e) {
			logger.info("异常信息>>>"+e.getMessage(),e);
			return "so/soError";
		}
		return "so/soList";
	}
	
	/**
	 * 1-1上家是会员_更新上家的——佣金总表和插入流水
	 * 
	 * @param user_id 当前会员的上家用户表id
	 * @param id id
	 * @param parentIdL 当前会员的上家分销商表id
	 * @param distributor_id 当前会员的分销商表id
	 * @param fee 当前会员缴款金额
	 * @param upName 当前会员的上家名称
	 * @param levelByUpNames 会员和星级对应关系
	 */
	public void fenPersonAccout(Long user_id,String id,Long parentIdL,Long distributor_id,BigDecimal fee,String upName,Map<String,Long> levelByUpNames){
		//插入用户的 我的粉丝佣金类型110002/共享收入佣金类型110003
		//1发给上家佣金根据上家星级比例
		//获取当前会员上家的总表信息；
		//entity_id是上家分销商id
		List<Map<String,Object>>  listAccountInfo = jdbcTemplateFinance.queryForList(" select  id , balance from  account_info where entity_id ="+parentIdL+" ");
		if(listAccountInfo.size()>0){
			//总表id
			BigInteger infoId = (BigInteger) listAccountInfo.get(0).get("id");
			BigDecimal infoIdBig=new BigDecimal(infoId);
			//总表金额
			Long balance = (Long) listAccountInfo.get(0).get("balance");
			BigDecimal balanceBig=new BigDecimal(balance);
			//得到上家的星级；
			Long startLevel = levelByUpNames.get(upName);
			//根据上家星级得到上家分佣和积分比例，分别插入上家分佣总表和明细，积分总表和明细；
			Double accoutBiLi = Constants.LevelAccout.mapLevelAccoutBiLi.get(startLevel);
			BigDecimal accoutBalanceBig = fee.multiply(new BigDecimal(accoutBiLi)).setScale(2, RoundingMode.HALF_UP);
			BigDecimal balanceFial = balanceBig.add(accoutBalanceBig);
			//1插入佣金流水
			final AccountOprLog accountOprLog=new AccountOprLog();
			accountOprLog.setId(new BigDecimal(id));
			accountOprLog.setInfoId(infoIdBig);
			accountOprLog.setDistributorId(parentIdL);//发给当前会员的上家
			accountOprLog.setBalanceL(accoutBalanceBig);
			accountOprLog.setAfterAmount(balanceFial);
			accountOprLog.setType(110002);//粉丝分佣类型110002；共享分佣类型110003；
			//粉丝佣金类型版本号version_no=20199999
			String sqlaccountOprLog=" INSERT INTO account_opr_log(`id`, `account_id`, `entity_id`, `entity_type`, `entity_code`, `entity_name`, `payment_type`, `payment_voucher`, `bank_name`, `bank_account_no`, `bank_account_name`, `outer_trans_type`, `inner_trans_type`, `balance_dc_direct`, `balance_trans_amount`, `balance_after_amount`, `freeze_dc_direct`, `freeze_trans_amount`, `freeze_after_amount`, `trans_time`, `unfreeze_time`, `order_no`, `ref_no`, `batch_no`, `remark`, `unique_flag`, `company_id`, `is_deleted`, `trans_src_userid`, `merchant_id`, `root_merchant_id`, `create_userid`, `create_username`, `create_time`, `server_ip`, `version_no`) "
					+ " VALUES (?, ?, ?, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ?, 1, 2, ?, ?, NULL, NULL, NULL, NULL, now(), '', '', NULL, NULL, NULL, 187, NULL, NULL, NULL, NULL, NULL, NULL, now(), '299.299.299.299', 20199999);";
			jdbcTemplateFinance.update(sqlaccountOprLog, 
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setBigDecimal(1, accountOprLog.getId());
							ps.setBigDecimal(2, accountOprLog.getInfoId());
							ps.setLong(3, accountOprLog.getDistributorId()); 
							ps.setInt(4, accountOprLog.getType());
							ps.setBigDecimal(5, accountOprLog.getBalanceL()); 
							ps.setBigDecimal(6, accountOprLog.getAfterAmount()); 
						}
				});
			//2更新佣金总表
			final AccountInfo accountInfo=new AccountInfo();
			accountInfo.setId(infoIdBig.longValue());
			accountInfo.setBalance(balanceFial);//上家应该分多少钱取决于自己的星级对应的比例*下家缴款金额
			String sqlAccountInfo=" update  account_info set  balance=? where id=? ";
			jdbcTemplateFinance.update(sqlAccountInfo, 
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setBigDecimal(1, accountInfo.getBalance());
							ps.setLong(2, accountInfo.getId());   
						}
				});			
		}
	}
	
	
	
	/**
	 * 1-2上家是会员_更新上家的——积分总表和插入流水
	 * 
	 * @param user_id 当前会员的上家用户表id
	 * @param id id
	 * @param parentIdL 当前会员的上家分销商表id
	 * @param distributor_id 当前会员的分销商表id
	 * @param fee 当前会员缴款金额
	 * @param upName 当前会员的上家名称
	 * @param levelByUpNames 会员和星级对应关系
	 */
	public void fenPersonPoint(Long user_id,String id,Long parentIdL,Long distributor_id,BigDecimal fee,String upName,Map<String,Long> levelByUpNames){
		//插入用户的 我的粉丝佣金类型110002/共享收入佣金类型110003
		//2发给上家积分根据上家星级比例
		//查询上家用户id
    	String sqlAccountinfo =" SELECT id,amount_balance FROM account_info where user_id="+user_id+"  ";
		List<Map<String,Object>> accountinfoList = jdbcTemplateOuser.queryForList(sqlAccountinfo);
		if(accountinfoList.size()>0){
			Long accountId = (Long) accountinfoList.get(0).get("id");
			Integer amountBalance = (Integer) accountinfoList.get(0).get("amount_balance");
			Long startLevel = levelByUpNames.get(upName);
			Double pointBiLi = Constants.LevelPoint.mapLevelPointBiLi.get(startLevel);
			BigDecimal pointBalanceBig = fee.multiply(new BigDecimal(pointBiLi)).setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
			BigDecimal pointBalanceBigFinal = pointBalanceBig.add(new BigDecimal(amountBalance));
			
			//1插入积分流水表；
			final PointsLog pointsLog=new PointsLog();
			pointsLog.setId(Long.valueOf(id));
			pointsLog.setUserId(user_id);
			pointsLog.setAid(accountId);
			pointsLog.setFee(pointBalanceBig);
			pointsLog.setFeeFinal(pointBalanceBigFinal);
			String insertSql=" INSERT INTO account_flow (`id`, `user_id`, `action_type`, `account_id`, `trans_type`, `amount_action`, `amount_trans`, `amount_trans_balance`, `amount_deadline`, `freeze_action`, `freeze_trans`, `freeze_trans_balance`, `freeze_deadline`, `order_no`, `company_id`, `amount_trans_time`, `remark`, `is_available`, `is_deleted`, `version_no`, `create_userid`, `create_username`, `create_userip`, `create_usermac`, `create_time`, `create_time_db`, `server_ip`, `update_userid`, `update_username`, `update_userip`, `update_usermac`, `update_time`, `update_time_db`, `busi_type`, `ref_no`, `identity_id`, `outflow_id`, `platform_id`, `merchant_id`, `channel_type`) "
					+ "  VALUES  (?, ?, 4, ?, 2, 1, ?, ?, NULL, NULL, NULL, NULL, NULL, NULL, 11, now(), '下单送积分', 1, 0, 0, NULL, 'superadmin', '299.299.299.299', NULL, now(), now(), NULL, NULL, NULL, NULL, NULL, now(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)";
			jdbcTemplateOuser.update(insertSql, 
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setLong(1, pointsLog.getId());
							ps.setLong(2, pointsLog.getUserId());
							ps.setLong(3, pointsLog.getAid()); 
							ps.setBigDecimal(4, pointsLog.getFee());
							ps.setBigDecimal(5, pointsLog.getFeeFinal());
						}
				});
			//2更新积分总表；
			final Points points=new Points();
			points.setUserId(user_id);
			points.setBalance(pointBalanceBigFinal);
			String sqlUpdateAccountInfo=" update account_info t1 set t1.amount_balance=? where t1.user_id=? ";
			jdbcTemplateOuser.update(sqlUpdateAccountInfo, 
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setBigDecimal(1, points.getBalance());
							ps.setLong(2, points.getUserId());
						}
				});
			
		}
	}
	
	
	/**
	 *1-3 上家是平台_更新平台的——佣金总表和插入佣金
	 * 
	 */
	public void fenPlatAccout(Long user_id,String id,Long parentIdL,Long distributor_id,BigDecimal fee){
		//cap_user_account / cap_user_account_log
		List<Map<String,Object>> list = jdbcTemplateFinance.queryForList(" select balance_amount from cap_user_account where user_id=888888888 and account_type=10 ");
		BigDecimal  balanceAmountAfter=new BigDecimal("0");
		if(list.size()>0){
		   BigDecimal  balanceAmount = (BigDecimal) list.get(0).get("balance_amount");
		   balanceAmountAfter = balanceAmount.add(fee);
		}
		//插入平台明细；
		String insertLogSql=" INSERT INTO cap_user_account_log (`id`, `user_id`, `login_name`, `user_name`, `account_id`, `account_type`, `currency_name`, `currency_code`, `business_type`, `balance_type`, `operation_type`, `trans_amount`, `after_amount`, `trans_time`, `unfreeze_time`, `order_code`, `source_order_code`, `source_order_type`, `trade_batch_no`, `remark`, `payment_type`, `payment_voucher`, `bank_name`, `bank_account_no`, `bank_account_name`, `unique_flag`, `caller`, `merchant_id`, `merchant_code`, `merchant_name`, `company_id`, `version_no`, `create_userid`, `create_username`, `create_time`, `server_ip`, `update_userid`, `update_username`, `update_time`, `is_deleted`) "
				+ " VALUES ("+id+", 888888888, NULL, NULL, 1, 10, NULL, 'CNY', 310001, 1, 2, "+fee+", "+balanceAmountAfter+",NOW(), NULL, NULL, NULL, 1601, NULL, NULL, NULL, NULL, NULL, NULL, NULL, "+id+", NULL, 0, NULL, NULL, 187, 0, NULL, NULL, NOW(), '299.299.299.299', NULL, NULL, NOW(), 0) ";
		jdbcTemplateFinance.execute(insertLogSql);
		//更新平台总表；
		String updateSql=" UPDATE cap_user_account SET balance_amount="+balanceAmountAfter+" where user_id = 888888888 and account_type = 10 ";
		jdbcTemplateFinance.execute(updateSql);
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
