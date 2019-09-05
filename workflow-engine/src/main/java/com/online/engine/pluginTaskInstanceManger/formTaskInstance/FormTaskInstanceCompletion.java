package com.online.engine.pluginTaskInstanceManger.formTaskInstance;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.engine.pluginTaskInstanceManger.ITaskInstanceCompletion;

public class FormTaskInstanceCompletion implements ITaskInstanceCompletion{

	@Override
	public Boolean taskInstanceCanBeCompleted(IWorkflowSession currentSession,
			RuntimeContext runtimeContext, IProcessInstance processInstance,
			ITaskInstance taskInstance) {
		
		  IPersistenceService persistenceService = runtimeContext.getPersistenceService();
          Integer aliveWorkItemCount = persistenceService.getAliveWorkItemCountForTaskInstance(taskInstance.getId());
          if (aliveWorkItemCount == 0)
          {
              return true;
          }
          else
          {
              return false;
          }
	}

}
