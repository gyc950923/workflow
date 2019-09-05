package com.online.workflow.executeflow.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.online.engine.WorkFlowHelper;
import com.online.engine.Instance.impl.Todo;
import com.online.engine.enums.ProcessInstanceStateEnum;
import com.online.engine.exception.EngineException;
import com.online.workflow.executeflow.dao.IWorkFlowNodeNamesDao;
import com.online.workflow.executeflow.service.IExecuteFlowService;
import com.online.workflow.process.WorkflowDefinitionInfo;
import com.online.workflow.process.resources.PageInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.process.resources.XPDLNames;
import com.online.workflow.util.ExceptionUtil;
import com.online.workflow.util.Result;
import com.online.workflow.util.Result.Status;

@Service("executeFlowService")
public class ExecuteFlowServiceImpl implements IExecuteFlowService{

    @Resource(name = "workFlowHelper")
    private WorkFlowHelper workFlowHelper;
    
    @Resource(name = "workFlowNodeNamesDao")
	private IWorkFlowNodeNamesDao workFlowNodeNamesDao;
    
    @Override
    public Map<String, Object> startWorkflow(String flowId, String entityType, String entityId, UserInfo userInfo, HashMap<String, String> actors, HashMap<String, Object> vars,String xmid,String jdtype, String flowshort){
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
            workFlowHelper.startWorkFlow(flowId, entityType, entityId, userInfo, actors, vars,xmid,jdtype, flowshort);
        } catch (Exception e) {
            map.put("status", false);
            map.put("data", ExceptionUtil.getStackTraceAsString(e));
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, Object> advance(String workitemId, UserInfo userInfo, HashMap<String, String> actors, HashMap<String, Object> vars, String comments) {
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
        	map = workFlowHelper.advance(workitemId, userInfo, actors, null, vars, comments);
        } catch (EngineException e) {
            map.put("status", false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return map;
    }

    @Override
    public Result back(String workitemId, UserInfo userInfo, HashMap<String, Object> vars, String comments) {
        Result result = null;
        try {
            workFlowHelper.back(workitemId, userInfo, null, vars, comments);
            result = new Result(Status.OK, "", "");
        } catch (EngineException e) {
            // TODO Auto-generated catch block
            result = new Result(Status.ERROR, "", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public Result termination(String workItemId, UserInfo userInfo, String comments, String processid, String processInstanceId) {
        Result result;
        try {
            workFlowHelper.termination(workItemId, userInfo, comments);
            result = new Result(Status.OK, "", "");
        } catch (Exception e) {
        	result = new Result(Status.ERROR, "", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        
        if(StringUtils.isNotBlank(processInstanceId)) {
        	initTodoInfo(processid, processInstanceId, userInfo);
        }
        return result;
    }

    private void initTodoInfo(String processid, String processInstanceId,UserInfo userInfo) {
    	
    	Todo todo = new Todo();
    	todo.setProcessInstanceId(processInstanceId);
    	todo.setStepNumber(0);
    	
    	todo = workFlowNodeNamesDao.getWfTodoLog(todo);
    	
    	if(null == todo || StringUtils.isBlank(todo.getProcessId())) return ;
    	
		try {
			WorkflowDefinitionInfo wdi = workFlowNodeNamesDao.getAuditNames(todo.getProcessId());// 获取原审批流程XML
			if (null == wdi || StringUtils.isBlank(wdi.getProcessContent())) {
				return;
			}

			Document doc = DocumentHelper.parseText(wdi.getProcessContent());
			Element root = doc.getRootElement();
			
			//获取流转规则，是否需要发送待阅给发起人
			Element element = (Element) root.selectSingleNode("//" + XPDLNames.XPDL_Activitys + "//" + XPDLNames.XPDL_Activity + "[@id='" + todo.getActivityId() + "']//"+XPDLNames.XPDL_InlineTasks+"//"+XPDLNames.XPDL_FormTask+"//"+XPDLNames.XPDL_AdvanceRule);
			//当前节点是否存在
			if(null == element) return;
			
			//判断是否发送待阅
			if(!"true".equals(element.attributeValue("isToBeRead"))) return ;
			
			//获取待办查看表单url
			if(StringUtils.isBlank(todo.getFormUrl())) {
				element = (Element) root.selectSingleNode("//" + XPDLNames.XPDL_Activitys + "//" + XPDLNames.XPDL_Activity + "[@id='" + todo.getActivityId() + "']//"+XPDLNames.XPDL_InlineTasks+"//"+XPDLNames.XPDL_FormTask+"//"+XPDLNames.XPDL_UserRule+"//"+XPDLNames.XPDL_EditForm);
				if(null != element) {
					todo.setFormUrl(element.attributeValue("url"));
				}
			}
			//向待办表wf_todo表中设置发起人待办信息
			todo.setWorkItemState(ProcessInstanceStateEnum.running);
			todo.setCreatedTime(new Date());
			todo.setClaimedTime(null);
			todo.setEndTime(null);
			todo.setIsToBeRead("true");
			todo.setWorkItemState(ProcessInstanceStateEnum.toberead);
			
			//插入待办数据
			workFlowNodeNamesDao.insertTodoInfo(todo, userInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
    public PageInfo getAuthority(String workItemId) {
        return workFlowHelper.getPageInfoByWorkItemId(workItemId);
    }

	@Override
	public Map<String, Object> executeFlow(String flowId, String workItemId, String entityType, String entityId, UserInfo userInfo, String actorsJson, String myVar, String comments,String xmid,String jdtype, String flowshort) {
		Result result;
        HashMap<String, Object> vars = this.getVars(myVar);
        Map<String, Object> actors = null;
        HashMap<String, String> actorsMap = null;
        Map<String, Object> map = new HashMap<String, Object>();
        
        if (StringUtils.isNotBlank(actorsJson)) {
        	actors = JSONObject.fromObject(actorsJson);
        	for (Entry<String, Object> entry : actors.entrySet()) {
        		actorsMap.put(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
        if (StringUtils.isNotBlank(workItemId)) {
        	map = this.advance(workItemId, userInfo, actorsMap, vars, comments);
        }else if(StringUtils.isNotBlank(flowId)){
        	map = this.startWorkflow(flowId,entityType,entityId,userInfo, actorsMap,vars,xmid,jdtype, flowshort);
        }else{
        	map.put("status", false);
        }
        return map;
	}

	private HashMap<String, Object> getVars(String str){
        if (StringUtils.isBlank(str)) {
            return null;
        }
        JSONObject varObject =JSONObject.fromObject(str);
        Iterator<String> iterator = varObject.keys();
        HashMap<String, Object> map = new HashMap<String, Object>();
        while(iterator.hasNext()){
            String key = iterator.next();
            String value = varObject.getString(key);
            Object varValue = this.getVarValue(value);
            if (varValue != null) {
                //变量key不区分大小写
                map.put(key.toLowerCase(), varValue);
            }
        }
        return map;
    }
    private Object getVarValue(String str){
        String[] temp = str.split("#");
        if(temp.length !=2){
            return null;
        }
        String type = temp[0];
        String value = temp[1];
        
        if ("Integer".equals(type)){
            return Integer.parseInt(value);
        }
        if ("Long".equals(type)){
            return Long.parseLong(value);
        }
        else if ("Float".equals(type)){
            return Float.parseFloat(value);
        }
        else if ("Double".equals(type)){
            return Double.parseDouble(value);
        }
        else if ("Boolean".equals(type)){
            return Boolean.parseBoolean(value);
        }else{
            return value;
        }
    }

	@Override
	public Result back(String workItemId, String actorId, String actorName, String myVar,
			String comments) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(actorId);
		userInfo.setName(actorName);
		HashMap<String, Object> vars = this.getVars(myVar);
        Result result = this.back(workItemId,userInfo,vars,comments);
        return result;
	}

	
	@Override
	public boolean batchAdvance(String workItemIds, String actorId, String actorName) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(actorId);
		userInfo.setName(actorName);
		String[] workItemIdArray = workItemIds.split(",");
		for (String workItemId : workItemIdArray) {
			this.advance(workItemId, userInfo, null, null, null);
		}
		return true;
	}

	@Override
	public boolean batchBack(String workItemIds, String actorId, String actorName) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(actorId);
		userInfo.setName(actorName);
		String[] workItemIdArray = workItemIds.split(",");
		for (String workItemId : workItemIdArray) {
			this.back(workItemId, userInfo, null, null);
		}
		return true;
	}

	@Override
	public Map<String, Object> advance(String workItemId, String actorId, String actorName,HashMap<String, String> actors,String comments) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(actorId);
		userInfo.setName(actorName);
		Map<String, Object> map = this.advance(workItemId, userInfo, actors, null, comments);
		return map;
	}

	@Override
	public boolean back(String workItemId, String actorId, String actorName,String comments) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(actorId);
		userInfo.setName(actorName);
		this.back(workItemId, userInfo, null, comments);
		return true;
	}
   
	@Override
	public boolean termination(String workItemId, String actorId, String actorName,String comments, String processid, String processInstanceId) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(actorId);
		userInfo.setName(actorName);
		this.termination(workItemId, userInfo, comments, processid, processInstanceId);
		return true;
	}

	@Override
	public Map<String, Object> toberead(String workItemId, UserInfo userInfo) {
		
		return workFlowNodeNamesDao.toberead(workItemId, userInfo);
	}
}
