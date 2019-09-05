package com.online.engine.Instance.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IWorkItemInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.IWorkflowSessionAware;
import com.online.engine.Instance.IWorkflowSessionCallback;
import com.online.engine.common.FreemarkerUtil;
import com.online.engine.enums.ProcessInstanceStateEnum;
import com.online.engine.enums.WorkItemInstanceStateEnum;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.workflow.common.DateUtil;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.process.tasks.FormTask;

public class WorkflowSession implements IWorkflowSession {

	private RuntimeContext runtimeContext;

	public WorkflowSession(RuntimeContext ctx) {
		this.runtimeContext = ctx;
	}

	public void setRuntimeContext(RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}

	@Override
	public RuntimeContext getRuntimeContext() {

		return runtimeContext;
	}

	@Override
	public void initMemoryWfData(String processInstanceId) {

		IPersistenceService persistenceService = getRuntimeContext()
				.getPersistenceService();
		persistenceService.initMemoryWfData(processInstanceId);
	}

	@Override
	public IProcessInstance createProcessInstance(String processId,
			String entityType, String entityId, UserInfo userInfo,
			HashMap<String, Object> var,String xmid,String jdtype, String flowshort) {

		return _createProcessInstance(processId, userInfo, entityType,
				entityId, null, null, var,xmid,jdtype, flowshort);
	}

