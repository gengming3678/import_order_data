package com.palline.system.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palline.system.dao.imp.NodeDaoImp;
import com.palline.system.entity.Node;
import com.palline.system.entity.NodeCheck;
import com.palline.system.service.NodeService;

@Service
public class NodeServiceImp implements NodeService{

	@Autowired
	private NodeDaoImp nodeDaoImp;

	@Override
	public List<Node> queryNodeListByParentId(Node node) {
		// TODO Auto-generated method stub
		return nodeDaoImp.queryNodeListByParentId(node);
	}
	
	public NodeCheck queryNodeListByRoleIdNodeId(Node node){
		return nodeDaoImp.queryNodeListByRoleIdNodeId(node);
	}
	
	public List<Integer> queryNodeListByRoleId(List<Integer> roleIds){
		return nodeDaoImp.queryNodeListByRoleId(roleIds);
	}
	@Override
	public int insertNode(Node node) {
		// TODO Auto-generated method stub
		return nodeDaoImp.insertNode(node);
	}
	public int insertRoleNode(List<Map<String,Integer>> list){
		return nodeDaoImp.insertRoleNode(list);
	}
	@Override
	public int updateNode(Node node) {
		// TODO Auto-generated method stub
		return nodeDaoImp.updateNode(node);
	}

	public int updateNodeReportIdById(Node node){
		return nodeDaoImp.updateNodeReportIdById(node);
	}
	@Override
	public int deleteNode(Node node) {
		// TODO Auto-generated method stub
		return nodeDaoImp.deleteNode(node);
	}
	public void updateNodeReportId(Integer nodeId,Integer reportId){
		nodeDaoImp.updateNodeReportId(nodeId, reportId);
	}
	

	
	
}
