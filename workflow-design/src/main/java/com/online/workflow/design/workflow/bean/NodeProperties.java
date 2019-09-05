package com.online.workflow.design.workflow.bean;

/**
 * 节点属性
 * @author Administrator
 *
 */
public class NodeProperties {

	private String resourceId;//当前节点id
	
	private String modelId;//所属流程id

	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
}
