package com.online.engine.pluginTaskInstanceManger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.IWorkItemInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.IWorkflowSessionAware;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.Instance.impl.TaskInstance;
import com.online.engine.Instance.impl.WorkItemInstance;
import com.online.engine.Instance.impl.WorkflowSession;
import com.online.engine.common.FreemarkerUtil;
import com.online.engine.common.HttpClientUtil;
import com.online.engine.common.SysConfigUtil;
import com.online.engine.enums.TaskInstanceStateEnum;
import com.online.engine.enums.WorkItemInstanceStateEnum;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.IActivityInstance;
import com.online.engine.pluginKernel.INetInstance;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.engine.pluginTaskInstanceManger.formTaskInstance.FormTaskInstanceCompletion;
import com.online.engine.pluginTaskInstanceManger.formTaskInstance.FormTaskInstanceCreator;
import com.online.engine.pluginTaskInstanceManger.formTaskInstance.FormTaskInstanceRunner;
import com.online.engine.pluginTaskInstanceManger.subflowTaskInstance.SubflowTaskInstanceCompletion;
import com.online.engine.pluginTaskInstanceManger.subflowTaskInstance.SubflowTaskInstanceCreator;
import com.online.engine.pluginTaskInstanceManger.subflowTaskInstance.SubflowTaskInstanceRunner;
import com.online.engine.pluginTaskInstanceManger.toolTaskInstance.ToolTaskInstanceCompletion;
import com.online.engine.pluginTaskInstanceManger.toolTaskInstance.ToolTaskInstanceCreator;
import com.online.engine.pluginTaskInstanceManger.toolTaskInstance.ToolTaskInstanceRunner;
import com.online.workflow.common.DateUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.enums.ActivityCallBackEnum;
import com.online.workflow.process.enums.BackModeEnum;
import com.online.workflow.process.enums.FormTaskEnum;
import com.online.workflow.process.enums.PageActionEnum;
import com.online.workflow.process.enums.TaskTypeEnum;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.rules.StartRule;
import com.online.workflow.process.tasks.FormTask;
import com.online.workflow.process.tasks.SubflowTask;
import com.online.workflow.process.tasks.Task;
import com.online.workflow.process.tasks.ToolTask;

public class TaskInstanceManger implements ITaskInstanceManger {

	protected RuntimeContext rtCtx = null;
	public ITaskInstanceCreator DefaultFormTaskInstanceCreator = new FormTaskInstanceCreator();
	public ITaskInstanceCreator DefaultToolTaskInstanceCreator = new ToolTaskInstanceCreator();
	public ITaskInstanceCreator DefaultSubflowTaskInstanceCreator = new SubflowTaskInstanceCreator();

	public ITaskInstanceRunner DefaultFormTaskInstanceRunner = new FormTaskInstanceRunner();
	public ITaskInstanceRunner DefaultToolTaskInstanceRunner = new ToolTaskInstanceRunner();
	public ITaskInstanceRunner DefaultSubflowTaskInstanceRunner = new SubflowTaskInstanceRunner();

	public ITaskInstanceCompletion DefaultFormTaskInstanceCompletion = new FormTaskInstanceCompletion();
	public ITaskInstanceCompletion DefaultToolTaskInstanceCompletion = new ToolTaskInstanceCompletion();
	public ITaskInstanceCompletion DefaultSubflowTaskInstanceCompletion = new SubflowTaskInstanceCompletion();

	@Override
	public void setRuntimeContext(RuntimeContext ctx) {

		rtCtx = ctx;
	}

	public RuntimeContext getRtCtx() {
		return rtCtx;
	}

	public void setRtCtx(RuntimeContext rtCtx) {
		this.rtCtx = rtCtx;
	}

	@Override
	public RuntimeContext getRuntimeContext() {

		return rtCtx;
	}

