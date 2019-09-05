package com.online.engine.Instance.impl;

import java.util.Date;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.IWorkflowSessionAware;
import com.online.engine.enums.TaskInstanceStateEnum;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.IActivityInstance;
import com.online.engine.pluginTaskInstanceManger.ITaskInstanceManger;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.enums.AssignmentStrategyEnum;
import com.online.workflow.process.enums.ForwardMode;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.tasks.Task;

public class TaskInstance implements ITaskInstance, IWorkflowSessionAware,
		IRuntimeContextAware {

	private RuntimeContext rcx;
	private String id;
	private String taskId;
	private Task task;
	private String activityId;
	private Activity activity;
	private String name;
	private Integer state = TaskInstanceStateEnum.initialized;
	private Boolean isSuspend = false;
	private Integer taskTypeEnum;
	private String creatorId;
	private Date createdTime;
	private Date startedTime;
	private Date expiredTime;
	private Date endTime;
	private Integer assignmentStrategyEnum = AssignmentStrategyEnum.all;
	private Integer backModeEnum;
	private Integer forwardMode=ForwardMode.advance;
	private String processInstanceId;
	private String targetActivityId;
	private String fromActivityId;
	private Activity fromActivity;
	private Integer stepNumber;
	private Boolean isTermination = false;
	private IProcessInstance aliveProcessInstance;
	private IWorkflowSession currentWorkflowSession;
	private String orgId;

	public RuntimeContext getRcx() {
		return rcx;
	}

	public void setRcx(RuntimeContext rcx) {
		this.rcx = rcx;
	}

	public IWorkflowSession getWorkflowSession() {
		return currentWorkflowSession;
	}

	public void setWorkflowSession(IWorkflowSession workflowSession) {
		this.currentWorkflowSession = workflowSession;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Task getTask() {
		if (this.task == null) {

			WorkflowProcess workflowProcess = rcx.getWorkflowProcess(
					aliveProcessInstance.getProcessId(),
					aliveProcessInstance.getVersion());
			this.task = (Task) workflowProcess.findWFElementById(taskId);
		}
		return this.task;

	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public Activity getActivity() {

		if (activity == null) {
			WorkflowProcess workflowProcess = getRuntimeContext().getWorkflowProcess(getAliveProcessInstance().getProcessId(), getAliveProcessInstance().getVersion());
			activity = (Activity) workflowProcess
					.findWFElementById(this.activityId);
		}
		return activity;

	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsSuspend() {
		return isSuspend;
	}

	public void setIsSuspend(Boolean isSuspend) {
		this.isSuspend = isSuspend;
	}

	public String getCreatorId() {
		return creatorId;
	}
	public Integer getForwardMode() {
		return forwardMode;
	}

	public void setForwardMode(Integer forwardMode) {
		this.forwardMode = forwardMode;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getStartedTime() {
		return startedTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setStartedTime(Date startedTime) {
		this.startedTime = startedTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public Integer getTaskTypeEnum() {
		return taskTypeEnum;
	}

	public void setTaskTypeEnum(Integer taskTypeEnum) {
		this.taskTypeEnum = taskTypeEnum;
	}

	public Integer getAssignmentStrategyEnum() {
		return assignmentStrategyEnum;
	}

	public void setAssignmentStrategyEnum(Integer assignmentStrategyEnum) {
		this.assignmentStrategyEnum = assignmentStrategyEnum;
	}

	public Integer getBackModeEnum() {
		return backModeEnum;
	}

	public void setBackModeEnum(Integer backModeEnum) {
		this.backModeEnum = backModeEnum;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTargetActivityId() {
		return targetActivityId;
	}

	public void setTargetActivityId(String targetActivityId) {
		this.targetActivityId = targetActivityId;
	}

	public String getFromActivityId() {
		return fromActivityId;
	}

	public void setFromActivityId(String fromActivityId) {
		this.fromActivityId = fromActivityId;
	}

	public Activity getFromActivity() {
		return fromActivity;
	}

	public void setFromActivity(Activity fromActivity) {
		this.fromActivity = fromActivity;
	}

	public Integer getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}

	public Boolean getIsTermination() {
		return isTermination;
	}

	public void setIsTermination(Boolean isTermination) {
		this.isTermination = isTermination;
	}

	public IProcessInstance getAliveProcessInstance() {

		if (this.aliveProcessInstance == null) {

			this.aliveProcessInstance = rcx.getPersistenceService()
					.getProcessInstanceById(processInstanceId);

			if (this.rcx != null) {
				((IRuntimeContextAware) this.aliveProcessInstance)
						.setRuntimeContext(rcx);
			}
			if (this.currentWorkflowSession != null) {
				((IWorkflowSessionAware) this.aliveProcessInstance)
						.setCurrentWorkflowSession(this.currentWorkflowSession);
			}

		}
		return this.aliveProcessInstance;

	}

	public void setAliveProcessInstance(IProcessInstance aliveProcessInstance) {
		this.aliveProcessInstance = aliveProcessInstance;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Override
	public void suspend() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restoreSuspend() {
		// TODO Auto-generated method stub

	}

	@Override
	public void abort() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRuntimeContext(RuntimeContext ctx) {
		this.rcx = ctx;
	}

	@Override
	public RuntimeContext getRuntimeContext() {

		return rcx;
	}

	@Override
	public IWorkflowSession getCurrentWorkflowSession() {

		return currentWorkflowSession;
	}

	@Override
	public void setCurrentWorkflowSession(IWorkflowSession workflowSession) {
		this.currentWorkflowSession = workflowSession;
	}

	@Override
	public void complete(IActivityInstance targetActivityInstance) throws EngineException {

		/*
		 * this.activity = ((Activity)
		 * rcx.getWorkflowProcess().findWFElementById(this.activityId));
		 * this.task = ((Task)
		 * rcx.getWorkflowProcess().findWFElementById(this.taskId
		 * ));//此处给task赋值，防止测试过程中出现null
		 */ITaskInstanceManger taskInstanceMgr = this.rcx
				.getTaskInstanceManager();
		taskInstanceMgr.completeTaskInstance(this.getCurrentWorkflowSession(),
				this.getAliveProcessInstance(), this, targetActivityInstance);
	}

}
