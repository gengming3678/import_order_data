package com.palline.system.service.imp;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.palline.system.entity.AccountInfo;
import com.palline.system.entity.AccountOprLog;
import com.palline.system.entity.Ouser;
import com.palline.system.entity.OuserUpDown;
import com.palline.system.entity.PlatCommission;
import com.palline.system.entity.PlatCommissionLog;
import com.palline.system.entity.Points;
import com.palline.system.entity.PointsLog;
import com.palline.system.service.ImportSoService;
@Service
public class ImportSoServiceImpl implements ImportSoService{

	@Resource(name="jdbcTemplateOuser")
	private JdbcTemplate  jdbcTemplateOuser;
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name="jdbcTemplateFinance")
	private JdbcTemplate jdbcTemplateFinance;
	/**
	 * 批量更新用户星级表
	 */
	public int  batchUpdateOuserLevelCode(final List<Ouser> list){
		String sql="update uc_user_membership_level t set t.membership_level_code=? where t.entity_id=?";
		int[] ii = jdbcTemplateOuser.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Ouser customer = list.get(i);
				ps.setString(1, customer.getLevelCode());
				ps.setLong(2, customer.getUserId());          
			}
					
			@Override            
			public int getBatchSize() {
				return list.size();
			}           
		  });
		return ii.length;
	}
	@Override
	public int batchUpdateOuserUpDownCode(List<OuserUpDown> list) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 *根据下家名称找到对应的_distributorId/给上家会员和平台发佣金
	 */
	@Override
	public List<Map<String,Long>> getCommissionUpDownDistributorIdByUserId(List<Map<String,Object>> list){
		List<Map<String,Long>> list2=new ArrayList<Map<String,Long>>();
		Map<String,Long> distributorIds=new HashMap<String, Long>();
		Map<String,Long> userIds=new HashMap<String, Long>();
		Map<String,Long> levelsByUpNames=new HashMap<String, Long>();
		//查询所有上家；
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=list.get(i);
			//获取tel这列数据,查询user_id
			String tel = (String) map.get("电话");
			Integer level = (Integer) map.get("商户星级");
			//Integer fee = (Integer) map.get("金额（元）");
			String idCardNum = (String) map.get("身份证号");
			//Integer num = (Integer) map.get("id");
			String name = (String) map.get("姓名");
			//String upName = (String) map.get("乔丽尔上家");
			//String type="1";
			Long user_id=null;
			Long distributor_id=null;
			//先根据手机号去系统查；
			String sqlDistributor = "select id,user_id from distributor where mobile = '"+tel+"' ORDER BY registration_time desc limit 1";
		    List<Map<String,Object>> distributorList = jdbcTemplate.queryForList(sqlDistributor);
		    if(distributorList.size()>0){//手机号查得到；
		    	//type="1";
		    	user_id = (Long) distributorList.get(0).get("user_id");
		    	distributor_id = (Long) distributorList.get(0).get("id");
		    	//p++;
		    }else{//尝试用姓名查找；
		    	String userIdentitySql = " select t.user_id from u_user_identity_card t where t.identity_card_name = '"+name+"'  ";
		    	List<Map<String,Object>> userIdentityList = jdbcTemplateOuser.queryForList(userIdentitySql);
		    	if(userIdentityList.size()==1){//2找到一个则系统处理
		    		user_id = (Long) userIdentityList.get(0).get("user_id");
		    		//type="2";
		    		//p++;
		    	}else if(userIdentityList.size()>1){//1找到2个以上同名，则再次根据身份证查找；
		    		String userIdCardSql = " select t.user_id from u_user_identity_card t where t.identity_card_number = CONCAT('@%^*',TO_BASE64(AES_ENCRYPT('"+idCardNum+"','1fi;qPa7utddahWy')))   ";
			    	List<Map<String,Object>> userIdCardList = jdbcTemplateOuser.queryForList(userIdCardSql);
			    	if(userIdCardList.size()==1){
			    		user_id = (Long) userIdCardList.get(0).get("user_id");
			    		//type="3";
			    	}else{
			    		//type="4";
			    		//jdbcTemplate.execute("update sheet3 set 邮箱='"+type+"' where 序号="+num+""); 
			    		continue;
			    	}
		    	}else{//3真的没找到；
		    		//type="5";
		    		//k++;
		    		//jdbcTemplate.execute("update sheet3 set 邮箱='"+type+"' where 序号="+num+""); 
		    		continue;
		    	}
		    }
		    
		    if(distributor_id!=null){
		    	distributorIds.put(name, distributor_id);
		    }else{//根据名字和身份证查到的
		    	List<Map<String,Object>>  curUserInfoList = jdbcTemplate.queryForList("select id,parent_id from distributor where user_id="+user_id+" ORDER BY registration_time desc limit 1");
			    if(curUserInfoList.size()>0){
			    	Long idL = (Long) curUserInfoList.get(0).get("id");
			    	distributorIds.put(name, idL);
			    }
		    }
		    userIds.put(name, user_id);
		    levelsByUpNames.put(name, Long.valueOf(level));
		}
		list2.add(distributorIds);
		list2.add(userIds);
		list2.add(levelsByUpNames);
		return list2;
	}
	
	
	/**
	 * 根据下家名称找到对应的_distributorId
	 */
	@Override
	public Map<String,Long> getDistributorIdByUserId(List<Map<String,Object>> list){
		Map<String,Long> distributorIds=new HashMap<String, Long>();
		//查询所有上家；
		//List<Map<String,Object>> list = jdbcTemplate.queryForList("select  distinct t.`乔丽尔上家` from sheet3  t");   
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=list.get(i);
			//获取tel这列数据,查询user_id
			String tel = (String) map.get("电话");
			Integer level = (Integer) map.get("星级");
			String fee = (String) map.get("缴款金额");
			String idCardNum = (String) map.get("身份证号");
			Integer num = (Integer) map.get("序号");
			
			String name = (String) map.get("姓名");
			String upName = (String) map.get("乔丽尔上家");
			String type="1";
			Long user_id=null;
			Long distributor_id=null;
			//先根据手机号去系统查；
			String sqlDistributor = "select id,user_id from distributor where mobile = '"+tel+"' ORDER BY registration_time desc limit 1";
		    List<Map<String,Object>> distributorList = jdbcTemplate.queryForList(sqlDistributor);
		    if(distributorList.size()>0){//手机号查得到；
		    	//type="1";
		    	user_id = (Long) distributorList.get(0).get("user_id");
		    	distributor_id = (Long) distributorList.get(0).get("id");
		    	//p++;
		    }else{//尝试用姓名查找；
		    	String userIdentitySql = " select t.user_id from u_user_identity_card t where t.identity_card_name = '"+name+"'  ";
		    	List<Map<String,Object>> userIdentityList = jdbcTemplateOuser.queryForList(userIdentitySql);
		    	if(userIdentityList.size()==1){//2找到一个则系统处理
		    		user_id = (Long) userIdentityList.get(0).get("user_id");
		    		//type="2";
		    		//p++;
		    	}else if(userIdentityList.size()>1){//1找到2个以上同名，则再次根据身份证查找；
		    		String userIdCardSql = " select t.user_id from u_user_identity_card t where t.identity_card_number = CONCAT('@%^*',TO_BASE64(AES_ENCRYPT('"+idCardNum+"','1fi;qPa7utddahWy')))   ";
			    	List<Map<String,Object>> userIdCardList = jdbcTemplateOuser.queryForList(userIdCardSql);
			    	if(userIdCardList.size()==1){
			    		user_id = (Long) userIdCardList.get(0).get("user_id");
			    		//type="3";
			    	}else{
			    		//type="4";
			    		//jdbcTemplate.execute("update sheet3 set 邮箱='"+type+"' where 序号="+num+""); 
			    		continue;
			    	}
		    		
		    	}else{//3真的没找到；
		    		//type="5";
		    		//k++;
		    		//jdbcTemplate.execute("update sheet3 set 邮箱='"+type+"' where 序号="+num+""); 
		    		continue;
		    	}
		    }
		    
		    if(distributor_id!=null){
		    	distributorIds.put(name, distributor_id);
		    }else{
		    	List<Map<String,Object>>  curUserInfoList = jdbcTemplate.queryForList("select id,parent_id from distributor where user_id="+user_id+" ORDER BY registration_time desc limit 1");
			    if(curUserInfoList.size()>0){
			    	Long idL = (Long) curUserInfoList.get(0).get("id");
			    	distributorIds.put(name, idL);
			    }
		    }
		}
		return distributorIds;
	}
	
	
	/**
	 * 批量插入佣金明细_会员
	 */
	@Override
	public int batchInsertAccountOprLog(final List<AccountOprLog> list) {
		String sqlaccountOprLog=" INSERT INTO account_opr_log(`id`, `account_id`, `entity_id`, `entity_type`, `entity_code`, `entity_name`, `payment_type`, `payment_voucher`, `bank_name`, `bank_account_no`, `bank_account_name`, `outer_trans_type`, `inner_trans_type`, `balance_dc_direct`, `balance_trans_amount`, `balance_after_amount`, `freeze_dc_direct`, `freeze_trans_amount`, `freeze_after_amount`, `trans_time`, `unfreeze_time`, `order_no`, `ref_no`, `batch_no`, `remark`, `unique_flag`, `company_id`, `is_deleted`, `trans_src_userid`, `merchant_id`, `root_merchant_id`, `create_userid`, `create_username`, `create_time`, `server_ip`, `version_no`) "
				+ " VALUES (?, ?, ?, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ?, 1, 2, ?, ?, NULL, NULL, NULL, NULL, now(), '', '', NULL, NULL, NULL, 187, NULL, NULL, NULL, NULL, NULL, NULL, now(), '299.299.299.299', 20198888);";
		int[] ii = jdbcTemplateFinance.batchUpdate(sqlaccountOprLog, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				AccountOprLog accountOprLog = list.get(i);
				ps.setBigDecimal(1, accountOprLog.getId());
				ps.setBigDecimal(2, accountOprLog.getInfoId());
				ps.setLong(3, accountOprLog.getDistributorId()); 
				ps.setInt(4, accountOprLog.getType());
				ps.setBigDecimal(5, accountOprLog.getBalanceL()); 
				ps.setBigDecimal(6, accountOprLog.getAfterAmount()); 
			}
					
			@Override            
			public int getBatchSize() {
				return list.size();
			}           
		  });
		return ii.length;
	}
	
	/**
	 * 批量插入佣金总表_会员
	 */
	@Override
	public int batchUpdateAccountInfo(final List<AccountInfo> list) {
		String sqlAccountInfo=" update  account_info set  balance=? where id=? ";
		int[] ii = jdbcTemplateFinance.batchUpdate(sqlAccountInfo, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				AccountInfo accountInfo = list.get(i);
				ps.setBigDecimal(1, accountInfo.getBalance());
				ps.setLong(2, accountInfo.getId());          
			}
					
			@Override            
			public int getBatchSize() {
				return list.size();
			}           
		  });
		return ii.length;
	}
	
	/**
	 * 根据上家名字得到星级
	 */
	@Override
	public Map<String, Integer> getLevelByUpName(List<Map<String, Object>> list) {
		Map<String,Integer> levelsByUpNames=new HashMap<String, Integer>();
		//查询所有上家；
		//List<Map<String,Object>> list = jdbcTemplate.queryForList("select  distinct t.`乔丽尔上家` from sheet3  t");   
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=list.get(i);
			//获取tel这列数据,查询user_id
			//String tel = (String) map.get("电话");
			Integer level = (Integer) map.get("商户星级");
			//Integer fee = (Integer) map.get("金额（元）");
			//String idCardNum = (String) map.get("身份证号");
			//Integer num = (Integer) map.get("序号");
			String name = (String) map.get("姓名");
			//String upName = (String) map.get("乔丽尔上家");
			//String type="1";
			//Long user_id=null;
			levelsByUpNames.put(name, level);
			//先根据手机号去系统查；
			/*String sqlDistributor = "select id,user_id from distributor where mobile = '"+tel+"' ORDER BY registration_time desc limit 1";
		    List<Map<String,Object>> distributorList = jdbcTemplate.queryForList(sqlDistributor);
		    if(distributorList.size()>0){//手机号查得到；
		    	//type="1";
		    	user_id = (Long) distributorList.get(0).get("user_id");
		    	//p++;
		    }else{//尝试用姓名查找；
		    	String userIdentitySql = " select t.user_id from u_user_identity_card t where t.identity_card_name = '"+name+"'  ";
		    	List<Map<String,Object>> userIdentityList = jdbcTemplateOuser.queryForList(userIdentitySql);
		    	if(userIdentityList.size()==1){//2找到一个则系统处理
		    		user_id = (Long) userIdentityList.get(0).get("user_id");
		    		//type="2";
		    		//p++;
		    	}else if(userIdentityList.size()>1){//1找到2个以上同名，则再次根据身份证查找；
		    		String userIdCardSql = " select t.user_id from u_user_identity_card t where t.identity_card_number = CONCAT('@%^*',TO_BASE64(AES_ENCRYPT('"+idCardNum+"','1fi;qPa7utddahWy')))   ";
			    	List<Map<String,Object>> userIdCardList = jdbcTemplateOuser.queryForList(userIdCardSql);
			    	if(userIdCardList.size()==1){
			    		user_id = (Long) userIdCardList.get(0).get("user_id");
			    		//type="3";
			    	}else{
			    		//type="4";
			    		//jdbcTemplate.execute("update sheet3 set 邮箱='"+type+"' where 序号="+num+""); 
			    		continue;
			    	}
		    		
		    	}else{//3真的没找到；
		    		//type="5";
		    		//k++;
		    		//jdbcTemplate.execute("update sheet3 set 邮箱='"+type+"' where 序号="+num+""); 
		    		continue;
		    	}
		    }*/
		    
		   /* List<Map<String,Object>>  curUserInfoList = jdbcTemplate.queryForList("select id,parent_id from distributor where user_id="+user_id+" ORDER BY registration_time desc limit 1");
		    if(curUserInfoList.size()>0){
		    	Long idL = (Long) curUserInfoList.get(0).get("id");
		    	//String userIdCardSql = " select t.user_id from u_user_identity_card t where t.user_id = "+user_id+" ";
		    	//List<Map<String,Object>> userIdCardList = jdbcTemplateOuser.queryForList(userIdCardSql);
		    	distributorIds.put(name, idL);
		    }*/
		    
		}
		return levelsByUpNames;
	}
	
	/**
	 * 插入积分流水_会员
	 */
	@Override
	public int batchInsertPointsLog(final List<PointsLog> list) {
		String insertSql=" INSERT INTO account_flow (`id`, `user_id`, `action_type`, `account_id`, `trans_type`, `amount_action`, `amount_trans`, `amount_trans_balance`, `amount_deadline`, `freeze_action`, `freeze_trans`, `freeze_trans_balance`, `freeze_deadline`, `order_no`, `company_id`, `amount_trans_time`, `remark`, `is_available`, `is_deleted`, `version_no`, `create_userid`, `create_username`, `create_userip`, `create_usermac`, `create_time`, `create_time_db`, `server_ip`, `update_userid`, `update_username`, `update_userip`, `update_usermac`, `update_time`, `update_time_db`, `busi_type`, `ref_no`, `identity_id`, `outflow_id`, `platform_id`, `merchant_id`, `channel_type`) VALUES  (?, ?, 4, ?, 2, 1, ?, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 11, '2018-11-20 16:21:13', '下单送积分', 1, 0, 0, NULL, 'superadmin', NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', NULL, NULL, NULL, NULL, NULL, '2018-11-20 16:21:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)";
    	//jdbcTemplateOuser.execute(insertSql);
		int[] ii = jdbcTemplateOuser.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PointsLog pointsLog = list.get(i);
				ps.setLong(1, pointsLog.getId());
				ps.setLong(2, pointsLog.getUserId());
				ps.setLong(3, pointsLog.getAid()); 
				ps.setBigDecimal(4, pointsLog.getFee());
			}
					
			@Override            
			public int getBatchSize() {
				return list.size();
			}           
		  });
		return ii.length;
	}
	
	/**
	 * 更新积分总表_会员
	 */
	@Override
	public int batchUpdatePoints(final List<Points> list) {
		String sqlUpdateAccountInfo=" update account_info t1 set t1.amount_balance=amount_balance+? where t1.user_id=? ";
    	//jdbcTemplateOuser.execute(sqlUpdateAccountInfo);
		int[] ii = jdbcTemplateOuser.batchUpdate(sqlUpdateAccountInfo, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Points points = list.get(i);
				ps.setLong(1, points.getUserId());
				ps.setBigDecimal(2, points.getBalance());
			}
					
			@Override            
			public int getBatchSize() {
				return list.size();
			}           
		  });
		return ii.length;
	}
	
	
	/**
	 * 批量插入佣金明细_平台
	 */
	@Override
	public int batchPlatInsertAccountOprLog(final List<PlatCommissionLog> list) {
		String sqlUpdateAccountInfo=" INSERT INTO cap_user_account_log (`id`, `user_id`, `login_name`, `user_name`, `account_id`, `account_type`, `currency_name`, `currency_code`, `business_type`, `balance_type`, `operation_type`, `trans_amount`, `after_amount`, `trans_time`, `unfreeze_time`, `order_code`, `source_order_code`, `source_order_type`, `trade_batch_no`, `remark`, `payment_type`, `payment_voucher`, `bank_name`, `bank_account_no`, `bank_account_name`, `unique_flag`, `caller`, `merchant_id`, `merchant_code`, `merchant_name`, `company_id`, `version_no`, `create_userid`, `create_username`, `create_time`, `server_ip`, `update_userid`, `update_username`, `update_time`, `is_deleted`) "
				+ " VALUES (?, 888888888, NULL, NULL, 1, 10, NULL, 'CNY', 310001, 1, 2, ?, ?,NOW(), NULL, NULL, NULL, 1601, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 187, 0, NULL, NULL, '2018-11-20 16:21:13', '255.255.255.255', NULL, NULL, NOW(), 0) ";
    	//jdbcTemplateOuser.execute(sqlUpdateAccountInfo);
		int[] ii = jdbcTemplateFinance.batchUpdate(sqlUpdateAccountInfo, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PlatCommissionLog platCommissionLog = list.get(i);
				ps.setLong(1, Long.valueOf(platCommissionLog.getId()));
				ps.setBigDecimal(2, platCommissionLog.getFee());
				ps.setBigDecimal(3, platCommissionLog.getBalanceAmountAfter());
			}
					
			@Override            
			public int getBatchSize() {
				return list.size();
			}           
		  });
		return ii.length;
	}
	
	/**
	 * 批量插入佣金总表_平台
	 */
	@Override
	public int batchPlatUpdateAccountInfo(final List<PlatCommission> list) {
		String sqlUpdateAccountInfo=" UPDATE cap_user_account SET balance_amount=balance_amount+? where user_id = 888888888 and account_type = 10 ";
    	//jdbcTemplateOuser.execute(sqlUpdateAccountInfo);
		int[] ii = jdbcTemplateFinance.batchUpdate(sqlUpdateAccountInfo, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PlatCommission platCommission = list.get(i);
				ps.setBigDecimal(1, platCommission.getBalanceAmountAfter());
			}
					
			@Override            
			public int getBatchSize() {
				return list.size();
			}           
		  });
		return ii.length;
	}	
}
