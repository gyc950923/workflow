package com.online.engine.pluginDefinition;

import java.util.List;

import org.springframework.stereotype.Service;

import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.workflow.process.WorkflowDefinition;
public class DefinitionService implements IRuntimeContextAware,
		IDefinitionService {

    private RuntimeContext ctx;
    
	@Override
	public WorkflowDefinition saveOrUpdateWorkflowDefinition(
			WorkflowDefinition workflowDef, Boolean isInsert) {
		return ctx.getPersistenceService().saveOrUpdateWorkflowDefinition(workflowDef,
				isInsert);
	}

	@Override
	public WorkflowDefinition getWorkflowDefinitionByProcessIdAndVersionNumber(
			String processId, Integer version) {
		return ctx.getPersistenceService().getWorkflowDefinitionByProcessIdAndVersionNumber(processId,
						version);
	}

	@Override
	public WorkflowDefinition getTheLatestVersionOfWorkflowDefinition(
			String processId) {
		return ctx.getPersistenceService()
				.getTheLatestVersionOfWorkflowDefinition(processId);
	}

	@Override
	public WorkflowDefinition getWorkflowDefinitionByDefId(String wfDefinitionId) {
	    return ctx.getPersistenceService().getWorkflowDefinitionByDefId(wfDefinitionId);
	}

	@Override
	public List<WorkflowDefinition> getWorkflowDefinitions() {
		return ctx.getPersistenceService().getWorkflowDefinitions();
	}

	@Override
    public void setRuntimeContext(RuntimeContext ctx) {

        this.ctx = ctx;
    }

    @Override
    public RuntimeContext getRuntimeContext() {

        return ctx;
    }

	

}
