package com.online.engine.Instance;

import java.util.Date;

import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.exception.EngineException;

public interface IWorkItemInstance {

	public String getId();

	public void setId(String id);
	
	public String getXmid();

	public void setXmid(String xmid);
    
	public String getJdtype();
	
	public String getIsToBeRead();

	public void setJdtype(String jdtype);
	
	public String getFlowshort();
	
	public void setFlowshort(String flowshort);
	 
	public Integer getState();

	public void setState(Integer state);

	public Date getCreatedTime();

	public void setCreatedTime(Date createdTime);

	public Date getClaimedTime();

	public void setClaimedTime(Date claimedTime);

	public Date getEndTime();

	public void setEndTime(Date endTime);

	public String getActorId();

	public void setActorId(String actorId);

	public String getActorName();

	public void setActorName(String actorName);

	public String getPost();

	public void setPost(String post);

	public String getVoteItem();

	public void setVoteItem(String voteItem);

	public String getComments();

	public void setComments(String comments);

	public String getOrgId();

	public void setOrgId(String orgId);

	public RuntimeContext getRuntimeContext();

	public void setRuntimeContext(RuntimeContext runtimeContext);

	public void setCurrentWorkflowSession(IWorkflowSession currentWorkflowSession);

	public IProcessInstance getProcessInstance();

	public ITaskInstance getTaskInstance();

	public IWorkItemInstance claim();

	public IWorkItemInstance vote(String voteItem);

	public void reject();

	public void reject(String comments);

	public void jumpTo(String targetActivityId, String comments, String isToBeRead) throws EngineException;

	public void complete() throws EngineException;

	public void complete(String comments) throws EngineException;

	public String getTaskInstanceId();

	public String getProcessInstanceId();

	public String getToDoDescription();
	
	public String getFormUrl();
	
	public Integer getCompeleteMode();

	public void setCompeleteMode(Integer compeleteMode);
	
	public Boolean getBatchApproval();
}
