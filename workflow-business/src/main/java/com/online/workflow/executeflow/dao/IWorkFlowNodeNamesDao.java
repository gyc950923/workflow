package com.online.workflow.executeflow.dao;

import java.util.List;
import java.util.Map;

import com.online.engine.Instance.impl.Todo;
import com.online.engine.model.User;
import com.online.util.Result;
import com.online.workflow.process.WorkflowDefinitionInfo;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.UserInfo;

public interface IWorkFlowNodeNamesDao {

    public WorkflowDefinitionInfo getAuditNames(String processid);

	public Object getSqlConditionValue(String sqlCondition, String entityId);

	public List<User> getUsersByRoleInfos(String auditIds);

	public List<User> getUsersByOrgRoleInfos(String auditIds, String entityId, String entityName, OrgRoleInfo ori);

	public List<User> getSqlConditionUserInfos(String entityId, String sqlKey);

	public Todo getWfTodo(Todo todo);
	public Todo getWfTodoLog(Todo todo);

	public Result insertTodoInfo(Todo todo, UserInfo userInfo);

	public Map<String, Object> toberead(String workItemId, UserInfo userInfo);

}
