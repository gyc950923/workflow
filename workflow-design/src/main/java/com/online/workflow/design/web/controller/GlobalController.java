package com.online.workflow.design.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.workflow.design.utils.SysJsonUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.forms.Form;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/global")
@SuppressWarnings("unchecked")
public class GlobalController {

    /**
     * 
     * 功能:加载全局属性<br>
     * 约束:与本函数相关的约束<br>
     * @param session
     * @param response
     */
    @RequestMapping("/loadProperties")
    public void loadProperties(HttpSession session,HttpServletResponse response,
            String modelId){
        Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
        WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
        WorkflowProcess tempWorkflowProcess = workflowProcess.cloneWorkflowProcess();
        String jsonStr = JSONObject.fromObject(tempWorkflowProcess).toString();
        SysJsonUtil.returnJson(response, jsonStr);
    }
    
    /**
     * 
     * 功能:保存全局属性<br>
     * 约束:与本函数相关的约束<br>
     * @param session
     * @param response
     * @param resourceId
     * @param name
     * @param description
     * @param entityName
     * @param formName
     * @param state
     */
    @RequestMapping("/saveProperties")
    public void saveProperties(HttpSession session, HttpServletResponse response,
            String modelId, String jsonStr){
        
        Map<String,Object> workflowData = (Map<String, Object>) session.getAttribute(modelId);
        WorkflowProcess workflowProcess=(WorkflowProcess) workflowData.get("workflowProcess");
        
        JSONObject globalJson = JSONObject.fromObject(jsonStr);
        workflowProcess.setName(globalJson.getString("name"));
        workflowProcess.setDescription(globalJson.getString("description"));
        workflowProcess.setToDoDescription(globalJson.getString("toDoDescription"));
        JSONObject startRuleJson = globalJson.getJSONObject("startRule");
        //StartRule startRule = new StartRule();此处暂无starRule相关数据，所以暂不执行set方法
        
        workflowProcess.setEntityName(startRuleJson.getString("entityName"));
        workflowProcess.setEntityValue(startRuleJson.getString("entityCode"));
        
        this.writeForm(startRuleJson,workflowProcess);
        
        workflowProcess.setState(Boolean.valueOf(startRuleJson.getString("state")));
    }

    private void writeForm(JSONObject startRuleJson, WorkflowProcess workflowProcess) {
        Form form = new Form();
        form.setName(startRuleJson.getString("formName"));
        form.setUrl(startRuleJson.getString("formUrl"));
        form.setDescription(startRuleJson.getString("formInfo"));
        workflowProcess.setForm(form);
    }

}
