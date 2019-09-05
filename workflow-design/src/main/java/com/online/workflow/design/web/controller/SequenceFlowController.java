package com.online.workflow.design.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.online.workflow.design.utils.SysJsonUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Transition;
import com.online.workflow.process.resources.PageActionItem;

@Controller
@RequestMapping("/sequenceFlow")
@SuppressWarnings("unchecked")
public class SequenceFlowController {
    /**
     * 
     * 功能:加载节点属性<br>
     * 约束:与本函数相关的约束<br>
     * @param session
     * @param response
     * @param modelId
     * @param resourceId
     */
    @RequestMapping("/loadProperties")
    public void loadProperties(HttpSession session, HttpServletResponse response,
            String modelId, String resourceId){
        Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
        WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
        WorkflowProcess tempWorkflowProcess = workflowProcess.cloneWorkflowProcess();
        Transition transition = (Transition) tempWorkflowProcess.findWFElementByChartId(resourceId);
        JSONObject jsonObject=JSONObject.fromObject(transition);
        SysJsonUtil.returnJson(response, jsonObject.toString());
    }
    /**
     * 
     * 功能:保存节点属性<br>
     * 约束:与本函数相关的约束<br>
     * @param session
     * @param response
     * @param modelId
     * @param resourceId
     */
    @RequestMapping(value = "/saveProperties", method = { RequestMethod.POST, RequestMethod.GET })
    public void saveProperties(HttpSession session, HttpServletResponse response,
            String modelId, String resourceId, Transition t, String jsonext){
    	
        Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
        WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
        Transition transition = (Transition) workflowProcess.findWFElementByChartId(resourceId);
        transition.setName(t.getName());
        transition.setStart(t.getStart());
        transition.setSqlCondition(t.getSqlCondition());
        transition.setSqlOperator(t.getSqlOperator());
        transition.setSqlResult(t.getSqlResult());
        transition.setClassName(t.getClassName());
        transition.setMethodName(t.getMethodName());
        transition.setVarCondition(t.getVarCondition());
        transition.setVarOperator(t.getVarOperator());
        transition.setVarResult(t.getVarResult());
        //装配节点任务
        writeInlineTasks(jsonext, transition);
        
        SysJsonUtil.returnJson(response, "{}");
    }
    
    /**
     * 装配节点任务
     * @param jsonext
     * @param transition
     */
	private void writeInlineTasks(String jsonext, Transition transition) {
		if(StringUtils.isNotBlank(jsonext)) {
			List<PageActionItem> paiList = new ArrayList<PageActionItem>();
    		JSONArray jsonArray = JSONArray.fromObject(jsonext);
    		if(jsonArray.size() > 0) {
    			for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jo = jsonArray.getJSONObject(i);
					PageActionItem pai = new PageActionItem();
					pai.setButtonName(jo.getString("buttonName"));
					pai.setMethodName(jo.getString("methodName"));
					paiList.add(pai);
				}
    		}else{
    			transition.setPageActions(new ArrayList<PageActionItem>());
    		}
    		
    		transition.setPageActions(paiList);
    	}
	}
    

}
