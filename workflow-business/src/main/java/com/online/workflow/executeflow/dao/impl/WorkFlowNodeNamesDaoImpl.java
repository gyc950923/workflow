package com.online.workflow.executeflow.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.online.engine.Instance.impl.Todo;
import com.online.engine.common.BaseDaoSupport;
import com.online.engine.enums.TablesName;
import com.online.engine.model.Org;
import com.online.engine.model.User;
import com.online.util.Result;
import com.online.workflow.executeflow.dao.IWorkFlowNodeNamesDao;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowDefinitionInfo;
import com.online.workflow.process.enums.ActorAssignTypeEnum;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.UserInfo;

@Repository("workFlowNodeNamesDao")
public class WorkFlowNodeNamesDaoImpl extends BaseDaoSupport implements IWorkFlowNodeNamesDao {

	@Override
	public WorkflowDefinitionInfo getAuditNames(String processid) {
		StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(" ID, DEFINITION_TYPE, PROCESS_ID, NAME, DISPLAY_NAME, DESCRIPTION, VERSION, STATE, UPLOAD_USER, UPLOAD_TIME, PUBLISH_USER, PUBLISH_TIME, PROCESS_CONTENT, PROCESSCHART_CONTENT ");
        sql.append("from wf_workflowdef where process_id=:processid order by version desc ");
        SQLQuery sqlQuery = this.createSqlQuery(sql.toString()).addEntity(WorkflowDefinition.class);
        sqlQuery.setParameter("processid", processid);
        if(sqlQuery.list().size() > 0){
        	return (WorkflowDefinitionInfo) sqlQuery.list().get(0);
        }
        
        return new WorkflowDefinitionInfo();
	}

