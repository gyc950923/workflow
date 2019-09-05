	package com.online.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IWorkFlowHelper;
import com.online.engine.Instance.IWorkItemInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.impl.ProcessInstance;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.Instance.impl.Todo;
import com.online.engine.common.HttpClientUtil;
import com.online.engine.common.SysConfigUtil;
import com.online.engine.enums.WorkItemInstanceStateEnum;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.engine.resource.PromptControl;
import com.online.util.ExceptionUtil;
import com.online.util.JsonlibUtil;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.enums.ForwardMode;
import com.online.workflow.process.enums.PageActionEnum;
import com.online.workflow.process.resources.PageInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.process.resources.XPDLNames;
public class WorkFlowHelper implements IWorkFlowHelper {

	private RuntimeContext runtimeContext;

	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

	public void setRuntimeContext(RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}

	public WorkFlowHelper() {
		// runtimeContext = (RuntimeContext) getBean("runtimeContext");
	}

	@Override
	public PageInfo getPagByFlowId(String flowId) {
		WorkflowProcess workflowProcess = runtimeContext
				.getWorkflowProcess(flowId);

		return null;
	}

	@Override
	public PageInfo getPageByWorkItemId(String workItemId) {

		return null;
	}

	/*
	 * @Override public PromptControl startWorkFlowQuery(String flowId, String
	 * entityType, String entityId, UserInfo userInfo) throws Exception {
	 * 
	 * runtimeContext.getTaskControl().setQuery(true);
	 * 
	 * return _startWorkFlow(flowId, entityType, entityId, userInfo);
	 * 
	 * }
	 */
	@Override
	public Boolean startWorkFlow(String flowId, String entityType,
	        String entityId, UserInfo userInfo, HashMap<String, String> actors, HashMap<String, Object> var,String xmid,String jdtype, String flowshort) throws EngineException {

		return _startWorkFlow(flowId, entityType, entityId, userInfo, actors, var,xmid,jdtype, flowshort);
	}
	

	private Boolean _startWorkFlow(String flowId, String entityType,
			String entityId, UserInfo userInfo, HashMap<String, String> actors, HashMap<String, Object> var,String xmid,String jdtype, String flowshort) throws EngineException {

		runtimeContext.setUserInfo(userInfo);
		runtimeContext.setActors(actors);
		runtimeContext.getTaskControl().setPageAction(PageActionEnum.none);
		runtimeContext.setForwardMode(ForwardMode.advance);
		IWorkflowSession wflsession = runtimeContext.getWorkflowSession();
		wflsession.initMemoryWfData("");
		IProcessInstance procInst = null;
		try {
			procInst = wflsession.createProcessInstance(flowId, entityType,
					entityId, userInfo,var,xmid,jdtype, flowshort);
		} catch (Exception e) {

			e.printStackTrace();
		}
		runtimeContext.getTaskControl().setStartFlow(true);
		procInst.run();
		runtimeContext.getTaskControl().setStartFlow(false);
		execFirstStep(runtimeContext, procInst);

		boolean flag = wflsession.execSql();
	
		if(flag) {
			flag = firstAudtiAfterProcess(procInst);
		}
		
		return flag;
	}
	
	

