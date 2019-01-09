package com.palline.system.service;

import java.util.List;
import java.util.Map;

import com.palline.system.entity.Node;
import com.palline.system.entity.NodeCheck;

/**
 * Copyright (C), 2015-8-10, 上海蓬海涞讯数据技术有限公司
 * @author gmm
 * @version 1.0
 * @date 2015-8-10
 */
public interface NodeService {

	public List<Node> queryNodeListByParentId(Node node);
	
	public NodeCheck queryNodeListByRoleIdNodeId(Node node);
	
	public List<Integer> queryNodeListByRoleId(List<Integer> roleIds);
	
	public int updateNode(Node node);
	
	public int updateNodeReportIdById(Node node);
	
	public int insertNode(Node node);
	
	public int insertRoleNode(List<Map<String,Integer>> list);
	
	public int deleteNode(Node node);
	
	public void updateNodeReportId(Integer nodeId,Integer reportId);
}
