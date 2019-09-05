package com.online.engine.Instance.impl;

import com.online.engine.Instance.IProcessInstanceHistTrace;
import com.online.engine.enums.ProcessInstanceTraceEnum;

public class ProcessInstanceHistTrace implements IProcessInstanceHistTrace {
	private String id;

	// / <summary>流程实例ID</summary>
	private String processInstanceId;

	// / <summary>类型</summary>
	private Integer processInstanceTraceEnum;

	private String fromNodeId;

	private String edgeId;

	private String toNodeId;
	private Integer type = ProcessInstanceTraceEnum.transition_type;
	// / <summary>步骤</summary>
	private Integer stepNumber;
	
	private Integer minorNumber;
	

	public Integer getMinorNumber() {
		return minorNumber;
	}

	public void setMinorNumber(Integer minorNumber) {
		this.minorNumber = minorNumber;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Integer getProcessInstanceTraceEnum() {
		return processInstanceTraceEnum;
	}

	public void setProcessInstanceTraceEnum(Integer processInstanceTraceEnum) {
		this.processInstanceTraceEnum = processInstanceTraceEnum;
	}

	public String getFromNodeId() {
		return fromNodeId;
	}

	public void setFromNodeId(String fromNodeId) {
		this.fromNodeId = fromNodeId;
	}

	public String getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(String edgeId) {
		this.edgeId = edgeId;
	}

	public String getToNodeId() {
		return toNodeId;
	}

	public void setToNodeId(String toNodeId) {
		this.toNodeId = toNodeId;
	}

	public Integer getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}

}
