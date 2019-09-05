package com.online.engine.Instance;

public interface IToken {

	public String getId();

	public void setId(String id);

	public String getProcessInstanceId();

	public IProcessInstance getProcessInstance();

	public void setProcessInstance(IProcessInstance processInstance);

	public void setProcessInstanceId(String processInstanceId);

	public Boolean getIsAlive();

	public void setIsAlive(Boolean isAlive);

	public String getFromActivityId();

	public void setFromActivityId(String fromActivityId);

	public String getNodeId();

	public void setNodeId(String nodeId);

	public Integer getStepNumber();

	public void setStepNumber(Integer stepNumber);

	public IToken getNextToken();

    public IToken cloneToken();
}
