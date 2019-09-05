package com.online.engine.pluginTaskInstanceManger;

import java.util.HashMap;





import org.springframework.stereotype.Service;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.IWorkItemInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.IActivityInstance;
import com.online.workflow.process.tasks.FormTask;
@Service
public interface ITaskInstanceManger extends IRuntimeContextAware {

	void createTaskInstances(IToken token, IActivityInstance activityInstance) throws EngineException;

	void runTaskInstance(IWorkflowSession workflowSession,
			IProcessInstance processInstance, ITaskInstance taskInstance);

	void completeTaskInstance(IWorkflowSession currentSession,
			IProcessInstance processInstance, ITaskInstance taskInstance,
			IActivityInstance targetActivityInstance) throws EngineException;

	void createWorkItem(IWorkflowSession currentSession,
			IProcessInstance processInstance, ITaskInstance taskInstance, FormTask formTask,
			HashMap<String, String> users);

	IWorkItemInstance claimWorkItem(IWorkItemInstance workItem);

	void completeWorkItem(IWorkItemInstance workItem, String targetActivityId,
			String comments) throws EngineException;

}
