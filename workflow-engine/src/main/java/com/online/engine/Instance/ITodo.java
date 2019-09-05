package com.online.engine.Instance;

import java.util.Date;

public interface ITodo {

	public String getWorkItemId();
	public void setWorkItemId(String workItemId);
	public String getActorId() ;
	public void setActorId(String actorId);
	public String getPost() ;
	public void setPost(String post);
	public Integer getWorkItemState() ;
	public void setWorkItemState(Integer workItemState) ;
	public Date getCreatedTime();
	public void setCreatedTime(Date createdTime);
	public Date getClaimedTime() ;
	public void setClaimedTime(Date claimedTime);
	public Date getEndTime();
	public void setEndTime(Date endTime);
	public String getComments() ;
	public void setComments(String comments) ;
	public String getTaskInstanceId() ;
	public void setTaskInstanceId(String taskInstanceId) ;
	public String getTaskInstanceName() ;
	public void setTaskInstanceName(String taskInstanceName) ;
	public String getActivityId();
	public void setActivityId(String activityId);
	public String getActivityDisplayname() ;
	public void setActivityDisplayname(String activityDisplayname);
	public String getTaskId() ;
	public void setTaskId(String taskId) ;
	public Integer getStepNumber() ;
	public void setStepNumber(Integer stepNumber);
	public String getProcessInstanceId() ;
	public void setProcessInstanceId(String processInstanceId);
	public Integer getProcessInstanceState() ;
	public void setProcessInstanceState(Integer processInstanceState) ;
	public String getProcessId() ;
	public void setProcessId(String processId) ;
	public Boolean getIsSuspended() ;
	public void setIsSuspended(Boolean isSuspended) ;
	public Boolean getIsTermination() ;
	public void setIsTermination(Boolean isTermination);
	public String getCreatorId() ;
	public void setCreatorId(String creatorId) ;

	public String getOrgId() ;
	public void setOrgId(String orgId) ;
	public String getFormUrl() ;
}
