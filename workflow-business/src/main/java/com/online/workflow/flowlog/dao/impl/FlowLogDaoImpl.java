package com.online.workflow.flowlog.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.online.engine.common.BaseDaoSupport;
import com.online.workflow.flowlog.dao.IFlowLogDao;
import com.online.workflow.util.PageModel;
import com.online.workflow.util.PageUtil;

@Repository("flowLogDao")
public class FlowLogDaoImpl extends BaseDaoSupport implements IFlowLogDao{
	 @Resource(name = "hibernateTemplate")
		private HibernateTemplate hibernateTemplate;
    private Integer getCount(String sql){
        SQLQuery sqlQuery = this.createSqlQuery(PageUtil.getCountSql(sql));
         Object counts = sqlQuery.list().get(0);
         Integer count = Integer.parseInt(counts.toString());
        return count;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Object> submitNotFinish(PageModel pageModel, String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("distinct id,name,entityId,processinstance_id,activity_dispalyName ,created_time,actor_name,taskinstance_name,PROCESSDESCRIPTION ");
        sql.append("from( ");
        sql.append("select ");
        sql.append("q.id,q.name,q.workItem_id,q.activity_dispalyName,q.entityId,q.processinstance_id,q.actor_id,q.actor_name,q.created_time,q.taskinstance_name,q.PROCESSDESCRIPTION ");
        sql.append("from( ");
        sql.append("select ");
        sql.append("p.id,p.name,a.workItem_id,a.entityId,a.processinstance_id,a.activity_dispalyName,a.actor_id,a.actor_name,p.created_time,a.taskinstance_name,p.PROCESSDESCRIPTION ");
        sql.append("from wf_todo a ");
        sql.append("inner join wf_processinstance p ");
        sql.append("on a.processinstance_id = p.id ");
        sql.append("where p.creator_id = '"+userId+"' and p.state not in (60,100) ");//state=100代表流程未结束,=60代表终止
        sql.append(") q ");
        sql.append(") a");
        
        pageModel.setTotal(getCount(sql.toString()));
        SQLQuery sqlQuery = this.createSqlQuery(PageUtil.getPageSql(sql.toString(), pageModel));
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    @Override
    public List<Object> submitAndFinish(PageModel pageModel, String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("distinct p.id,p.name,l.processinstance_id,l.entityId,p.created_time,p.end_time,p.PROCESSDESCRIPTION ");
        sql.append("from wf_todolog l ");
        sql.append("inner join wf_processinstance p ");
        sql.append("on l.processinstance_id = p.id ");
        sql.append("where p.state in (60,100) and p.creator_id='"+userId+"'");//state=100代表流程已结束,=60代表终止
        
        pageModel.setTotal(getCount(sql.toString()));
        SQLQuery sqlQuery = this.createSqlQuery(PageUtil.getPageSql(sql.toString(), pageModel));
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    @Override
    public List<Object> passAndFinish(PageModel pageModel, String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("distinct p.id,p.name,l.entityId,l.processinstance_id,p.created_time,p.end_time,p.PROCESSDESCRIPTION ");
        sql.append("from wf_todolog l ");
        sql.append("inner join wf_processinstance p ");
        sql.append("on l.processinstance_id = p.id ");
        sql.append("where (l.actor_id = '"+userId+"' or l.agent_id ='"+userId+"') and p.state in (60,100) and p.creator_id!='"+userId+"'");//state=100代表流程已结束,=60代表终止
        
        pageModel.setTotal(getCount(sql.toString()));
        SQLQuery sqlQuery = this.createSqlQuery(PageUtil.getPageSql(sql.toString(), pageModel));
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    @Override
    public List<Object> passNotFinish(PageModel pageModel, String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("distinct id,name,entityId,processinstance_id,created_time,actor_name,taskinstance_name,PROCESSDESCRIPTION ");
        sql.append("from (");
        sql.append("select ");
        sql.append("p.id,p.name,a.workitem_id,a.entityId,a.processinstance_id,a.activity_dispalyName,a.actor_id,p.created_time,a.actor_name,a.taskinstance_name,p.PROCESSDESCRIPTION ");
        sql.append("from wf_todo a ");
        sql.append("inner join wf_processinstance p ");
        sql.append("on a.processinstance_id = p.id ");
        sql.append("where processinstance_id in (");
        sql.append(" select mt.id from ( (select p.id ");
        sql.append("from wf_processinstance p ");
        sql.append("inner join wf_todolog l ");
        sql.append("on p.id = l.processinstance_id ");
        sql.append("where (l.actor_id = '"+userId+"' or l.agent_id = '"+userId+"') and p.state not in (60,100) and p.creator_id!='"+userId+"') ");//state=100代表流程已结束,=60代表终止
        sql.append("union ");
        sql.append("(select p.id ");
        sql.append("from wf_processinstance p ");
        sql.append("inner join wf_todo l ");
        sql.append("on p.id = l.processinstance_id ");
        sql.append("where (l.actor_id='"+userId+"' or l.agent_id='"+userId+"') and p.state not in (60,100) and p.creator_id!='"+userId+"') ");//state=100代表流程已结束,=60代表终止
        sql.append(")mt ) ");
        sql.append(" ) b ");
        
        pageModel.setTotal(getCount(sql.toString()));
        SQLQuery sqlQuery = this.createSqlQuery(PageUtil.getPageSql(sql.toString(), pageModel));
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    @Override
    public List<Object> todoList(PageModel pageModel, String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("WORKITEM_ID,PROCESSINSTANCE_ID,ENTITYID,ENTITYTYPE,PROCESS_ID,ACTIVITY_DispalyName,CREATOR_NAME,ACTOR_ID,ACTOR_NAME,Post,WORKITEM_STATE,CREATED_TIME,COMMENTS,TASKINSTANCE_NAME,STEP_NUMBER,TODODESCRIPTION,FORMURL,BATCHAPPROVAL ");
        sql.append("from wf_todo ");
        sql.append("where WORKITEM_STATE not in (60,100) ");//state=100代表流程已结束,=60代表终止      and ACTOR_ID ='"+userId+"'
        sql.append("union ");
        sql.append("select ");
        sql.append("WORKITEM_ID,PROCESSINSTANCE_ID,ENTITYID,ENTITYTYPE,PROCESS_ID,ACTIVITY_DispalyName,CREATOR_NAME,ACTOR_ID,ACTOR_NAME,Post,WORKITEM_STATE,CREATED_TIME,COMMENTS,TASKINSTANCE_NAME,STEP_NUMBER,TODODESCRIPTION,FORMURL,BATCHAPPROVAL ");
        sql.append("from wf_todolog ");
        sql.append("where STEP_NUMBER!=1 AND WORKITEM_STATE not in (60,100)  ORDER BY CREATED_TIME DESC ");//state=100代表流程已结束,=60代表终止    and ACTOR_ID ='"+userId+"'
        
        pageModel.setTotal(getCount(sql.toString()));
        SQLQuery sqlQuery = this.createSqlQuery(PageUtil.getPageSql(sql.toString(), pageModel));
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    @Override
    public List<Object> detaiLog(PageModel pageModel, String processinstanceId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT *from ( (select ");
        sql.append("COMPELETEMODE,WORKITEM_ID,PROCESSINSTANCE_ID,entityId,PROCESS_ID,ACTIVITY_DispalyName,CREATOR_NAME,ACTOR_ID,ACTOR_NAME,Post,WORKITEM_STATE,STEP_NUMBER,COMMENTS,TASKINSTANCE_NAME,PROCESS_NAME,TODODESCRIPTION,FORMURL ");
        sql.append("from wf_todolog ");
        sql.append("where PROCESSINSTANCE_ID = '"+processinstanceId+"') ");
        sql.append("union ");
        sql.append("(select ");
        sql.append("COMPELETEMODE,WORKITEM_ID,PROCESSINSTANCE_ID,entityId,PROCESS_ID,ACTIVITY_DispalyName,CREATOR_NAME,ACTOR_ID,ACTOR_NAME,Post,WORKITEM_STATE,STEP_NUMBER,COMMENTS,TASKINSTANCE_NAME,PROCESS_NAME,TODODESCRIPTION,FORMURL ");
        sql.append("from wf_todo ");
        sql.append("where PROCESSINSTANCE_ID = '"+processinstanceId+"') )a ");
        sql.append("order by STEP_NUMBER");
        pageModel.setTotal(getCount(sql.toString()));
        SQLQuery sqlQuery = this.createSqlQuery(PageUtil.getPageSql(sql.toString(), pageModel));
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    

}
