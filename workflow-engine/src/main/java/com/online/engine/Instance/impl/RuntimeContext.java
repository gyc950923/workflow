package com.online.engine.Instance.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.pluginBusiness.IBusinessService;
import com.online.engine.pluginConditionResolver.IConditionResolver;
import com.online.engine.pluginDefinition.IDefinitionService;
import com.online.engine.pluginKernel.IKernelManager;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.engine.pluginPersistence.impl.PersistenceService;
import com.online.engine.pluginTaskInstanceManger.ITaskInstanceManger;
import com.online.engine.resource.PromptControl;
import com.online.engine.resource.TaskControl;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.enums.ForwardMode;
import com.online.workflow.process.resources.UserInfo;

public class RuntimeContext {
	private boolean isEnableTrace;
	private boolean isCacheData=false;
	private String identify = null;
	private UserInfo userInfo = null;
	private Integer forwardMode=ForwardMode.advance;
	private PromptControl promptControl = null;
	private TaskControl taskControl = new TaskControl();
	private IDefinitionService definitionService = null;
	private WorkflowProcess workflowProcess = null;
	private IBusinessService businessService = null;
	private ITaskInstanceManger taskInstanceManager = null;
	private IKernelManager kernelManager = null;
	private IConditionResolver conditionResolver = null;
	private PersistenceService persistenceService = null;
	private IWorkflowSession workflowSession = null;
	private List<Query> listCmd = null;
	private HashMap<String, Object> cacheData = null;
	
	private HashMap<String, String> actors = null;//接收外部动态指定的处理人集合

	public RuntimeContext() {
		setIdentify(UUID.randomUUID().toString());
		listCmd = new ArrayList<Query>();
		cacheData = new HashMap<String, Object>();
	}

	

	public IConditionResolver getConditionResolver() {
		return conditionResolver;
	}

	public IWorkflowSession getWorkflowSession() {
		if (workflowSession != null) {
			return workflowSession;
		} else {
			workflowSession = new WorkflowSession(this);
			return workflowSession;
		}

	}

	public void setWorkflowSession(IWorkflowSession workflowSession) {
		this.workflowSession = workflowSession;
	}

	public WorkflowProcess getWorkflowProcess(String processId, Integer version) {
		/*if (workflowProcess == null) {*/
			WorkflowDefinition def = this.getDefinitionService()
					.getWorkflowDefinitionByProcessIdAndVersionNumber(
							processId, version);
			workflowProcess = def.getWorkflowProcess();
		/*}*/

		return workflowProcess;
	}

	public WorkflowProcess getWorkflowProcess(String flowId) {
		if (workflowProcess == null) {
			WorkflowDefinition def = this.getDefinitionService()
					.getWorkflowDefinitionByDefId(flowId);
			workflowProcess = def.getWorkflowProcess();
		}

		return workflowProcess;
	}

	public Integer getForwardMode() {
		return forwardMode;
	}

	public void setForwardMode(Integer forwardMode) {
		this.forwardMode = forwardMode;
	}

	public boolean isEnableTrace() {
		return isEnableTrace;
	}

	public void setEnableTrace(boolean isEnableTrace) {
		this.isEnableTrace = isEnableTrace;
	}

	public boolean isCacheData() {
		return isCacheData;
	}

	public void setCacheData(boolean isCacheData) {
		this.isCacheData = isCacheData;
	}

	public PromptControl getPromptControl() {
		return promptControl;
	}

	public void setPromptControl(PromptControl promptControl) {
		this.promptControl = promptControl;
	}

	public TaskControl getTaskControl() {
		return taskControl;
	}

	public void setTaskControl(TaskControl taskControl) {
		this.taskControl = taskControl;
	}

	public ITaskInstanceManger getTaskInstanceManager() {
		return taskInstanceManager;
	}

	public void setTaskInstanceManager(ITaskInstanceManger taskInstanceManager) {
		this.taskInstanceManager = taskInstanceManager;
		if (taskInstanceManager instanceof IRuntimeContextAware) {
			taskInstanceManager.setRuntimeContext(this);
		}
	}

	public IKernelManager getKernelManager() {
		return kernelManager;
	}

	public void setKernelManager(IKernelManager kernelManager) {
		this.kernelManager = kernelManager;
		if (kernelManager instanceof IRuntimeContextAware) {
			kernelManager.setRuntimeContext(this);
		}
	}

	public IDefinitionService getDefinitionService() {
		return definitionService;
	}

	public void setDefinitionService(IDefinitionService definitionService) {
		this.definitionService = definitionService;
		if (definitionService instanceof IRuntimeContextAware) {
			((IRuntimeContextAware) definitionService).setRuntimeContext(this);;
		}
	}

	public IBusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(IBusinessService businessService) {
		this.businessService = businessService;
		if (businessService instanceof IRuntimeContextAware) {
			businessService.setRuntimeContext(this);
		}

	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
		if (persistenceService instanceof IPersistenceService) {
			persistenceService.setRuntimeContext(this);
		}
	}

	public HashMap<String, Object> getCacheData() {
		return cacheData;
	}

	public void setCacheData(HashMap<String, Object> cacheData) {
		this.cacheData = cacheData;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public List<Query> getListCmd() {
		return listCmd;
	}

	public void setListCmd(List<Query> listCmd) {
		this.listCmd = listCmd;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public void setConditionResolver(IConditionResolver conditionResolver) {
		this.conditionResolver = conditionResolver;
		if (conditionResolver instanceof IConditionResolver) {
			conditionResolver.setRuntimeContext(this);
		}
	}

	public void setWorkflowProcess(WorkflowProcess wfProcess) {
		this.workflowProcess=wfProcess;
	}

    public WorkflowProcess getWorkflowProcess() {
        return this.workflowProcess;
    }

	public HashMap<String, String> getActors() {
		return actors;
	}


	public void setActors(HashMap<String, String> actors) {
		this.actors = actors;
	}
    
}
