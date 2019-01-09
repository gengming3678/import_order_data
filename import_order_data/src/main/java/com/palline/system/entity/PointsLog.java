package com.palline.system.entity;

import java.math.BigDecimal;

public class PointsLog {
	
	private Long id;
	private Long userId;
	private Long aid;
	private BigDecimal fee;
	private BigDecimal feeFinal;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public Long getAid() {
		return aid;
	}
	public void setAid(Long aid) {
		this.aid = aid;
	}
	public BigDecimal getFeeFinal() {
		return feeFinal;
	}
	public void setFeeFinal(BigDecimal feeFinal) {
		this.feeFinal = feeFinal;
	}

	
	
}
