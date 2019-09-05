package com.online.engine.pluginTaskInstanceManger.toolTaskInstance;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.Instance.impl.WorkflowSession;
import com.online.engine.pluginTaskInstanceManger.ITaskInstanceCreator;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.tasks.Task;

public class ToolTaskInstanceCreator implements ITaskInstanceCreator{

	@Override
	public ITaskInstance createTaskInstance(WorkflowSession workflowSession,
			RuntimeContext rtCtx, IToken token,
			IProcessInstance processInstance, Task task, Activity activity) {
		// TODO Auto-generated method stub
		return null;
	}

}
