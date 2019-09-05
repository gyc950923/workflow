package com.online.engine.Instance;

public interface IProcessInstanceHistTrace {
	public String getId();

	public void setId(String id);

	public String getProcessInstanceId();

	public void setProcessInstanceId(String processInstanceId);

	public Integer getProcessInstanceTraceEnum();

	public void setProcessInstanceTraceEnum(Integer processInstanceTraceEnum);

	public Integer getType();

	public void setType(Integer type);

	public String getFromNodeId();

	public void setFromNodeId(String fromNodeId);

	public String getEdgeId();

	public void setEdgeId(String edgeId);

	public String getToNodeId();

	public void setToNodeId(String toNodeId);

	public Integer getStepNumber();

	public void setStepNumber(Integer stepNumber);
	
	public Integer getMinorNumber() ;

	public void setMinorNumber(Integer minorNumber) ;

}