	@Override
	public void createTaskInstances(IToken token,
			IActivityInstance activityInstance) throws EngineException {

		IProcessInstance processInstance = token.getProcessInstance();
		WorkflowSession workflowSession = (WorkflowSession) ((IWorkflowSessionAware) processInstance)
				.getCurrentWorkflowSession();
		Activity activity = activityInstance.getActivity();

		// 1.创建任务实例
		int createdTaskInstanceCount = 0;
		for (int i = 0; i < activity.getTasks().size(); i++) {
			Task task = activity.getTasks().get(i);
			ITaskInstance taskInstance = null;
			if (task instanceof FormTask) {
				taskInstance = DefaultFormTaskInstanceCreator
						.createTaskInstance(workflowSession, rtCtx, token,
								processInstance, task, activity);

			} else if (task instanceof ToolTask) {
				taskInstance = DefaultToolTaskInstanceCreator
						.createTaskInstance(workflowSession, rtCtx, token,
								processInstance, task, activity);
			} else if (task instanceof SubflowTask) {
				taskInstance = DefaultSubflowTaskInstanceCreator
						.createTaskInstance(workflowSession, rtCtx, token,
								processInstance, task, activity);
			}

			if (taskInstance == null) {
				continue;
			}

			createdTaskInstanceCount = createdTaskInstanceCount + 1;

			// 2.运行任务实例
			this.runTaskInstance(workflowSession, processInstance, taskInstance);

			if (createdTaskInstanceCount == 0) {
				// 如果是空activity，哪么直接结束

				IActivityInstance targetActivityInstance = null;

				if (rtCtx.getTaskControl().getPageAction() == PageActionEnum.Back
						.intValue()) {
					INetInstance netInstance = this.rtCtx.getKernelManager()
							.getNetInstance(
									token.getProcessInstance().getProcessId(),
									token.getProcessInstance().getVersion());
					Activity toActivity = (Activity) netInstance
							.getWFElementInstance(token.getFromActivityId());
					FormTask formTask = (FormTask) toActivity.getTasks().get(0);

					if (formTask.getBackRule().getReturnMode() == BackModeEnum.oldRoad
							.intValue())
						targetActivityInstance = (IActivityInstance) netInstance
								.getWFElementInstance(token.getFromActivityId());

				}

				activityInstance.complete(token, targetActivityInstance);
			} else {
				// TODO 更新运营状态
			}
		}

	}

	@Override
	public void runTaskInstance(IWorkflowSession workflowSession,
			IProcessInstance processInstance, ITaskInstance taskInstance) {

		Task task = taskInstance.getTask();
		String taskInstanceRunnerName = null;
		ITaskInstanceRunner taskInstanceRunner = null;

		Integer taskType = task.getTaskType();

		taskInstanceRunnerName = task.getTaskInstanceRunner();
		if (StringUtils.isNotBlank(taskInstanceRunnerName)) {

			// taskInstanceRunner =
			// PluginFacade.GetTaskInstanceRunner(taskInstanceRunnerName);

		}

		if (taskInstanceRunner == null) {
			if (TaskTypeEnum.form.intValue() == taskType) {
				taskInstanceRunnerName = processInstance.getWorkflowProcess()
						.getStartRule().getFormTaskInstanceRunner();
			} else if (TaskTypeEnum.tool.intValue() == taskType) {
				taskInstanceRunnerName = processInstance.getWorkflowProcess()
						.getStartRule().getToolTaskInstanceRunner();
			} else if (TaskTypeEnum.subflow.intValue() == taskType) {
				taskInstanceRunnerName = processInstance.getWorkflowProcess()
						.getStartRule().getSubflowTaskInstanceRunner();
			}
			if (StringUtils.isNotBlank(taskInstanceRunnerName)) {
				// taskInstanceRunner =
				// PluginFacade.GetTaskInstanceRunner(taskInstanceRunnerName);
			}
		}

		if (taskInstanceRunner == null) {
			if (TaskTypeEnum.form.intValue() == taskType) {
				taskInstanceRunner = DefaultFormTaskInstanceRunner;
			} else if (TaskTypeEnum.tool.intValue() == taskType) {
				taskInstanceRunner = DefaultToolTaskInstanceRunner;
			} else if (TaskTypeEnum.subflow.intValue() == taskType) {
				taskInstanceRunner = DefaultSubflowTaskInstanceRunner;
			}
		}
		if (taskInstanceRunner != null) {
			taskInstanceRunner.run(workflowSession, this.getRuntimeContext(),
					processInstance, taskInstance);
		} else {
			WorkflowProcess process = taskInstance.getAliveProcessInstance()
					.getWorkflowProcess();

			// TODO 异常
		}

	}

