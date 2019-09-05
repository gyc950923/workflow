package com.online.workflow.design.workflow.service;

import java.util.List;

import com.online.workflow.design.workflow.bean.FlowGlobalProperties;
import com.online.workflow.design.workflow.bean.FlowNodeProperties;
import com.online.workflow.design.workflow.dao.FlowPropertiesDao;

/**
 * 流程属性service
 * @author Administrator
 *
 */
public class FlowPropertiesService {

	private FlowPropertiesDao dao = new FlowPropertiesDao();
	
	/**
	 * 保存全局属性
	 */
	public void saveGlobal(FlowGlobalProperties fg){
		this.dao.saveGlobal(fg);
	}
	
	/**
	 * 保存节点属性
	 * @param fn
	 */
	public void saveNode(FlowNodeProperties fn){
		this.dao.saveNode(fn);
	}
	
	/**
	 * 根据流程主键得到全局属性
	 * @param modelId
	 * @return
	 */
	public FlowGlobalProperties getFlowGlobalProperties(String modelId){
		return this.dao.getFlowGlobalProperties(modelId);
	}
	

	/**
	 * 得到指定流程下的所有节点集合
	 * @param modelId
	 * @return
	 */
	public List<FlowNodeProperties> getFlowNodePropertiesByList(String modelId){
		return this.dao.getFlowNodePropertiesByList(modelId);
	}
	
	/**
	 * 根据流程主键获取指定属性主键的属性
	 * @param flowId
	 * @param id
	 * @return
	 */
	public FlowNodeProperties getFlowNodeProperties(String modelId,String id){
		return this.dao.getFlowNodeProperties(modelId, id);
	}
	
	/**
	 * 保存所有属性
	 * @param list
	 */
	public void saveFlowNodePropertiesByList(String modelId,List<FlowNodeProperties> list){
		 this.dao.saveFlowNodePropertiesByList(modelId,list);
	}
}
