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
 * 导入会员订单和更新星级
 * @author gmm
 * @version 1.0
 */
@Controller
@RequestMapping("/ImportSoOut")
public class ImportSoOutController {
	private static Logger logger=Logger.getLogger(ImportSoOutController.class); 
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name="jdbcTemplateOrder")
	private JdbcTemplate jdbcTemplateOrder;
	@Resource(name="jdbcTemplateOuser")
	private JdbcTemplate jdbcTemplateOuser;
	@Resource
	private ImportSoServiceImpl importSoServiceImpl;

	/**
	 * 导入会员订单和更新星级
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/importOutData.do")
	public String importOutData(HttpServletRequest request,Model model) {
		try {
			logger.info("导入星级用户订单数据开始>>>"+StringUtil.dateToString2(new Date()));
			List<String> ids=new ArrayList<String>();
			String suffix="188721181228";//12位，1201，日期编码每次导入依次递增
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
			List<Ouser> listLevelCode=new ArrayList<Ouser>();
			int p=0;
			int k=0;
			for(int i=0;i<list.size();i++){
				Map<String,Object> map=list.get(i);
				String id = ids.get(i);
				//获取tel这列数据,查询user_id
				String name = (String) map.get("姓名");
				String tel = (String) map.get("电话");
				String level = (String) map.get("星级");
				String fee = (String) map.get("缴款金额");
				String idCardNum = (String) map.get("身份证号");
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
			    		jdbcTemplate.execute("update sheet3 set 邮箱='"+type+"' where 序号="+num+""); 
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
				//外部用户专用收货人手机号-18717718990；/create_time 2018-11-26 16:21:13；
				String sqlSo = " ("+id+", '"+id+"', '0', 1, "+user_id+", 1590058601000003, NULL, -1, 1872018091301, "+fee+", "+fee+", 0.00, 35, 1, 0, NULL, '2018-11-26 16:21:13', NULL, 0.00, 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, '2018-11-26 16:21:13', 0, NULL, 1, NULL, 0, '10001', '10', '2018-11-26 16:21:13', '2018-11-26 16:21:13', NULL, NULL, NULL, NULL, NULL, 0, 5, 0, 0, 0, NULL, NULL, 1086059000000411, '新闸路831号', '200135', 'tom', NULL, NULL, '18717718990', NULL, NULL, NULL, 10, '上海', 110, '上海市', NULL, NULL, 1140, '静安区', '', '2018-11-26 16:21:13', '2018-11-26 16:21:13', 0, NULL, 0.00, NULL, 0, NULL, 0, 1, 0, 0, 1870081300000047, '18717718990', NULL, NULL, '2018-11-26 16:21:13', '2018-11-26 16:21:13', '127.0.0.1', NULL, '系统', NULL, NULL, '2018-11-26 16:21:13', '2018-11-26 16:21:13', NULL, 187, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, 'ody', NULL, '2018-11-26 16:21:13', '2018-11-26 16:21:13', 0, '', 0.00, 0.00, 0.00, NULL, 0, NULL, 1046014700000833, NULL, NULL, NULL, 35, 0, 0, '上海三少猩球贸易有限公司', NULL, '2018-11-26 16:21:13', 0, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL, NULL, NULL, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, NULL, 0.00, NULL, 1590058601000008, '三少猩球贸易有限公司', NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
				String sqlSoItem = "("+id+", NULL, 1, '"+id+"', "+user_id+", 1590058601000003, "+levelProId+", 1900084000000240, 1872018091301, 0.00, NULL, NULL, "+fee+", 1, '"+levelProName+"', NULL, 'https://cdn.oudianyun.com/dev/back-product-web/1542541241488_59.52936780076672_5ed25f84-0545-4f29-a211-b0d6c2ca0c25.png', NULL, 1, 0.00, 0.00, 0.00, NULL, 0, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, 1, NULL, 0.00, NULL, 0.00, NULL, NULL, 0.000, NULL, NULL, NULL, 0, 1, 0, 0, 1870081300000047, NULL, NULL, NULL, '2018-11-26 16:21:13', '2018-11-26 16:21:13', '127.0.0.1', NULL, NULL, NULL, NULL, '2018-11-26 16:21:13', '2018-11-26 16:21:13', NULL, 187, '181120537951367047', 0.00, NULL, NULL, NULL, NULL, NULL, '1900084100000002', NULL, '份', NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, 0.00, 0, NULL, NULL, NULL, NULL, NULL, 1900082400000089, '来伊份', NULL, 1900083602000056, '增值服务费', '1900080800000001-1900083602000056', 'B2C后台类目->增值服务费', NULL, NULL, NULL, 1, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, 90, 30, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);";

				String sqlSo2 = " INSERT INTO `order`.`so` (`id`, `order_code`, `parent_order_code`, `is_leaf`, `user_id`, `merchant_id`, `parent_merchant_name`, `parent_merchant_id`, `warehouse_id`, `order_amount`, `product_amount`, `tax_amount`, `order_status`, `order_payment_type`, `order_payment_status`, `order_payment_confirm_amount`, `order_payment_confirm_date`, `order_payment_confirm_type`, `order_delivery_fee`, `order_delivery_fee_insurance_type`, `order_delivery_fee_insurance_amount`, `order_paid_by_account`, `order_paid_by_coupon`, `order_paid_by_card`, `order_paid_by_rebate`, `order_paid_by_referral_code`, `order_used_points`, `order_promotion_discount`, `points_used_money`, `order_give_points`, `order_delivery_fee_accounting`, `order_cancel_reason_id`, `order_cancel_date`, `order_need_cs`, `order_cs_cancel_reason`, `order_cance_operate_type`, `order_cance_operate_id`, `order_data_exchange_flag`, `order_delivery_service_type`, `order_delivery_method_id`, `expect_receive_date_start`, `expect_receive_date_end`, `expect_receive_time_start`, `expect_receive_time_end`, `order_remark_user`, `order_remark_merchant2user`, `order_remark_merchant`, `order_source`, `order_channel`, `order_business_type`, `order_promotion_type`, `order_promotion_status`, `order_spread_type`, `order_message_phone`, `good_receiver_id`, `good_receiver_address`, `good_receiver_postcode`, `good_receiver_name`, `good_receiver_last_name`, `good_receiver_first_name`, `good_receiver_mobile`, `good_receiver_phone`, `good_receiver_country_id`, `good_receiver_country`, `good_receiver_province_id`, `good_receiver_province`, `good_receiver_city_id`, `good_receiver_city`, `good_receiver_county_id`, `good_receiver_county`, `good_receiver_area_id`, `good_receiver_area`, `identity_card_number`, `order_logistics_time`, `order_receive_date`, `order_invoice_type`, `virtual_stock_status`, `order_weight`, `is_fragile_or_liquid`, `order_delete_status`, `paid_online_threshold`, `order_return_flag`, `is_available`, `is_deleted`, `version_no`, `create_userid`, `create_username`, `create_userip`, `create_usermac`, `create_time`, `create_time_db`, `server_ip`, `update_userid`, `update_username`, `update_userip`, `update_usermac`, `update_time`, `update_time_db`, `client_versionno`, `company_id`, `order_before_amount`, `order_before_delivery_fee`, `deliver_id`, `deliver_code`, `order_cs_audit_reason`, `order_audit_reason_id`, `order_audit_operate_id`, `order_audit_operate_type`, `order_change_status`, `change_order_code`, `sex`, `exact_address`, `expect_receive_type`, `merchant_full_id_path`, `flow_type`, `sys_source`, `out_order_code`, `order_audit_date`, `order_change_date`, `order_payment_flag`, `order_payment_two_type`, `excise_tax_amount`, `increment_tax_amount`, `customs_duties_amount`, `mode_id`, `sync_flag`, `source_code`, `distributor_id`, `distributor_name`, `distributor_shop_id`, `distributor_shop_name`, `delivery_status`, `comment_status`, `aftersales_flag`, `merchant_name`, `order_remark_company`, `order_complete_date`, `order_free_flag`, `promotion_ids`, `estimated_time_of_arrival`, `guid`, `order_vip_discount`, `order_type`, `order_payment_source`, `history_flag`, `order_activity_id`, `order_activity_type`, `cps_uid`, `manual_type`, `order_paid_by_ucard`, `merchant_amount`, `merchant_currency`, `merchant_currency_rate`, `order_currency`, `delivery_company_id`, `good_receiver_country_code`, `good_receiver_province_code`, `good_receiver_city_code`, `good_receiver_area_code`, `order_used_yidou`, `yidou_used_money`, `supplier_id`, `rebate_points_amount`, `rebate_brokerage_amount`, `good_other_contact_phone`, `store_id`, `store_name`, `do_flag`, `channel_code`, `brokerage_status`, `picking_user_id`, `latitude`, `longitude`, `order_total_original_amount`) "
								+"VALUES "+sqlSo;
				
				String sqlSoItem2 = " INSERT INTO `order`.`so_item` INSERT INTO `order`.`so_item`(`id`, `parent_so_item_id`, `is_item_leaf`, `order_code`, `user_id`, `merchant_id`, `product_id`, `mp_id`, `warehouse_id`, `product_item_amount`, `product_item_currency`, `product_item_currency_rate`, `product_price_final`, `product_item_num`, `product_cname`, `product_ename`, `product_pic_path`, `product_version_no`, `product_sale_type`, `product_price_original`, `product_price_market`, `product_price_sale`, `product_tax_amount`, `buy_type`, `product_type`, `pm_give_points`, `pm_used_points`, `pm_used_money`, `pm_paid_by_account`, `pm_need_points`, `pm_need_points_total`, `frozen_virtal_stock_num`, `frozen_real_stock_num`, `virtual_stock_status`, `amount_share_coupon`, `amount_share_promotion`, `amount_share_delivery_fee`, `amount_share_delivery_fee_accounting`, `amount_share_referral_code`, `product_gross_weight`, `binding_so_item_id`, `price_rule_id`, `comment_id`, `main_activity_id`, `is_available`, `is_deleted`, `version_no`, `create_userid`, `create_username`, `create_userip`, `create_usermac`, `create_time`, `create_time_db`, `server_ip`, `update_userid`, `update_username`, `update_userip`, `update_usermac`, `update_time`, `update_time_db`, `client_versionno`, `company_id`, `parent_order_code`, `product_item_before_amount`, `vehicle_warranty`, `delivery_time`, `delivery_method`, `violation_responsibility`, `source_id`, `code`, `third_merchant_product_code`, `unit`, `place_of_origin`, `excise_tax_amount`, `increment_tax_amount`, `customs_duties_amount`, `ext_info`, `referrer_user_id`, `standard`, `material`, `product_price_before_final`, `comment_status`, `product_price_settle`, `product_price_out`, `product_price_purchasing`, `promotion_product_price_settle`, `series_product_code`, `brand_id`, `brand_name`, `pm_paid_by_card`, `category_id`, `category_name`, `whole_category_id`, `whole_category_name`, `series_parent_id`, `relation_mp_id`, `amount_share_vip`, `type`, `product_del_num`, `pm_paid_by_ucard`, `bar_code`, `merchant_amount`, `merchant_currency`, `merchant_currency_rate`, `order_currency`, `delivery_company_id`, `out_order_item_id`, `pm_used_yidou`, `pm_yidou_used_money`, `guarantee_days`, `return_days`, `replacement_days`, `return_days_source`, `replacement_days_source`, `mp_model`, `status`, `price_status`, `proxy_sale_price`, `share_price`, `membership_price`, `purchasing_price`, `settle_price`, `market_price`, `recommend_retail_price`, `merchant_product_price`) VALUES (1001084800000531, NULL, 1, '181126963310371037', 1011084500000037, 1872018091301, 1015084200000027, 1014086401000005, 1872018091301, 0.10, NULL, NULL, 0.10, 1, '二星会员', NULL, 'https://cdn.oudianyun.com/back-product-web/1542711180380_22.78431971620466_bd8a6bfc-87f9-408b-adbe-4ea575b342f2.jpg', NULL, 1, 0.10, NULL, 0.10, NULL, 0, NULL, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, 1, NULL, 0.00, NULL, 0.00, NULL, NULL, 0.000, NULL, NULL, NULL, 0, 1, 0, 0, 1011084500000037, NULL, NULL, NULL, '2018-11-26 20:54:43', NULL, '127.0.0.1', NULL, NULL, NULL, NULL, '2018-11-26 20:54:43', NULL, NULL, 187, '181126963310371037', 0.10, NULL, NULL, NULL, NULL, NULL, '1014084200000058', NULL, '张', NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, 0.10, 0, NULL, NULL, NULL, NULL, NULL, 1014084200000013, '三少', NULL, 1014084200000003, '会员卡券', '1014084200000001-1014084200000002-1014084200000003', 'b2c后台类目->会员卡券类目->会员卡券', NULL, NULL, NULL, 1, 0, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0, 7, 7, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL) "
									+" VALUES "+sqlSoItem;
				jdbcTemplateOrder.execute(sqlSo2);//插入订单
				jdbcTemplateOrder.execute(sqlSoItem2);//插入订单详情
				//更改星级；
				String sqlLevel="update uc_user_membership_level t set t.membership_level_code='"+levelCode+"' where t.entity_id="+user_id+"";
				jdbcTemplateOuser.execute(sqlLevel);
				//更改会员更新状态；
				jdbcTemplate.execute("update sheet3 set 邮箱='"+type+"' where 序号="+num+"");   
				/*if(i==(list.size()-1)){
					//更新用户星级
					int length = importSoServiceImpl.batchUpdateOuserLevelCode(listLevelCode);       
					logger.info("批量更新用户星级数量>>>："+length+"&&&用户id:"+listLevelCode.toString());
				}*/
			}
			logger.info("导入星级用户订单数据结束>>>成功插入 "+p+" 条数据！");
			logger.info("导入星级用户订单数据结束>>>失败插入 "+k+" 条数据！");
			logger.info("导入星级用户订单数据结束>>>"+StringUtil.dateToString2(new Date()));
		} catch (Exception e) {
			logger.info("异常信息>>>"+e.getMessage(),e);
			model.addAttribute("errorInfo", e.getMessage());
			return "so/soError";
		}
		
		return "so/soList";
	}
	
	

}
