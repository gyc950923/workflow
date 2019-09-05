package com.online.engine.pluginTaskInstanceManger.subflowTaskInstance;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.pluginTaskInstanceManger.ITaskInstanceCompletion;

public class SubflowTaskInstanceCompletion implements ITaskInstanceCompletion{

	@Override
	public Boolean taskInstanceCanBeCompleted(IWorkflowSession currentSession,
			RuntimeContext runtimeContext, IProcessInstance processInstance,
			ITaskInstance taskInstance) {
		
		
		
		return null;
	}

}
