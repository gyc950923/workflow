package com.online.engine.pluginPersistence;

import java.util.List;

import com.online.engine.Instance.IWorkItemInstance;
import com.online.engine.Instance.impl.WorkItemInstance;

public interface IWorkItemInstanceService {

	Boolean saveOrUpdateWorkItemInstance(IWorkItemInstance workItemInstance);

	IWorkItemInstance getWorkItemById(String workItemInstanceId);

	List<IWorkItemInstance> getCompletedWorkItemsForTaskInstance(
			String taskInstanceId);

	List<IWorkItemInstance> getWorkItemsByProcessInstId(String processInstanceId);

	List<IWorkItemInstance> getWorkItemsByProcessInstIdAndTaskInstanceId(
			String taskInstanceId, String processInstanceId);

	List<IWorkItemInstance> getNeedDoWorkItemsByProcessInstIdAndTaskInstId(
			String taskInstanceId, String processInstanceId);

	List<IWorkItemInstance> getWorkItemsByTaskInstanceIdAndActorId(
			String actorId, String taskInstanceId);

	Boolean deleteOtherWorkItemsById(IWorkItemInstance workItemInstance);

	Integer getAliveWorkItemCountForTaskInstance(String taskInstId);
}
