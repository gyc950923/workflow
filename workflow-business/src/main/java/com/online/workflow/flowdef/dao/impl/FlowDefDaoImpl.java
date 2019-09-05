package com.online.workflow.flowdef.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.online.engine.common.BaseDaoSupport;
import com.online.engine.enums.TablesName;
import com.online.workflow.flowdef.dao.IFlowDefDao;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.util.PageModel;
import com.online.workflow.util.PageUtil;

@Repository("flowDefDao")
public class FlowDefDaoImpl extends BaseDaoSupport implements IFlowDefDao{

    @Resource(name = "hibernateTemplate")
	private HibernateTemplate hibernateTemplate;
    private Integer getCount(String sql){
        SQLQuery sqlQuery = this.createSqlQuery(PageUtil.getCountSql(sql));
         Object counts = sqlQuery.list().get(0);
         Integer count = Integer.parseInt(counts.toString());
         System.out.println(count+"--------------------");
        return count;
    }
    
    @Override
    public List<Object> flowDefList(PageModel pageModel) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("ID,PROCESS_ID,VERSION,STATE,UPLOAD_USER,UPLOAD_TIME,PUBLISH_USER,PUBLISH_TIME ");
        sql.append("from wf_workflowdef ");
        pageModel.setTotal(getCount(sql.toString()));
        SQLQuery sqlQuery = this.createSqlQuery(PageUtil.getPageSql(sql.toString(), pageModel));
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }
    /**
     * 表单管理
     */
    @Override
    public List<Object> getStudents(PageModel pageModel) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("ID,name,age,days ");
        sql.append("from student ");
        pageModel.setTotal(getCount(sql.toString()));
        SQLQuery sqlQuery = this.createSqlQuery(PageUtil.getPageSql(sql.toString(), pageModel));
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

	@Override
	public String saveFrom(String name, String age, String days) {
	        
	        StringBuilder strSql = new StringBuilder();
			strSql.append("insert into student (");
			strSql.append("id,name,age,days,studentdate)");
			strSql.append(" values (");
			strSql.append(" :id,:name,:age,:days,:studentdate )");

			Query query = this.createSqlQuery(strSql.toString());
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			query.setParameter("age", age);
			query.setParameter("days", days);
			query.setParameter("id", uuid);
			query.setParameter("name", name);
			Date date = new Date();
			query.setParameter("studentdate", date);
				query.executeUpdate();
				return uuid;
			}

	@Override
	public List<WorkflowDefinition>  saveWorkflow(String flowId) {
		        StringBuilder sql = new StringBuilder();
		        sql.append("select ");
		        sql.append(" * ");
		        sql.append("from wf_workflowdef where ID = '"+flowId+"'");
		        SQLQuery sqlQuery = this.createSqlQuery(sql.toString());
		        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		        return sqlQuery.list();

	}

	@Override
	public void saveTodo(WorkflowDefinition workflowDefinition) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("insert into " + TablesName.todoLog);
		strSql.append(" (WORKITEM_ID,ACTOR_ID,ACTOR_NAME,Post,WORKITEM_STATE,CREATED_TIME,CLAIMED_TIME,END_TIME,COMMENTS,TASKINSTANCE_ID,TASKINSTANCE_NAME,ACTIVITY_ID,TASK_ID,STEP_number,PROCESSINSTANCE_ID,PROCESS_ID,PROCESS_NAME,SUSPENDED,CREATOR_ID,ENTITYID,ENTITYTYPE,TODODESCRIPTION,FORMURL,COMPELETEMODE,CREATOR_NAME,XMID,JDTYPE)");
		strSql.append(" values(:workItemId,:actorId,:actorName,:post,:workItemState,:createdTime,:claimedTime,:endTime,:comments,:taskInstanceId,:taskInstanceName,:activityId,:taskId,:stepNumber,:processInstanceId,:processId,:processName,:isSuspended,:creatorId,:entityId,:entityType,:toDoDescription,:formUrl,:completeMode,:creatorName,:xmid,:jdtype)");

		SQLQuery query = this.createSqlQuery(strSql.toString());

//		query.setParameter("workItemId", workflowDefinition.getId());
//		query.setParameter("actorId", workItemInstance.getActorId());
//		query.setParameter("actorName", workItemInstance.getActorName());
//		query.setParameter("post", workItemInstance.getPost());
//		query.setParameter("workItemState", workItemInstance.getState());
//		query.setParameter("createdTime", sqlParamFilter(workItemInstance.getCreatedTime()));
//		query.setParameter("claimedTime", sqlParamFilter(workItemInstance.getClaimedTime()));
//		query.setParameter("endTime", sqlParamFilter(workItemInstance.getEndTime()));
//		query.setParameter("comments", workItemInstance.getComments());
//		query.setParameter("taskInstanceId",
//				workItemInstance.getTaskInstanceId());
//		query.setParameter("taskInstanceName", workItemInstance
//				.getTaskInstance().getName());
//		query.setParameter("activityId", workItemInstance.getTaskInstance()
//				.getActivityId());
//
//		query.setParameter("taskId", workItemInstance.getTaskInstanceId());
//		query.setParameter("stepNumber", workItemInstance.getTaskInstance()
//				.getStepNumber());
//		query.setParameter("processInstanceId",
//				workItemInstance.getProcessInstanceId());
//		query.setParameter("processId", workItemInstance.getProcessInstance().getProcessId());
//		
//		
//		
//		query.setParameter("processName", StringUtils.isNotBlank(workItemInstance.getProcessInstance().getFlowshort())?workItemInstance.getProcessInstance().getFlowshort():workItemInstance.getProcessInstance().getName());
//		query.setParameter("isSuspended", workItemInstance.getTaskInstance()
//				.getIsSuspend());
//		query.setParameter("creatorId", this.getRuntimeContext().getUserInfo()
//				.getId());
//		query.setParameter("entityId", workItemInstance.getProcessInstance().getEntityId());
//		query.setParameter("entityType", workItemInstance.getProcessInstance().getEntityType());
//		query.setParameter("toDoDescription", workItemInstance.getToDoDescription());
//		query.setParameter("formUrl", workItemInstance.getFormUrl());
//		query.setParameter("completeMode", workItemInstance.getRuntimeContext().getForwardMode());
//		query.setParameter("creatorName", this.getRuntimeContext().getUserInfo().getName());
//		query.setParameter("xmid", workItemInstance.getXmid());
//		query.setParameter("jdtype", workItemInstance.getJdtype());
//		// query.setParameter("",
//		// workItemInstance.getProcessInstance().getEntityType());//待分配EntityType
//		// query.setParameter("",
//		// workItemInstance.getProcessInstance().getEntityId());//待分配EntityId
//		// query.setParameter("", workItemInstance);//待分配OrgId
//		// query.setParameter("",
//		// workItemInstance.getProcessInstance().getIsTermination());//待分配IsTermination
//		if (this.IsCacheData()) {
//			addCommand(query);
//		} else {
//			query.executeUpdate();
//		}
//		
	}
	

}
