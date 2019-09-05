package com.online.workflow.design.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.workflow.design.utils.ResultMsg;
import com.online.workflow.design.utils.SysJsonUtil;
import com.online.workflow.design.workflow.service.IUserTaskNodeService;
import com.online.workflow.design.workflow.service.impl.UserTaskNodeServiceImpl;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Activity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/userTaskNode")
@SuppressWarnings("unchecked")
public class UserTaskNodeController {

    private IUserTaskNodeService userTaskNodeService = new UserTaskNodeServiceImpl();
    
    /**
     * 
     * 功能:加载活动节点属性<br>
     * 约束:与本函数相关的约束<br>
     * @param session
     * @param response
     * @param modelId
     * @param resourceId
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping("/loadProperties")
    public void loadProperties(HttpSession session, HttpServletResponse response,
            String modelId, String resourceId){
        String jsonStr = "{}";
        
        if(StringUtils.isNotEmpty(resourceId) && StringUtils.isNotEmpty(modelId)){
            Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
            WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
            WorkflowProcess tempWorkflowProcess = workflowProcess.cloneWorkflowProcess();
            Activity activity=(Activity) tempWorkflowProcess.findWFElementByChartId(resourceId);   
            List<Map> backRangeList = userTaskNodeService.getBackRangeList(resourceId,workflowProcess.getActivities());
            JSONArray backRangeJson=JSONArray.fromObject(backRangeList);
            JSONObject jsonObject=JSONObject.fromObject(activity);
            JSONArray jSONArray=new JSONArray();
            jSONArray.add(jsonObject);//activity的json对象
            jSONArray.add(backRangeJson);//参数列表的json对象
            jsonStr=jSONArray.toString();
        }
        SysJsonUtil.returnJson(response, jsonStr);
    }
    
    /**
     * 
     * 功能:保存活动节点属性<br>
     * 约束:与本函数相关的约束<br>
     */
    @RequestMapping("/saveProperties")
    public void saveProperties(HttpSession session,HttpServletResponse response, String jsonStr,
            String modelId, String resourceId){
        //接收前台传来的json字符串，并解析成Object对象，然后将对应的值装配到节点对象中
        JSONObject activityJson = JSONObject.fromObject(jsonStr);
        Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
        WorkflowProcess workflowProcess=(WorkflowProcess) workflowData.get("workflowProcess");
        Activity activity=(Activity) workflowProcess.findWFElementByChartId(resourceId);
        activity.setName(activityJson.getString("name"));
        activity.setDescription(activityJson.getString("description"));
        activity.setEntityDocStatusName(activityJson.getString("entityDocStatusName"));
        activity.setCallBackStatus(Integer.valueOf(activityJson.getString("callBackStatus")));
        activity.setLocalAddress(activityJson.getString("localAddress"));
        activity.setRestfulAddress(activityJson.getString("restfulAddress"));
        activity.setIsSendMsg(Boolean.valueOf(activityJson.getString("isSendMsg")));
        activity.setMsgtTemplate(activityJson.getString("msgtTemplate"));
        //装配节点任务
        userTaskNodeService.writeInlineTasks(activityJson,activity);
        
        //装配活动节点公用配置项
//        assemblyActivitysNode(workflowProcess, activityJson.get("activitsPublicParam").toString());
        
        SysJsonUtil.returnJson(response, "{}");
    }
    
    /**
     * 向activitys活动节点添加公用属性配置 此处主要是部门+角色的一些公用属性
     * @param workflowProcess
     * @param object
     */
    private void assemblyActivitysNode(WorkflowProcess workflowProcess, String json) {
    	JSONObject activitysJson = JSONObject.fromObject(json);
    	workflowProcess.getActivitsPublicParam().setAllNode(activitysJson.getString("allNode"));
    	workflowProcess.getActivitsPublicParam().setConditionId(activitysJson.getString("conditionId"));
    	workflowProcess.getActivitsPublicParam().setConditionName(activitysJson.getString("conditionName"));
    	workflowProcess.getActivitsPublicParam().setDepartmentSrc(activitysJson.getString("departmentSrc"));
	}

	@RequestMapping("/validate")
    public void validateRepeatName(String modelId, String resourceId,String name, HttpServletResponse response,HttpSession session){
        Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
        WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
        Activity activity = (Activity) workflowProcess.findWFElementByChartId(resourceId);
        List<Activity> activities = workflowProcess.getActivities();
        boolean flag = userTaskNodeService.validateRepeatName(name,activity,activities);
        ResultMsg result = new ResultMsg(flag,"");
        SysJsonUtil.returnJson(response, JSONObject.fromObject(result).toString());  
    }

}
