package com.palline.order.controller;

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
import com.palline.system.entity.Constants;
import com.palline.system.service.imp.ImportSoServiceImpl;

/**
 * 核查所有会员订单和星级
 * @author gmm
 * @version 1.0
 */
@Controller
@RequestMapping("/ImportSoAll")
public class ImportSoAllController {
	private static Logger logger=Logger.getLogger(ImportSoAllController.class); 
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name="jdbcTemplateOrder")
	private JdbcTemplate jdbcTemplateOrder;
	@Resource(name="jdbcTemplateOuser")
	private JdbcTemplate jdbcTemplateOuser;
	@Resource
	private ImportSoServiceImpl importSoServiceImpl;
	public Long count=188721190107l;
	public int lun=1;

	
	/**
	 * 核查所有会员订单和星级
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/raskCheckImportSoAllData.do")
	public String raskCheckImportSoAllData(HttpServletRequest request,Model model) {
		  Runnable runnable = new Runnable() {  
	            public void run() {  
	            	 java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                 String startDate = format1.format(new Date());
	                 logger.info("第"+lun+"轮核查所有会员订单和星级");
	                 logger.info("开始时间："+startDate);
	                 //importOutData();
	                 String endDate = format1.format(new Date());
	                 logger.info("结束时间："+endDate);
	            }  
	        };  
	        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();  
	        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
	        service.scheduleAtFixedRate(runnable, 10, 30, TimeUnit.SECONDS);  
	        return "so/soList";
	}
	
	/**
	 * 核查所有会员订单和星级
	 * @param request
	 * @param model
	 * @return
	 */
	public void importOutData() {
		try {
			logger.info("核查所有会员订单和星级开始>>>"+StringUtil.dateToString2(new Date()));
			List<String> ids=new ArrayList<String>();
			String suffix=String.valueOf(count);//12位，1201，日期编码每次导入依次递增
			List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from sheet_import_so_level where 是否核查=3  limit 200");         
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
			int p=0;
			int k=0;
			int o=0;
			int l=0;
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=list.get(i);
				String id = ids.get(i);
				//获取tel这列数据,查询user_id
				String name = (String) map.get("姓名");
				String tel = (String) map.get("电话");
				String level = (String) map.get("星级");
				String fee = (String) map.get("缴款金额");
				String idCardNum = (String) map.get("身份证号");
				Integer num = (Integer) map.get("id");
				Integer seed = (Integer) map.get("是否种子");
				
				Integer type=2;
				Long user_id=null;
				//先根据手机号去系统查；
				String sqlDistributor = "select user_id from distributor where mobile = '"+tel+"' ORDER BY registration_time desc limit 1";
			    List<Map<String,Object>> distributorList = jdbcTemplate.queryForList(sqlDistributor);
			    if(distributorList.size()>0){//手机号查得到；
			    	type=1;
			    	user_id = (Long) distributorList.get(0).get("user_id");
			    	p++;
			    }else{
			    	//2则再次根据身份证查找；
			    	String userIdCardSql = " select t.user_id from u_user_identity_card t where t.identity_card_number = CONCAT('@%^*',TO_BASE64(AES_ENCRYPT('"+idCardNum+"','1fi;qPa7utddahWy')))   ";
			    	List<Map<String,Object>> userIdCardList = jdbcTemplateOuser.queryForList(userIdCardSql);
			    	if(userIdCardList.size()>0){
			    		user_id = (Long) userIdCardList.get(0).get("user_id");
			    		type=1;
			    		p++;
			    	}else{
			    		type=2;
			    		k++;
			    		jdbcTemplate.execute("update sheet_import_so_level set 是否核查=1 where id="+num+""); 
			    		continue;
			    	}
			    }
			    Long levelProId = Constants.MapLevelId.mapLevel.get(level);
				String levelProName = Constants.MapLevelName.mapLevelCh.get(level);
				String levelCode = Constants.MapLevelCode.mapLevelCode.get(level);
				
				//第一步：核查订单并修复
				String soCheckSql = "select t.order_code from so t where t.create_userid=1870081300000047 and t.user_id="+user_id+" and t.order_amount="+fee+" and t.product_amount="+fee+" ";
				List<Map<String,Object>> checkList = jdbcTemplateOrder.queryForList(soCheckSql);
				if(checkList.size()>0){//找到订单了，则什么都不用做	
				}else{//没找到订单/找到的金额不对，则为其插入/更新订单
					o++;
					//判断是不是升级或退款过；
					String sqlSo2="";
					String sqlSoItem2="";
					String soCheckSql2 = "select t.order_code from so t where t.create_userid=1870081300000047 and t.user_id="+user_id+"  ";
					List<Map<String,Object>> checkList2 = jdbcTemplateOrder.queryForList(soCheckSql2);
					if(checkList2.size()>0){//更新订单
						String orderCode = (String) checkList2.get(0).get("order_code");
						if(level.equals("0")){//0代表降为普通用户，删除订单，
							 sqlSo2 = " delete  FROM  `order`.`so`   where order_code='"+orderCode+"' ";
							 sqlSoItem2 = " delete  FROM    `order`.`so_item` where order_code='"+orderCode+"'  ";
						}else{
							 sqlSo2 = " UPDATE  `order`.`so`  SET order_amount="+fee+", product_amount="+fee+" where order_code='"+orderCode+"' ";
							 sqlSoItem2 = " UPDATE  `order`.`so_item` SET product_id="+levelProId+",product_price_final="+fee+",product_cname='"+levelProName+"' where order_code='"+orderCode+"' ";
						}
					}else{//插入订单
						if(seed==1){//是种子会员
							//内部种子用户专用收货人手机号——17521282079 /create_time 2018-11-20 16:21:13；
							String sqlSo = " ("+id+", '"+id+"', '0', 1, "+user_id+", 1590058601000003, NULL, -1, 1872018091301, "+fee+", "+fee+", 0.00, 35, 1, 0, NULL, '2018-11-20 16:21:13', NULL, 0.00, 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '2018-11-20 16:21:13', 0, NULL, 1, NULL, 0, '10001', '10', '2018-11-20 16:21:13', '2018-11-20 16:21:13', NULL, NULL, NULL, NULL, NULL, 0, 5, 0, 0, 0, NULL, NULL, 1086059000000411, '新闸路831号', '200135', 'tom', NULL, NULL, '17521282079', NULL, NULL, NULL, 10, '上海', 110, '上海市', NULL, NULL, 1140, '静安区', '', '2018-11-20 16:21:13', '2018-11-20 16:21:13', 0, NULL, 0.00, NULL, 0, NULL, 0, 1, 0, 0, 1870081300000047, '17521282079', NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', '127.0.0.1', NULL, '系统', NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', NULL, 187, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, 'ody', NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', 0, '', 0.00, 0.00, 0.00, NULL, 0, NULL, 1046014700000833, NULL, NULL, NULL, 35, 0, 0, '上海三少猩球贸易有限公司', NULL, '2018-11-20 16:21:13', 0, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL, NULL, NULL, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, NULL, 0.00, NULL, 1590058601000008, '三少猩球贸易有限公司', NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
							String sqlSoItem = " ("+id+", NULL, 1, '"+id+"', "+user_id+", 1590058601000003, "+levelProId+", 1900084000000240, 1872018091301, 0.00, NULL, NULL, "+fee+", 1, '"+levelProName+"', NULL, 'https://cdn.oudianyun.com/dev/back-product-web/1542541241488_59.52936780076672_5ed25f84-0545-4f29-a211-b0d6c2ca0c25.png', NULL, 1, 0.00, 0.00, 0.00, NULL, 0, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, 1, NULL, 0.00, NULL, 0.00, NULL, NULL, 0.000, NULL, NULL, NULL, 0, 1, 0, 0, 1870081300000047, NULL, NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', '127.0.0.1', NULL, NULL, NULL, NULL, '2018-11-20 16:21:13', '2018-11-20 16:21:13', NULL, 187, '181120537951367047', 0.00, NULL, NULL, NULL, NULL, NULL, '1900084100000002', NULL, '份', NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, 0.00, 0, NULL, NULL, NULL, NULL, NULL, 1900082400000089, '来伊份', NULL, 1900083602000056, '增值服务费', '1900080800000001-1900083602000056', 'B2C后台类目->增值服务费', NULL, NULL, NULL, 1, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, 90, 30, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);";

							sqlSo2 = " INSERT INTO `order`.`so` (`id`, `order_code`, `parent_order_code`, `is_leaf`, `user_id`, `merchant_id`, `parent_merchant_name`, `parent_merchant_id`, `warehouse_id`, `order_amount`, `product_amount`, `tax_amount`, `order_status`, `order_payment_type`, `order_payment_status`, `order_payment_confirm_amount`, `order_payment_confirm_date`, `order_payment_confirm_type`, `order_delivery_fee`, `order_delivery_fee_insurance_type`, `order_delivery_fee_insurance_amount`, `order_paid_by_account`, `order_paid_by_coupon`, `order_paid_by_card`, `order_paid_by_rebate`, `order_paid_by_referral_code`, `order_used_points`, `order_promotion_discount`, `points_used_money`, `order_give_points`, `order_delivery_fee_accounting`, `order_cancel_reason_id`, `order_cancel_date`, `order_need_cs`, `order_cs_cancel_reason`, `order_cance_operate_type`, `order_cance_operate_id`, `order_data_exchange_flag`, `order_delivery_service_type`, `order_delivery_method_id`, `expect_receive_date_start`, `expect_receive_date_end`, `expect_receive_time_start`, `expect_receive_time_end`, `order_remark_user`, `order_remark_merchant2user`, `order_remark_merchant`, `order_source`, `order_channel`, `order_business_type`, `order_promotion_type`, `order_promotion_status`, `order_spread_type`, `order_message_phone`, `good_receiver_id`, `good_receiver_address`, `good_receiver_postcode`, `good_receiver_name`, `good_receiver_last_name`, `good_receiver_first_name`, `good_receiver_mobile`, `good_receiver_phone`, `good_receiver_country_id`, `good_receiver_country`, `good_receiver_province_id`, `good_receiver_province`, `good_receiver_city_id`, `good_receiver_city`, `good_receiver_county_id`, `good_receiver_county`, `good_receiver_area_id`, `good_receiver_area`, `identity_card_number`, `order_logistics_time`, `order_receive_date`, `order_invoice_type`, `virtual_stock_status`, `order_weight`, `is_fragile_or_liquid`, `order_delete_status`, `paid_online_threshold`, `order_return_flag`, `is_available`, `is_deleted`, `version_no`, `create_userid`, `create_username`, `create_userip`, `create_usermac`, `create_time`, `create_time_db`, `server_ip`, `update_userid`, `update_username`, `update_userip`, `update_usermac`, `update_time`, `update_time_db`, `client_versionno`, `company_id`, `order_before_amount`, `order_before_delivery_fee`, `deliver_id`, `deliver_code`, `order_cs_audit_reason`, `order_audit_reason_id`, `order_audit_operate_id`, `order_audit_operate_type`, `order_change_status`, `change_order_code`, `sex`, `exact_address`, `expect_receive_type`, `merchant_full_id_path`, `flow_type`, `sys_source`, `out_order_code`, `order_audit_date`, `order_change_date`, `order_payment_flag`, `order_payment_two_type`, `excise_tax_amount`, `increment_tax_amount`, `customs_duties_amount`, `mode_id`, `sync_flag`, `source_code`, `distributor_id`, `distributor_name`, `distributor_shop_id`, `distributor_shop_name`, `delivery_status`, `comment_status`, `aftersales_flag`, `merchant_name`, `order_remark_company`, `order_complete_date`, `order_free_flag`, `promotion_ids`, `estimated_time_of_arrival`, `guid`, `order_vip_discount`, `order_type`, `order_payment_source`, `history_flag`, `order_activity_id`, `order_activity_type`, `cps_uid`, `manual_type`, `order_paid_by_ucard`, `merchant_amount`, `merchant_currency`, `merchant_currency_rate`, `order_currency`, `delivery_company_id`, `good_receiver_country_code`, `good_receiver_province_code`, `good_receiver_city_code`, `good_receiver_area_code`, `order_used_yidou`, `yidou_used_money`, `supplier_id`, `rebate_points_amount`, `rebate_brokerage_amount`, `good_other_contact_phone`, `store_id`, `store_name`, `do_flag`, `channel_code`, `brokerage_status`, `picking_user_id`, `latitude`, `longitude`, `order_total_original_amount`) "+
									 " VALUES "+ sqlSo;
							sqlSoItem2 = "  INSERT INTO `order`.`so_item`(`id`, `parent_so_item_id`, `is_item_leaf`, `order_code`, `user_id`, `merchant_id`, `product_id`, `mp_id`, `warehouse_id`, `product_item_amount`, `product_item_currency`, `product_item_currency_rate`, `product_price_final`, `product_item_num`, `product_cname`, `product_ename`, `product_pic_path`, `product_version_no`, `product_sale_type`, `product_price_original`, `product_price_market`, `product_price_sale`, `product_tax_amount`, `buy_type`, `product_type`, `pm_give_points`, `pm_used_points`, `pm_used_money`, `pm_paid_by_account`, `pm_need_points`, `pm_need_points_total`, `frozen_virtal_stock_num`, `frozen_real_stock_num`, `virtual_stock_status`, `amount_share_coupon`, `amount_share_promotion`, `amount_share_delivery_fee`, `amount_share_delivery_fee_accounting`, `amount_share_referral_code`, `product_gross_weight`, `binding_so_item_id`, `price_rule_id`, `comment_id`, `main_activity_id`, `is_available`, `is_deleted`, `version_no`, `create_userid`, `create_username`, `create_userip`, `create_usermac`, `create_time`, `create_time_db`, `server_ip`, `update_userid`, `update_username`, `update_userip`, `update_usermac`, `update_time`, `update_time_db`, `client_versionno`, `company_id`, `parent_order_code`, `product_item_before_amount`, `vehicle_warranty`, `delivery_time`, `delivery_method`, `violation_responsibility`, `source_id`, `code`, `third_merchant_product_code`, `unit`, `place_of_origin`, `excise_tax_amount`, `increment_tax_amount`, `customs_duties_amount`, `ext_info`, `referrer_user_id`, `standard`, `material`, `product_price_before_final`, `comment_status`, `product_price_settle`, `product_price_out`, `product_price_purchasing`, `promotion_product_price_settle`, `series_product_code`, `brand_id`, `brand_name`, `pm_paid_by_card`, `category_id`, `category_name`, `whole_category_id`, `whole_category_name`, `series_parent_id`, `relation_mp_id`, `amount_share_vip`, `type`, `product_del_num`, `pm_paid_by_ucard`, `bar_code`, `merchant_amount`, `merchant_currency`, `merchant_currency_rate`, `order_currency`, `delivery_company_id`, `out_order_item_id`, `pm_used_yidou`, `pm_yidou_used_money`, `guarantee_days`, `return_days`, `replacement_days`, `return_days_source`, `replacement_days_source`, `mp_model`, `status`, `price_status`, `proxy_sale_price`, `share_price`, `membership_price`, `purchasing_price`, `settle_price`, `market_price`, `recommend_retail_price`, `merchant_product_price`)   "+
								      	 "  VALUES "+ sqlSoItem;
						}else{//其它会员
							//外部用户专用收货人手机号-18717718990；/create_time 2018-11-26 16:21:13；
							String sqlSo = " ("+id+", '"+id+"', '0', 1, "+user_id+", 1590058601000003, NULL, -1, 1872018091301, "+fee+", "+fee+", 0.00, 35, 1, 0, NULL, '2018-11-26 16:21:13', NULL, 0.00, 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '2018-11-26 16:21:13', 0, NULL, 1, NULL, 0, '10001', '10', '2018-11-26 16:21:13', '2018-11-26 16:21:13', NULL, NULL, NULL, NULL, NULL, 0, 5, 0, 0, 0, NULL, NULL, 1086059000000411, '新闸路831号', '200135', 'tom', NULL, NULL, '18717718990', NULL, NULL, NULL, 10, '上海', 110, '上海市', NULL, NULL, 1140, '静安区', '', '2018-11-26 16:21:13', '2018-11-26 16:21:13', 0, NULL, 0.00, NULL, 0, NULL, 0, 1, 0, 0, 1870081300000047, '18717718990', NULL, NULL, '2018-11-26 16:21:13', '2018-11-26 16:21:13', '127.0.0.1', NULL, '系统', NULL, NULL, '2018-11-26 16:21:13', '2018-11-26 16:21:13', NULL, 187, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, 'ody', NULL, '2018-11-26 16:21:13', '2018-11-26 16:21:13', 0, '', 0.00, 0.00, 0.00, NULL, 0, NULL, 1046014700000833, NULL, NULL, NULL, 35, 0, 0, '上海三少猩球贸易有限公司', NULL, '2018-11-26 16:21:13', 0, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL, NULL, NULL, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, NULL, 0.00, NULL, 1590058601000008, '三少猩球贸易有限公司', NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
							String sqlSoItem = "("+id+", NULL, 1, '"+id+"', "+user_id+", 1590058601000003, "+levelProId+", 1900084000000240, 1872018091301, 0.00, NULL, NULL, "+fee+", 1, '"+levelProName+"', NULL, 'https://cdn.oudianyun.com/dev/back-product-web/1542541241488_59.52936780076672_5ed25f84-0545-4f29-a211-b0d6c2ca0c25.png', NULL, 1, 0.00, 0.00, 0.00, NULL, 0, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, 1, NULL, 0.00, NULL, 0.00, NULL, NULL, 0.000, NULL, NULL, NULL, 0, 1, 0, 0, 1870081300000047, NULL, NULL, NULL, '2018-11-26 16:21:13', '2018-11-26 16:21:13', '127.0.0.1', NULL, NULL, NULL, NULL, '2018-11-26 16:21:13', '2018-11-26 16:21:13', NULL, 187, '181120537951367047', 0.00, NULL, NULL, NULL, NULL, NULL, '1900084100000002', NULL, '份', NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, 0.00, 0, NULL, NULL, NULL, NULL, NULL, 1900082400000089, '来伊份', NULL, 1900083602000056, '增值服务费', '1900080800000001-1900083602000056', 'B2C后台类目->增值服务费', NULL, NULL, NULL, 1, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, 90, 30, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);";

							sqlSo2 = " INSERT INTO `order`.`so` (`id`, `order_code`, `parent_order_code`, `is_leaf`, `user_id`, `merchant_id`, `parent_merchant_name`, `parent_merchant_id`, `warehouse_id`, `order_amount`, `product_amount`, `tax_amount`, `order_status`, `order_payment_type`, `order_payment_status`, `order_payment_confirm_amount`, `order_payment_confirm_date`, `order_payment_confirm_type`, `order_delivery_fee`, `order_delivery_fee_insurance_type`, `order_delivery_fee_insurance_amount`, `order_paid_by_account`, `order_paid_by_coupon`, `order_paid_by_card`, `order_paid_by_rebate`, `order_paid_by_referral_code`, `order_used_points`, `order_promotion_discount`, `points_used_money`, `order_give_points`, `order_delivery_fee_accounting`, `order_cancel_reason_id`, `order_cancel_date`, `order_need_cs`, `order_cs_cancel_reason`, `order_cance_operate_type`, `order_cance_operate_id`, `order_data_exchange_flag`, `order_delivery_service_type`, `order_delivery_method_id`, `expect_receive_date_start`, `expect_receive_date_end`, `expect_receive_time_start`, `expect_receive_time_end`, `order_remark_user`, `order_remark_merchant2user`, `order_remark_merchant`, `order_source`, `order_channel`, `order_business_type`, `order_promotion_type`, `order_promotion_status`, `order_spread_type`, `order_message_phone`, `good_receiver_id`, `good_receiver_address`, `good_receiver_postcode`, `good_receiver_name`, `good_receiver_last_name`, `good_receiver_first_name`, `good_receiver_mobile`, `good_receiver_phone`, `good_receiver_country_id`, `good_receiver_country`, `good_receiver_province_id`, `good_receiver_province`, `good_receiver_city_id`, `good_receiver_city`, `good_receiver_county_id`, `good_receiver_county`, `good_receiver_area_id`, `good_receiver_area`, `identity_card_number`, `order_logistics_time`, `order_receive_date`, `order_invoice_type`, `virtual_stock_status`, `order_weight`, `is_fragile_or_liquid`, `order_delete_status`, `paid_online_threshold`, `order_return_flag`, `is_available`, `is_deleted`, `version_no`, `create_userid`, `create_username`, `create_userip`, `create_usermac`, `create_time`, `create_time_db`, `server_ip`, `update_userid`, `update_username`, `update_userip`, `update_usermac`, `update_time`, `update_time_db`, `client_versionno`, `company_id`, `order_before_amount`, `order_before_delivery_fee`, `deliver_id`, `deliver_code`, `order_cs_audit_reason`, `order_audit_reason_id`, `order_audit_operate_id`, `order_audit_operate_type`, `order_change_status`, `change_order_code`, `sex`, `exact_address`, `expect_receive_type`, `merchant_full_id_path`, `flow_type`, `sys_source`, `out_order_code`, `order_audit_date`, `order_change_date`, `order_payment_flag`, `order_payment_two_type`, `excise_tax_amount`, `increment_tax_amount`, `customs_duties_amount`, `mode_id`, `sync_flag`, `source_code`, `distributor_id`, `distributor_name`, `distributor_shop_id`, `distributor_shop_name`, `delivery_status`, `comment_status`, `aftersales_flag`, `merchant_name`, `order_remark_company`, `order_complete_date`, `order_free_flag`, `promotion_ids`, `estimated_time_of_arrival`, `guid`, `order_vip_discount`, `order_type`, `order_payment_source`, `history_flag`, `order_activity_id`, `order_activity_type`, `cps_uid`, `manual_type`, `order_paid_by_ucard`, `merchant_amount`, `merchant_currency`, `merchant_currency_rate`, `order_currency`, `delivery_company_id`, `good_receiver_country_code`, `good_receiver_province_code`, `good_receiver_city_code`, `good_receiver_area_code`, `order_used_yidou`, `yidou_used_money`, `supplier_id`, `rebate_points_amount`, `rebate_brokerage_amount`, `good_other_contact_phone`, `store_id`, `store_name`, `do_flag`, `channel_code`, `brokerage_status`, `picking_user_id`, `latitude`, `longitude`, `order_total_original_amount`)  "+
									 " VALUES "+sqlSo;
							sqlSoItem2 = " INSERT INTO `order`.`so_item`(`id`, `parent_so_item_id`, `is_item_leaf`, `order_code`, `user_id`, `merchant_id`, `product_id`, `mp_id`, `warehouse_id`, `product_item_amount`, `product_item_currency`, `product_item_currency_rate`, `product_price_final`, `product_item_num`, `product_cname`, `product_ename`, `product_pic_path`, `product_version_no`, `product_sale_type`, `product_price_original`, `product_price_market`, `product_price_sale`, `product_tax_amount`, `buy_type`, `product_type`, `pm_give_points`, `pm_used_points`, `pm_used_money`, `pm_paid_by_account`, `pm_need_points`, `pm_need_points_total`, `frozen_virtal_stock_num`, `frozen_real_stock_num`, `virtual_stock_status`, `amount_share_coupon`, `amount_share_promotion`, `amount_share_delivery_fee`, `amount_share_delivery_fee_accounting`, `amount_share_referral_code`, `product_gross_weight`, `binding_so_item_id`, `price_rule_id`, `comment_id`, `main_activity_id`, `is_available`, `is_deleted`, `version_no`, `create_userid`, `create_username`, `create_userip`, `create_usermac`, `create_time`, `create_time_db`, `server_ip`, `update_userid`, `update_username`, `update_userip`, `update_usermac`, `update_time`, `update_time_db`, `client_versionno`, `company_id`, `parent_order_code`, `product_item_before_amount`, `vehicle_warranty`, `delivery_time`, `delivery_method`, `violation_responsibility`, `source_id`, `code`, `third_merchant_product_code`, `unit`, `place_of_origin`, `excise_tax_amount`, `increment_tax_amount`, `customs_duties_amount`, `ext_info`, `referrer_user_id`, `standard`, `material`, `product_price_before_final`, `comment_status`, `product_price_settle`, `product_price_out`, `product_price_purchasing`, `promotion_product_price_settle`, `series_product_code`, `brand_id`, `brand_name`, `pm_paid_by_card`, `category_id`, `category_name`, `whole_category_id`, `whole_category_name`, `series_parent_id`, `relation_mp_id`, `amount_share_vip`, `type`, `product_del_num`, `pm_paid_by_ucard`, `bar_code`, `merchant_amount`, `merchant_currency`, `merchant_currency_rate`, `order_currency`, `delivery_company_id`, `out_order_item_id`, `pm_used_yidou`, `pm_yidou_used_money`, `guarantee_days`, `return_days`, `replacement_days`, `return_days_source`, `replacement_days_source`, `mp_model`, `status`, `price_status`, `proxy_sale_price`, `share_price`, `membership_price`, `purchasing_price`, `settle_price`, `market_price`, `recommend_retail_price`, `merchant_product_price`)  "+
									     " VALUES "+sqlSoItem;
						}
					}
					jdbcTemplateOrder.execute(sqlSo2);//插入订单
					jdbcTemplateOrder.execute(sqlSoItem2);//插入订单详情
					jdbcTemplate.execute("update sheet_import_so_level set 是否更新订单=1 where id="+num+"");  
				}
				
				//第二步：核查星级并修复
				String soCheckLevelSql = "select t.id from uc_user_membership_level t where  t.entity_id="+user_id+" and t.membership_level_code='"+levelCode+"' ";
				List<Map<String,Object>> checkLevelList = jdbcTemplateOuser.queryForList(soCheckLevelSql);
				if(checkLevelList.size()>0){//核对的到，什么都不做；	
				}else{//核查不对，则更新之；
					//更改星级；
					l++;
					String sqlLevel="update uc_user_membership_level t set t.membership_level_code='"+levelCode+"' where t.entity_id="+user_id+"";
					jdbcTemplateOuser.execute(sqlLevel);
					jdbcTemplate.execute("update sheet_import_so_level set 是否更新星级=1 where id="+num+"");   
				}
				//更改会员更新状态；
				jdbcTemplate.execute("update sheet_import_so_level set 是否核查=1 where id="+num+"");   
				/*if(i==(list.size()-1)){
					//更新用户星级
					int length = importSoServiceImpl.batchUpdateOuserLevelCode(listLevelCode);       
					logger.info("批量更新用户星级数量>>>："+length+"&&&用户id:"+listLevelCode.toString());
				}*/
			}
			count++;
			lun++;
			logger.info("核查所有会员订单和星级结束>>>成功找到user_id "+p+" 条数据！");
			logger.info("核查所有会员订单和星级结束>>>失败找到user_id "+k+" 条数据！");
			logger.info("核查所有会员订单和星级结束>>>更新订单 "+o+" 条数据！");
			logger.info("核查所有会员订单和星级结束>>>更新星级 "+l+" 条数据！");
			logger.info("核查所有会员订单和星级结束>>>"+StringUtil.dateToString2(new Date()));
		} catch (Exception e) {
			logger.info("异常信息>>>"+e.getMessage(),e);
		}
		
	}
	
	

}
