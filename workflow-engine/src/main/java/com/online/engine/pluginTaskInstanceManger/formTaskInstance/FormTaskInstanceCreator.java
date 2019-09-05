package com.online.engine.pluginTaskInstanceManger.formTaskInstance;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.IWorkflowSessionAware;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.Instance.impl.TaskInstance;
import com.online.engine.Instance.impl.WorkflowSession;
import com.online.engine.enums.TaskInstanceStateEnum;
import com.online.engine.pluginKernel.INetInstance;
import com.online.engine.pluginKernel.impl.ActivityInstance;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.engine.pluginTaskInstanceManger.ITaskInstanceCreator;
import com.online.workflow.common.DateUtil;
import com.online.workflow.process.enums.BackModeEnum;
import com.online.workflow.process.enums.LoopStrategyEnum;
import com.online.workflow.process.enums.PageActionEnum;
import com.online.workflow.process.enums.TaskTypeEnum;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.resources.Duration;
import com.online.workflow.process.tasks.FormTask;
import com.online.workflow.process.tasks.Task;

public class FormTaskInstanceCreator implements ITaskInstanceCreator {

	@Override
	public ITaskInstance createTaskInstance(WorkflowSession workflowSession,
			RuntimeContext rtCtx, IToken token,
			IProcessInstance processInstance, Task task, Activity activity) {

		if (LoopStrategyEnum.skip.intValue() == task.getLoopStrategy()) {
			// 检查是否已经执行过的task instance
			IPersistenceService persistenceService = rtCtx
					.getPersistenceService();
			Integer count = persistenceService
					.getCompletedTaskInstanceCountForTask(
							processInstance.getId(), task.getId());
			if (count > 0) {
				return null;
			}
		}

		TaskInstance taskInstance = new TaskInstance();

		Integer taskType = task.getTaskType();
		taskInstance.setTaskTypeEnum(taskType); 
		taskInstance.setStepNumber(token.getStepNumber()); 
		taskInstance.setProcessInstanceId(processInstance.getId()); 
		taskInstance.setActivityId(activity.getId()); 
		taskInstance.setAssignmentStrategyEnum(((FormTask) task).getUserRule().getFormTaskEnum());
		taskInstance.setCreatedTime(DateUtil.getSysDate()); 
		taskInstance.setStartedTime(DateUtil.getSysDate());
		taskInstance.setName(task.getName());
		taskInstance.setState(TaskInstanceStateEnum.running); 
		taskInstance.setTaskId(task.getId());
		taskInstance.setTask(task); 
		taskInstance.setFromActivityId(token.getFromActivityId());
		taskInstance.setForwardMode(rtCtx.getForwardMode());

		if (rtCtx.getTaskControl().getPageAction() == PageActionEnum.Back) {
			INetInstance netInstance = rtCtx.getKernelManager().getNetInstance(
					token.getProcessInstance().getProcessId(),
					token.getProcessInstance().getVersion());
			ActivityInstance activityInstance = (ActivityInstance) netInstance
					.getWFElementInstance(token.getFromActivityId());
			
			FormTask formTask = (FormTask) activityInstance.getActivity().getTasks().get(0);

			if (formTask.getBackRule().getReturnMode() == BackModeEnum.oldRoad.intValue())
				taskInstance.setTargetActivityId(token.getFromActivityId());
		}

		((IRuntimeContextAware) taskInstance).setRuntimeContext(rtCtx);
		((IWorkflowSessionAware) taskInstance)
				.setCurrentWorkflowSession(workflowSession);
		// 计算超时
		Duration duration = task.getDuration();
		((TaskInstance) taskInstance).setExpiredTime(DateUtil.getSysDate());// TODO计算

		rtCtx.getPersistenceService().saveOrUpdateTaskInstance(taskInstance);

		return taskInstance;

	}

}
