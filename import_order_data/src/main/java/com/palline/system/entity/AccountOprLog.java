package com.palline.system.entity;

import java.math.BigDecimal;

public class AccountOprLog {
	
	private BigDecimal id;
	private BigDecimal infoId;
	private Long distributorId;
	private BigDecimal balanceL;
	private BigDecimal afterAmount;
	private Integer type;
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public BigDecimal getInfoId() {
		return infoId;
	}
	public void setInfoId(BigDecimal infoId) {
		this.infoId = infoId;
	}
	public Long getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}
	public BigDecimal getBalanceL() {
		return balanceL;
	}
	public void setBalanceL(BigDecimal balanceL) {
		this.balanceL = balanceL;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getAfterAmount() {
		return afterAmount;
	}
	public void setAfterAmount(BigDecimal afterAmount) {
		this.afterAmount = afterAmount;
	}
	
	
	
	
	
}
