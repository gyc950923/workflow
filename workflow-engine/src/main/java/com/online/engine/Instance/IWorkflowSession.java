package com.online.engine.Instance;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.exception.EngineException;
import com.online.workflow.process.resources.UserInfo;
public interface IWorkflowSession {

	public RuntimeContext getRuntimeContext();

	public void initMemoryWfData(String processInstanceId);

	public Boolean execSql();

	public Object executeTemplate(IWorkflowSessionCallback calback);

	public IProcessInstance createProcessInstance(String flowId,
			String entityType, String entityId, UserInfo userInfo,HashMap<String, Object> var,String xmid,String jdtype, String flowshort)
			throws Exception;

	public List<ITaskInstance> getTaskInstancesForProcessInstance(
			String processInstanceId, String activityId);

	public List<IWorkItemInstance> getWorkItemsByProcessInstId(
			String processInstanceId);

	public IWorkItemInstance getWorkItemsById(String workItemId);

	public IWorkItemInstance claimWorkItem(String workItemId);

	public void completeWorkItem(String workItemId, String comments) throws EngineException;

	public void backByWorkItemId(IWorkItemInstance wi, String targetActivityId, String comments) throws EngineException;
	
	public ITaskInstance getUpTaskInstance(String taskInstanceId, String processInstanceId);

    public void terminationWorkItem(IWorkItemInstance wi, String comments) throws EngineException;
}
