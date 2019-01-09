package com.palline.system.service;

import java.util.List;
import java.util.Map;

import com.palline.system.entity.AccountInfo;
import com.palline.system.entity.AccountOprLog;
import com.palline.system.entity.Ouser;
import com.palline.system.entity.OuserUpDown;
import com.palline.system.entity.PlatCommission;
import com.palline.system.entity.PlatCommissionLog;
import com.palline.system.entity.Points;
import com.palline.system.entity.PointsLog;

public interface ImportSoService {

	public  int batchUpdateOuserLevelCode(final List<Ouser> list);
	
	public  int batchUpdateOuserUpDownCode(final List<OuserUpDown> list);
	
	public Map<String,Long> getDistributorIdByUserId(List<Map<String,Object>> list);
	
	public List<Map<String,Long>> getCommissionUpDownDistributorIdByUserId(List<Map<String,Object>> list);
	
	public Map<String,Integer> getLevelByUpName(List<Map<String,Object>> list);
	
	
	/**
	 * 批量插入佣金流水_会员
	 * @param list
	 * @return
	 */
	public  int batchInsertAccountOprLog(final List<AccountOprLog> list);
	
	/**
	 * 批量更新佣金总表_会员
	 * @param list
	 * @return
	 */
	public  int batchUpdateAccountInfo(final List<AccountInfo> list);
	
	
	
	/**
	 * 批量插入积分流水_会员
	 * @param list
	 * @return
	 */
	public  int batchInsertPointsLog(final List<PointsLog> list);
	
	/**
	 * 批量更新积分总表_会员
	 * @param list
	 * @return
	 */
	public  int batchUpdatePoints(final List<Points> list);
	
	
	
	/**
	 * 批量插入佣金流水_平台
	 * @param list
	 * @return
	 */
	public  int batchPlatInsertAccountOprLog(final List<PlatCommissionLog> list);
	
	/**
	 * 批量更新佣金总表_平台
	 * @param list
	 * @return
	 */
	public  int batchPlatUpdateAccountInfo(final List<PlatCommission> list);
	
	
	
	
}
