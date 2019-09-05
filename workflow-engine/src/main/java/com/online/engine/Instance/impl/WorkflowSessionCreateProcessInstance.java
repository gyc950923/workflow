package com.online.engine.Instance.impl;

import com.online.engine.Instance.IWorkflowSessionCallback;
import com.online.engine.enums.ProcessInstanceStateEnum;
import com.online.workflow.common.DateUtil;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.resources.UserInfo;

public class WorkflowSessionCreateProcessInstance implements IWorkflowSessionCallback {

	private UserInfo userInfo;
	private WorkflowProcess wfProcess;
	private WorkflowDefinition workflowDef;
	private String parentProcessInstanceId;
	private String parentTaskInstanceId;
	private String entityType;
	private String entityId;
	private String xmid;
	private String jdtype;
	private String flowshort;

	public WorkflowSessionCreateProcessInstance(UserInfo userInfo,
			WorkflowProcess wfProcess, WorkflowDefinition workflowDef,
			String entityType, String entityId, String parentProcessInstanceId,
			String parentTaskInstanceId,String xmid,String jdtype, String flowshort) {

		this.userInfo = userInfo;
		this.wfProcess = wfProcess;
		this.workflowDef = workflowDef;
		this.entityType = entityType;
		this.entityId = entityId;
		this.parentProcessInstanceId = parentProcessInstanceId;
		this.parentTaskInstanceId = parentTaskInstanceId;
		this.xmid = xmid;
		this.jdtype = jdtype;
		this.flowshort = flowshort;

	}

	public Object DoInWorkflowSession(RuntimeContext runtimeContext) {

		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setName(wfProcess.getName()); 
		processInstance.setCreatorId(userInfo.getId());
		processInstance.setCreatorName(userInfo.getName());
		processInstance.setProcessId(workflowDef.getProcessId()); 
		processInstance.setVersion(workflowDef.getVersion());
		processInstance.setState(ProcessInstanceStateEnum.running); 
		processInstance.setCreatedTime(DateUtil.getSysDate());
		processInstance.setEntityType(entityType);
		processInstance.setEntityId(entityId); 
		processInstance.setXmid(xmid);
		processInstance.setJdtype(jdtype);
		processInstance.setFlowshort(flowshort);
		processInstance.setProcessDescription(wfProcess.getDescription());
		processInstance.setParentProcessInstanceId(parentProcessInstanceId);
		processInstance.setParentTaskInstanceId(parentTaskInstanceId);
		processInstance.setOrgId(userInfo.getOrgId());
		return processInstance;
	}
}
