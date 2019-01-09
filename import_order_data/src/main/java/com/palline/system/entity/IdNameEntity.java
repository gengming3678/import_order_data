package com.palline.system.entity;

import java.util.List;

public class IdNameEntity {

	private Integer id;
	private String name;
	private String code;
	private String rowColTitle;
	private List<IdNameEntity> subCells;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<IdNameEntity> getSubCells() {
		return subCells;
	}
	public void setSubCells(List<IdNameEntity> subCells) {
		this.subCells = subCells;
	}
	public String getRowColTitle() {
		return rowColTitle;
	}
	public void setRowColTitle(String rowColTitle) {
		this.rowColTitle = rowColTitle;
	}
	
	
}
