package com.online.engine.Instance.impl;

import java.util.Date;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IWorkItemInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.IWorkflowSessionAware;
import com.online.engine.enums.WorkItemInstanceStateEnum;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginTaskInstanceManger.ITaskInstanceManger;

public class WorkItemInstance implements IWorkItemInstance,
		IWorkflowSessionAware, IRuntimeContextAware {

	private String id;
	private Integer state = null;
	private Date createdTime;
	private Date claimedTime;
	private Date endTime;
	private String actorId;
	private String actorName;
	private String post;
	private String voteItem;
	private String comments;
	private String orgId;
	private RuntimeContext runtimeContext;
	private IWorkflowSession currentWorkflowSession;
	private String processInstanceId;
	private IProcessInstance processInstance;
	private String taskInstanceId;
	private ITaskInstance taskInstance;
	private String toDoDescription;
	private String formUrl;
	private Integer compeleteMode;
	private Boolean batchApproval;
	private String xmid;
	private String jdtype;
	private String flowshort;
	private String isToBeRead;//1: 待办数据是待阅状态 空：正常审批状态；
	
	
    
	public String getJdtype() {
		return jdtype;
	}

	public void setJdtype(String jdtype) {
		this.jdtype = jdtype;
	}
	
	public String getFlowshort() {
		return flowshort;
	}
	
	public void setFlowshort(String flowshort) {
		this.flowshort = flowshort;
	}
	
	

	public String getXmid() {
		return xmid;
	}

	public void setXmid(String xmid) {
		this.xmid = xmid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getState() {
		return state;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public IProcessInstance getProcessInstance() {

		if (processInstance == null) {
			this.processInstance = runtimeContext.getPersistenceService()
					.getProcessInstanceById(processInstanceId);
		}

		if (this.getRuntimeContext() != null) {
			((IRuntimeContextAware) this.processInstance)
					.setRuntimeContext(this.getRuntimeContext());
		}
		if (this.getCurrentWorkflowSession() != null) {
			((IWorkflowSessionAware) this.processInstance)
					.setCurrentWorkflowSession(this.currentWorkflowSession);
		}

		return processInstance;
	}

	public String getTaskInstanceId() {

		return taskInstanceId;
	}

	public void setTaskInstanceId(String taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	public ITaskInstance getTaskInstance() {

		if (taskInstance == null) {
			if(null == runtimeContext) {
				return null;
			}
			this.taskInstance = runtimeContext.getPersistenceService()
					.getTaskInstanceByWorkItemId(this.getId());

			if (this.getRuntimeContext() != null) {
				((IRuntimeContextAware) taskInstance).setRuntimeContext(this
						.getRuntimeContext());
			}
			if (this.getCurrentWorkflowSession() != null) {
				((IWorkflowSessionAware) taskInstance)
						.setCurrentWorkflowSession(this.currentWorkflowSession);
			}
		}
		return taskInstance;
	}

	public void setTaskInstance(ITaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}

	public void setProcessInstance(IProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getClaimedTime() {
		return claimedTime;
	}

	public void setClaimedTime(Date claimedTime) {
		this.claimedTime = claimedTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getVoteItem() {
		return voteItem;
	}

	public void setVoteItem(String voteItem) {
		this.voteItem = voteItem;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

	public void setRuntimeContext(RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}

	public IWorkflowSession getCurrentWorkflowSession() {
		return currentWorkflowSession;
	}

	public void setCurrentWorkflowSession(
			IWorkflowSession currentWorkflowSession) {
		this.currentWorkflowSession = currentWorkflowSession;
	}

	@Override
	public IWorkItemInstance claim() {

		ITaskInstanceManger taskInstanceMgr = getRuntimeContext()
				.getTaskInstanceManager();
		IWorkItemInstance newWorkItem = taskInstanceMgr.claimWorkItem(this);

		if (newWorkItem == null) {
			this.setState(WorkItemInstanceStateEnum.canceled);
		}
		return newWorkItem;
	}

	@Override
	public IWorkItemInstance vote(String voteItem) {

		return null;
	}

	@Override
	public void reject() {

	}

	@Override
	public void reject(String comments) {

	}

	@Override
	public void jumpTo(String targetActivityId, String comments, String isToBeRead) throws EngineException {
		ITaskInstanceManger taskInstanceMgr = this.getRuntimeContext()
				.getTaskInstanceManager();
		this.isToBeRead=isToBeRead;
		taskInstanceMgr.completeWorkItem(this, targetActivityId, comments);
	}

	@Override
	public void complete() throws EngineException {
		complete("");
	}

	@Override
	public void complete(String comments) throws EngineException {
		if (this.getState() != WorkItemInstanceStateEnum.running.intValue()) {
			// TODO 异常
		}

		ITaskInstanceManger taskInstanceManager = this.getRuntimeContext()
				.getTaskInstanceManager();
		taskInstanceManager.completeWorkItem(this, null, comments);

	}

    public String getToDoDescription() {
        return toDoDescription;
    }

    public void setToDoDescription(String toDoDescription) {
        this.toDoDescription = toDoDescription;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

	public Integer getCompeleteMode() {
		return compeleteMode;
	}

	public void setCompeleteMode(Integer compeleteMode) {
		this.compeleteMode = compeleteMode;
	}

	public Boolean getBatchApproval() {
		return batchApproval;
	}

	public void setBatchApproval(Boolean batchApproval) {
		this.batchApproval = batchApproval;
	}

	public String getIsToBeRead() {
		return isToBeRead;
	}

	public void setIsToBeRead(String isToBeRead) {
		this.isToBeRead = isToBeRead;
	}

}