	@Override
	public Object getSqlConditionValue(String sql, String entityId) {
		if(StringUtils.isEmpty(sql)){
			return null; 
		}
		
		sql = sql.replace(":xid", entityId);
		return this.createSqlQuery(sql).list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsersByRoleInfos(String auditIds) {
		if(StringUtils.isEmpty(auditIds)){
			return null; 
		}
		String sql = "select DISTINCT * from sys_user where id in ( select userid from sys_user_role where roleid in ("+auditIds+") )";
        return this.createSqlQuery(sql).addEntity(User.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsersByOrgRoleInfos(String auditIds, String entityId, String entityName, OrgRoleInfo ori) {
		List<User> listUser = new ArrayList<User>();
		String orghql = "select * from sys_org where id = (select sysorgid from "+ entityName +" where id = :id)";
		
		if(StringUtils.isNotBlank(ori.getAllNode())) {
			if(ori.getDepartmentSrc().equals(ActorAssignTypeEnum.PREVIOUSHANDLER)){//60; 部门来源--上一处理人
				
			}else if(ori.getDepartmentSrc().equals(ActorAssignTypeEnum.DESIGNATEDDEPARTMENT)) {//70; 部门来源--指定部门
//				String sql = String.format(ori.getConditionName(), entityId);
				orghql = "select * from sys_org where id = :id";
				entityId = ori.getConditionId();
			}else if(ori.getDepartmentSrc().equals(ActorAssignTypeEnum.CUSTOMIZESQL.toString())) {//80; 部门来源--自定义sql 实例select org_id from sys_user_org where user_id = (select xmfzrid from ia_sjxm where id = %s )
//				String sql = String.format(ori.getConditionName(), entityId);
				orghql = "select * from sys_org where id in ("+ori.getConditionName().replace("%s", ":id")+")";
			}
		}
		
		SQLQuery orgSqlQuery = this.createSqlQuery(orghql).addEntity(Org.class);
		orgSqlQuery.setParameter("id", entityId);
		
		List<Org> orgList = orgSqlQuery.list();
		if(orgList.size() > 0){
			Org org = orgList.get(0);
			getOrgUnderRoleUser(org, listUser, auditIds);
		}
		
		return listUser;
	}

	/**
	 * 递归获取人员信息
	 * @param org
	 * @param listUser
	 * @param id
	 * @param auditIds
	 */
	@SuppressWarnings("unchecked")
	private void getOrgUnderRoleUser(Org org, List<User> listUser, String auditIds) {
		String sql = "select t1.* from sys_user_org t, sys_user t1 where t.user_id = t1.id and  t.user_id in (select userid from sys_user_role where roleid in("+auditIds+")) and t.org_id=:orgid";
		SQLQuery sqlQuery = this.createSqlQuery(sql).addEntity(User.class);
		sqlQuery.setParameter("orgid", org.getId());
		List<User> l = sqlQuery.list(); 
		if(null != l && l.size() > 0) {
			listUser.addAll(l);
		}else if(null != org.getParentDepartId()){
			String orghql = "select * from sys_org where id = :id ";
			SQLQuery osuery = this.createSqlQuery(orghql).addEntity(Org.class);
			osuery.setParameter("id", org.getParentDepartId());
			List<Org> orgList = osuery.list();
			if(null != orgList && orgList.size() > 0) {
				org = orgList.get(0);
				getOrgUnderRoleUser(org, listUser, auditIds);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getSqlConditionUserInfos(String entityId, String sqlKey) {
		//sql语句格式：select * from test where id = %s;
		String sql = String.format(sqlKey, entityId);
        List<Object[]> list = this.createSqlQuery(sql).list();

        List<User> userInfoList = new ArrayList<User>();
        if(null != list && list.size()>0) {
        	for(int i=0;i<list.size();i++){
        		Object[] objList = list.get(i);
        		if(objList.length>2){
        			initUserInfo(userInfoList, objList);
        			continue;
        		}
        		User ui = new User();
        		ui.setId(objList[0].toString());
        		ui.setUserName(String.valueOf(objList[1]));
        		userInfoList.add(ui);
        	}
        }
        
        return userInfoList;
	}
	
	private void initUserInfo(List<User> userInfoList, Object[] objList) {
		User ui = new User();
		for (int i = 0; i < objList.length; i++) {
			if(i%2==0){
				ui.setId(objList[i].toString());
			}else{
				ui.setUserName(String.valueOf(objList[i]));
			}
			if(null != ui.getId() && !StringUtils.isEmpty(ui.getUserName())) {
				userInfoList.add(ui);
				ui = new User();
			}
		}
	}
	
	

//	
//	todo.setActivityId(activityId);
//	todo.setProcessId(processid);
//	todo.setEntityId(entityId);
//	todo.setActorId(this.getUserId().toString());
	@Override
	public Todo getWfTodo(Todo todo) {
		// 缓存流程处理记录
		SQLQuery query;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select * from " + TablesName.todo + " where 1=1 ");
		if(StringUtils.isNotBlank(todo.getActivityId())) {
			sb.append(" AND ACTIVITY_ID=:activityId ");
		}
		
		if(StringUtils.isNotBlank(todo.getProcessId())) {
			sb.append(" AND PROCESS_ID=:processId ");
		}
		
		if(StringUtils.isNotBlank(todo.getEntityId())) {
			sb.append(" AND ENTITYID=:entityId ");
		}
		
		if(StringUtils.isNotBlank(todo.getActorId())) {
			sb.append(" AND ACTOR_ID=:actorId ");
		}
		
		if(StringUtils.isNotBlank(todo.getWorkItemId())) {
			sb.append(" AND WORKITEM_ID=:workItemId ");
		}
		
		if(StringUtils.isNotBlank(todo.getProcessInstanceId())) {
			sb.append(" AND PROCESSINSTANCE_ID=:processInstanceId ");
		}
		
		query = this.getSession().createSQLQuery(sb.toString()).addEntity(Todo.class);
		
		if(StringUtils.isNotBlank(todo.getActivityId())) {
			query.setParameter("activityId", todo.getActivityId());
		}
		
		if(StringUtils.isNotBlank(todo.getProcessId())) {
			query.setParameter("processId", todo.getProcessId());
		}
		
		if(StringUtils.isNotBlank(todo.getEntityId())) {
			query.setParameter("entityId", todo.getEntityId());
		}
		
		if(StringUtils.isNotBlank(todo.getActorId())) {
			query.setParameter("actorId", todo.getActorId());
		}
		
		if(StringUtils.isNotBlank(todo.getWorkItemId())) {
			query.setParameter("workItemId", todo.getWorkItemId());
		}
		
		if(StringUtils.isNotBlank(todo.getProcessInstanceId())) {
			query.setParameter("processInstanceId", todo.getProcessInstanceId());
		}
		
		
		List<Todo> list = query.list();
		if(null != list && list.size() > 0) {
			return list.get(0);
		}
		return todo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Todo getWfTodoLog(Todo todo) {
		// 缓存流程处理记录
		SQLQuery query;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select * from " + TablesName.todoLog + " where 1=1 ");
		if(StringUtils.isNotBlank(todo.getProcessInstanceId())) {
			sb.append(" AND PROCESSINSTANCE_ID=:processInstanceId ");
		}
		
		if(null != todo.getStepNumber()) {
			sb.append(" AND STEP_NUMBER=:stepNumber  ");
		}
		
		query = this.getSession().createSQLQuery(sb.toString()).addEntity(Todo.class);
		
		if(StringUtils.isNotBlank(todo.getProcessInstanceId())) {
			query.setParameter("processInstanceId", todo.getProcessInstanceId());
		}
		
		if(null != todo.getStepNumber()) {
			query.setParameter("stepNumber", todo.getStepNumber());
		}		
		
		List<Todo> list = query.list();
		if(null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Result insertTodoInfo(Todo todo, UserInfo userInfo) {
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.todo);
		strSql.append(" (WORKITEM_ID,ACTOR_ID,ACTOR_NAME,Post,WORKITEM_STATE,CREATED_TIME,ACTIVITY_ID,TASK_ID,STEP_number,PROCESSINSTANCE_ID,PROCESS_ID,PROCESS_NAME,CREATOR_ID,CREATOR_NAME,ENTITYID,ENTITYTYPE,TODODESCRIPTION,FORMURL,XMID,JDTYPE,ISTOBEREAD)");
		strSql.append(" values(:workItemId,:actorId,:actorName,:post,:workItemState,:createdTime,:activityId,:taskId,:stepNumber,:processInstanceId,:processId,:processName,:creatorId,:creatorName,:entityId,:entityType,:toDoDescription,:formUrl,:xmid,:jdtype,:isToBeRead)");

		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("workItemId", UUID.randomUUID());
		query.setParameter("actorId", todo.getActorId());
		query.setParameter("actorName", todo.getActorName());
		query.setParameter("post", todo.getPost());
		query.setParameter("workItemState", todo.getWorkItemState());
		query.setParameter("createdTime", todo.getCreatedTime());
		query.setParameter("activityId", todo.getActivityId());
		query.setParameter("taskId", todo.getTaskInstanceId());
		query.setParameter("stepNumber", todo.getStepNumber());
		query.setParameter("processInstanceId",todo.getProcessInstanceId());
		query.setParameter("processId", todo.getProcessId());
		query.setParameter("processName", todo.getProcessName());
		query.setParameter("creatorId", userInfo.getId());
		query.setParameter("creatorName", userInfo.getName());
		query.setParameter("entityId", todo.getEntityId());
		query.setParameter("entityType", todo.getEntityType());
		query.setParameter("toDoDescription", todo.getToDoDescription());
		query.setParameter("formUrl", todo.getFormUrl());
		query.setParameter("xmid", todo.getXmid());
		query.setParameter("jdtype", todo.getJdtype());
		query.setParameter("isToBeRead", todo.getIsToBeRead());
		this.getSession().clear();
		query.executeUpdate();
		return null;
	}

	/**
	 * 将待阅记录添加到历史wf_todolog表中
	 */
	@Override
	public Map<String, Object> toberead(String workItemId, UserInfo userInfo) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		
		Todo todo = new Todo();
		todo.setWorkItemId(workItemId);
		todo = this.getWfTodo(todo);
		
		if(null == todo) return map;
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.todoLog);
		strSql.append(" (WORKITEM_ID,ACTOR_ID,ACTOR_NAME,Post,WORKITEM_STATE,CREATED_TIME,CLAIMED_TIME,END_TIME,COMMENTS,TASKINSTANCE_ID,TASKINSTANCE_NAME,ACTIVITY_ID,TASK_ID,STEP_number,PROCESSINSTANCE_ID,PROCESS_ID,PROCESS_NAME,CREATOR_ID,ENTITYID,ENTITYTYPE,TODODESCRIPTION,FORMURL,CREATOR_NAME,XMID,JDTYPE,ISTOBEREAD)");
		strSql.append(" values(:workItemId,:actorId,:actorName,:post,:workItemState,:createdTime,:claimedTime,:endTime,:comments,:taskInstanceId,:taskInstanceName,:activityId,:taskId,:stepNumber,:processInstanceId,:processId,:processName,:creatorId,:entityId,:entityType,:toDoDescription,:formUrl,:creatorName,:xmid,:jdtype,:istoberead)");

		SQLQuery query = this.createSqlQuery(strSql.toString());

		query.setParameter("workItemId", UUID.randomUUID());
		query.setParameter("actorId", todo.getActorId());
		query.setParameter("actorName", todo.getActorName());
		query.setParameter("post", todo.getPost());
		query.setParameter("workItemState", todo.getWorkItemState());
		query.setParameter("createdTime", new Date());
		query.setParameter("claimedTime", new Date());
		query.setParameter("endTime", new Date());
		query.setParameter("comments", "");
		query.setParameter("taskInstanceId",todo.getTaskInstanceId());
		query.setParameter("taskInstanceName", todo.getTaskInstanceName());
		query.setParameter("activityId", todo.getActivityId());
		query.setParameter("taskId", todo.getTaskInstanceId());
		query.setParameter("stepNumber", todo.getStepNumber());
		query.setParameter("processInstanceId",todo.getProcessInstanceId());
		query.setParameter("processId", todo.getProcessId());
		query.setParameter("processName", todo.getProcessName());
		query.setParameter("creatorId", userInfo.getId());
		query.setParameter("entityId", todo.getEntityId());
		query.setParameter("entityType", todo.getEntityType());
		query.setParameter("toDoDescription", todo.getToDoDescription());
		query.setParameter("formUrl", todo.getFormUrl());
		/*query.setParameter("completeMode", todo.getCompeletemode());*/
		query.setParameter("creatorName", userInfo.getName());
		query.setParameter("xmid", todo.getXmid());
		query.setParameter("jdtype", todo.getJdtype());
		query.setParameter("istoberead", todo.getIsToBeRead());
		this.getSession().clear();
		query.executeUpdate();
		
		if(StringUtils.isNotBlank(workItemId)) {
			strSql = new StringBuilder();
			strSql.append("delete from " + TablesName.todo);
			strSql.append(" where WORKITEM_ID = :workItemId");
			query = this.createSqlQuery(strSql.toString());
			query.setParameter("workItemId", workItemId);
			query.executeUpdate();
		}
		map.put("success", true);
		return map;
	}
}
