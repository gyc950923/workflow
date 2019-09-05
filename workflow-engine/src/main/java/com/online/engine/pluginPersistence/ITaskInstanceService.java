package com.online.engine.pluginPersistence;

import java.util.List;

import com.online.engine.Instance.ITaskInstance;

public interface ITaskInstanceService {
	 void saveOrUpdateTaskInstance(ITaskInstance taskInstance);

     ITaskInstance getTaskInstanceByWorkItemId(String workItemId);

     int getCompletedTaskInstanceCountForTask(String processInstanceId, String taskId);

     ITaskInstance getLastCompletedTaskInstance(String processInstanceId, String taskId);

     List<ITaskInstance> getTaskInstancesForProcessInstance(String processInstanceId, String activityId);

     ITaskInstance getTaskInstanceByParentId(String ParentTaskInstanceId);
     
     ITaskInstance getTaskInstanceById(String TaskInstanceId);
     
     ITaskInstance getUpTaskInstance(String taskInstanceId, String processInstanceId);
}
