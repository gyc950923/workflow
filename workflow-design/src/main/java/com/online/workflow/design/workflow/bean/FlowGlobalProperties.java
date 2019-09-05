package com.online.workflow.design.workflow.bean;

/**
 * 流程全局属性
 * @author Administrator
 *
 */
public class FlowGlobalProperties extends NodeProperties {

	private String name;//名称
	
	private String transaction;//描述

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
	
}
