package com.online.workflow.executeflow.service;

import java.util.List;
import java.util.Map;

import com.online.engine.Instance.impl.Todo;


public interface IWorkFlowNodeNamesService {
	public Map<String, Object> getAuditNames(String sid, String processid, String entityId, String entityName);
	
	public List<Map<String, Object>> getPageActionButton(String sid, String processid, String entityId);
	
	public Todo getWfTodo(Todo todo);
}
