package com.online.workflow.executeflow.service;

import java.util.HashMap;
import java.util.Map;

import com.online.workflow.process.resources.PageInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.util.Result;

public interface IExecuteFlowService {

    Map<String, Object> startWorkflow(String flowId, String entityType, String entityId, UserInfo userInfo, HashMap<String, String> actors, HashMap<String, Object> vars,String xmid,String jdtype, String flowshort);

    Map<String, Object> advance(String workitemId, UserInfo userInfo, HashMap<String, String> actors, HashMap<String, Object> vars, String comments);

    Result back(String workitemId, UserInfo userInfo, HashMap<String, Object> vars, String comments);

    Result termination(String workItemId, UserInfo userInfo, String comments, String processid, String processInstanceId);

    PageInfo getAuthority(String workItemId);

	Map<String, Object> executeFlow(String flowId, String workItemId, String entityType, String entityId, UserInfo userInfo, String actorsJson, String myVar, String comments,String xmid,String jdtype, String flowshort);

	Result back(String workItemId, String actorId, String actorName, String myVar,
			String comments);

	boolean batchAdvance(String workItemIds, String actorId, String actorName);

	boolean batchBack(String workItemIds, String actorId, String actorName);

	Map<String, Object> advance(String workItemId, String actorId, String actorName,HashMap<String, String> actors,String comments);
	
	boolean back(String workItemId, String actorId, String actorName,String comments);

	public boolean termination(String workItemId, String actorId, String actorName,String comments, String processid, String processInstanceId);

	public Map<String, Object> toberead(String workItemId, UserInfo userInfo); 
}
