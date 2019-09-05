package com.online.engine.Instance;

import java.util.Date;

import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.IActivityInstance;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.tasks.Task;

public interface ITaskInstance {
	String getId();

	void setId(String id);

	Integer getState();

	Boolean getIsTermination();

	Boolean getIsSuspend();

	String getProcessInstanceId();

	String getActivityId();

	IProcessInstance getAliveProcessInstance();

	Integer getStepNumber();

	String getTargetActivityId();

	Integer getTaskTypeEnum();

	void setTaskTypeEnum(Integer taskTypeEnum);

	Activity getActivity();

	Integer getAssignmentStrategyEnum();

	Task getTask();

	Integer getForwardMode();

	void setForwardMode(Integer forwardMode);

	// / <summary>暂停 </summary>
	void suspend();

	// / <summary>恢复暂停的实例</summary>
	void restoreSuspend();

	// / <summary>任务实例中止！</summary>
	void abort();

	void complete(IActivityInstance targetActivityInstance) throws EngineException;

	String getTaskId();

	void setTaskId(String taskId);

	String getName();

	void setName(String name);

	Date getCreatedTime();

	void setCreatedTime(Date createdTime);

	Date getExpiredTime();

	void setExpiredTime(Date expiredTime);

	Date getStartedTime();

	void setStartedTime(Date startedTime);

	Date getEndTime();

	void setEndTime(Date endTime);

	String getFromActivityId();

	void setFromActivityId(String fromActivityId);

}
