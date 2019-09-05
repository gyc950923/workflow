package com.online.engine.Instance;

import java.util.Date;
import java.util.HashMap;

import com.online.engine.events.INodeInstanceEvent;
import com.online.engine.exception.EngineException;
import com.online.workflow.process.WorkflowProcess;

public interface IProcessInstance {

	public String getId();

	public void setId(String id);
	
	public String getXmid();

	public void setXmid(String xmid);

	public String getJdtype();

	public void setJdtype(String jdtype);
	
	public String getFlowshort();
	
	public void setFlowshort(String flowshort);
	
	public WorkflowProcess getWorkflowProcess();

	// / <summary>流程实例开始运行！</summary>
	public void run() throws EngineException;

	public String getProcessId();

	public void setProcessId(String processId);

	public Integer getVersion();

	public void setVersion(Integer version);

	// / <summary>流程实例结束！</summary>
	public void Complete(INodeInstanceEvent e) throws EngineException;

	// / <summary>流程实例中止！</summary>
	public void abort();

	// / <summary>流程实例暂停！</summary>
	public void suspend();

	// / <summary>恢复暂停的实例</summary>
	public void restoreSuspend();

	public HashMap<String, Object> getProcessInstanceVariables();

	public void setProcessInstanceVariables(
			HashMap<String, Object> processInstanceVariables);
	public Object getProcessInstanceVariablesByName(String name);
	public String getName();

	public void setName(String name);

	public Integer getState();

	public void setState(Integer state);

	public Boolean getIsSuspended();

	public void setIsSuspended(Boolean isSuspended);

	public String getCreatorId();

	public void setCreatorId(String creatorId);

	public String getCreatorName();

	public void setCreatorName(String creatorName);

	public Date getCreatedTime();

	public void setCreatedTime(Date createdTime);

	public Date getStartedTime();

	public void setStartedTime(Date startedTime);

	public Date getExpiredTime();

	public void setExpiredTime(Date expiredTime);

	public Date getEndTime();

	public void setEndTime(Date endTime);

	public String getParentProcessInstanceId();

	public void setParentProcessInstanceId(String parentProcessInstanceId);

	public String getParentTaskInstanceId();

	public void setParentTaskInstanceId(String parentTaskInstanceId);

	public String getProcessDescription();

	public void setProcessDescription(String processDescription);

	public String getDisplayName();

	public void setDisplayName(String displayName);

	public Integer getCompeleteMode();

	public void setCompeleteMode(Integer compeleteMode);

	public String getEntityType();

	public String getEntityId();

	public Boolean getIsTermination();
}
