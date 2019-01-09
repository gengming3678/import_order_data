package com.palline.system.entity;

import java.util.List;


public class Node {
	
    private Integer id;
    private Integer parentId;
    private String text;
    private boolean leaf;
    private boolean expanded;
    private String url;
    private String imgType;
    private Integer roleId;
    private Integer xuHao;
    private Integer type;
    private List<Integer> roleIds;
    private List<Integer> nodeIds;
    private Integer reportId;
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImgType() {
		return imgType;
	}
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getXuHao() {
		return xuHao;
	}
	public void setXuHao(Integer xuHao) {
		this.xuHao = xuHao;
	}
	public List<Integer> getNodeIds() {
		return nodeIds;
	}
	public void setNodeIds(List<Integer> nodeIds) {
		this.nodeIds = nodeIds;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public List<Integer> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	
	
	
	
}
