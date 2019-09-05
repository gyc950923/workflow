package com.online.engine.Instance;

import java.util.HashMap;
import java.util.Map;

import com.online.engine.exception.EngineException;
import com.online.engine.resource.PromptControl;
import com.online.workflow.process.resources.PageInfo;
import com.online.workflow.process.resources.UserInfo;

public interface IWorkFlowHelper {

	 PageInfo getPagByFlowId(String flowId);
     PageInfo getPageByWorkItemId(String workItemId);
     void  termination(String workItemId, UserInfo userInfo, String comments)throws EngineException;
      
    
     Boolean startWorkFlow(String flowId, String entityType,
 			String entityId, UserInfo userInfo, HashMap<String, String> actors, HashMap<String, Object> var,String xmid,String jdtype, String flowshort) throws EngineException;


     PromptControl advanceQuery(String workItemId, UserInfo userInfo,HashMap<String, Object> var, String comments) throws EngineException;
     Map<String, Object> advance(String workItemId,UserInfo userInfo, HashMap<String, String> actors, PromptControl promptControl,HashMap<String, Object> var, String comments) throws EngineException;

     PromptControl backQuery(String workItemId, UserInfo userInfo,HashMap<String, Object> var, String comments);
     boolean back(String workItemId, UserInfo userInfo, PromptControl promptControl,HashMap<String, Object> var, String comments) throws EngineException;
}
