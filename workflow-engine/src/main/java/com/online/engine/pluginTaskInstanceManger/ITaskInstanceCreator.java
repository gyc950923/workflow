package com.online.engine.pluginTaskInstanceManger;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.Instance.impl.WorkflowSession;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.tasks.Task;

public interface ITaskInstanceCreator {

	ITaskInstance createTaskInstance(WorkflowSession workflowSession,
			RuntimeContext rtCtx, IToken token,
			IProcessInstance processInstance, Task task, Activity activity);

}
