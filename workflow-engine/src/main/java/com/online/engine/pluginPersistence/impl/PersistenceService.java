package com.online.engine.pluginPersistence.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IProcessInstanceHistTrace;
import com.online.engine.Instance.ITaskInstance;
import com.online.engine.Instance.ITodo;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.IWorkItemInstance;
import com.online.engine.Instance.IWorkflowSession;
import com.online.engine.Instance.impl.ProcessInstance;
import com.online.engine.Instance.impl.ProcessInstanceHistTrace;
import com.online.engine.Instance.impl.ProcessInstanceVar;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.Instance.impl.TaskInstance;
import com.online.engine.Instance.impl.Todo;
import com.online.engine.Instance.impl.Token;
import com.online.engine.Instance.impl.WorkItemInstance;
import com.online.engine.common.BaseDaoSupport;
import com.online.engine.enums.NodeInstanceEventEnum;
import com.online.engine.enums.ProcessInstanceStateEnum;
import com.online.engine.enums.TablesName;
import com.online.engine.enums.TaskInstanceStateEnum;
import com.online.engine.enums.WorkItemInstanceStateEnum;
import com.online.engine.pluginKernel.impl.EndNodeInstance;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.util.BeanUtil;
import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.enums.ForwardMode;
import com.online.workflow.process.resources.PagerNumber;