	protected IProcessInstance _createProcessInstance(String processId,
			UserInfo userInfo, String entityType, String entityId,
			String parentProcessInstanceId, String parentTaskInstanceId,
			HashMap<String, Object> var,String xmid,String jdtype, String flowshort) {

		WorkflowDefinition workflowDef = runtimeContext.getDefinitionService()
				.getTheLatestVersionOfWorkflowDefinition(processId);

		WorkflowProcess wfProcess = workflowDef.getWorkflowProcess();
		if (wfProcess == null) {
			try {
				throw new Exception("工作流没找到,id=[" + processId + "]");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.runtimeContext.setWorkflowProcess(wfProcess);
		IProcessInstance processInstance = (IProcessInstance) this
				.executeTemplate(new WorkflowSessionCreateProcessInstance(
						userInfo, wfProcess, workflowDef, entityType, entityId,
						parentProcessInstanceId, parentTaskInstanceId,xmid,jdtype, flowshort));
		//替换流程描述中的模板变量
		FreemarkerUtil freemarkerUtil = new FreemarkerUtil();
		String processDescription = freemarkerUtil.getContent(processInstance.getProcessDescription(), var);
		processInstance.setProcessDescription(processDescription);
		// 保存流程实例
		runtimeContext.getPersistenceService().saveOrUpdateProcessInstance(
				processInstance);
		//保存流程变量
		processInstance.setProcessInstanceVariables(var);
		
		return processInstance;
	}



	@Override
	public Boolean execSql() {

		boolean isReslt = runtimeContext.getPersistenceService().execSql();
		runtimeContext.getListCmd().clear();
		return isReslt;
	}

	@Override
	public List<IWorkItemInstance> getWorkItemsByProcessInstId(
			String processInstanceId) {

		List<IWorkItemInstance> lstInstances = runtimeContext
				.getPersistenceService().getWorkItemsByProcessInstId(
						processInstanceId);

		for (IWorkItemInstance iWorkItemInstance : lstInstances) {
			iWorkItemInstance.setCurrentWorkflowSession(this);
			iWorkItemInstance.setRuntimeContext(this.runtimeContext);
		}
		return lstInstances;
	}

	@Override
	public Object executeTemplate(IWorkflowSessionCallback calback) {

		Object result = calback.DoInWorkflowSession(this.runtimeContext);
		if (result != null) {
			if (result instanceof IRuntimeContextAware) {
				((IRuntimeContextAware) result)
						.setRuntimeContext(this.runtimeContext);
			}
			if (result instanceof IWorkflowSessionAware) {
				((IWorkflowSessionAware) result)
						.setCurrentWorkflowSession(this);
			}
			if (result instanceof List) {
				List l = (List) result;
				for (int i = 0; i < l.size(); i++) {
					Object item = l.get(i);
					if (item instanceof IRuntimeContextAware) {
						((IRuntimeContextAware) item)
								.setRuntimeContext(runtimeContext);
						if (item instanceof IWorkflowSessionAware) {
							((IWorkflowSessionAware) item)
									.setCurrentWorkflowSession(this);
						}
					} else {
						break;
					}
				}

			}

		}

		return result;

	}

	@Override
	public List<ITaskInstance> getTaskInstancesForProcessInstance(
			String processInstanceId, String activityId) {

		return null;
	}

	@Override
	public IWorkItemInstance getWorkItemsById(String workItemId) {

		IPersistenceService persistenceService = this.runtimeContext
				.getPersistenceService();
		WorkItemInstance workItemInstance = (WorkItemInstance) persistenceService
				.getWorkItemById(workItemId);
		workItemInstance.setCurrentWorkflowSession(this);
		workItemInstance.setRuntimeContext(runtimeContext);
		return workItemInstance;
	}

	@Override
	public IWorkItemInstance claimWorkItem(String workItemId) {

		IPersistenceService persistenceService = this.runtimeContext
				.getPersistenceService();
		IWorkItemInstance workItem = persistenceService
				.getWorkItemById(workItemId);
		return workItem.claim();
	}

	@Override
	public void completeWorkItem(String workItemId, String comments) throws EngineException {

		IWorkItemInstance IWorkItemInstance = getWorkItemsById(workItemId);
		if (IWorkItemInstance.getState() == WorkItemInstanceStateEnum.initialized.intValue())
			IWorkItemInstance.claim();
		IWorkItemInstance.complete(comments);
	}

	@Override
	public void backByWorkItemId(IWorkItemInstance wi, String targetActivityId, String comments) throws EngineException {
		if (wi.getState() == WorkItemInstanceStateEnum.initialized.intValue())
			wi.claim();
		
		String isToBeRead = "";

		List<IWorkItemInstance> workItems = runtimeContext
				.getPersistenceService()
				.getNeedDoWorkItemsByProcessInstIdAndTaskInstId(
						wi.getTaskInstanceId(), wi.getProcessInstanceId());

		IWorkItemInstance workItem = null;

		for (IWorkItemInstance iWorkItemInstance : workItems) {
			if (iWorkItemInstance.getId() == wi.getId()) {
				workItem = iWorkItemInstance;
				break;
			}
		}

		if (workItem != null) {
			workItems.remove(workItem);
		}

		for (IWorkItemInstance item : workItems) {
			if (item instanceof IRuntimeContextAware)
				((IRuntimeContextAware) item).setRuntimeContext(runtimeContext);

			if (item instanceof IWorkflowSessionAware)
				((IWorkflowSessionAware) item).setCurrentWorkflowSession(this);

			item.setState(WorkItemInstanceStateEnum.running);
			item.complete();//Todo
		}

		if (StringUtils.isBlank(targetActivityId)) {
	        Activity activity =  wi.getTaskInstance().getActivity();
	        FormTask task = (FormTask)activity.getInlineTasks().get(0);
	        targetActivityId = task.getBackRule().getBackRange().getNodeId();
	        
	        if(null != task && null != task.getAdvanceRule()) {
	        	if(task.getAdvanceRule().isToBeRead()){
	        		isToBeRead = "1"; //当isToBeRead为true时，说明是待阅单据
	        	}
	        }
	        
		    if (StringUtils.isBlank(targetActivityId)) {
		        ITaskInstance taskInstance = getUpTaskInstance(
                        wi.getTaskInstanceId(), wi.getProcessInstanceId());
                targetActivityId = taskInstance.getActivityId(); 
            }
			
		}
		wi.jumpTo(targetActivityId, comments, isToBeRead);
	}

	public ITaskInstance getUpTaskInstance(String taskInstanceId,
			String processInstanceId) {
		IPersistenceService persistenceService = this.runtimeContext
				.getPersistenceService();
		return persistenceService.getUpTaskInstance(taskInstanceId,
				processInstanceId);
	}

    @Override
    public void terminationWorkItem(IWorkItemInstance workItemInstance, String comments) throws EngineException {
    	if (workItemInstance.getState() == WorkItemInstanceStateEnum.initialized.intValue())
			workItemInstance.claim();

		List<IWorkItemInstance> workItems = runtimeContext
				.getPersistenceService()
				.getNeedDoWorkItemsByProcessInstIdAndTaskInstId(
						workItemInstance.getTaskInstanceId(), workItemInstance.getProcessInstanceId());

		IWorkItemInstance workItem = null;

		for (IWorkItemInstance iWorkItemInstance : workItems) {
			if (iWorkItemInstance.getId() == workItemInstance.getId()) {
				workItem = iWorkItemInstance;
				break;
			}
		}

		if (workItem != null) {
			workItems.remove(workItem);
		}

		for (IWorkItemInstance item : workItems) {
			if (item instanceof IRuntimeContextAware)
				((IRuntimeContextAware) item).setRuntimeContext(runtimeContext);

			if (item instanceof IWorkflowSessionAware)
				((IWorkflowSessionAware) item).setCurrentWorkflowSession(this);

			item.setState(WorkItemInstanceStateEnum.running);
			item.complete();
		}
		
        IProcessInstance processInstance = workItemInstance.getProcessInstance();
        processInstance.setState(ProcessInstanceStateEnum.termination);
        IPersistenceService persistenceService = runtimeContext.getPersistenceService();
        persistenceService.saveOrUpdateProcessInstance(processInstance);
        workItemInstance.setState(WorkItemInstanceStateEnum.termination);
        workItemInstance.setComments(comments);
        workItemInstance.setCompeleteMode(WorkItemInstanceStateEnum.termination);
        workItemInstance.setEndTime(DateUtil.getSysDate());
        persistenceService.saveOrUpdateWorkItemInstance(workItemInstance);
    }
}