	private Boolean firstAudtiAfterProcess(IProcessInstance procInst) {
		Todo todo = new Todo();
		todo.setProcessInstanceId(procInst.getId());
		List<Todo> list = runtimeContext.getBusinessService().queryTodoList(todo);
		
		if(list.size() == 0) {
			ProcessInstance processInstance = runtimeContext.getBusinessService().getProcessInstanceEntity(procInst.getId());
			String activityId = procInst.getWorkflowProcess().getActivities().get(0).getId();
			String restpath = getRestfullPath(processInstance, activityId);
			if(StringUtils.isNotBlank(restpath)) {
				List<NameValuePair> params=new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("entityId",processInstance.getEntityId()));
	            params.add(new BasicNameValuePair("entityType",processInstance.getEntityType()));
	            params.add(new BasicNameValuePair("xmid",processInstance.getXmid()));
	            String resturl = SysConfigUtil.getString("resturl");
	            String msg = HttpClientUtil.executeCallBack(resturl + restpath, params);
	            if(msg != null && !msg.isEmpty()) {
	                return false;
	            } else {
	    			return true;
	            }
			}
		}
		return true;
	}

	/**
	 * 获取结束节点调用的远程接口
	 * @param processInstance
	 * @return
	 */
	private String getRestfullPath(ProcessInstance processInstance, String activityId) {
		WorkflowDefinition wdef = runtimeContext.getPersistenceService().getWorkflowDefinitionByProcessIdAndVersionNumber(processInstance.getProcessId(), processInstance.getVersion());
		String restfulPath = "";
		if(null != wdef && StringUtils.isNotBlank(wdef.getProcessContent())) {
			restfulPath = analysisWdefXml(wdef.getProcessContent(), activityId);
		}
		return restfulPath;
	}

	/**
	 * 解析流程定义xml
	 * @param processContent
	 * @return
	 */
	private String analysisWdefXml(String processContent, String activityId) {
		try {
			Document doc = DocumentHelper.parseText(processContent);
			Element root = doc.getRootElement();
			Element element = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_Transitions+"//"+XPDLNames.XPDL_Transition+"[@fromNodeId='"+activityId+"']");
			if(null != element) {
				Element et = (Element) root.selectSingleNode("//"+XPDLNames.XPDL_EndNodes+"//"+XPDLNames.XPDL_EndNode+"[@id='" + element.attributeValue("toNodeId") + "']");
				if(null != et) {
					return et.attributeValue("restfulAddress");
				}
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return "";
	}

	private void execFirstStep(RuntimeContext runtimeContext,
			IProcessInstance procInst) throws EngineException {

		IWorkItemInstance workItem = null;
		IWorkflowSession workflowSession = runtimeContext.getWorkflowSession();

		// 得到工作流实例
		List<IWorkItemInstance> workItems = workflowSession
				.getWorkItemsByProcessInstId(procInst.getId());
		if (workItems != null && workItems.size() == 1) {
			workItem = workItems.get(0);
			workItem.setXmid(procInst.getXmid());
			workItem.setJdtype(procInst.getJdtype());
			workItem.setFlowshort(procInst.getFlowshort());
			if (workItem.getState() == WorkItemInstanceStateEnum.initialized.intValue()) {
				workItem.claim();
			}
			workItem.complete("");			
		} else {
			// TODO 异常

		}

	}

	@Override
	public PromptControl advanceQuery(String workItemId, UserInfo userInfo,HashMap<String, Object> var, String comments) throws EngineException {

		runtimeContext.setUserInfo(userInfo);
		runtimeContext.getTaskControl().setPageAction(PageActionEnum.Advance);
		runtimeContext.getTaskControl().setQuery(true);
		runtimeContext.setForwardMode(ForwardMode.advance);

		IWorkflowSession wflsession = runtimeContext.getWorkflowSession();
		IWorkItemInstance wi = wflsession.getWorkItemsById(workItemId);
		wflsession.initMemoryWfData(wi.getProcessInstanceId());
		wflsession.completeWorkItem(workItemId, comments);
		
		wflsession.execSql();

		return runtimeContext.getPromptControl();
	}

	@Override
	public Map<String, Object> advance(String workItemId, UserInfo userInfo, HashMap<String, String> actors,
			PromptControl promptControl,HashMap<String, Object> var, String comments) throws EngineException {
		
		runtimeContext.setUserInfo(userInfo);
		runtimeContext.setActors(actors);
		runtimeContext.getTaskControl().setPageAction(PageActionEnum.Advance);
		runtimeContext.getTaskControl().setQuery(false);
		runtimeContext.setForwardMode(ForwardMode.advance);
		runtimeContext.setPromptControl(promptControl);
		
		IWorkflowSession wflsession = runtimeContext.getWorkflowSession();
		IWorkItemInstance wi = wflsession.getWorkItemsById(workItemId);
		
		wflsession.initMemoryWfData(wi.getProcessInstanceId());
		
		wi.getProcessInstance().setProcessInstanceVariables(var);
		try {
		    wflsession.completeWorkItem(workItemId, comments);
        } catch (EngineException e) {
            throw  (EngineException)e.fillInStackTrace();
        }
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = wflsession.execSql();
		map.put("status", flag);
		
		processAfterAudit(wi, map);
		
		return map;
	}

	private void processAfterAudit(IWorkItemInstance wi, Map<String, Object> map) {
		/**
		 * 审批完成后，调用restful
		 */
		try{
			IPersistenceService is = runtimeContext.getPersistenceService();
			ProcessInstance p = is.getProcessInstanceEntity(wi.getProcessInstanceId());
			if(null != p) {
				List<NameValuePair> params=new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("entityId",p.getEntityId()));
                params.add(new BasicNameValuePair("entityType",p.getEntityType()));
                params.add(new BasicNameValuePair("xmid",p.getXmid()));
                String resturl = SysConfigUtil.getString("resturl");
                String msg = HttpClientUtil.executeCallBack(resturl + p.getRestaddress(), params);
                if(msg != null && !msg.isEmpty()) {
	                map.putAll(JsonlibUtil.toMap(msg));
	                //调用接口发生异常-回滚审批数据
	                if("false".equals(map.get("status").toString())) {
	                	runtimeContext.getPersistenceService().rollbackAuditInfo(runtimeContext.getWorkflowSession());
	                }
                } else {
        			map.put("status", Boolean.TRUE);
                }
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("status", Boolean.FALSE);
			map.put("data", ExceptionUtil.getStackTraceAsString(e));
		}
	}

	@Override
	public PromptControl backQuery(String workItemId, UserInfo userInfo,HashMap<String, Object> var, String comments) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean back(String workItemId, UserInfo userInfo,
			PromptControl promptControl,HashMap<String, Object> var, String comments) throws EngineException {
		runtimeContext.setUserInfo(userInfo);
		runtimeContext.getTaskControl().setPageAction(PageActionEnum.Back);
		runtimeContext.getTaskControl().setQuery(false);
		runtimeContext.setForwardMode(ForwardMode.jump);
		IWorkflowSession wflsession = runtimeContext.getWorkflowSession();
		IWorkItemInstance wi = wflsession.getWorkItemsById(workItemId);
		wflsession.initMemoryWfData(wi.getProcessInstanceId());
		wi.getProcessInstance().setProcessInstanceVariables(var);
		String targetActivityId = null;
		if (promptControl != null && promptControl.getItems().size() > 0) {
			targetActivityId = promptControl.getItems().get(0).getActivityId();
		}
        wflsession.backByWorkItemId(wi, targetActivityId, comments);

		return wflsession.execSql();
	}

	/**
	 * 
	 * 功能:终止流程<br>
	 * 约束:与本函数相关的约束<br>
	 * @param workItemId
	 * @param userInfo
	 * @param comments
	 * @throws EngineException 
	 */
    public void termination(String workItemId, UserInfo userInfo, String comments) throws EngineException {
        runtimeContext.setUserInfo(userInfo);
        runtimeContext.setForwardMode(ForwardMode.termination);
        IWorkflowSession wflsession = runtimeContext.getWorkflowSession();
        IWorkItemInstance wi = wflsession.getWorkItemsById(workItemId);
        wflsession.initMemoryWfData(wi.getProcessInstanceId());
        wflsession.terminationWorkItem(wi, comments);
        wflsession.execSql();
    }

    /**
     * 
     * 功能:根据workItemId获取页面信息<br>
     * 约束:与本函数相关的约束<br>
     * @see com.online.engine.Instance.IWorkFlowHelper#getPageByWorkItemId(java.lang.String)
     */
    public PageInfo getPageInfoByWorkItemId(String workItemId) {
        IWorkflowSession wflsession = runtimeContext.getWorkflowSession();
        IWorkItemInstance workItem = wflsession.getWorkItemsById(workItemId);
        wflsession.initMemoryWfData(workItem.getProcessInstanceId());
        PageInfo pageInfo = workItem.getTaskInstance().getActivity().findPageInfo();
        return pageInfo;
    }

	

	

	
}
