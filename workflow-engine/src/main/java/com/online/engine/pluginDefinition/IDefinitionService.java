package com.online.engine.pluginDefinition;

import java.util.List;

import com.online.engine.Instance.IRuntimeContextAware;
import com.online.workflow.process.WorkflowDefinition;

public interface IDefinitionService {

	 WorkflowDefinition saveOrUpdateWorkflowDefinition(WorkflowDefinition workflowDef, Boolean isInsert);
     WorkflowDefinition getWorkflowDefinitionByProcessIdAndVersionNumber(String processId, Integer version);
     WorkflowDefinition getTheLatestVersionOfWorkflowDefinition(String processId);
     WorkflowDefinition getWorkflowDefinitionByDefId(String wfDefinitionId);
     /// <summary>
     /// 获取所有定义列表
     /// </summary>
     /// <returns></returns>
     List<WorkflowDefinition> getWorkflowDefinitions();

}
