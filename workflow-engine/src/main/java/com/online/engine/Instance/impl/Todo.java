package com.online.engine.Instance.impl;

import java.util.Date;

import com.online.engine.Instance.ITodo;

public class Todo implements ITodo{

	 private String workItemId;//主键id
     private String actorId;//审批人id
     private String actorName;//审批人名称
     private String post;//提交方式
     private Integer workItemState;//审批状态 参考ProcessInstanceStateEnum类
     private Date createdTime;
     private Date claimedTime;//审批时间
     private Date endTime;
     private String comments;//审批内容
     private String taskInstanceId;
     private String taskInstanceName;
     private String activityId;
     private String activityDisplayname;
     private String taskId;
     private Integer stepNumber;
     private String processInstanceId;
     private Integer processInstanceState;
     private String processId;
     private Boolean isSuspended=false;
     private Boolean isTermination=false;// 表中没有
     private String creatorId;//表中没有
     private String orgId;//表中没有
     private String toDoDescription;
     private String formUrl;
     private String entityId;
     private String entityType;
     private String xmid;
     private String jdtype;
     private String processName;     
     private String isToBeRead;
     private String compeletemode;
     
     
     
	public String getJdtype() {
		return jdtype;
	}
	public void setJdtype(String jdtype) {
		this.jdtype = jdtype;
	}
	public String getXmid() {
		return xmid;
	}
	public void setXmid(String xmid) {
		this.xmid = xmid;
	}
	public String getWorkItemId() {
		return workItemId;
	}
	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}
	public String getActorId() {
		return actorId;
	}
	public void setActorId(String actorId) {
		this.actorId = actorId;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public Integer getWorkItemState() {
		return workItemState;
	}
	public void setWorkItemState(Integer workItemState) {
		this.workItemState = workItemState;
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
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getTaskInstanceId() {
		return taskInstanceId;
	}
	public void setTaskInstanceId(String taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}
	public String getTaskInstanceName() {
		return taskInstanceName;
	}
	public void setTaskInstanceName(String taskInstanceName) {
		this.taskInstanceName = taskInstanceName;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getActivityDisplayname() {
		return activityDisplayname;
	}
	public void setActivityDisplayname(String activityDisplayname) {
		this.activityDisplayname = activityDisplayname;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Integer getStepNumber() {
		return stepNumber;
	}
	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public Integer getProcessInstanceState() {
		return processInstanceState;
	}
	public void setProcessInstanceState(Integer processInstanceState) {
		this.processInstanceState = processInstanceState;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public Boolean getIsSuspended() {
		return isSuspended;
	}
	public void setIsSuspended(Boolean isSuspended) {
		this.isSuspended = isSuspended;
	}
	public Boolean getIsTermination() {
		return isTermination;
	}
	public void setIsTermination(Boolean isTermination) {
		this.isTermination = isTermination;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
    public String getActorName() {
        return actorName;
    }
    public void setActorName(String actorName) {
        this.actorName = actorName;
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
    public String getEntityId() {
        return entityId;
    }
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    public String getEntityType() {
        return entityType;
    }
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
	public String getIsToBeRead() {
		return isToBeRead;
	}
	public void setIsToBeRead(String isToBeRead) {
		this.isToBeRead = isToBeRead;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getCompeletemode() {
		return compeletemode;
	}
	public void setCompeletemode(String compeletemode) {
		this.compeletemode = compeletemode;
	}
    
}
