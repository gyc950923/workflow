package com.online.engine.pluginBusiness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.ITodo;
import com.online.engine.Instance.impl.ProcessInstance;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.Instance.impl.Todo;
import com.online.engine.common.BaseDaoSupport;
import com.online.engine.enums.TablesName;
import com.online.engine.model.Org;
import com.online.engine.model.User;
import com.online.workflow.process.enums.ActorAssignTypeEnum;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.resources.DeptInfo;
import com.online.workflow.process.resources.EntityDocStatus;
import com.online.workflow.process.resources.MetaData;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.PagerNumber;
import com.online.workflow.process.resources.RestInfo;
import com.online.workflow.process.resources.RoleInfo;
import com.online.workflow.process.resources.SqlInfo;
import com.online.workflow.process.resources.UserInfo;

@SuppressWarnings("unchecked")
@Transactional
public class BusinessService extends BaseDaoSupport implements IBusinessService{

	private RuntimeContext runtimeContext;
	@Override
	public void setRuntimeContext(RuntimeContext ctx) {
		runtimeContext=ctx;
		
	}

	@Override
	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

	@Override
	public List<MetaData> getAllEntityInfos() {
		return null;
	}

	@Override
	public List<MetaData> getRefenceEntityById(String idOrName) {
		return null;
	}

	@Override
	public List<Form> getAllFroms() {
		return null;
	}

	@Override
	public List<EntityDocStatus> getEntityDocStatusValues(String entityId) {
		return null;
	}

	@Override
	public List<UserInfo> getUsersByDepts(List<DeptInfo> deptInfos, String orgId) {
		return null;
	}

	@Override
	public List<UserInfo> getUsersByRoles(List<RoleInfo> roleInfos) {
		List<UserInfo> userlist = new ArrayList<UserInfo>();
		List<User> userlist1 = new ArrayList<User>();
		String roleids="";
		for(int i=0;i<roleInfos.size();i++){
			if(i != roleInfos.size()-1){
				roleids += "'"+roleInfos.get(i).getId()+"'"+",";
			}else{
				roleids += "'"+roleInfos.get(i).getId()+"'";
			}
		}
		if(!"".equals(roleids)){
		  userlist1 = this.getAllUser(roleids);
		}
		for(User u:userlist1){
			UserInfo uf = new UserInfo();
			uf.setId(String.valueOf(u.getId()));
			uf.setName(u.getUserName());
			userlist.add(uf);
		}
		return userlist;
	}
	
	
	public List<User> getAllUser(String roleid) {
	    String sql = "select DISTINCT * from sys_user where id in ( select userid from sys_user_role where roleid in ("+roleid+") )";
        SQLQuery sqlQuery = this.createSqlQuery(sql).addEntity(User.class);
        return sqlQuery.list();
    }

	@Override
	public List<UserInfo> getDeptUsers(String deprtId, String orgId) {
		return null;
	}

	@Override
	public List<DeptInfo> getDeptOrg(UUID orgId) {
		return null;
	}

	@Override
	public List<RoleInfo> getRoles(String name) {
		return null;
	}

	@Override
	public List<String> getValueByEnttyExpression(HashMap<String, Object> vars,
			String entityColumn) {
		return null;
	}

	@Override
	public HashMap<String, String> ListFieldValue(String orgId, String tableId,
			String dataId, List<String> fieldCodes) {
		return null;
	}

	@Override
	public Boolean updateDocStatus(String entityType, String entityId,
			Integer docStatus) {
		return null;
	}

	@Override
	public List<ITodo> getTodoInstanceByActorId(String actorId,
			String entityType, String orgId) {
		return null;
	}

	@Override
	public List<ITodo> getPassEndProcessInstByActorId(String actorId,
			String orgId) {
		return null;
	}

	@Override
	public List<ITodo> getWorkflowHistory(String entityType, String entityId,
			String orgId, PagerNumber page) {
		return null;
	}

	@Override
	public List<UserInfo> getSqlConditionUserInfos(SqlInfo sqlInfos) {
		//sql语句格式：select * from test where id = %s;
		String sql = String.format(sqlInfos.getSqlKey(), sqlInfos.getEntityId());
        List<Object[]> list = this.createSqlQuery(sql).list();

        List<UserInfo> userInfoList = new ArrayList<UserInfo>();
        if(null != list && list.size()>0) {
        	
        	for(int i=0;i<list.size();i++){
        		Object[] objList = list.get(i);
        		if(objList.length>2){
        			initUserInfo(userInfoList, objList);
        			continue;
        		}
        		UserInfo ui = new UserInfo();
        		ui.setId(String.valueOf(objList[0]));
        		ui.setName(String.valueOf(objList[1]));
        		userInfoList.add(ui);
        	}
        }
      
        return userInfoList;
	}

