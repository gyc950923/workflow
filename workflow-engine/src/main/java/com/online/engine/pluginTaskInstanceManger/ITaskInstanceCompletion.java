package com.online.engine.pluginTaskInstanceManger;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.impl.RuntimeContext;

public interface ITaskInstanceCompletion {
	Boolean taskInstanceCanBeCompleted(IWorkflowSession currentSession, RuntimeContext runtimeContext,
            IProcessInstance processInstance, ITaskInstance taskInstance);
}
