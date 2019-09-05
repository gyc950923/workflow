package com.online.engine.pluginTaskInstanceManger.subflowTaskInstance;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.Instance.impl.WorkflowSession;
import com.online.engine.pluginTaskInstanceManger.ITaskInstanceCreator;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.tasks.Task;

public class SubflowTaskInstanceCreator implements ITaskInstanceCreator{

	@Override
	public ITaskInstance createTaskInstance(WorkflowSession workflowSession,
			RuntimeContext rtCtx, IToken token,
			IProcessInstance processInstance, Task task, Activity activity) {
		// TODO Auto-generated method stub
		return null;
	}

}
