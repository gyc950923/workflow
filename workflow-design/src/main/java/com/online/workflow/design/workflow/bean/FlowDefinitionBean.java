package com.online.workflow.design.workflow.bean;


/**
 * 流程定义Bean
 * @author Administrator
 *
 */
public class FlowDefinitionBean {

	private String modelId;//主键
	private String name;//流程名称
	private String description;//描述

	private String model;//具体定义的json串
	
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	
}
