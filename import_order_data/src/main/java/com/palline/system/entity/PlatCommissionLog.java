package com.palline.system.entity;

import java.math.BigDecimal;

public class PlatCommissionLog {
	private String id;
	private BigDecimal fee;
	private BigDecimal balanceAmountAfter;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public BigDecimal getBalanceAmountAfter() {
		return balanceAmountAfter;
	}
	public void setBalanceAmountAfter(BigDecimal balanceAmountAfter) {
		this.balanceAmountAfter = balanceAmountAfter;
	}
	
	
	
}
