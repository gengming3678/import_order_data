package com.palline.common.util;

import java.util.List;
/**
 * 页面分页工具类
 * @author gmm
 * @param <E>
 */
public class PageData<E> {

	private Long total;
	private List<E> rows;
	
	
	
	public PageData(Long total, List<E> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<E> getRows() {
		return rows;
	}
	public void setRows(List<E> rows) {
		this.rows = rows;
	}
	
	
	
}
