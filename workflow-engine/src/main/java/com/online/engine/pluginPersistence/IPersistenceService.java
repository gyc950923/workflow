package com.online.engine.pluginPersistence;

import java.util.Map;

import org.hibernate.Query;

import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.impl.ProcessInstance;
import com.online.engine.pluginDefinition.IDefinitionService;
import com.online.engine.pluginKernel.impl.EndNodeInstance;

public interface IPersistenceService extends IRuntimeContextAware,
		IDefinitionService, IProcessInstanceService,
		IProcessInstanceVarService, ITaskInstanceService,
		ITokenInstanceService, IProcessInstanceTrace, IWorkItemInstanceService,
		ITodoInstanceService {

	void initMemoryWfData(String processInstanceId);

	Boolean execSql();

	public void addCommand(Query query);

	public void updateWfProcessInstance(EndNodeInstance endNodeInstance);

	public ProcessInstance getProcessInstanceEntity(String processInstanceId);
	
	public Map<String, Object> rollbackAuditInfo(IWorkflowSession workflowSession);
}
