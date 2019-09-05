package com.online.engine.pluginTaskInstanceManger;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.impl.RuntimeContext;

public interface ITaskInstanceRunner {

	void run(IWorkflowSession workflowSession, RuntimeContext runtimeContext,
			IProcessInstance processInstance, ITaskInstance taskInstance);

}