@SuppressWarnings("unchecked")
public class PersistenceService extends BaseDaoSupport implements
		IPersistenceService {
	@Resource(name = "hibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	

	Logger log = Logger.getLogger(PersistenceService.class);
	private RuntimeContext runtimeContext;

	public Boolean IsCacheData() {
		return getRuntimeContext().isCacheData();
	}

	@Override
	public void setRuntimeContext(RuntimeContext ctx) {
		this.runtimeContext = ctx;
	}

	@Override
	public RuntimeContext getRuntimeContext() {

		return runtimeContext;
	}

	@Override
	public Boolean execSql() {
		List<Query> listQuery = getRuntimeContext().getListCmd();
		for (Query query : listQuery) {
			query.executeUpdate();
			
		}
		//清空已被执行的query对象，避免重复执行时因session已关闭造成异常
		return Boolean.TRUE;

	}

	@Override
	public void addCommand(Query query) {
		getRuntimeContext().getListCmd().add(query);
	}

	@Override
	public void initMemoryWfData(String processInstanceId) {

		List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();
		List<TaskInstance> taskInstances = new ArrayList<TaskInstance>();
		List<ProcessInstanceHistTrace> histTraces = new ArrayList<ProcessInstanceHistTrace>();
		List<WorkItemInstance> WorkItemInstances = new ArrayList<WorkItemInstance>();
		List<ProcessInstanceVar> processInstanceVars = new ArrayList<ProcessInstanceVar>();
		List<Token> tokens = new ArrayList<Token>();
		List<Todo> todos = new ArrayList<Todo>();

		Query query;

		if (StringUtils.isNotBlank(processInstanceId)) {
			// 缓存流程实例表
			String processInstancesSql = "select * from "
					+ TablesName.processInstance + " where id=:id";
			query = this.getSession().createSQLQuery(processInstancesSql)
					.addEntity(ProcessInstance.class);
			query.setParameter("id", processInstanceId);
			processInstances = query.list();

			// 缓存流程任务表
			String taskInstanceSql = " select * from "
					+ TablesName.taskInstance
					+ " where processInstance_Id =:processInstanceId order by step_number desc";
			query = this.getSession().createSQLQuery(taskInstanceSql)
					.addEntity(TaskInstance.class);
			query.setParameter("processInstanceId", processInstanceId);
			taskInstances = query.list();

			// 缓存流程流转记录表
			String histTraceSql = " select * from " + TablesName.histTrace
					+ " where PROCESSINSTANCE_ID =:processInstanceId";
			query = this.getSession().createSQLQuery(histTraceSql)
					.addEntity(ProcessInstanceHistTrace.class);
			query.setParameter("processInstanceId", processInstanceId);
			histTraces = query.list();

			// 缓存工单表
			String workItemInstanceSql = "select * from " + TablesName.workItem
					+ " where PROCESSINSTANCE_ID = :processInstanceId";
			query = this.getSession().createSQLQuery(workItemInstanceSql)
					.addEntity(WorkItemInstance.class);
			query.setParameter("processInstanceId", processInstanceId);
			WorkItemInstances = query.list();

			// 缓存Token表
			String tokenSql = "select * from " + TablesName.token
					+ " where PROCESSINSTANCE_ID = :processInstanceId";
			query = this.getSession().createSQLQuery(tokenSql)
					.addEntity(Token.class);
			query.setParameter("processInstanceId", processInstanceId);
			tokens = query.list();

			// 缓存流程变量表
			String processInstanceVarSql = "select * from "
					+ TablesName.procInstVar
					+ " where PROCESSINSTANCE_ID = :processInstanceId";
			query = this.getSession().createSQLQuery(processInstanceVarSql)
					.addEntity(ProcessInstanceVar.class);
			query.setParameter("processInstanceId", processInstanceId);
			processInstanceVars = query.list();

			// 缓存流程处理记录
			String todoSql = "select * from " + TablesName.todo
					+ " where processInstance_id =:processInstanceId";
			query = this.getSession().createSQLQuery(todoSql)
					.addEntity(Todo.class);
			query.setParameter("processInstanceId", processInstanceId);
			todos = query.list();

		}
		getRuntimeContext().getListCmd().clear();
		getRuntimeContext().getCacheData().clear();
		addCacheData(processInstances, TablesName.processInstance);
		addCacheData(taskInstances, TablesName.taskInstance);
		addCacheData(histTraces, TablesName.histTrace);
		addCacheData(WorkItemInstances, TablesName.workItem);
		addCacheData(tokens, TablesName.token);
		addCacheData(processInstanceVars, TablesName.procInstVar);
		addCacheData(todos, TablesName.todo);

	}

	// / <summary>
	// / 往总线添加虚拟表
	// / </summary>
	// / <param name="tableName"></param>
	// / <param name="dt"></param>
	private void addCacheData(Object data, String tableName) {

		HashMap<String, Object> hashMap = getRuntimeContext().getCacheData();
		if (!hashMap.containsKey(tableName)) {
			getRuntimeContext().getCacheData().put(tableName, data);
		}

	}

	@Override
	public WorkflowDefinition saveOrUpdateWorkflowDefinition(
			WorkflowDefinition workflowDef, Boolean isInsert) {
		if (isInsert) {//判断是否作为新版保存
			String hql = "select max(version) from WorkflowDefinition where processId=?0";
			Object[] params = new Object[] { workflowDef.getProcessId() };
			Integer version = (Integer) this.getHibernateTemplate()
					.find(hql, params).get(0);
			version = version == null ? 1 : version + 1;
			workflowDef.setVersion(version);
			this.getHibernateTemplate().save(workflowDef);
		} else {//发布
			this.getHibernateTemplate().saveOrUpdate(workflowDef);
		}
		return workflowDef;
	}

	@Override
	public WorkflowDefinition getWorkflowDefinitionByProcessIdAndVersionNumber(
			String processId, Integer version) {
		String hql = "from WorkflowDefinition where processId=?0 and version=?1";
		Object[] params = new Object[] { processId, version };
		List<WorkflowDefinition> workflowDefinitions = (List<WorkflowDefinition>) this
				.getHibernateTemplate().find(hql, params);
		WorkflowDefinition workflowDefinition = workflowDefinitions.get(0);
		return workflowDefinition;
	}

	@Override
	public WorkflowDefinition getTheLatestVersionOfWorkflowDefinition(
			String processId) {
		String hql = "from WorkflowDefinition where processId=:processId and version = (select max(version) from WorkflowDefinition where processId=:processId)";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("processId", processId);
		List<WorkflowDefinition> list = query.list();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unused")
	@Override
	public WorkflowDefinition getWorkflowDefinitionByDefId(String wfDefinitionId) {
		String hql = "from WorkflowDefinition where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", wfDefinitionId);
		List<WorkflowDefinition> list = query.list();
		if (list.isEmpty()) {
			return null;
		}
		RuntimeContext cddContext = runtimeContext;
		return list.get(0);
	}

	@Override
	public List<WorkflowDefinition> getWorkflowDefinitions() {
		// PersistenceFactory persistenceFactory = new PersistenceFactory();
		String hql = "from WorkflowDefinition";
		// Query query = persistenceFactory.createQuery(hql);
		List<WorkflowDefinition> workflowDefinitions = (List<WorkflowDefinition>) this.getHibernateTemplate().find(hql, null);// (List<WorkflowDefinition>)
														// query.list();
		// persistenceFactory.destroy();
		return workflowDefinitions;
	}

	@Override
	public void saveOrUpdateTaskInstance(ITaskInstance taskInstance) {

		// 更新
		if (StringUtils.isNotBlank(taskInstance.getId())) {
			// 生成需要保存的数据集合 IsCacheData是否关闭缓存
			if (IsCacheData()) {
				UpdateTaskInstanceData(taskInstance);
			}
			UpdateTaskInstanceCommand(taskInstance);
		} else {
			String uuid = UUID.randomUUID().toString();
			taskInstance.setId(uuid);
			if (IsCacheData()) {
				SaveTaskInstanceData(taskInstance);
			}
			SaveTaskInstanceCommand(taskInstance);
		}

	}

	private void UpdateTaskInstanceData(ITaskInstance taskInstance) {
		List<TaskInstance> listInstances = (List<TaskInstance>) getRuntimeContext()
				.getCacheData().get(TablesName.taskInstance);

		for (TaskInstance instance : listInstances) {
			if (instance.getId().equals(taskInstance.getId())) {
				// 移除
				listInstances.remove(instance);
				// 添加
				listInstances.add((TaskInstance) taskInstance);

				break;
			}
		}
	}

	private void UpdateTaskInstanceCommand(ITaskInstance taskInstance) {

		StringBuilder strSql = new StringBuilder();
		strSql.append(" update  " + TablesName.taskInstance + " ");
		strSql.append(" set Task_Id =:Task_Id ,");
		strSql.append(" Activity_Id =:Activity_Id ,");
		strSql.append(" forwardMode =:forwardMode ,");
		strSql.append(" Name =:Name ,");
		strSql.append(" State =:State ,");
		strSql.append(" Suspended =:Suspended ,");
		strSql.append(" Task_Type =:Task_Type ,");
		strSql.append(" Created_Time =:Created_Time ,");
		strSql.append(" Started_Time =:Started_Time ,");
		strSql.append(" Expired_Time =:Expired_Time ,");
		strSql.append(" End_Time =:End_Time ,");
		strSql.append(" Assignment_Strategy =:Assignment_Strategy ,");
		strSql.append(" Processinstance_Id =:Processinstance_Id ,");
		strSql.append(" Target_Activity_Id =:Target_Activity_Id ,");
		strSql.append(" From_Activity_Id =:From_Activity_Id ,");
		strSql.append(" Step_Number =:Step_Number ");
		// strSql.append(" Biz_Typeid =:Biz_Typeid ,");
		// strSql.append(" Biz_type =:Biz_type ,");
		// strSql.append(" Biz_Info =:Biz_Info ,");
		// strSql.append(" Assignment_Type =:Assignment_Type ");
		strSql.append(" where id=:Id");

		Query query = this.createSqlQuery(strSql.toString());

		query.setParameter("Id", taskInstance.getId());
		query.setParameter("Task_Id", taskInstance.getTaskId());
		query.setParameter("Activity_Id", taskInstance.getActivityId());
		query.setParameter("forwardMode", taskInstance.getForwardMode());
		query.setParameter("Name", taskInstance.getName());
		query.setParameter("State", taskInstance.getState());
		query.setParameter("Suspended", taskInstance.getIsSuspend());
		query.setParameter("Task_Type", taskInstance.getTaskTypeEnum());
		query.setParameter("Created_Time", taskInstance.getCreatedTime());
		query.setParameter("Started_Time", taskInstance.getStartedTime());
		query.setParameter("Expired_Time", taskInstance.getExpiredTime());
		query.setParameter("End_Time", taskInstance.getEndTime());
		query.setParameter("Assignment_Strategy",
				taskInstance.getAssignmentStrategyEnum());
		query.setParameter("Processinstance_Id",
				taskInstance.getProcessInstanceId());
		query.setParameter("Target_Activity_Id",
				taskInstance.getTargetActivityId());
		query.setParameter("From_Activity_Id", taskInstance.getFromActivityId());
		query.setParameter("Step_Number", taskInstance.getStepNumber());

		// query.setParameter("Biz_Typeid",null);//对象中没有此属性
		// query.setParameter("Biz_type",null);//对象中没有此属性
		// query.setParameter("Biz_Info",null);//对象中没有此属性
		// query.setParameter("Assignment_Type",null);//对象中没有此属性

		if (this.IsCacheData()) {
			// 将命令增加到总线上
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void SaveTaskInstanceData(ITaskInstance taskInstance) {

		List<TaskInstance> listInstances = (List<TaskInstance>) getRuntimeContext()
				.getCacheData().get(TablesName.taskInstance);
		listInstances.add((TaskInstance) taskInstance);
	}

	private void SaveTaskInstanceCommand(ITaskInstance taskInstance) {

		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.taskInstance + " (");
		strSql.append("Id,Task_Id,Activity_Id,forwardMode,Name,State,Suspended,Task_Type,Created_Time,Started_Time,Expired_Time,End_Time,Assignment_Strategy,Processinstance_Id,Target_Activity_Id,From_Activity_Id,Step_Number )");
		strSql.append(" values (");
		strSql.append(" :Id,:Task_Id,:Activity_Id,:forwardMode,:Name,:State,:Suspended,:Task_Type,"
				+ ":Created_Time,:Started_Time,:Expired_Time,:End_Time,:Assignment_Strategy,:Processinstance_Id,"
				+ ":Target_Activity_Id,:From_Activity_Id,:Step_Number )");

		Query query = this.createSqlQuery(strSql.toString());

		query.setParameter("Id", taskInstance.getId());
		query.setParameter("Task_Id", taskInstance.getTaskId());
		query.setParameter("Activity_Id", taskInstance.getActivityId());
		query.setParameter("forwardMode", taskInstance.getForwardMode());
		query.setParameter("Name", taskInstance.getName());
		query.setParameter("State", taskInstance.getState());
		query.setParameter("Suspended", taskInstance.getIsSuspend());
		query.setParameter("Task_Type", taskInstance.getTaskTypeEnum());
		query.setParameter("Created_Time", (Date)sqlParamFilter(taskInstance.getCreatedTime()));
		query.setParameter("Started_Time", (Date)sqlParamFilter(taskInstance.getStartedTime()));
		query.setParameter("Expired_Time", (Date)sqlParamFilter(taskInstance.getExpiredTime()));
		query.setParameter("End_Time", taskInstance.getEndTime());
		query.setParameter("Assignment_Strategy",
				taskInstance.getAssignmentStrategyEnum());
		query.setParameter("Processinstance_Id",
				taskInstance.getProcessInstanceId());
		query.setParameter("Target_Activity_Id",
				taskInstance.getTargetActivityId());
		query.setParameter("From_Activity_Id", taskInstance.getFromActivityId());
		query.setParameter("Step_Number", taskInstance.getStepNumber());
//		query.setParameter("Biz_Typeid", null);// 对象中没有此属性
//		query.setParameter("Biz_type", null);// 对象中没有此属性
//		query.setParameter("Biz_Info", null);// 对象中没有此属性
//		query.setParameter("Assignment_Type", null);// 对象中没有此属性

		if (this.IsCacheData()) {
			// 将命令追加到总线上
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	@Override
	public List<IToken> getTokensForProcessInstance(String processInstanceId,
			String nodeId) {
		List<IToken> tempTokens = new ArrayList<IToken>();
		if (this.IsCacheData()) {
			List<IToken> tokens = (List<IToken>) getRuntimeContext()
					.getCacheData().get(TablesName.token);
			for (IToken token : tokens) {
				if (token.getNodeId().equals(nodeId)
						&& token.getProcessInstanceId().equals(
								processInstanceId)) {
					tempTokens.add(token);
				}
			}
		} else {
			List<IToken> tokens = getTokenList(processInstanceId, nodeId);
			tempTokens.addAll(tokens);
		}
		return tempTokens;
	}

	private List<IToken> getTokenList(String processInstanceId, String nodeId) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("select * from " + TablesName.token);
		strSql.append(" where PROCESSINSTANCE_ID= :processInstanceId and NODE_ID= :nodeId ");
		SQLQuery query = this.createSqlQuery(strSql.toString()).addEntity(
				Token.class);
		query.setParameter("processInstanceId", processInstanceId);
		query.setParameter("nodeId", nodeId);
		return query.list();
	}

	@Override
	public List<ITaskInstance> getTaskInstancesForProcessInstance(
			String processInstanceId, String activityId) {

		List<ITaskInstance> listTaskInstances = new ArrayList<ITaskInstance>();
		if (this.IsCacheData()) {
			List<ITaskInstance> listITasks = (List<ITaskInstance>) getRuntimeContext()
					.getCacheData().get(TablesName.taskInstance);

			for (ITaskInstance iTask : listITasks) {

				if (iTask.getProcessInstanceId().equals(processInstanceId)
						&& iTask.getActivityId().equals(activityId)) {
					listTaskInstances.add(iTask);
				}
			}
		} else {
			StringBuilder strSql = new StringBuilder();
			strSql.append("select Id,Task_Id,Activity_Id,forwardMode,Name,State,Suspended,Task_Type,Created_Time,Started_Time,Expired_Time,End_Time,Assignment_Strategy,ProcessInstance_Id,Target_Activity_Id,From_Activity_Id,Step_Number");
			strSql.append(" FROM " + TablesName.taskInstance);
			strSql.append(" where ProcessInstance_Id =:ProcessInstanceId and Activity_Id =:ActivityId");
			Query query = this.createSqlQuery(strSql.toString()).addEntity(
	                TaskInstance.class);
			query.setParameter("ProcessInstanceId", processInstanceId);
			query.setParameter("ActivityId", activityId);

			listTaskInstances = query.list();
		}
		return listTaskInstances;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IProcessInstance getProcessInstanceById(String processInstanceId) {
		ProcessInstance processInstance = null;
		if (this.IsCacheData())// 从缓存中查询
		{
			List<ProcessInstance> listInstances = (List) getRuntimeContext()
					.getCacheData().get(TablesName.processInstance);
			for (ProcessInstance pInstance : listInstances) {
				if (pInstance.getId().equals(processInstanceId)) {
					processInstance = pInstance;
					break;
				}
			}
		} else// 从数据库查询
		{
			String getProcessInstanceSql = "select * from "
					+ TablesName.processInstance + " where id=:id";
			Query query = this.createSqlQuery(getProcessInstanceSql).addEntity(ProcessInstance.class);
			query.setParameter("id", processInstanceId);
			processInstance = (ProcessInstance) query.list().get(0);
		}
		return processInstance;
	}

	@Override
	public ITaskInstance getTaskInstanceByWorkItemId(String id) {

		if (this.IsCacheData())// 开启缓存，从缓存中查询
		{
			List<WorkItemInstance> listInstances = (List<WorkItemInstance>) getRuntimeContext()
					.getCacheData().get(TablesName.workItem);
			for (WorkItemInstance wInstance : listInstances) {
				if (wInstance.getId().equals(id)) {

					List<TaskInstance> listTasks = (List<TaskInstance>) getRuntimeContext()
							.getCacheData().get(TablesName.taskInstance);
					for (TaskInstance task : listTasks) {
						if (task.getId().equals(wInstance.getTaskInstanceId())) {
							return task;
						}
					}
					break;
				}
			}
			return null;
		} else// 从数据库中查询
		{
			StringBuilder strSql = new StringBuilder();
			strSql.append("select  t.* from " + TablesName.workItem + " w, "
					+ TablesName.taskInstance + " t ");
			strSql.append(" where w.TaskInstance_Id=t.Id and  w.Id =:Id  ");//limit 1
			Query query = this.getSession().createSQLQuery(strSql.toString())
					.addEntity(TaskInstance.class);
			query.setParameter("Id", id);

			List<TaskInstance> list = query.list();
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		}
	}

	@Override
	public Boolean saveOrUpdateProcessInstance(IProcessInstance processInstance) {

		// 生成需要保存的数据集合 IsCacheData是否关闭缓存
		if (StringUtils.isNotBlank(processInstance.getId())) {
			if (IsCacheData()) {
				updateProcessInstanceData(processInstance);
			}
			updateProcessInstanceCommand(processInstance);

		} else {
			String uuid = UUID.randomUUID().toString();
			processInstance.setId(uuid);
			if (IsCacheData()) {
				SaveProcessInstanceData(processInstance);
			}
			SaveProcessInstanceCommand(processInstance);
		}
		return true;
	}

	private void SaveProcessInstanceCommand(IProcessInstance processInstance) {

		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.processInstance + " (");
		strSql.append("Id,process_id,version,Name,display_name,state,suspended,creator_id,Created_Time,Started_Time,Expired_Time,End_Time,Parent_ProcessInstance_Id,Parent_TaskInstance_Id,CompeleteMode,ProcessDescription,EntityID,EntityType,creator_Name,xmid,jdtype)");
		strSql.append(" values (");
		strSql.append(":Id,:ProcessId,:Version,:Name,:DisplayName,:State,:Suspended,:CreatorId,:CreatedTime,:StartedTime,:ExpiredTime,:EndTime,:ParentProcessInstanceId,:ParentTaskInstanceId,:CompeleteMode,:ProcessDescription,:EntityID,:EntityType,:creatorName,:xmid,:jdtype)");

		Query query = this.createSqlQuery(strSql.toString());

		query.setParameter("Id", processInstance.getId());
		query.setParameter("ProcessId", processInstance.getProcessId());
		query.setParameter("Version", processInstance.getVersion());
		query.setParameter("Name", StringUtils.isNotBlank(processInstance.getFlowshort())?processInstance.getFlowshort():processInstance.getName());//个性化名称
		query.setParameter("DisplayName", processInstance.getDisplayName());// 对象没有，后添加
		query.setParameter("State", processInstance.getState());
		query.setParameter("Suspended", processInstance.getIsSuspended());
		query.setParameter("CreatorId", processInstance.getCreatorId());
		query.setParameter("CreatedTime", processInstance.getCreatedTime());
		query.setParameter("StartedTime", processInstance.getStartedTime());
		query.setParameter("ExpiredTime", processInstance.getExpiredTime());
		query.setParameter("EndTime", processInstance.getEndTime());
		query.setParameter("ParentProcessInstanceId",
                processInstance.getParentProcessInstanceId());
		query.setParameter("ParentTaskInstanceId",
				processInstance.getParentTaskInstanceId());
		query.setParameter("CompeleteMode", processInstance.getCompeleteMode());// 对象中没有,后添加
		query.setParameter("ProcessDescription",
				processInstance.getProcessDescription());
		query.setParameter("EntityID", processInstance.getEntityId());
		query.setParameter("EntityType",
		        processInstance.getEntityType());
		query.setParameter("creatorName", processInstance.getCreatorName());
		query.setParameter("xmid", processInstance.getXmid());
		query.setParameter("jdtype", processInstance.getJdtype());
		if (this.IsCacheData()) {
			// 将命令追加到总线上
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void SaveProcessInstanceData(IProcessInstance processInstance) {
		List<ProcessInstance> listInstances = (List<ProcessInstance>) getRuntimeContext()
				.getCacheData().get(TablesName.processInstance);
		listInstances.add((ProcessInstance) processInstance);

	}

	private void updateProcessInstanceCommand(IProcessInstance processInstance) {

		StringBuilder strSql = new StringBuilder();
		strSql.append(" update  " + TablesName.processInstance + " ");
		strSql.append(" set version =:Version ,");
		strSql.append("process_id =:ProcessId ,");
		strSql.append(" name =:Name ,");
		strSql.append(" display_name =:DisplayName ,");
		strSql.append(" state =:State ,");
		strSql.append(" suspended =:Suspended ,");
		strSql.append(" creator_id =:CreatorId ,");
		strSql.append(" created_time =:CreatedTime ,");
		strSql.append(" started_time =:StartedTime ,");
		strSql.append(" expired_time =:ExpiredTime ,");
		strSql.append(" end_time =:EndTime ,");
		strSql.append(" parent_processinstance_id =:ParentProcessInstanceId ,");
		strSql.append(" parent_taskinstance_id =:ParentTaskInstanceId ,");
		strSql.append(" compeleteMode =:CompeleteMode ,");
		strSql.append(" processDescription =:ProcessDescription ");
		strSql.append(" where id=:Id");

		Query query = this.createSqlQuery(strSql.toString());

		query.setParameter("Id", processInstance.getId());
		query.setParameter("ProcessId", processInstance.getProcessId());
		query.setParameter("Version", processInstance.getVersion());
		query.setParameter("Name", StringUtils.isNotBlank(processInstance.getName())?processInstance.getName():processInstance.getFlowshort());
		query.setParameter("DisplayName", sqlParamFilter(processInstance.getDisplayName()));
		query.setParameter("State", processInstance.getState());
		query.setParameter("Suspended", processInstance.getIsSuspended());
		query.setParameter("CreatorId", processInstance.getCreatorId());
		query.setParameter("CreatedTime", sqlParamFilter(processInstance.getCreatedTime()));
		query.setParameter("StartedTime", processInstance.getStartedTime());
		query.setParameter("ExpiredTime", processInstance.getExpiredTime());
		query.setParameter("EndTime", processInstance.getEndTime());
		query.setParameter("ParentProcessInstanceId",
				processInstance.getParentProcessInstanceId());
		query.setParameter("ParentTaskInstanceId",
				processInstance.getParentTaskInstanceId());
		query.setParameter("CompeleteMode", processInstance.getCompeleteMode());
		query.setParameter("ProcessDescription",
				processInstance.getProcessDescription());

		if (this.IsCacheData()) {
			// 将命令增加到总线上
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void updateProcessInstanceData(IProcessInstance processInstance) {
		List<ProcessInstance> listInstances = (List<ProcessInstance>) getRuntimeContext()
				.getCacheData().get(TablesName.processInstance);

		for (ProcessInstance instance : listInstances) {
			if (instance.getId().equals(processInstance.getId())) {
				// 移除
				listInstances.remove(instance);
				// 添加
				listInstances.add((ProcessInstance) processInstance);
			}
		}

	}

	@Override
	public Integer getAliveWorkItemCountForTaskInstance(String taskInstId) {
		int count = 0;
		if (this.IsCacheData()) {
			List<IWorkItemInstance> workItemInstances = (List<IWorkItemInstance>) this
					.getRuntimeContext().getCacheData()
					.get(TablesName.workItem);
			for (IWorkItemInstance workItemInstance : workItemInstances) {
				if (workItemInstance.getTaskInstanceId().equals(taskInstId) && 
						(workItemInstance.getState()==10 || workItemInstance.getState()==20)) {
					count++;
				}
			}
		} else {
			StringBuilder strSql = new StringBuilder();
			strSql.append("select count(*) from " + TablesName.workItem);
			strSql.append(" where TASKINSTANCE_ID=:taskInstanceId and STATE in (10,20)");
			SQLQuery query = this.createSqlQuery(strSql.toString());
			query.setParameter("taskInstanceId", taskInstId);

			if (!(query.list().isEmpty())) {
				count = Integer.valueOf((String.valueOf(query.list().get(0))));
			}
		}
		return count;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int getCompletedTaskInstanceCountForTask(String processInstanceId,
			String taskId) {

		if (this.IsCacheData()) {
			List<TaskInstance> listTasks = (List<TaskInstance>) getRuntimeContext()
					.getCacheData().get(TablesName.taskInstance);
			int count = 0; // 符合条件的个数
			for (TaskInstance tInstance : listTasks) {

				if (tInstance.getProcessInstanceId().equals(processInstanceId)
						&& tInstance.getTaskId().equals(taskId)
						&& tInstance.getState().equals(30)) {
					count++;
				}
			}
			return count;
		} else {
			StringBuilder strSql = new StringBuilder();
			strSql.append("select count(*) FROM " + TablesName.taskInstance);
			strSql.append(" where ProcessInstance_Id=:ProcessInstanceId and Task_Id=:TaskId and State=30");

			Query query = this.createSqlQuery(strSql.toString());
			query.setParameter("ProcessInstanceId", processInstanceId);
			query.setParameter("TaskId", taskId);
			List list = query.list();

			if (list == null) {
				return 0;
			} else {
				return Integer.parseInt(list.get(0).toString());
			}
		}

	}

	@Override
	public ITaskInstance getLastCompletedTaskInstance(String processInstanceId,
			String taskId) {

		if (this.IsCacheData()) {
			List<TaskInstance> listTasks = (List<TaskInstance>) getRuntimeContext()
					.getCacheData().get(TablesName.taskInstance);

			for (TaskInstance tInstance : listTasks) {

				if (tInstance.getProcessInstanceId().equals(processInstanceId)
						&& tInstance.getTaskId().equals(taskId)
						&& tInstance.getState() ==TaskInstanceStateEnum.completed.intValue()) {
					return tInstance;
				}
			}
			return null;
		} else {
			StringBuilder strSql = new StringBuilder();
			// strSql.append("select Id,Task_Id,Activity_Id,Name,State,Suspended,Task_Type,Created_Time,Started_Time,Expired_Time,End_Time,Assignment_Strategy,ProcessInstance_Id,Target_Activity_Id,From_Activity_Id,Step_Number ");
			strSql.append("select *  ");
			strSql.append(" from " + TablesName.taskInstance);
			strSql.append(" where ProcessInstance_Id =:ProcessInstanceId and Task_Id =:TaskId and State=100 order by step_number desc");//limit 1 

			Query query = this.getSession().createSQLQuery(strSql.toString())
					.addEntity(TaskInstance.class);
			query.setParameter("TaskId", taskId);
			query.setParameter("ProcessInstanceId", processInstanceId);

			List<TaskInstance> list = query.list();
			if (list != null && list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		}
	}

	@Override
	public ITaskInstance getTaskInstanceByParentId(String ParentTaskInstanceId) {
		if (this.IsCacheData()) {
			List<ProcessInstance> listInstances = (List<ProcessInstance>) getRuntimeContext()
					.getCacheData().get(TablesName.processInstance);
			for (ProcessInstance pInstance : listInstances) {
				if (pInstance.getParentTaskInstanceId().equals(
						ParentTaskInstanceId)) {
					List<ITaskInstance> listTasks = (List<ITaskInstance>) getRuntimeContext()
							.getCacheData().get(TablesName.taskInstance);
					for (ITaskInstance iTaskInstance : listTasks) {
						if (iTaskInstance.getProcessInstanceId().equals(
								pInstance.getId())) {
							return iTaskInstance;
						}
					}
				}
			}

			return null;
		} else {
			StringBuilder strSql = new StringBuilder();
			strSql.append("select  t.* from " + TablesName.taskInstance
					+ " t left join " + TablesName.processInstance
					+ " p on t.ProcessInstance_Id=p.Id");
			strSql.append(" and p.Parent_TaskInstance_Id=:ParentTaskInstanceId  ");//limit 1
			Query query = this.getSession().createSQLQuery(strSql.toString())
					.addEntity(TaskInstance.class);
			query.setParameter("ParentTaskInstanceId", ParentTaskInstanceId);

			List<TaskInstance> list = query.list();

			if (list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		}
	}

	@Override
	public void saveOrUpdateToken(IToken token) {
		if (null != token.getId()) {
			if (this.IsCacheData()) {
				updateTokenData(token);
			}
			updateTokenCommand(token);
		} else {
			String id = UUID.randomUUID().toString();
			token.setId(id);
			if (this.IsCacheData()) {
				saveTokenData(token);
			}
			saveTokenCommand(token);
		}
	}

	private void updateTokenData(IToken token) {
		List<IToken> tokens = (List<IToken>) this.getRuntimeContext()
				.getCacheData().get(TablesName.token);
		for (int index = 0; index < tokens.size(); index++) {
			if (tokens.get(index).getId().equals(token.getId())) {
				tokens.set(index, token);
				break;
			}
		}
	}

	private void saveTokenCommand(IToken token) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.token);
		strSql.append(" (ID,NODE_ID,PROCESSINSTANCE_ID,STEP_number,FROM_ACTIVITY_ID,ALIVE)");
		strSql.append(" values(:id,:nodeId,:processInstanceId,:stepNumber,:fromActivityId,:isAlive)");
		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("id", token.getId());
		query.setParameter("nodeId", token.getNodeId());
		query.setParameter("processInstanceId", token.getProcessInstanceId());
		query.setParameter("stepNumber", token.getStepNumber());
		query.setParameter("fromActivityId", token.getFromActivityId());
		query.setParameter("isAlive", token.getIsAlive());
		if (this.IsCacheData()) {
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void saveTokenData(IToken token) {
		List<IToken> tokens = (List<IToken>) this.getRuntimeContext()
				.getCacheData().get(TablesName.token);
		tokens.add(token);
	}

	private void updateTokenCommand(IToken token) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("update " + TablesName.token);
		strSql.append(" set NODE_ID = :nodeId,");
		strSql.append("PROCESSINSTANCE_ID = :processInstanceId,");
		strSql.append("STEP_number= :stepNumber,");
		strSql.append("FROM_ACTIVITY_ID= :fromActivityId,");
		strSql.append("ALIVE= :isAlive");
		strSql.append(" where ID= :id ");

		SQLQuery query = this.createSqlQuery(strSql.toString());

		query.setParameter("nodeId", token.getNodeId());
		query.setParameter("processInstanceId", token.getProcessInstanceId());
		query.setParameter("stepNumber", token.getStepNumber());
		query.setParameter("fromActivityId", token.getFromActivityId());
		query.setParameter("isAlive", token.getIsAlive());
		query.setParameter("id", token.getId());

		if (this.IsCacheData()) {
			addCommand(query);
		}else {
			query.executeUpdate();
		}
		
	}

	@Override
	public void deleteTokensForNode(String processInstanceId, String nodeId) {
		if (this.IsCacheData()) {
			deleteTokenData(nodeId, processInstanceId);
		}
		deleteTokenCommand(nodeId, processInstanceId);
	}

	private void deleteTokenData(String nodeId, String processInstanceId) {
		List<IToken> tokens = (List<IToken>) this.getRuntimeContext()
				.getCacheData().get(TablesName.token);
		for (int index = 0; index < tokens.size(); index++) {
			if (tokens.get(index).getProcessInstanceId()
					.equals(processInstanceId)
					&& tokens.get(index).getNodeId().equals(nodeId)) {
				tokens.remove(index);
			}
		}
	}

	private void deleteTokenCommand(String nodeId, String processInstanceId) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("delete from " + TablesName.token);
		strSql.append(" where NODE_ID = :nodeId and PROCESSINSTANCE_ID = :processInstanceId ");
		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("nodeId", nodeId);
		query.setParameter("processInstanceId", processInstanceId);
		if (this.IsCacheData()) {
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	@Override
	public Integer getAliveTokensCountForProcessInst(String processInstanceId) {
		// C#源码中此处 return 0 ,具体实现待定
		return 0;
	}

	@Override
	public Boolean saveOrUpdateWorkItemInstance(
			IWorkItemInstance workItemInstance) {
		if (StringUtils.isNotEmpty(workItemInstance.getId())) {
			// 生成需要保存的数据集合 IsCacheData是否关闭缓存
			if (this.IsCacheData()) {
				updateWorkItemInstanceData(workItemInstance);
			}
			// 生成命令
			updateWorkItemInstanceCommand(workItemInstance);
			if (workItemInstance.getState() == WorkItemInstanceStateEnum.completed.intValue()) {
				deleteCurrentTodoCommand(workItemInstance);
				saveTodoLogCommand(workItemInstance);
			} else {
				updateTodoCommand(workItemInstance);
			}
		} else {
			String id = UUID.randomUUID().toString();
			workItemInstance.setId(id);
			if (this.IsCacheData()) {
				saveWorkItemInstanceData(workItemInstance);
			}
			saveTodoCommand(workItemInstance);
			saveWorkItemInstanceCommand(workItemInstance);
		}
		return true;
	}

	private void updateTodoCommand(IWorkItemInstance workItemInstance) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("update " + TablesName.todo);
		strSql.append(" set ACTOR_ID=:actorId,");
		strSql.append("Post=:post,");
		strSql.append("WORKITEM_STATE=:workItemState,");
		strSql.append("CREATED_TIME=:createdTime,");
		strSql.append("CLAIMED_TIME=:claimedTime,");
		strSql.append("END_TIME=:endTime,");
		strSql.append("COMMENTS=:comments,");
		strSql.append("TASKINSTANCE_ID=:taskInstanceId,");
		strSql.append("TASKINSTANCE_NAME=:taskInstanceName,");
		strSql.append("ACTIVITY_ID=:activityId,");
//		strSql.append("ACTIVITY_DispalyName=:activityDisplayName,");
		strSql.append("TASK_ID=:taskId,");
		strSql.append("Step_Number=:stepNumber,");
		strSql.append("PROCESSINSTANCE_ID=:processInstanceId,");
		strSql.append("PROCESS_ID=:processId,");
		strSql.append("PROCESS_NAME=:processName,");
		strSql.append("Suspended=:isSuspended,");
		strSql.append("CREATOR_ID=:creatorId,");
		strSql.append("CREATOR_NAME=:creatorName,");
		strSql.append("FORMURL=:formUrl,");
		strSql.append("TODODESCRIPTION=:toDoDescription,");
		strSql.append("COMPELETEMODE=:compeleteMode ");
		// strSql.append("EntityType=@EntityType,");//待分配
		// strSql.append("EntityId=@EntityId,");//待分配
		// strSql.append("OrgId=@OrgId,");//待分配
		// strSql.append("IsTermination=@IsTermination");//待分配
		strSql.append(" where WORKITEM_ID=:workItemId");

		SQLQuery query = createSqlQuery(strSql.toString());

		query.setParameter("actorId", workItemInstance.getActorId());
		query.setParameter("post", workItemInstance.getPost());
		query.setParameter("workItemState", workItemInstance.getState());
		query.setParameter("createdTime", sqlParamFilter(workItemInstance.getCreatedTime()));
		query.setParameter("claimedTime", sqlParamFilter(workItemInstance.getClaimedTime()));
		query.setParameter("endTime", workItemInstance.getEndTime());
		query.setParameter("comments", workItemInstance.getComments());
		query.setParameter("taskInstanceId",
				workItemInstance.getTaskInstanceId());
		query.setParameter("taskInstanceName", workItemInstance
				.getTaskInstance().getName());
		query.setParameter("activityId", workItemInstance.getTaskInstance()
				.getActivityId());

		query.setParameter("taskId", workItemInstance.getTaskInstanceId());
		query.setParameter("stepNumber", workItemInstance.getTaskInstance()
				.getStepNumber());
		query.setParameter("processInstanceId",
				workItemInstance.getProcessInstanceId());
		query.setParameter("processId", workItemInstance.getProcessInstance().getProcessId());
		query.setParameter("processName", StringUtils.isNotBlank(workItemInstance.getProcessInstance().getFlowshort())?workItemInstance.getProcessInstance().getFlowshort():workItemInstance.getProcessInstance().getName());
		query.setParameter("isSuspended", workItemInstance.getTaskInstance()
				.getIsSuspend());
		query.setParameter("creatorId", this.getRuntimeContext().getUserInfo()
				.getId());
		query.setParameter("creatorName", this.getRuntimeContext().getUserInfo()
		        .getName());
		// query.setParameter("",
		// workItemInstance.getProcessInstance().getEntityType());//待分配EntityType
		// query.setParameter("",
		// workItemInstance.getProcessInstance().getEntityId());//待分配EntityId
		// query.setParameter("", workItemInstance.getOrgId());//表中不存在orgId字段
		// query.setParameter("",
		// workItemInstance.getProcessInstance().getIsTermination());//待分配IsTermination
		query.setParameter("formUrl", workItemInstance.getFormUrl());
		query.setParameter("toDoDescription", workItemInstance.getToDoDescription());
		query.setParameter("compeleteMode", workItemInstance.getCompeleteMode(), StandardBasicTypes.INTEGER );
		query.setParameter("workItemId", workItemInstance.getId());
		if (this.IsCacheData()) {
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void saveTodoLogCommand(IWorkItemInstance workItemInstance) {
		// EntityType,EntityId,OrgId,IsTermination字段尚未匹配
		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.todoLog);
		strSql.append(" (WORKITEM_ID,ACTOR_ID,ACTOR_NAME,Post,WORKITEM_STATE,CREATED_TIME,CLAIMED_TIME,END_TIME,COMMENTS,TASKINSTANCE_ID,TASKINSTANCE_NAME,ACTIVITY_ID,TASK_ID,STEP_number,PROCESSINSTANCE_ID,PROCESS_ID,PROCESS_NAME,SUSPENDED,CREATOR_ID,ENTITYID,ENTITYTYPE,TODODESCRIPTION,FORMURL,COMPELETEMODE,CREATOR_NAME,XMID,JDTYPE)");
		strSql.append(" values(:workItemId,:actorId,:actorName,:post,:workItemState,:createdTime,:claimedTime,:endTime,:comments,:taskInstanceId,:taskInstanceName,:activityId,:taskId,:stepNumber,:processInstanceId,:processId,:processName,:isSuspended,:creatorId,:entityId,:entityType,:toDoDescription,:formUrl,:completeMode,:creatorName,:xmid,:jdtype)");

		SQLQuery query = this.createSqlQuery(strSql.toString());

		query.setParameter("workItemId", workItemInstance.getId());
		query.setParameter("actorId", workItemInstance.getActorId());
		query.setParameter("actorName", workItemInstance.getActorName());
		query.setParameter("post", workItemInstance.getPost());
		query.setParameter("workItemState", workItemInstance.getState());
		query.setParameter("createdTime", sqlParamFilter(workItemInstance.getCreatedTime()));
		query.setParameter("claimedTime", sqlParamFilter(workItemInstance.getClaimedTime()));
		query.setParameter("endTime", sqlParamFilter(workItemInstance.getEndTime()));
		query.setParameter("comments", workItemInstance.getComments());
		query.setParameter("taskInstanceId",
				workItemInstance.getTaskInstanceId());
		query.setParameter("taskInstanceName", workItemInstance
				.getTaskInstance().getName());
		query.setParameter("activityId", workItemInstance.getTaskInstance()
				.getActivityId());

		query.setParameter("taskId", workItemInstance.getTaskInstanceId());
		query.setParameter("stepNumber", workItemInstance.getTaskInstance()
				.getStepNumber());
		query.setParameter("processInstanceId",
				workItemInstance.getProcessInstanceId());
		query.setParameter("processId", workItemInstance.getProcessInstance().getProcessId());
		
		
		
		query.setParameter("processName", StringUtils.isNotBlank(workItemInstance.getProcessInstance().getFlowshort())?workItemInstance.getProcessInstance().getFlowshort():workItemInstance.getProcessInstance().getName());
		query.setParameter("isSuspended", workItemInstance.getTaskInstance()
				.getIsSuspend());
		query.setParameter("creatorId", this.getRuntimeContext().getUserInfo()
				.getId());
		query.setParameter("entityId", workItemInstance.getProcessInstance().getEntityId());
		query.setParameter("entityType", workItemInstance.getProcessInstance().getEntityType());
		query.setParameter("toDoDescription", workItemInstance.getToDoDescription());
		query.setParameter("formUrl", workItemInstance.getFormUrl());
		query.setParameter("completeMode", workItemInstance.getRuntimeContext().getForwardMode());
		query.setParameter("creatorName", this.getRuntimeContext().getUserInfo().getName());
		query.setParameter("xmid", workItemInstance.getXmid());
		query.setParameter("jdtype", workItemInstance.getJdtype());
		// query.setParameter("",
		// workItemInstance.getProcessInstance().getEntityType());//待分配EntityType
		// query.setParameter("",
		// workItemInstance.getProcessInstance().getEntityId());//待分配EntityId
		// query.setParameter("", workItemInstance);//待分配OrgId
		// query.setParameter("",
		// workItemInstance.getProcessInstance().getIsTermination());//待分配IsTermination
		if (this.IsCacheData()) {
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	/**
	 * 
	 * 功能:根据workItemInstance删除todo数据<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param workItemInstance
	 */
	private void deleteCurrentTodoCommand(IWorkItemInstance workItemInstance) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("delete from " + TablesName.todo);
		strSql.append(" where WORKITEM_ID = :workItemId and TASKINSTANCE_ID = :taskInstanceId");
		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("workItemId", workItemInstance.getId());
		query.setParameter("taskInstanceId",
				workItemInstance.getTaskInstanceId());
		if (this.IsCacheData()) {
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void updateWorkItemInstanceCommand(
			IWorkItemInstance workItemInstance) {
		StringBuilder strSql = new StringBuilder();
		strSql.append(" update " + TablesName.workItem);
		strSql.append(" set STATE = :STATE,");
		strSql.append("CREATED_TIME = :CREATED_TIME,");
		strSql.append("CLAIMED_TIME = :CLAIMED_TIME,");
		strSql.append("END_TIME = :END_TIME,");
		strSql.append("ACTOR_ID = :ACTOR_ID,");
		strSql.append("ACTOR_NAME = :ACTOR_NAME,");
		strSql.append("POST = :POST,");
		strSql.append("COMMENTS = :COMMENTS,");
		strSql.append("TASKINSTANCE_ID = :TASKINSTANCE_ID,");
		strSql.append("VoteItem = :VoteItem,");
		strSql.append("PROCESSINSTANCE_ID = :processInstanceId,");
		strSql.append("TODODESCRIPTION = :toDoDescription,");
		strSql.append("FORMURL = :formUrl ");
		// strSql.append("OrgId = :orgId,");//该字段表中不存在
		strSql.append(" where id = :id");

		Query query = this.createSqlQuery(strSql.toString());
		query.setParameter("STATE", workItemInstance.getState());
		query.setParameter("CREATED_TIME", sqlParamFilter(workItemInstance.getCreatedTime()));
		query.setParameter("CLAIMED_TIME", sqlParamFilter(workItemInstance.getClaimedTime()));
		query.setParameter("END_TIME", workItemInstance.getEndTime());
		query.setParameter("ACTOR_ID", workItemInstance.getActorId());
		query.setParameter("ACTOR_NAME", workItemInstance.getActorName());
		query.setParameter("POST", workItemInstance.getPost());
		query.setParameter("COMMENTS", workItemInstance.getComments());
		query.setParameter("TASKINSTANCE_ID",
				workItemInstance.getTaskInstanceId());
		query.setParameter("VoteItem", workItemInstance.getVoteItem());
		query.setParameter("processInstanceId",
				workItemInstance.getProcessInstanceId());
		query.setParameter("toDoDescription",
		        workItemInstance.getToDoDescription());
		query.setParameter("formUrl",
		        workItemInstance.getFormUrl());
		// query.setParameter("orgId", workItemInstance.getOrgId());
		query.setParameter("id", workItemInstance.getId());
		if (this.IsCacheData()) {
			// 将命令增加到总线上
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	/**
	 * 
	 * 功能:更新缓存中的WorkItemInstance列表<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param workItemInstance
	 */
	private void updateWorkItemInstanceData(IWorkItemInstance workItemInstance) {
		// 取出虚拟表
		List<WorkItemInstance> workItemInstances = (List<WorkItemInstance>) getRuntimeContext()
				.getCacheData().get(TablesName.workItem);
		for (int index = 0; index < workItemInstances.size(); index++) {
			if (workItemInstances.get(index).getId()
					.equals(workItemInstance.getId())) {
				workItemInstances.set(index,
						(WorkItemInstance) workItemInstance);
			}
		}
	}

	private void saveWorkItemInstanceCommand(IWorkItemInstance workItemInstance) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.workItem);
		strSql.append("(ID,STATE,CREATED_TIME,CLAIMED_TIME,END_TIME,ACTOR_ID,ACTOR_NAME,POST,COMMENTS,");
		strSql.append("TASKINSTANCE_ID,VoteItem,PROCESSINSTANCE_ID,TODODESCRIPTION,FORMURL,XMID,JDTYPE)");
		strSql.append("values(:ID,:STATE,:CREATED_TIME,:CLAIMED_TIME,:END_TIME,:ACTOR_ID,:ACTOR_NAME,:POST,:COMMENTS,");
		strSql.append(":TASKINSTANCE_ID,:VoteItem,:PROCESSINSTANCE_ID,:TODODESCRIPTION,:formUrl,:xmid,:jdtype)");

		Query query = this.createSqlQuery(strSql.toString());
		query.setParameter("ID", workItemInstance.getId());
		query.setParameter("STATE", workItemInstance.getState());
		query.setParameter("CREATED_TIME", (Date)sqlParamFilter(workItemInstance.getCreatedTime()));
		query.setParameter("CLAIMED_TIME", workItemInstance.getClaimedTime());
		query.setParameter("END_TIME", workItemInstance.getEndTime());
		query.setParameter("ACTOR_ID", workItemInstance.getActorId());
		query.setParameter("ACTOR_NAME", workItemInstance.getActorName());
		query.setParameter("POST", workItemInstance.getPost());
		query.setParameter("COMMENTS", workItemInstance.getComments());
		query.setParameter("TASKINSTANCE_ID",
				workItemInstance.getTaskInstanceId());
		query.setParameter("VoteItem", workItemInstance.getVoteItem());
		query.setParameter("PROCESSINSTANCE_ID",
				workItemInstance.getProcessInstanceId());
		query.setParameter("TODODESCRIPTION",
		        workItemInstance.getToDoDescription());
		query.setParameter("formUrl",
		        workItemInstance.getFormUrl());
		query.setParameter("xmid",
		        workItemInstance.getXmid());
		query.setParameter("jdtype",
		        workItemInstance.getJdtype());
		// query.setParameter("OrgId", workItemInstance.getOrgId());

		if (this.IsCacheData()) {
			// 将命令追加到总线上
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void saveTodoCommand(IWorkItemInstance workItemInstance) {
		// 为完成分配字段 EntityType,EntityId,OrgId,IsTermination
		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.todo);
		strSql.append(" (WORKITEM_ID,ACTOR_ID,ACTOR_NAME,Post,WORKITEM_STATE,CREATED_TIME,CLAIMED_TIME,END_TIME,COMMENTS,TASKINSTANCE_ID,TASKINSTANCE_NAME,ACTIVITY_ID,TASK_ID,STEP_number,PROCESSINSTANCE_ID,PROCESS_ID,PROCESS_NAME,Suspended,CREATOR_ID,CREATOR_NAME,ENTITYID,ENTITYTYPE,TODODESCRIPTION,FORMURL,BATCHAPPROVAL,XMID,JDTYPE,ISTOBEREAD)");
		strSql.append(" values(:workItemId,:actorId,:actorName,:post,:workItemState,:createdTime,:claimedTime,:endTime,:comments,:taskInstanceId,:taskInstanceName,:activityId,:taskId,:stepNumber,:processInstanceId,:processId,:processName,:isSuspended,:creatorId,:creatorName,:entityId,:entityType,:toDoDescription,:formUrl,:batchApproval,:xmid,:jdtype,:isToBeRead)");

		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("workItemId", workItemInstance.getId());
		query.setParameter("actorId", workItemInstance.getActorId());
		query.setParameter("actorName", workItemInstance.getActorName());
		query.setParameter("post", workItemInstance.getPost());
		query.setParameter("workItemState", workItemInstance.getState());
		query.setParameter("createdTime", (Date)sqlParamFilter(workItemInstance.getCreatedTime()));
		query.setParameter("claimedTime", workItemInstance.getClaimedTime());
		query.setParameter("endTime", workItemInstance.getEndTime());
		query.setParameter("comments", workItemInstance.getComments());
		query.setParameter("taskInstanceId",
				workItemInstance.getTaskInstanceId());
		query.setParameter("taskInstanceName", workItemInstance
				.getTaskInstance().getName());
		query.setParameter("activityId", workItemInstance.getTaskInstance()
				.getActivityId());
		query.setParameter("taskId", workItemInstance.getTaskInstanceId());
		query.setParameter("stepNumber", workItemInstance.getTaskInstance()
				.getStepNumber());
		query.setParameter("processInstanceId",
				workItemInstance.getProcessInstanceId());
		query.setParameter("processId", workItemInstance.getProcessInstance().getProcessId());
		query.setParameter("processName", StringUtils.isNotBlank(workItemInstance.getProcessInstance().getFlowshort())?workItemInstance.getProcessInstance().getFlowshort():workItemInstance.getProcessInstance().getName());
		query.setParameter("isSuspended", workItemInstance.getTaskInstance()
				.getIsSuspend());// 待分配IsSuspended
		query.setParameter("creatorId", this.getRuntimeContext().getUserInfo()
				.getId());
		query.setParameter("creatorName", this.getRuntimeContext().getUserInfo()
		        .getName());
		query.setParameter("entityId", workItemInstance.getProcessInstance().getEntityId());
		query.setParameter("entityType", workItemInstance.getProcessInstance().getEntityType());
		query.setParameter("toDoDescription", workItemInstance.getToDoDescription());
		query.setParameter("formUrl", workItemInstance.getFormUrl());
		query.setParameter("batchApproval", workItemInstance.getBatchApproval());
		query.setParameter("xmid", workItemInstance.getXmid());
		query.setParameter("jdtype", workItemInstance.getJdtype());
		query.setParameter("isToBeRead", workItemInstance.getIsToBeRead());
		// query.setParameter("",
		// workItemInstance.getProcessInstance().getEntityType());//待分配EntityType
		// query.setParameter("",
		// workItemInstance.getProcessInstance().getEntityId());//待分配EntityId
		// query.setParameter("", workItemInstance.getOrgId());//待分配OrgId
		// query.setParameter("",
		// workItemInstance.getProcessInstance().getIsTermination());//待分配IsTermination

		if (this.IsCacheData()) {
			// 将命令追加到总线上
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void saveWorkItemInstanceData(IWorkItemInstance workItemInstance) {
		List<WorkItemInstance> workItemInstances = (List<WorkItemInstance>) getRuntimeContext()
				.getCacheData().get(TablesName.workItem);
		workItemInstances.add((WorkItemInstance) workItemInstance);
	}

	@Override
	public IWorkItemInstance getWorkItemById(String workItemInstanceId) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("select * from " + TablesName.workItem
				+ " where id = :id");
		SQLQuery query = this.createSqlQuery(strSql.toString()).addEntity(
				WorkItemInstance.class);// setResultTransformer(Transformers.aliasToBean(WorkItemInstance.class));
		query.setParameter("id", workItemInstanceId);
		List<IWorkItemInstance> rsList = query.list();
		if (ValidateUtil.isNullAndEmpty(rsList)) {
			return null;
		}
		return rsList.get(0);
	}

	/**
	 * 
	 * 功能:根据taskInstanceId获取WorkItemInstance列表<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param taskInstanceId
	 * @return
	 */
	@Override
	public List<IWorkItemInstance> getCompletedWorkItemsForTaskInstance(
			String taskInstanceId) {
		List<IWorkItemInstance> tempWorkItemInstances = new ArrayList<IWorkItemInstance>();
		if (this.IsCacheData()) {
			List<IWorkItemInstance> workItemInstances = (List<IWorkItemInstance>) this
					.getRuntimeContext().getCacheData()
					.get(TablesName.workItem);
			for (IWorkItemInstance workItemInstance : workItemInstances) {
				if (workItemInstance.getTaskInstanceId().equals(taskInstanceId)) {
					tempWorkItemInstances.add(workItemInstance);
				}
			}
		} else {
			List<IWorkItemInstance> workItemInstances = getWorkItemInstanceList(taskInstanceId);
			tempWorkItemInstances.addAll(workItemInstances);
		}
		return tempWorkItemInstances;
	}

	/**
	 * 
	 * 功能:获得WorkItemInstance列表<br>
	 * 约束:与本函数相关的约束<br>
	 * 
	 * @param taskInstanceId
	 * @return
	 */
	private List<IWorkItemInstance> getWorkItemInstanceList(
			String taskInstanceId) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("select *");// Id,State,CreatedTime,ClaimedTime,EndTime,ActorId,Post,Comments,TaskInstanceId,VoteItem,ProcessInstanceId,OrgId
		strSql.append(" from " + TablesName.workItem);
		strSql.append(" where TASKINSTANCE_ID = :taskInstanceId");
		SQLQuery query = this.createSqlQuery(strSql.toString()).addEntity(
				WorkItemInstance.class);
		query.setParameter("taskInstanceId", taskInstanceId);
		return query.list();
	}

	@Override
	public List<IWorkItemInstance> getWorkItemsByProcessInstId(
			String processInstanceId) {
		List<IWorkItemInstance> tempWorkItemInstances = new ArrayList<IWorkItemInstance>();
		if (this.IsCacheData()) {
			List<IWorkItemInstance> workItemInstances = (List<IWorkItemInstance>) this
					.getRuntimeContext().getCacheData()
					.get(TablesName.workItem);
			for (IWorkItemInstance workItemInstance : workItemInstances) {
				if (workItemInstance.getProcessInstanceId().equals(
						processInstanceId)) {
					tempWorkItemInstances.add(workItemInstance);
				}
			}
		} else {
			List<IWorkItemInstance> workItemInstances = getWorkItemInstanceListProcessInstId(processInstanceId);
			tempWorkItemInstances.addAll(workItemInstances);
		}
		return tempWorkItemInstances;
	}

	private List<IWorkItemInstance> getWorkItemInstanceListProcessInstId(
			String processInstanceId) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("select * from " + TablesName.workItem);
		strSql.append(" where PROCESSINSTANCE_ID=:processInstanceId");

		SQLQuery query = this.createSqlQuery(strSql.toString()).addEntity(
				WorkItemInstance.class);
		query.setParameter("processInstanceId", processInstanceId);
		return query.list();
	}

	@Override
	public List<IWorkItemInstance> getWorkItemsByProcessInstIdAndTaskInstanceId(
			String taskInstanceId, String processInstanceId) {
		List<IWorkItemInstance> tempWorkItemInstances = new ArrayList<IWorkItemInstance>();
		if (this.IsCacheData()) {
			List<IWorkItemInstance> workItemInstances = (List<IWorkItemInstance>) this
					.getRuntimeContext().getCacheData()
					.get(TablesName.workItem);
			for (IWorkItemInstance workItemInstance : workItemInstances) {
				if (workItemInstance.getProcessInstanceId().equals(
						processInstanceId)
						&& workItemInstance.getTaskInstanceId().equals(
								taskInstanceId)) {
					tempWorkItemInstances.add(workItemInstance);
				}
			}
		} else {
			List<IWorkItemInstance> workItemInstances = getListByProcessInstIdAndTaskInstanceId(
					taskInstanceId, processInstanceId);
			tempWorkItemInstances.addAll(workItemInstances);
		}
		return tempWorkItemInstances;
	}

	private List<IWorkItemInstance> getListByProcessInstIdAndTaskInstanceId(
			String taskInstanceId, String processInstanceId) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("select * from " + TablesName.workItem);
		strSql.append(" where PROCESSINSTANCE_ID = :processInstanceId and TASKINSTANCE_ID = :taskInstanceId");

		SQLQuery query = this.createSqlQuery(strSql.toString()).addEntity(
				WorkItemInstance.class);
		query.setParameter("processInstanceId", processInstanceId);
		query.setParameter("taskInstanceId", taskInstanceId);
		return query.list();
	}

	@Override
	public List<IWorkItemInstance> getNeedDoWorkItemsByProcessInstIdAndTaskInstId(
			String taskInstanceId, String processInstanceId) {
		
		List<IWorkItemInstance> tempWorkItemInstances = new ArrayList<IWorkItemInstance>();
		if (this.IsCacheData()) {
			List<IWorkItemInstance> workItemInstances = (List<IWorkItemInstance>) this
					.getRuntimeContext().getCacheData()
					.get(TablesName.workItem);
			for (IWorkItemInstance workItemInstance : workItemInstances) {
				if (workItemInstance.getProcessInstanceId().equals(processInstanceId)
						&& workItemInstance.getTaskInstanceId().equals(
								taskInstanceId)&&(workItemInstance.getState()==WorkItemInstanceStateEnum.initialized.intValue()||workItemInstance.getState()==WorkItemInstanceStateEnum.running.intValue())) {
					tempWorkItemInstances.add(workItemInstance);
				}
			}
		} else {
			List<IWorkItemInstance> workItemInstances = getListNeedDoWorkItemsByProcessInstIdAndTaskInstId(
					taskInstanceId, processInstanceId);
			tempWorkItemInstances.addAll(workItemInstances);
		}
		return tempWorkItemInstances;
	}

	@Override
	public List<IWorkItemInstance> getWorkItemsByTaskInstanceIdAndActorId(
			String actorId, String taskInstanceId) {
		List<IWorkItemInstance> tempWorkItemInstances = new ArrayList<IWorkItemInstance>();
		if (this.IsCacheData()) {
			List<IWorkItemInstance> workItemInstances = (List<IWorkItemInstance>) this
					.getRuntimeContext().getCacheData()
					.get(TablesName.workItem);
			for (IWorkItemInstance workItemInstance : workItemInstances) {
				if (workItemInstance.getActorId().equals(actorId)
						&& workItemInstance.getTaskInstanceId().equals(
								taskInstanceId)) {
					tempWorkItemInstances.add(workItemInstance);
				}
			}
		} else {
			List<IWorkItemInstance> workItemInstances = getListByTaskInstanceIdAndActorId(
					taskInstanceId, actorId);
			tempWorkItemInstances.addAll(workItemInstances);
		}
		return tempWorkItemInstances;
	}

	private List<IWorkItemInstance> getListByTaskInstanceIdAndActorId(
			String taskInstanceId, String actorId) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("select * from " + TablesName.workItem);
		strSql.append(" where PROCESSINSTANCE_ID = :processInstanceId and ActorId = :actorId");

		SQLQuery query = this.createSqlQuery(strSql.toString()).addEntity(
				WorkItemInstance.class);
		query.setParameter("actorId", actorId);
		query.setParameter("taskInstanceId", taskInstanceId);
		return query.list();
	}
	private List<IWorkItemInstance> getListNeedDoWorkItemsByProcessInstIdAndTaskInstId(
			String taskInstanceId, String processInstanceId) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("select * from " + TablesName.workItem);
		strSql.append(" where PROCESSINSTANCE_ID = :processInstanceId and TASKINSTANCE_ID = :taskInstanceId and STATE in(10,20)");

		SQLQuery query = this.createSqlQuery(strSql.toString()).addEntity(
				WorkItemInstance.class);
		query.setParameter("processInstanceId", processInstanceId);
		query.setParameter("taskInstanceId", taskInstanceId);
		return query.list();
	}
	@Override
	public Boolean deleteOtherWorkItemsById(IWorkItemInstance workItemInstance) {
	    this.deleteOtherTodosByWorkItem(workItemInstance);
	    if (this.IsCacheData()) {
            List<IWorkItemInstance> workItemInstances = (List<IWorkItemInstance>) this
                    .getRuntimeContext().getCacheData()
                    .get(TablesName.workItem);
            for(int i=0; i<workItemInstances.size(); ){
                IWorkItemInstance wItemInstance = workItemInstances.get(i);
                if (wItemInstance.getTaskInstanceId().equals(workItemInstance.getTaskInstanceId()) 
                        && !wItemInstance.getId().equals(workItemInstance.getId())) {
                    workItemInstances.remove(i);
                }else{
                    i++;
                }
            }
        }
	    StringBuilder strSql = new StringBuilder();
		strSql.append("delete from " + TablesName.workItem);
		strSql.append(" where Id<>:id and TASKINSTANCE_ID = :taskInstanceId");
		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("id", workItemInstance.getId());
		query.setParameter("taskInstanceId", workItemInstance.getTaskInstance());
		if (this.IsCacheData()) {
			addCommand(query);
		} else {
			query.executeUpdate();
		}
		return true;
	}

	private boolean deleteOtherTodosByWorkItem(IWorkItemInstance workItemInstance) {
	    if (this.IsCacheData()) {
	        List<ITodo> todos = (List<ITodo>) this
                    .getRuntimeContext().getCacheData()
                    .get(TablesName.todo);
	        for(int i=0; i<todos.size(); ){
	            ITodo todo = todos.get(i);
                if (todo.getTaskInstanceId().equals(workItemInstance.getTaskInstanceId()) 
                        && !todo.getWorkItemId().equals(workItemInstance.getId())) {
                    todos.remove(i);
                }else{
                    i++;
                }
            }
	    }
	    StringBuilder strSql = new StringBuilder();
        strSql.append("delete from " + TablesName.todo);
        strSql.append(" where WORKITEM_ID<>:workItemId and TASKINSTANCE_ID = :taskInstanceId");
        SQLQuery query = this.createSqlQuery(strSql.toString());
        query.setParameter("workItemId", workItemInstance.getId());
        query.setParameter("taskInstanceId", workItemInstance.getTaskInstance());
        if (this.IsCacheData()) {
            addCommand(query);
        } else {
            query.executeUpdate();
        }
        return true;
    }

    @Override
	public void saveOrUpdateProcessInstanceTrace(IProcessInstanceHistTrace trace) {

		if (trace.getId() != null) {
			// 生成需要保存的数据集合 IsCacheData是否关闭缓存
			if (this.IsCacheData()) {
				UpdateHistTraceData(trace);
			}
			// 生成命令
			UpdateHistTraceCommand(trace);

		} else {
			String id = UUID.randomUUID().toString();
			trace.setId(id);
			// 生成需要保存的数据集合 IsCacheData是否关闭缓存
			if (this.IsCacheData()) {
				SaveHistTraceData(trace);
			}
			SaveHistTraceCommand(trace);
		}

	}

	private void UpdateHistTraceData(IProcessInstanceHistTrace trace) {
		List<ProcessInstanceHistTrace> listInstances = (List<ProcessInstanceHistTrace>) getRuntimeContext()
				.getCacheData().get(TablesName.histTrace);
		for (ProcessInstanceHistTrace instance : listInstances) {
			if (instance.getId().equals(trace.getId())) {
				// 移除
				listInstances.remove(instance);
				// 添加
				listInstances.add((ProcessInstanceHistTrace) trace);
			}
		}

	}

	private void UpdateHistTraceCommand(IProcessInstanceHistTrace trace) {

		StringBuilder strSql = new StringBuilder();
		strSql.append(" update  " + TablesName.histTrace + " ");
		strSql.append(" set PROCESSINSTANCE_ID =:PROCESSINSTANCE_ID ,");
		strSql.append("STEP_number =:STEP_number ,");
		strSql.append(" MINOR_number =:MINOR_number ,");
		strSql.append(" TYPE =:TYPE ,");
		strSql.append(" EDGE_ID =:EDGE_ID ,");
		strSql.append(" FROM_NODE_ID =:FROM_NODE_ID ,");
		strSql.append(" TO_NODE_ID =:TO_NODE_ID ");
		strSql.append(" where ID =:ID");

		Query query = this.createSqlQuery(strSql.toString());
		query.setParameter("ID", trace.getId());
		query.setParameter("PROCESSINSTANCE_ID", trace.getProcessInstanceId());
		query.setParameter("STEP_number", trace.getStepNumber());
		query.setParameter("MINOR_number", trace.getMinorNumber()); // 对象中没有,后添加
		query.setParameter("TYPE", trace.getType());
		query.setParameter("EDGE_ID", trace.getEdgeId());
		query.setParameter("FROM_NODE_ID", trace.getFromNodeId());
		query.setParameter("TO_NODE_ID", trace.getToNodeId());

		if (this.IsCacheData()) {
			// 将命令增加到总线上
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void SaveHistTraceData(IProcessInstanceHistTrace trace) {
		List<ProcessInstanceHistTrace> listInstances = (List<ProcessInstanceHistTrace>) getRuntimeContext()
				.getCacheData().get(TablesName.histTrace);
		listInstances.add((ProcessInstanceHistTrace) trace);
	}

	private void SaveHistTraceCommand(IProcessInstanceHistTrace trace) {

		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.histTrace);
		strSql.append("(ID,PROCESSINSTANCE_ID,STEP_number,MINOR_number,TYPE,EDGE_ID,FROM_NODE_ID,TO_NODE_ID )");
		strSql.append("values(:ID,:PROCESSINSTANCE_ID,:STEP_number,:MINOR_number,:TYPE,:EDGE_ID,:FROM_NODE_ID,:TO_NODE_ID )");

		Query query = this.createSqlQuery(strSql.toString());
		query.setParameter("ID", trace.getId());
		query.setParameter("PROCESSINSTANCE_ID", trace.getProcessInstanceId());
		query.setParameter("STEP_number", trace.getStepNumber());
		query.setParameter("MINOR_number", trace.getMinorNumber()); // 对象中没有,后添加
		query.setParameter("TYPE", trace.getType());
		query.setParameter("EDGE_ID", trace.getEdgeId());
		query.setParameter("FROM_NODE_ID", trace.getFromNodeId());
		query.setParameter("TO_NODE_ID", trace.getToNodeId());

		if (this.IsCacheData()) {
			// 将命令追加到总线上
			addCommand((SQLQuery) query);
		} else {
			query.executeUpdate();
		}
	}

	@Override
	public boolean saveProcessInstanceVariable(ProcessInstanceVar var) {
		if (this.IsCacheData()) {
			saveProcessInstanceVarData(var);
		}
		saveProcessInstanceVarCommand(var);
		return true;
	}

	private void saveProcessInstanceVarCommand(ProcessInstanceVar var) {
		// 数据表中缺少ValueType、OrgId字段
		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.procInstVar);
		strSql.append(" (ID,PROCESSINSTANCE_ID,NAME,VALUE)"/* ,ValueType,OrgId)" */);
		strSql.append(" values(:id,:processInstanceId,:name,:value)"/*
																 * ,:valueType,:
																 * orgId)"
																 */);
		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("id", UUID.randomUUID().toString());
		query.setParameter("processInstanceId", var.getProcessInstanceId());
		query.setParameter("value", var.getValue());
		query.setParameter("name", var.getName());
		// query.setParameter("valueType", var.getValueType());
		// query.setParameter("orgId", var.getOrgId());
		if (this.IsCacheData()) {
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void saveProcessInstanceVarData(ProcessInstanceVar var) {
		List<ProcessInstanceVar> vars = (List<ProcessInstanceVar>) this
				.getRuntimeContext().getCacheData().get(TablesName.procInstVar);
		vars.add(var);
	}

	@Override
	public boolean updateProcessInstanceVariable(ProcessInstanceVar var) {
		// 生成需要保存的数据集合 IsCacheData是否关闭缓存
		if (this.IsCacheData()) {
			updateProcessInstanceVarData(var);
		}
		// 生成命令
		updateProcessInstanceVarCommand(var);
		return true;
	}

	private void updateProcessInstanceVarCommand(ProcessInstanceVar var) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("update " + TablesName.procInstVar);
		strSql.append(" set VALUE = :value ");
		// strSql.append("ValueType = :valueType,");
		// strSql.append("OrgId = :orgId,");
		strSql.append(" where PROCESSINSTANCE_ID= :processInstanceId and NAME = :name ");
		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("value", var.getValue());
		// query.setParameter("valueType", var.getValueType());
		// query.setParameter("orgId", var.getOrgId());
		query.setParameter("processInstanceId", var.getProcessInstanceId());
		query.setParameter("name", var.getName());
		if (this.IsCacheData()) {
			addCommand(query);
		} else {
			query.executeUpdate();
		}
	}

	private void updateProcessInstanceVarData(ProcessInstanceVar var) {
		List<ProcessInstanceVar> vars = (List<ProcessInstanceVar>) this
				.getRuntimeContext().getCacheData().get(TablesName.procInstVar);
		for (int index = 0; index < vars.size(); index++) {
			if (vars.get(index).getProcessInstanceId()
					.equals(var.getProcessInstanceId())) {
				vars.set(index, var);
			}
		}
	}

	@Override
	public List<ProcessInstanceVar> getProcessInstanceVariable(
			String processInstanceId) {
		List<ProcessInstanceVar> tempProcessInstanceVars = new ArrayList<ProcessInstanceVar>();
		if (this.IsCacheData()) {
			List<ProcessInstanceVar> processInstanceVars = (List<ProcessInstanceVar>) this
					.getRuntimeContext().getCacheData()
					.get(TablesName.procInstVar);
			for (ProcessInstanceVar var : processInstanceVars) {
				if (var.getProcessInstanceId().equals(processInstanceId)) {
					tempProcessInstanceVars.add(var);
				}
			}
		} else {
			List<ProcessInstanceVar> processInstanceVars = getProcessVarList(processInstanceId);
			tempProcessInstanceVars.addAll(processInstanceVars);
		}
		return tempProcessInstanceVars;
	}

	private List<ProcessInstanceVar> getProcessVarList(String processInstanceId) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("select * from " + TablesName.procInstVar);
		strSql.append(" where PROCESSINSTANCE_ID = :processInstanceId");
		SQLQuery query = this.createSqlQuery(strSql.toString()).addEntity(
				ProcessInstanceVar.class);
		query.setParameter("processInstanceId", processInstanceId);
		return query.list();
	}

	@Override
	public ProcessInstanceVar getProcessInstanceVariable(
			String processInstanceId, String name) {
		if (this.IsCacheData()) {
			List<ProcessInstanceVar> processInstanceVars = (List<ProcessInstanceVar>) this
					.getRuntimeContext().getCacheData()
					.get(TablesName.procInstVar);
			for (ProcessInstanceVar processInstanceVar : processInstanceVars) {
				if (processInstanceVar.getProcessInstanceId().equals(
						processInstanceId) && processInstanceVar.getName().equals(name) ) {
					return processInstanceVar;
				}
			}
		} else {
			StringBuilder strSql = new StringBuilder();
			strSql.append("select * from " + TablesName.procInstVar);
			strSql.append(" where PROCESSINSTANCE_ID = :processInstanceId and NAME = :name");
			SQLQuery query = this.createSqlQuery(strSql.toString());
			query.setParameter("processInstanceId", processInstanceId);
			query.setParameter("name", name);
			if (!(query.list().isEmpty())) {
				return (ProcessInstanceVar) query.list().get(0);
			}
		}
		return null;
	}

	@Override
	public List<ITodo> getTodoInstanceByActorId(String actorId,
			String entityType, String orgId) {

		List<ITodo> list = new ArrayList<ITodo>();

		if (this.IsCacheData()) {
			List<Todo> listTodo = (List<Todo>) getRuntimeContext()
					.getCacheData().get(TablesName.todo);
			if (listTodo != null && listTodo.size() > 0) {
				for (Todo todo : listTodo) {
					if (todo.getActorId().equals(actorId)) {
						list.add(todo);
					}
				}
			}
		} else {
			StringBuilder strSql = new StringBuilder();
			strSql.append("select WorkItem_Id,Actor_Id,Post,WorkItem_State,A.Created_Time,Claimed_Time,A.End_Time,Comments,TaskInstance_Id,"
					+ "TaskInstance_Name,Activity_Id,Activity_DisplayName,Task_Id,Step_Number,ProcessInstance_Id,B.Process_Id,B.Suspended,"
					+ "B.Creator_Id ");
//					+ "B.Entity_Type,B.Entity_Id,B.OrgId,B.Termination ");
			strSql.append(" FROM " + TablesName.todo + " A ");
			strSql.append(" inner join " + TablesName.processInstance
					+ " B   on A.ProcessInstance_Id=B.Id");
			strSql.append(" where Actor_Id=:Actor_Id  and B.Suspended=:Suspended ");
//					+ "and B.Entity_Type=:Entity_Type and B.OrgId=:OrgId and B.Termination=:Termination");

			Query query = this.getSession().createSQLQuery(strSql.toString())
					.addEntity(Todo.class);
			query.setParameter("ActorId", actorId);
//			query.setParameter("Entity_Type", entityType);
//			query.setParameter("OrgId", orgId);
			query.setParameter("Suspended", false);
//			query.setParameter("Termination", false);

			list = query.list();

		}
		return list;
	}

	@Override
	public List<ITodo> getPassEndProcessInstByActorId(String actorId,
			String orgId) {
		List<ITodo> list = new ArrayList<ITodo>();
		if (this.IsCacheData()) {
			List<ProcessInstance> pInstances = (List<ProcessInstance>) getRuntimeContext()
					.getCacheData().get(TablesName.processInstance);
			List<Todo> todos = (List<Todo>) getRuntimeContext().getCacheData()
					.get(TablesName.todo);
			for (ProcessInstance pt : pInstances) {
				for (Todo td : todos) {
					if (pt.getId().equals(td.getProcessInstanceId())
							&& td.getActorId().equals(actorId)
//							&& pt.getOrgId().equals(orgId)
							&& pt.getState().equals(
									ProcessInstanceStateEnum.completed)) {
						list.add(td);

					}
				}
			}
		} else {
			// processInstance表中字段欠缺，无法使用
			StringBuilder strSql = new StringBuilder();
			strSql.append("select t.* from " + TablesName.processInstance
					+ " p");
			strSql.append(" left join " + TablesName.todo + " t");
			strSql.append(" on p.Id=t.ProcessInstance_Id");
			strSql.append(" where t.Actor_Id=:ActorId and p.State=:State ");
//					"and p.Org_Id=:Org_Id"
			Query query = this.getSession().createSQLQuery(strSql.toString())
					.addEntity(Todo.class);
			query.setParameter("ActorId", actorId);
//			query.setParameter("Org_Id", orgId);
			query.setParameter("State", ProcessInstanceStateEnum.completed);

			list = query.list();
		}
		return list;
	}

	@Override
	public List<ITodo> getWorkflowHistory(String entityType, String entityId,
			String orgId, PagerNumber page) {

		// 表字段缺少，EntityId,EntityType,OrgId
		List<ITodo> list = new ArrayList<ITodo>();
		StringBuilder strSql = new StringBuilder();
		strSql.append("select * from");
		strSql.append("(");
		strSql.append("select * from ");
		strSql.append("(");
		strSql.append("select * from " + TablesName.todo);

		strSql.append(" where Entity_Type =:EntityType and Entity_Id=:EntityId and Org_Id=:OrgId");
		strSql.append(" UNION ");
		strSql.append("select * from " + TablesName.todoLog);

		strSql.append(" where Entity_Type=:EntityType and Entity_Id=:EntityId and Org_Id=:OrgId");
		strSql.append(") todo");
		strSql.append(") a");

		strSql.append(" order by Step_Number asc");
		Query query = this.getSession().createSQLQuery(strSql.toString())
				.addEntity(Todo.class);
		query.setParameter("EntityType", entityType);
		query.setParameter("EntityId", entityId);
		query.setParameter("OrgId", orgId);

		list = query.list();

		return list;

	}

	@Override
    public ITaskInstance getTaskInstanceById(String taskInstanceId) {
	    List<ITaskInstance> listTaskInstances = new ArrayList<ITaskInstance>();
        if (this.IsCacheData()) {
            List<ITaskInstance> listITasks = (List<ITaskInstance>) getRuntimeContext()
                    .getCacheData().get(TablesName.taskInstance);

            for (ITaskInstance iTask : listITasks) {

                if (iTask.getId().equals(taskInstanceId)) {
                    listTaskInstances.add(iTask);
                }
            }
        } else {
            StringBuilder strSql = new StringBuilder();
            strSql.append("select Id,Task_Id,Activity_Id,forwardMode,Name,State,Suspended,Task_Type,Created_Time,Started_Time,Expired_Time,End_Time,Assignment_Strategy,ProcessInstance_Id,Target_Activity_Id,From_Activity_Id,Step_Number");
            strSql.append(" FROM " + TablesName.taskInstance);
            strSql.append(" where id =:id");
            Query query = this.createSqlQuery(strSql.toString()).addEntity(
                    TaskInstance.class);
            query.setParameter("id", taskInstanceId);

            listTaskInstances = query.list();
        }
        return listTaskInstances.get(0);
    }
	
    @Override
    public ITaskInstance getUpTaskInstance(String taskInstanceId, String processInstanceId) {
        ITaskInstance taskInstance = this.getTaskInstanceById(taskInstanceId);
        List<ITaskInstance> taskInstanceList = null;
        if(this.IsCacheData()){
            taskInstanceList = (List<ITaskInstance>) runtimeContext.getCacheData().get(TablesName.taskInstance);
        }else{
            String taskInstanceSql = " select * from "
                    + TablesName.taskInstance
                    + " where processInstance_Id =:processInstanceId ";
            Query query = this.getSession().createSQLQuery(taskInstanceSql)
                    .addEntity(TaskInstance.class);
            query.setParameter("processInstanceId", processInstanceId);
            taskInstanceList = query.list();
        }
        return getUpTaskInstanceRecursion(taskInstance,taskInstanceList);
        
    }
    private ITaskInstance getUpTaskInstanceRecursion(ITaskInstance taskInstance, List<ITaskInstance> taskInstanceList){
        if (ForwardMode.advance.intValue() == taskInstance.getForwardMode()) {//如果当前任务为正常流转所得
            return getLastAdvanceTaskInstanceByActivityId(taskInstance.getFromActivityId(),taskInstanceList);
        }else{//如果当前任务为跳转所得
            ITaskInstance tempTaskInstance = getLastAdvanceTaskInstanceByActivityId(taskInstance.getActivityId(),taskInstanceList);
            if(tempTaskInstance == null){
                return getLastAdvanceTaskInstanceByActivityId(taskInstance.getFromActivityId(),taskInstanceList);
            }else{
                return  getUpTaskInstanceRecursion(tempTaskInstance,taskInstanceList);
            }
        }
    }
    private ITaskInstance getLastAdvanceTaskInstanceByActivityId(String activityId, List<ITaskInstance> taskInstanceList){
        for (int i = taskInstanceList.size(); i > 0; i--) {
            ITaskInstance taskInstance = taskInstanceList.get(i-1);
            if (activityId.equals(taskInstance.getActivityId()) &&  ForwardMode.advance.intValue() == taskInstance.getForwardMode()) {
                return taskInstance;
            }
        }
        return null;
    }

    @Override
    public void deleteTokenById(String id) {
        if (this.IsCacheData()) {
            List<IToken> tokens = (List<IToken>) this.getRuntimeContext().getCacheData().get(TablesName.token);
            for (int index = 0; index < tokens.size(); index++) {
                if (tokens.get(index).getId().equals(id)) {
                    tokens.remove(index);
                    break;
                }
            }
        }
        StringBuilder strSql = new StringBuilder();
        strSql.append("delete from " + TablesName.token);
        strSql.append(" where ID = :id ");
        SQLQuery query = this.createSqlQuery(strSql.toString());
        query.setParameter("id", id);
        if (this.IsCacheData()) {
            addCommand(query);
        } else {
            query.executeUpdate();
        }
    }
    
    public void updateWfProcessInstance(EndNodeInstance endNodeInstance) {
    	if(null != endNodeInstance.getEndNode().getProcessInstanceId()) {
    		StringBuilder strSql = new StringBuilder();
    		strSql.append("update " + TablesName.processInstance);
    		strSql.append(" set CALLSTATE = :callstate,");
    		strSql.append(" RESTADDRESS = :restaddress ");
    		strSql.append(" where ID= :id ");
    		
    		SQLQuery query = this.createSqlQuery(strSql.toString());
    		query.setParameter("callstate", endNodeInstance.getEndNode().getCallBackStatus());
    		query.setParameter("restaddress", endNodeInstance.getEndNode().getRestfulAddress());
    		query.setParameter("id", endNodeInstance.getEndNode().getProcessInstanceId());
    		query.executeUpdate();
    	}
	}

	@Override
	public ProcessInstance getProcessInstanceEntity(String processInstanceId) {
		this.getSession().clear();
		String pi = " select * from " + TablesName.processInstance + " where id =:processInstanceId and state=:state and restaddress is not null ";
        Query query = this.getSession().createSQLQuery(pi).addEntity(ProcessInstance.class);
        query.setParameter("processInstanceId", processInstanceId);
        query.setParameter("state", NodeInstanceEventEnum.nodeinstance_completed.intValue());
        
        List<ProcessInstance> list = query.list();
        if(null != list && list.size() >0) {
        	ProcessInstance pEntity = list.get(0);
        	return pEntity;
        }
        return null;
	}

	@Override
	public Map<String, Object> rollbackAuditInfo(IWorkflowSession workflowSession) {
        List<ProcessInstance> processInstance = (List<ProcessInstance>) workflowSession.getRuntimeContext().getCacheData().get("WF_ProcessInstance");
        List<WorkItemInstance> workItem = (List<WorkItemInstance>)workflowSession.getRuntimeContext().getCacheData().get("WF_WorkItem");
        List<TaskInstance> taskInstance= (List<TaskInstance>)workflowSession.getRuntimeContext().getCacheData().get("WF_TaskInstance");
        List<Todo> todoList = (List<Todo>) workflowSession.getRuntimeContext().getCacheData().get("WF_Todo");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", true);
        rollbackworkItem(workItem, map);
		rollbackTaskInstance(taskInstance, map);//
		rolllbackToken(workItem, taskInstance, map);//
		rollbackProcessInstance(processInstance, map);//
		rollbackTodo(todoList, processInstance, taskInstance, workItem, map);
		rollbackTodoLog(todoList, map);
		return map;
	}

	/**
	 * 回滚流程实例表数据
	 * @param processInstance
	 */
	private void rollbackProcessInstance(List<ProcessInstance> processInstanceList, Map<String, Object> map) {
		if(null == processInstanceList || processInstanceList.size() <= 0) {
			map.put("status", false);
			map.put("data", "Rollback数据异常！");
			return;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(" update "+TablesName.processInstance);
		sb.append(" set state=10, end_Time=null, callstate=null, restaddress='' ");
		sb.append(" where id=:id ");
		SQLQuery query = this.createSqlQuery(sb.toString());
		query.setParameter("id", processInstanceList.get(processInstanceList.size()-1).getId());
		query.executeUpdate();
	}

	/**
	 * 回滚token表数据
	 * @param token
	 */
	private void rolllbackToken(List<WorkItemInstance> workItemList, List<TaskInstance> taskInstanceList, Map<String, Object> map) {
		if(null == workItemList || workItemList.size() <= 0) {
			map.put("status", false);
			map.put("data", "Rollback数据异常！");
			return;
		}
		
		TaskInstance taskInstance = new TaskInstance();
		if(null != workItemList.get(workItemList.size()-1).getTaskInstance()){
			BeanUtil.copyFields(taskInstance, workItemList.get(workItemList.size()-1).getTaskInstance());
		}else{
			taskInstance = getTaskInstanceEntity(taskInstance, workItemList.get(workItemList.size()-1).getTaskInstanceId());
		}
		workItemList.get(workItemList.size()-1).setTaskInstance(taskInstance);
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.token);
		strSql.append(" (ID,NODE_ID,PROCESSINSTANCE_ID,STEP_number,FROM_ACTIVITY_ID,ALIVE)");
		strSql.append(" values(:id,:nodeId,:processInstanceId,:stepNumber,:fromActivityId,:isAlive)");
		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("id", UUID.randomUUID().toString());
		query.setParameter("nodeId", taskInstance.getActivityId());
		query.setParameter("processInstanceId", taskInstance.getProcessInstanceId());
		query.setParameter("stepNumber", taskInstanceList.get(taskInstanceList.size()-1).getStepNumber());
		query.setParameter("fromActivityId", taskInstance.getFromActivityId());
		query.setParameter("isAlive", "1");
		query.executeUpdate();
	}

	private TaskInstance getTaskInstanceEntity(TaskInstance taskInstances, String taskInstanceId) {
		Query query;
		// 缓存流程任务表
		String taskInstanceSql = " select * from "
				+ TablesName.taskInstance
				+ " where id =:taskInstanceId order by step_number desc";
		query = this.getSession().createSQLQuery(taskInstanceSql)
				.addEntity(TaskInstance.class);
		query.setParameter("taskInstanceId", taskInstanceId);
		List<TaskInstance> list = query.list();
		if(null != list && list.size() > 0){
			taskInstances = list.get(0);
		}
		return taskInstances;
	}

	/**
	 * 回滚任务实例表
	 * @param taskInstance
	 */
	private void rollbackTaskInstance(List<TaskInstance> taskInstanceList, Map<String, Object> map) {
		if(null == taskInstanceList || taskInstanceList.size() <= 0) {
			map.put("status", false);
			map.put("data", "Rollback数据异常！");
			return;
		}
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("update " + TablesName.taskInstance);
		strSql.append(" set state=10,end_Time=null ");
		strSql.append(" where id =:id ");
		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("id", taskInstanceList.get(taskInstanceList.size()-1).getId());
		query.executeUpdate();
		map.put("status", true);
	}

	/**
	 * 回滚已办数据
	 * @param todo
	 */
	private void rollbackTodoLog(List<Todo> todoList, Map<String, Object> map) {
		if(null == todoList || todoList.size() <= 0) {
			map.put("status", false);
			map.put("data", "Rollback数据异常！");
			return;
		}
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("delete from " + TablesName.todoLog);
		strSql.append(" where workitem_id =:id ");
		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("id", todoList.get(todoList.size()-1).getWorkItemId());
		query.executeUpdate();
		map.put("status", true);
	}

	/**
	 * 回滚数据
	 * @param workItemInstance
	 */
	private void rollbackworkItem(List<WorkItemInstance> workItemList, Map<String, Object> map) {
		if(null == workItemList || workItemList.size() <= 0) {
			map.put("status", false);
			map.put("data", "Rollback数据异常！");
			return;
		}
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("update " + TablesName.workItem);
		strSql.append(" set state=10,end_Time=null,comments='' ");
		strSql.append(" where id =:id ");
		SQLQuery query = this.createSqlQuery(strSql.toString());
		query.setParameter("id", workItemList.get(workItemList.size()-1).getId());
		query.executeUpdate();	
		map.put("status", true);
	}

	/**
	 * 回滚数据后，向WF_todo待办中 回滚当前审批人数据
	 * @param todo
	 */
	private void rollbackTodo(List<Todo> todoList, List<ProcessInstance> processInstance, List<TaskInstance> taskInstance, List<WorkItemInstance> workItem, Map<String, Object> map) {
		if(null == todoList || todoList.size() <1){
			map.put("status", false);
			map.put("data", "rollback数据发生异常！");
		}else {
			Todo workItemInstance = todoList.get(todoList.size()-1);
			
			// 为完成分配字段 EntityType,EntityId,OrgId,IsTermination
			StringBuilder strSql = new StringBuilder();
			strSql.append("insert into " + TablesName.todo);
			strSql.append(" (WORKITEM_ID,ACTOR_ID,ACTOR_NAME,Post,WORKITEM_STATE,CREATED_TIME,CLAIMED_TIME,END_TIME,COMMENTS,TASKINSTANCE_ID,TASKINSTANCE_NAME,ACTIVITY_ID,TASK_ID,STEP_number,PROCESSINSTANCE_ID,PROCESS_ID,PROCESS_NAME,Suspended,CREATOR_ID,CREATOR_NAME,ENTITYID,ENTITYTYPE,TODODESCRIPTION,FORMURL,BATCHAPPROVAL,XMID,JDTYPE)");
			strSql.append(" values(:workItemId,:actorId,:actorName,:post,:workItemState,:createdTime,:claimedTime,:endTime,:comments,:taskInstanceId,:taskInstanceName,:activityId,:taskId,:stepNumber,:processInstanceId,:processId,:processName,:isSuspended,:creatorId,:creatorName,:entityId,:entityType,:toDoDescription,:formUrl,:batchApproval,:xmid,:jdtype)");
			SQLQuery query = this.createSqlQuery(strSql.toString());
			query.setParameter("workItemId", workItemInstance.getWorkItemId());
			query.setParameter("actorId", workItemInstance.getActorId());
			query.setParameter("actorName", workItemInstance.getActorName());
			query.setParameter("post", workItemInstance.getPost());
			query.setParameter("workItemState", workItemInstance.getWorkItemState());
			query.setParameter("createdTime", sqlParamFilter(workItemInstance.getCreatedTime()));
			query.setParameter("claimedTime", sqlParamFilter(workItemInstance.getClaimedTime()));
			query.setParameter("endTime", sqlParamFilter(workItemInstance.getEndTime()));
			query.setParameter("comments", workItemInstance.getComments());
			query.setParameter("taskInstanceId", workItemInstance.getTaskInstanceId());
			query.setParameter("taskInstanceName", taskInstance.get(taskInstance.size()-1).getName());
			query.setParameter("activityId", taskInstance.get(taskInstance.size()-1).getActivityId());
			query.setParameter("taskId", workItemInstance.getTaskInstanceId());
			query.setParameter("stepNumber", taskInstance.get(taskInstance.size()-1).getStepNumber());
			query.setParameter("processInstanceId", workItemInstance.getProcessInstanceId());
			query.setParameter("processId", processInstance.get(processInstance.size()-1).getProcessId());
			query.setParameter("processName", StringUtils.isNotBlank(processInstance.get(processInstance.size()-1).getFlowshort())?processInstance.get(processInstance.size()-1).getFlowshort():processInstance.get(processInstance.size()-1).getName());
			query.setParameter("isSuspended", taskInstance.get(taskInstance.size()-1).getIsSuspend());// 待分配IsSuspended
			query.setParameter("creatorId", this.getRuntimeContext().getUserInfo().getId());
			query.setParameter("creatorName", this.getRuntimeContext().getUserInfo().getName());
			query.setParameter("entityId", processInstance.get(processInstance.size()-1).getEntityId());
			query.setParameter("entityType", processInstance.get(processInstance.size()-1).getEntityType());
			query.setParameter("toDoDescription", workItemInstance.getToDoDescription());
			
			query.setParameter("formUrl", workItemInstance.getFormUrl());
			query.setParameter("batchApproval", false);
			query.setParameter("xmid", workItemInstance.getXmid());
			query.setParameter("jdtype", workItemInstance.getJdtype());
			query.executeUpdate();
			map.put("status", true);
		}
	}
}