	@Override
	public void completeTaskInstance(IWorkflowSession currentSession,
			IProcessInstance processInstance, ITaskInstance taskInstance,
			IActivityInstance targetActivityInstance) throws EngineException {
		// 如果TaskInstance处于结束状态，则直接返回
		if (taskInstance.getState() == TaskInstanceStateEnum.completed
				.intValue()
				|| taskInstance.getState() == TaskInstanceStateEnum.canceled
						.intValue()) {
			return;
		}
		if (taskInstance.getState() == TaskInstanceStateEnum.initialized
				.intValue()) {
			// TODO 抛异常
		}
		if (taskInstance.getIsTermination() || taskInstance.getIsSuspend()) {
			// TODO 抛异常
		}

		if (targetActivityInstance != null) {
			((TaskInstance) taskInstance)
					.setTargetActivityId(targetActivityInstance.getActivity()
							.getId());
		}

		IPersistenceService persistenceService = this.rtCtx
				.getPersistenceService();

		// 第一步，首先结束当前taskInstance
		if (!this.taskInstanceCanBeCompleted(currentSession, this.rtCtx,
				processInstance, taskInstance)) {
			return;
		}
		((TaskInstance) taskInstance).setState(TaskInstanceStateEnum.completed);
		((TaskInstance) taskInstance).setEndTime(DateUtil.getSysDate());
		persistenceService.saveOrUpdateTaskInstance(taskInstance);
		// 触发相应的事件 TODO

		// 第二步，检查ActivityInstance是否可以结束
		if (!activityInstanceCanBeCompleted(taskInstance)) {
			return;
		}

		// 第三步，尝试结束对应的activityInstance
		List<IToken> tokens = persistenceService.getTokensForProcessInstance(
				taskInstance.getProcessInstanceId(),
				taskInstance.getActivityId());

		if (tokens == null || tokens.size() == 0) {
			return;// 表明activityInstance已经结束了。
		}
		if (tokens.size() > 1) {
			// TODO 异常
		}
		IToken token = tokens.get(0);
		// stepNumber不相等，不允许执行结束操作。
		if (token.getStepNumber() != taskInstance.getStepNumber()) {
			// TODO 异常
		}
		if (token.getIsAlive() == false) {
			// TODO 异常
		}

		Object obj = null;
		INetInstance netInstance = this.rtCtx.getKernelManager()
				.getNetInstance(
						taskInstance.getAliveProcessInstance().getProcessId(),
						taskInstance.getAliveProcessInstance().getVersion());
		obj = netInstance.getWFElementInstance(taskInstance.getActivityId());

		if (obj == null) {
			// TODO 异常
		}

		if (rtCtx.getTaskControl().getPageAction() == PageActionEnum.Advance
				.intValue()) {
			if (StringUtils.isNotBlank(taskInstance.getTargetActivityId())) {
				targetActivityInstance = (IActivityInstance) netInstance
						.getWFElementInstance(taskInstance
								.getTargetActivityId());
			}

			IActivityInstance activityInstance = (IActivityInstance) obj;
			Activity activity = activityInstance.getActivity();
			// 判断回调方式是本地函数的还是远程的restful服务
			if (activity.getCallBackStatus().intValue() == ActivityCallBackEnum.local) {// 本地函数

			} else if (activity.getCallBackStatus().intValue() == ActivityCallBackEnum.remote) {// 远程的restful服务
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("entityId", taskInstance
						.getAliveProcessInstance().getEntityId()));
				params.add(new BasicNameValuePair("entityType", taskInstance
						.getAliveProcessInstance().getEntityType()));
				params.add(new BasicNameValuePair("jdtype", taskInstance
						.getAliveProcessInstance().getJdtype()));
				params.add(new BasicNameValuePair("userId", this.rtCtx
						.getUserInfo().getId()));
				params.add(new BasicNameValuePair("userName", this.rtCtx
						.getUserInfo().getName()));
				params.add(new BasicNameValuePair("xmid", taskInstance
						.getAliveProcessInstance().getXmid()));
				String myVar = HttpClientUtil.creatMyVar(processInstance
						.getProcessInstanceVariables());
				params.add(new BasicNameValuePair("myVar", myVar));
				String resturl = SysConfigUtil.getString("resturl");
				// if(rtCtx.getTaskControl().getPageAction() ==
				// PageActionEnum.Back.intValue()){
				HttpClientUtil.executeCallBack(
						resturl + activity.getRestfulAddress(), params);
				// }
			}
		} else {
			if (StringUtils.isNotBlank(taskInstance.getTargetActivityId())) {
				targetActivityInstance = (IActivityInstance) netInstance
						.getWFElementInstance(taskInstance
								.getTargetActivityId());
			}

			IActivityInstance activityInstance = (IActivityInstance) obj;
			Activity activity = activityInstance.getActivity();
			// 判断回调方式是本地函数的还是远程的restful服务
			if (activity.getCallBackStatus().intValue() == ActivityCallBackEnum.local) {// 本地函数

			} else if (activity.getCallBackStatus().intValue() == ActivityCallBackEnum.remote) {// 远程的restful服务
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("entityId", taskInstance
						.getAliveProcessInstance().getEntityId()));
				params.add(new BasicNameValuePair("entityType", taskInstance
						.getAliveProcessInstance().getEntityType()));
				params.add(new BasicNameValuePair("jdtype", taskInstance
						.getAliveProcessInstance().getJdtype()));
				params.add(new BasicNameValuePair("userId", this.rtCtx
						.getUserInfo().getId()));
				params.add(new BasicNameValuePair("userName", this.rtCtx
						.getUserInfo().getName()));
				String myVar = HttpClientUtil.creatMyVar(processInstance
						.getProcessInstanceVariables());
				params.add(new BasicNameValuePair("myVar", myVar));
				String resturl = SysConfigUtil.getString("resturl");
				// if(rtCtx.getTaskControl().getPageAction() ==
				// PageActionEnum.Back.intValue()){
				HttpClientUtil.executeCallBack(
						resturl + activity.getRestfulAddress(), params);
				// }
			}
		}

		token.setProcessInstance(processInstance);
		((IActivityInstance) obj).complete(token.cloneToken(),
				targetActivityInstance);

	}

	private boolean activityInstanceCanBeCompleted(ITaskInstance taskInstance) {

		IPersistenceService persistenceService = this.rtCtx
				.getPersistenceService();
		Activity thisActivity = taskInstance.getActivity();

		List<ITaskInstance> taskInstanceList = persistenceService
				.getTaskInstancesForProcessInstance(
						taskInstance.getProcessInstanceId(),
						thisActivity.getId());
		if (taskInstanceList == null
				|| taskInstanceList.size() < thisActivity.getTasks().size()) {
			return false;
		} else {
			return true;
		}

	}

	private boolean taskInstanceCanBeCompleted(IWorkflowSession currentSession,
			RuntimeContext rtCtx2, IProcessInstance processInstance,
			ITaskInstance taskInstance) {

		Task task = taskInstance.getTask();
		String taskInstanceCompletionName = null;
		ITaskInstanceCompletion taskInstanceCompletion = null;

		Integer taskType = task.getTaskType();

		taskInstanceCompletionName = task.getTaskInstanceCompletionEvaluator();
		if (StringUtils.isNotBlank(taskInstanceCompletionName.trim())) {

			// taskInstanceCompletion =
			// PluginFacade.GetTaskInstanceCompletion(taskInstanceCompletionName);
		}

		if (taskInstanceCompletion == null) {
			StartRule startRule = processInstance.getWorkflowProcess()
					.getStartRule();
			if (startRule != null) {
				if (TaskTypeEnum.form.intValue() == taskType) {
					taskInstanceCompletionName = startRule
							.getFormTaskInstanceCompletionEvaluator();
				} else if (TaskTypeEnum.tool.intValue() == taskType) {
					taskInstanceCompletionName = startRule
							.getToolTaskInstanceCompletionEvaluator();
				} else if (TaskTypeEnum.subflow.intValue() == taskType) {
					taskInstanceCompletionName = startRule
							.getSubflowTaskInstanceCompletionEvaluator();
				}
				if (StringUtils.isNotBlank(taskInstanceCompletionName)) {
					// taskInstanceCompletion =
					// PluginFacade.GetTaskInstanceCompletion(taskInstanceCompletionName);
				}
			}

		}

		if (taskInstanceCompletion == null) {
			if (TaskTypeEnum.form.intValue() == taskType) {
				taskInstanceCompletion = this.DefaultFormTaskInstanceCompletion;
			} else if (TaskTypeEnum.tool.intValue() == taskType) {
				taskInstanceCompletion = this.DefaultToolTaskInstanceCompletion;
			} else if (TaskTypeEnum.subflow.intValue() == taskType) {
				taskInstanceCompletion = this.DefaultToolTaskInstanceCompletion;
			}
		}
		if (taskInstanceCompletion != null) {
			return taskInstanceCompletion.taskInstanceCanBeCompleted(
					currentSession, rtCtx2, processInstance, taskInstance);
		} else {
			// TODO 异常

		}
		return true;

	}

	@Override
	public void createWorkItem(IWorkflowSession currentSession,
			IProcessInstance processInstance, ITaskInstance taskInstance,
			FormTask formTask, HashMap<String, String> users) {
		IPersistenceService persistenceService = this.rtCtx
				.getPersistenceService();

		String isToBeRead = "";

		if (null != formTask && null != formTask.getAdvanceRule()) {
			if (formTask.getAdvanceRule().isToBeRead()) {
				isToBeRead = "1"; // 1: 待办数据是待阅状态 空：正常审批状态；
			}
		}

		for (Entry<String, String> item : users.entrySet()) {
			WorkItemInstance wi = new WorkItemInstance();
			wi.setProcessInstanceId(processInstance.getId());
			wi.setProcessInstance(processInstance);
			wi.setTaskInstanceId(taskInstance.getId());
			wi.setTaskInstance(taskInstance);

			wi.setState(WorkItemInstanceStateEnum.initialized);
			wi.setCreatedTime(DateUtil.getSysDate());
			wi.setRuntimeContext(this.rtCtx);
			wi.setCurrentWorkflowSession(currentSession);

			wi.setActorId(item.getKey());
			wi.setActorName(item.getValue());
			wi.setXmid(processInstance.getXmid());
			wi.setJdtype(processInstance.getJdtype());
			wi.setFormUrl(this.getFormUrl(processInstance.getWorkflowProcess(),
					taskInstance.getActivityId()));
			wi.setIsToBeRead(isToBeRead);// 1: 待办数据是待阅状态 空：正常审批状态；
			String todoDescription = this.rtCtx.getWorkflowProcess()
					.getToDoDescription();

			FreemarkerUtil freemarkerUtil = new FreemarkerUtil();
			wi.setToDoDescription(freemarkerUtil.getContent(todoDescription,
					processInstance.getProcessInstanceVariables()));

			wi.setBatchApproval(formTask.getUserRule().getBatchApproval());// 是否允许批量审批
			persistenceService.saveOrUpdateWorkItemInstance(wi);

		}

	}

	private String getFormUrl(WorkflowProcess workflowProcess, String activityId) {
		Activity activity = (Activity) workflowProcess
				.findWFElementById(activityId);
		List<Task> tasks = activity.getInlineTasks();
		if (tasks != null && tasks.size() == 1) {
			if (tasks.get(0) instanceof FormTask) {
				FormTask formTask = (FormTask) tasks.get(0);
				return formTask.getUserRule().getEditForm().getUrl();
			}
		}
		return null;
	}

	@Override
	public IWorkItemInstance claimWorkItem(IWorkItemInstance workItem) {
		if (workItem == null)
			return null;

		IPersistenceService persistenceService = this.rtCtx
				.getPersistenceService();

		if (workItem.getState() != WorkItemInstanceStateEnum.initialized
				.intValue()) {
			// TODO 异常
		}

		((IWorkItemInstance) workItem)
				.setState(WorkItemInstanceStateEnum.running);
		((IWorkItemInstance) workItem).setClaimedTime(DateUtil.getSysDate());

		persistenceService.saveOrUpdateWorkItemInstance(workItem);

		// 1、如果不是会签，则删除其他的workitem
		if (FormTaskEnum.any.intValue() == workItem.getTaskInstance()
				.getAssignmentStrategyEnum()) {
			persistenceService.deleteOtherWorkItemsById(workItem);
		}

		return workItem;
	}

	@Override
	public void completeWorkItem(IWorkItemInstance workItem,
			String targetActivityId, String comments) throws EngineException {
		if (workItem.getState() != WorkItemInstanceStateEnum.running.intValue()) {
			// TODO异常
		}

		IPersistenceService persistenceService = this.rtCtx
				.getPersistenceService();

		((IWorkItemInstance) workItem).setComments(comments);
		((IWorkItemInstance) workItem)
				.setState(WorkItemInstanceStateEnum.completed);
		((IWorkItemInstance) workItem).setEndTime(DateUtil.getSysDate());

		persistenceService.saveOrUpdateWorkItemInstance(workItem);

		IActivityInstance targetActivityInstance = null;
		if (StringUtils.isNotBlank(targetActivityId)) {
			INetInstance netInstance = this.rtCtx.getKernelManager()
					.getNetInstance(
							workItem.getProcessInstance().getProcessId(),
							workItem.getProcessInstance().getVersion());
			targetActivityInstance = (IActivityInstance) netInstance
					.getWFElementInstance(targetActivityId);
		}

		((TaskInstance) workItem.getTaskInstance())
				.complete(targetActivityInstance);
	}
}
