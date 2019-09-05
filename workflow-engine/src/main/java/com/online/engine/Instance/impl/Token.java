package com.online.engine.Instance.impl;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IToken;

public class Token implements IToken {
	private String id;
	private IProcessInstance processInstance;
	private String processInstanceId;
	private Boolean isAlive=false;
	private String fromActivityId;
	private String nodeId;
	private Integer stepNumber;

	/**
	 * 功能:生成下一个节点的令牌<br>
	 * 约束:与本函数相关的约束<br>
	 * @return
	 */
	public Token getNextToken(){
	    Token token = new Token();
	    token.setFromActivityId(this.fromActivityId);
	    token.setIsAlive(this.isAlive);
	    token.setNodeId(this.nodeId);
	    token.setProcessInstance(this.processInstance);
	    token.setProcessInstanceId(this.processInstanceId);
	    token.setStepNumber(this.stepNumber);
	    return token;
	}
	
	/**
     * 功能:克隆当前的token<br>
     * 约束:与本函数相关的约束<br>
     * @return
     */
	public Token cloneToken(){
	    Token token = this.getNextToken();
	    token.setId(this.id);
	    return token;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(IProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Boolean getIsAlive() {
		return isAlive;
	}

	public void setIsAlive(Boolean isAlive) {
		this.isAlive = isAlive;
	}

	public String getFromActivityId() {
		return fromActivityId;
	}

	public void setFromActivityId(String fromActivityId) {
		this.fromActivityId = fromActivityId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}

}
