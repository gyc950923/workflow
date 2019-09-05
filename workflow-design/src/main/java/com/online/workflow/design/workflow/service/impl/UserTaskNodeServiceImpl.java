package com.online.workflow.design.workflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.online.workflow.design.workflow.service.IUserTaskNodeService;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.resources.BackNode;
import com.online.workflow.process.resources.DeptInfo;
import com.online.workflow.process.resources.FieldAuthority;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.PageActionItem;
import com.online.workflow.process.resources.Performer;
import com.online.workflow.process.resources.RestInfo;
import com.online.workflow.process.resources.RoleInfo;
import com.online.workflow.process.resources.SqlInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.process.rules.AdvanceRule;
import com.online.workflow.process.rules.BackRule;
import com.online.workflow.process.rules.UserRule;
import com.online.workflow.process.tasks.FormTask;
import com.online.workflow.process.tasks.Task;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UserTaskNodeServiceImpl implements IUserTaskNodeService{

    
    @Override
    public void writeInlineTasks(JSONObject activityJson, Activity activity) {
        List<Task> inlineTasks = activity.getInlineTasks();
        FormTask formTask = new FormTask();
        if(!inlineTasks.isEmpty()){
            FormTask oldFormTask = (FormTask) inlineTasks.get(0);
            formTask.setId(oldFormTask.getId());
            formTask.setChartId(oldFormTask.getChartId());
        }
        /*else{
            String id = "sid-"+UUID.randomUUID().toString();//带上sid-前缀，保持与其他节点的id格式一致
            formTask.setId(id);
            formTask.setChartId(id);
        }*/
        inlineTasks.clear();
        
        JSONObject advanceRuleJson = activityJson.getJSONObject("advanceRule");
        this.writeAdvanceRule(advanceRuleJson,formTask);
        
        JSONObject backRuleJson = activityJson.getJSONObject("backRule");
        this.writeBackRule(backRuleJson,formTask);
        
        JSONObject userRuleJson = activityJson.getJSONObject("userRule");
        this.writeUserRule(userRuleJson,formTask);
        
        JSONArray fieldAuthorityJson = activityJson.getJSONArray("fieldAuthority");
        this.writeFieldAuthoritys(fieldAuthorityJson,formTask);
        
        JSONArray pageActionJson = activityJson.getJSONArray("pageAction");
        this.writePageActions(pageActionJson,formTask);
        
        formTask.setName(userRuleJson.getString("title"));
        inlineTasks.add(formTask);
        activity.setInlineTasks(inlineTasks);
        
    }

    /**
     * 
     * 功能:装配PageActionItems数据<br>
     * 约束:与本函数相关的约束<br>
     * @param pageActionJson
     * @param formTask
     */
    private void writePageActions(JSONArray pageActionJson, FormTask formTask) {
        List<PageActionItem> pageActions = new ArrayList<PageActionItem>();
        for(int i=0;i<pageActionJson.size();i++){
            JSONObject obj = pageActionJson.getJSONObject(i);
            PageActionItem item = new PageActionItem();
            item.setButtonName(obj.getString("buttonName"));
            item.setMethodName(obj.getString("methodName"));
            pageActions.add(item);
        }
        formTask.setPageActions(pageActions);
    }

    /**
     * 
     * 功能:装配UserRule数据<br>
     * 约束:与本函数相关的约束<br>
     * @param userRuleJson
     * @param formTask
     */
    private void writeUserRule(JSONObject userRuleJson, FormTask formTask) {
        UserRule userRule = new UserRule();
        Form form = new Form();
        form.setName(userRuleJson.getString("formName"));
        form.setUrl(userRuleJson.getString("formUrl"));
        String formType = userRuleJson.getString("formType");
        if ("可编辑表单".equals(formType)) {
            userRule.setEditForm(form);
        }else if ("只读表单".equals(formType)) {
            userRule.setViewForm(form);
        }else if ("列表表单".equals(formType)) {
            userRule.setListForm(form);
        }
        
        this.writePerformer(userRuleJson,userRule);
        
        userRule.setFormTaskEnum(Integer.parseInt(userRuleJson.getString("formTaskEnum")));
        userRule.setLoopStrategy(Integer.parseInt(userRuleJson.getString("loopStrategy")));
        userRule.setBatchApproval(Boolean.parseBoolean(userRuleJson.getString("batchApproval")));
        
        userRule.setActorAssignType(Integer.parseInt(userRuleJson.getString("actorAssignType")));
        /*userRule.setActorIdKey(userRuleJson.getString("actorIdKey"));
        userRule.setActorNameKey(userRuleJson.getString("actorNameKey"));*/
        
        formTask.setUserRule(userRule);
    }

    private void writePerformer(JSONObject userRuleJson, UserRule userRule) {
        Performer performer = new Performer();
        
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        JSONArray userInfosJson = userRuleJson.getJSONArray("userInfos");
        for(int i = 0; i< userInfosJson.size(); i++){
            JSONObject obj =userInfosJson.getJSONObject(i);
            UserInfo userInfo = new UserInfo();
            userInfo.setId(obj.getString("id"));
            userInfo.setName(obj.getString("name"));
            userInfo.setOrgId(obj.getString("orgId"));
            userInfo.setOrgName(obj.getString("orgName"));
            userInfos.add(userInfo);
        }  
        performer.setUserInfos(userInfos);
        
        List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
        JSONArray roleInfosJson = userRuleJson.getJSONArray("roleInfos");
        for(int i = 0; i< roleInfosJson.size(); i++){
            JSONObject obj =roleInfosJson.getJSONObject(i);
            RoleInfo roleInfo = new RoleInfo();
            roleInfo.setId(obj.getString("id"));
            roleInfo.setName(obj.getString("name"));
            roleInfos.add(roleInfo);
        }  
        performer.setRoleInfos(roleInfos);
        
        List<DeptInfo> deptInfos = new ArrayList<DeptInfo>();
        JSONArray deptInfosJson = userRuleJson.getJSONArray("deptInfos");
        for(int i = 0; i< deptInfosJson.size(); i++){
            JSONObject obj =deptInfosJson.getJSONObject(i);
            DeptInfo deptInfo = new DeptInfo();
            deptInfo.setId(obj.getString("id"));
            deptInfo.setName(obj.getString("name"));
            deptInfos.add(deptInfo);
        }  
        performer.setDeptInfos(deptInfos);
        
        JSONObject sqlInfosJson = userRuleJson.getJSONObject("sqlInfos");
        if(null != sqlInfosJson) {
        	SqlInfo sqlInfos = new SqlInfo();
        	String sqlKey = null == sqlInfosJson.get("sqlKey")?"":String.valueOf(sqlInfosJson.get("sqlKey"));
        	sqlInfos.setSqlKey(sqlKey);
        	performer.setSqlInfos(sqlInfos);
        }
        
        JSONObject restInfosJson = userRuleJson.getJSONObject("restInfos");
        if(null != restInfosJson) {
        	RestInfo restInfos = new RestInfo();
        	String restKey = null == sqlInfosJson.get("restKey")?"":String.valueOf(sqlInfosJson.get("restKey"));
        	restInfos.setRestKey(restKey);
        	performer.setRestInfos(restInfos);
        }

        /**
         * 机构和角色
         */
        
        JSONObject orgRoleInfosObject = userRuleJson.getJSONObject("orgRoleInfos");
        if(null != orgRoleInfosObject){
        	OrgRoleInfo ori = new OrgRoleInfo();
        	ori.setAllNode(orgRoleInfosObject.getString("allNode"));//所有节点是否应用同一个自定义sql
        	ori.setConditionId(orgRoleInfosObject.getString("conditionId"));// 自定义sql或者指定部门id
        	ori.setConditionName(orgRoleInfosObject.getString("conditionName"));// 自定义sql或者指定部门名称
        	ori.setDepartmentSrc(orgRoleInfosObject.getString("departmentSrc"));//部门来源 50： 业务单据所属创建部门 60：根据上一步处理人所属部门 70：指定所属部门 80：根据自定义sql查询出部门id
        	performer.setOrgRoleInfo(ori);
        	
        	setOrgRoleInfosList(performer, orgRoleInfosObject);
        }
        
        userRule.setPerformer(performer);
    }

    /**
     * 向机构角色列表中设置值
     * @param performer
     * @param orgRoleInfosObject
     */
	private void setOrgRoleInfosList(Performer performer,
			JSONObject orgRoleInfosObject) {
		List<OrgRoleInfo> orgRoleInfos = new ArrayList<OrgRoleInfo>();
		JSONArray orgRoleInfosJson = orgRoleInfosObject.getJSONArray("orgRoleInfosList");
		for(int i = 0; i< orgRoleInfosJson.size(); i++){
		    JSONObject obj =orgRoleInfosJson.getJSONObject(i);
		    OrgRoleInfo orgRoleInfo = new OrgRoleInfo();
		    orgRoleInfo.setId(obj.getString("id"));
		    orgRoleInfo.setName(obj.getString("name"));
		    orgRoleInfos.add(orgRoleInfo);
		}  
		performer.setOrgRoleInfos(orgRoleInfos);
	}

    /**
     * 
     * 功能:装配BackRule数据<br>
     * 约束:与本函数相关的约束<br>
     * @param backRuleJson
     * @param formTask
     */
    private void writeBackRule(JSONObject backRuleJson, FormTask formTask) {
        BackRule backRule=new BackRule();
        backRule.setBackConfirm(Boolean.valueOf(backRuleJson.getString("isBackConfirm")));
        BackNode backNode = new BackNode();
        backNode.setNodeId(backRuleJson.getString("backRange"));
        backRule.setBackRange(backNode);
        //backRule.setReturnMode(Integer.valueOf(backRuleJson.getString("returnMode")));回退方式暂不启用
        formTask.setBackRule(backRule);
    }

    /**
     * 
     * 
     * 功能:装配AdvanceRule数据<br>
     * 约束:与本函数相关的约束<br>
     * @param advanceRuleJson
     * @param formTask
     */
    private void writeAdvanceRule(JSONObject advanceRuleJson, FormTask formTask) {
        AdvanceRule advanceRule = new AdvanceRule();
        advanceRule.setAdjustStaff(Boolean.valueOf(advanceRuleJson.getString("isAdjustStaff")));
        advanceRule.setAdvanceConfirm(Boolean.valueOf(advanceRuleJson.getString("isAdvanceConfirm")));
        advanceRule.setIsToBeRead(Boolean.valueOf(advanceRuleJson.getString("isToBeRead")));
        formTask.setAdvanceRule(advanceRule);
    }
    /**
     * 
     * 功能:装配字段权限<br>
     * 约束:与本函数相关的约束<br>
     * @param jsonArray
     * @param formTask
     */
    private void writeFieldAuthoritys(JSONArray jsonArray, FormTask formTask) {
        List<FieldAuthority> fieldAuthoritys = new ArrayList<FieldAuthority>();
        for(int i=0;i<jsonArray.size();i++){
            JSONObject p = jsonArray.getJSONObject(i);
            FieldAuthority authority = new FieldAuthority();
            authority.setCode(p.getString("code"));
            authority.setIsVisible(Integer.parseInt(p.getString("isVisible")));
            authority.setIsRead(Integer.parseInt(p.getString("isRead")));
            fieldAuthoritys.add(authority);
        }
        formTask.setFieldAuthoritys(fieldAuthoritys);
    }

    /**
     * 
     * 功能:获取回退范围的节点信息<br>
     * 约束:与本函数相关的约束<br>
     * @see com.online.workflow.design.workflow.service.IUserTaskNodeService#getBackRangeList(java.util.List)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Map> getBackRangeList(String resourceId,List<Activity> activities) {
        List<Map> list = new ArrayList<Map>();
        for(Activity activity : activities){
            if (resourceId.equals(activity.getChartId())) {
                continue;
            }
            List<Task> tasks = activity.getInlineTasks();
            if (!tasks.isEmpty() && tasks.get(0) instanceof FormTask) {
            	HashMap<String,String> map = new HashMap<String, String>();
                map.put("value", activity.getChartId());
                map.put("text", activity.getName());
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public boolean validateRepeatName(String name, Activity activity, List<Activity> activities) {
        boolean flag = false;
        for(Activity act : activities){
            List<Task> tasks = act.getInlineTasks();
            if (!tasks.isEmpty() && tasks.get(0) instanceof FormTask) {
                if (!activity.getChartId().equals(act.getChartId()) && name.equals(act.getName())) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

}
