package com.online.engine.Instance.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.IWorkflowSessionAware;
import com.online.engine.enums.ProcessInstanceEventEnum;
import com.online.engine.enums.ProcessInstanceStateEnum;
import com.online.engine.enums.TablesName;
import com.online.engine.events.INodeInstanceEvent;
import com.online.engine.events.ProcessInstanceEvent;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.IEndNodeInstance;
import com.online.engine.pluginKernel.INetInstance;
import com.online.workflow.common.DateUtil;
import com.online.workflow.process.WorkflowProcess;

public class ProcessInstance implements IProcessInstance,
		IWorkflowSessionAware, IRuntimeContextAware {

	private String id;
	private String processId;
	private Integer version;
	private String name;
	private String displayName;
	private Integer state;
	private Boolean isSuspended = false;
	private String creatorId;
	private String creatorName;
	private Date createdTime;
	private Date startedTime;
	private Date expiredTime;
	private Date endTime;
	private String parentProcessInstanceId;
	private String parentTaskInstanceId;
	private String processDescription;
	private String entityType;
	private String entityId;
	private Integer compeleteMode;
	private String orgId;
	private String xmid;
	private String jdtype;
	private String flowshort;
	private Boolean isTermination = false;
	private HashMap<String, Object> processInstanceVariables;
	private WorkflowProcess workflowProcess;
	private IWorkflowSession workflowSession;
	private RuntimeContext runtimeContext;
	private Integer callstate;//结束节点是否有远程服务接口
	private String restaddress;//结束节点远程服务接口

	@Override
	public void setRuntimeContext(RuntimeContext ctx) {
		runtimeContext = ctx;
	}

	@Override
	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

	@Override
	public IWorkflowSession getCurrentWorkflowSession() {
		return workflowSession;
	}

	@Override
	public void setCurrentWorkflowSession(IWorkflowSession session) {
		this.workflowSession = session;
	}

	@Override
	public void setProcessInstanceVariables(HashMap<String, Object> vars) {
		if (vars == null) {
			processInstanceVariables = new HashMap<String, Object>();
		} else {
			processInstanceVariables = keyToLowerCase(vars);
			List<ProcessInstanceVar> newVars = this.processInstanceVarMapToList(processInstanceVariables);
			List<ProcessInstanceVar> processInstanceVars = runtimeContext.getPersistenceService().getProcessInstanceVariable(this.id);
			
			for(ProcessInstanceVar newVar : newVars){
			    boolean isExist = false;
			    for(ProcessInstanceVar var : processInstanceVars){
			        if (newVar.getName().equals(var.getName())) {
			            isExist = true;
			            var.setValue(newVar.getValue());
			            runtimeContext.getPersistenceService().updateProcessInstanceVariable(var);
			            break;
                    }
			    }
			    if (!isExist) {
			        newVar.setProcessInstanceId(this.id);
			        runtimeContext.getPersistenceService().saveProcessInstanceVariable(newVar);
			        processInstanceVars.add(newVar);
                }
			}
			runtimeContext.getCacheData().put(TablesName.procInstVar, processInstanceVars);
			processInstanceVariables = this.processInstanceVarListToMap(processInstanceVars);
		}

	}

    private HashMap<String, Object> keyToLowerCase(HashMap<String, Object> vars) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(Entry<String,Object> entry : vars.entrySet()){
            if (entry.getValue() instanceof String) {
                map.put(entry.getKey().toLowerCase(), ((String)entry.getValue()).toLowerCase());
            }else{
                map.put(entry.getKey().toLowerCase(), entry.getValue());
            }
        }
        return map;
    }
    
    private List<ProcessInstanceVar> processInstanceVarMapToList(HashMap<String, Object> map){
        List<ProcessInstanceVar> vars = new ArrayList<ProcessInstanceVar>();
        for(Entry<String,Object> entry : map.entrySet()){
            ProcessInstanceVar var = new ProcessInstanceVar();
            var.setName(entry.getKey());
            var.SetProcessInstanceVarValue(entry.getValue());
            vars.add(var);
        }
        return vars;
    }
    
    @SuppressWarnings("unused")
    private HashMap<String, Object> processInstanceVarListToMap(List<ProcessInstanceVar> list) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (ProcessInstanceVar var : list) {
            try {
                map.put(var.getName(), ProcessInstanceVar.GetProcessInstanceVarValue(var.getValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
	public HashMap<String, Object> getProcessInstanceVariables() {
		/*if (processInstanceVariables==null) {*/
			processInstanceVariables=new HashMap<String, Object>();
			List<ProcessInstanceVar> vars = runtimeContext.getPersistenceService().getProcessInstanceVariable(this.id);
			for(int i =0; vars!=null && i<vars.size(); i++){
			    Object obj = null;
                try {
                    obj = ProcessInstanceVar.GetProcessInstanceVarValue(vars.get(i).getValue());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
			    processInstanceVariables.put(vars.get(i).getName(), obj);
			}
		/*}*/
		return processInstanceVariables;
	}
	@Override
	public Object getProcessInstanceVariablesByName(String name) {
	    this.getProcessInstanceVariables();
	    if (processInstanceVariables==null) {
	        processInstanceVariables=new HashMap<String, Object>();
            List<ProcessInstanceVar> vars = runtimeContext.getPersistenceService().getProcessInstanceVariable(this.id);
            for(int i =0; vars!=null && i<vars.size(); i++){
                Object obj = null;
                try {
                    obj = ProcessInstanceVar.GetProcessInstanceVarValue(vars.get(i).getValue());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                processInstanceVariables.put(vars.get(i).getName(), obj);
            }
	    }
		return processInstanceVariables.get(name.toLowerCase());
	}

	
	public String getXmid() {
		return xmid;
	}

	public void setXmid(String xmid) {
		this.xmid = xmid;
	}
	

	public String getJdtype() {
		return jdtype;
	}

	public void setJdtype(String jdtype) {
		this.jdtype = jdtype;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getCompeleteMode() {
		return compeleteMode;
	}

	public void setCompeleteMode(Integer compeleteMode) {
		this.compeleteMode = compeleteMode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Boolean getIsSuspended() {
		return isSuspended;
	}

	public void setIsSuspended(Boolean isSuspended) {
		this.isSuspended = isSuspended;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getStartedTime() {
		return startedTime;
	}

	public void setStartedTime(Date startedTime) {
		this.startedTime = startedTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getParentProcessInstanceId() {
		return parentProcessInstanceId;
	}

	public void setParentProcessInstanceId(String parentProcessInstanceId) {
		this.parentProcessInstanceId = parentProcessInstanceId;
	}

	public String getParentTaskInstanceId() {
		return parentTaskInstanceId;
	}

	public void setParentTaskInstanceId(String parentTaskInstanceId) {
		this.parentTaskInstanceId = parentTaskInstanceId;
	}

	public String getProcessDescription() {
		return processDescription;
	}

	public void setProcessDescription(String processDescription) {
		this.processDescription = processDescription;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Boolean getIsTermination() {
		return isTermination;
	}

	public void setIsTermination(Boolean isTermination) {
		this.isTermination = isTermination;
	}

	public WorkflowProcess getWorkflowProcess() {

		if (workflowProcess == null) {
			workflowProcess = this.getRuntimeContext().getWorkflowProcess(
					processId, version);
		}
		return workflowProcess;
	}

	@Override
	public void run() throws EngineException {
		INetInstance netInstance = (INetInstance) this.getRuntimeContext()
				.getKernelManager()
				.getNetInstance(getProcessId(), getVersion());
		if (netInstance == null) {
			// TODO 异常
		}

		// 触发事件
		ProcessInstanceEvent pevent = new ProcessInstanceEvent();
		pevent.setEventType(ProcessInstanceEventEnum.before_process_instance_run);
		pevent.setProcessInstance(this);
		pevent.fireEvent();

		netInstance.run(this);

	}

	@Override
	public void Complete(INodeInstanceEvent e) throws EngineException {
		// TODO 思路： 1.判断能否结束当前流程实例
		int iCount = this.runtimeContext.getPersistenceService()
				.getAliveTokensCountForProcessInst(this.getId());
		if (iCount > 0) {
			return;
		}
		// 2.结束当前流程实例，
		this.setState(ProcessInstanceStateEnum.completed);
		this.setEndTime(DateUtil.getSysDate());
		this.runtimeContext.getPersistenceService()
				.saveOrUpdateProcessInstance(this);
		// 3.删除当前流程实例的所有死了的token,
		// 4.如果当前流程实例是子流程 则结束主流程的子流程任务实例，使主流程继续流转
		if (StringUtils.isNotBlank(this.getParentTaskInstanceId())
				&& StringUtils
						.isNotBlank(this.getParentTaskInstanceId().trim())) {
			ITaskInstance taskInstance = this.runtimeContext
					.getPersistenceService().getTaskInstanceByParentId(
							this.getParentTaskInstanceId());
			((IRuntimeContextAware) taskInstance)
					.setRuntimeContext(this.runtimeContext);
			((IWorkflowSessionAware) taskInstance)
					.setCurrentWorkflowSession(this.getCurrentWorkflowSession());
			((TaskInstance) taskInstance).complete(null);
		}

		IEndNodeInstance endInst = (IEndNodeInstance) e.getSource();
		if (StringUtils.isNotBlank(endInst.getEndNode().getEntityDocStatus())) {
			// int docStatus =
			// Integer.parseInt(endInst.getEndNode().getEntityDocStatus());
			// RuntimeContext.BusinessService.UpdateDocStatus(this.EntityType,
			// this.EntityId, docStatus);
		}

	}

	@Override
	public void abort() {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspend() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restoreSuspend() {
		// TODO Auto-generated method stub

	}

	public Integer getCallstate() {
		return callstate;
	}

	public void setCallstate(Integer callstate) {
		this.callstate = callstate;
	}

	public String getRestaddress() {
		return restaddress;
	}

	public void setRestaddress(String restaddress) {
		this.restaddress = restaddress;
	}

	@Override
	public String getFlowshort() {
		return flowshort;
	}

	@Override
	public void setFlowshort(String flowshort) {
		this.flowshort = flowshort;
	}
}