	private void initUserInfo(List<UserInfo> userInfoList, Object[] objList) {
		UserInfo ui = new UserInfo();
		for (int i = 0; i < objList.length; i++) {
			if(i%2==0){
				ui.setId(String.valueOf(objList[i]));
			}else{
				ui.setName(String.valueOf(objList[i]));
			}
			if(StringUtils.isNotBlank(ui.getId()) && StringUtils.isNotBlank(ui.getName())) {
				userInfoList.add(ui);
				ui = new UserInfo();
			}
		}
	}

	@Override
	public List<UserInfo> getRestConditionUserInfos(RestInfo restInfo) {
		System.err.println("======================================="+restInfo.getRestKey());
		return null;
	}

	@Override
	public List<UserInfo> getConditionOrgOnRoleUserInfos( List<OrgRoleInfo> orgRoleInfos, IProcessInstance processInstance) {
		List<User> list = new ArrayList<User>();
		List<UserInfo> userList = new ArrayList<UserInfo>();
		String orghql = "select * from sys_org where id = (select sysorgid from "+processInstance.getEntityType()+" where id = :id )";
		String id = processInstance.getEntityId();
		OrgRoleInfo ori = processInstance.getWorkflowProcess().getActivitsPublicParam();
		if(StringUtils.isNotBlank(ori.getAllNode())) {
			if(ori.getDepartmentSrc().equals(ActorAssignTypeEnum.PREVIOUSHANDLER)){//60; 部门来源--上一处理人
				
			}else if(ori.getDepartmentSrc().equals(ActorAssignTypeEnum.DESIGNATEDDEPARTMENT)) {//70; 部门来源--指定部门
				orghql = "select * from sys_org where id = :id";
				id = ori.getConditionId();
			}else if(ori.getDepartmentSrc().equals(ActorAssignTypeEnum.CUSTOMIZESQL)) {//80; 部门来源--自定义sql 实例select org_id from sys_user_org where user_id = (select xmfzrid from ia_sjxm where id = %s )
//				String sql = String.format(ori.getConditionName(), processInstance.getEntityId());
				orghql = "select * from sys_org where id in ("+ori.getConditionName()+")";
			}
		}
		 SessionFactory sessionFactory = this.getHibernateTemplate().getSessionFactory();
		 Session openSession = sessionFactory.openSession();
		SQLQuery orgSqlQuery = this.createSqlQuery(orghql).addEntity(Org.class);
		orgSqlQuery.setParameter("id", id);
		List<Org> orgList = orgSqlQuery.list();
		if(null != orgList && orgList.size() > 0 && null != orgRoleInfos) {
			Org org = orgList.get(0);
			for(OrgRoleInfo or:orgRoleInfos){
				getOrgUnderRoleUser(org, list, or.getId());
			}
		}
		
		for(User u:list) {
			UserInfo ui = new UserInfo();
    		ui.setId(String.valueOf(u.getId()));
    		ui.setName(u.getUserName());
    		userList.add(ui);
		}
		
        return userList;
	}

	private List<User> getOrgUnderRoleUser(Org org, List<User> list, String roleid) {
		String sql = "select t1.* from sys_user_org t, sys_user t1 where t.user_id = t1.id and  t.user_id in (select userid from sys_user_role where roleid=:roleid) and t.org_id=:orgid";
		SQLQuery sqlQuery = this.createSqlQuery(sql).addEntity(User.class);
		sqlQuery.setParameter("roleid", roleid);
		sqlQuery.setParameter("orgid", org.getId());
		List<User> l = sqlQuery.list(); 
		if(null != l && l.size() > 0) {
			list.addAll(l);
		}else if(null != org.getParentDepartId()){
			String orghql = "select * from sys_org where id = :id ";
			SQLQuery osuery = this.createSqlQuery(orghql).addEntity(Org.class);
			osuery.setParameter("id", org.getParentDepartId());
			List<Org> orgList = osuery.list();
			if(null != orgList && orgList.size() > 0) {
				org = orgList.get(0);
				getOrgUnderRoleUser(org, list, roleid);
			}
		}
		
		return list;
	}

	@Override
	public List<Todo> queryTodoList(Todo todo) {
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
		
		return query.list();
	}

	@Override
	public ProcessInstance getProcessInstanceEntity(String id) {
		SQLQuery query;
		StringBuffer sb = new StringBuffer();
		sb.append("select * from " + TablesName.processInstance + " where id=:id ");
		query = this.getSession().createSQLQuery(sb.toString()).addEntity(ProcessInstance.class);
		if(StringUtils.isNotBlank(id)) {
			query.setParameter("id", id);
		}
		
		if(query.list().size() > 0){
			return (ProcessInstance) query.list().get(0);
		}
		
		return null;
	}
}
