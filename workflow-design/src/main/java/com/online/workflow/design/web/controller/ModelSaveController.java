package com.online.workflow.design.web.controller;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.workflow.common.DateUtil;
import com.online.workflow.design.utils.ResultMsg;
import com.online.workflow.design.utils.SysJsonUtil;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowProcess;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/model")
@SuppressWarnings("unchecked")
public class ModelSaveController {
    
    @Resource(name="persistenceService")
    private IPersistenceService persistenceService;
    
    @RequestMapping("/saveModel")
    public void saveModel(HttpSession session,HttpServletResponse response, 
            String modelId, boolean isInsert, String json_model){
        
        Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
        WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
        if(StringUtils.isEmpty(workflowProcess.getName())){
            ResultMsg result = new ResultMsg(false, "流程名称未定义！");
            SysJsonUtil.returnJson(response, JSONObject.fromObject(result).toString());
            return ;
        }
        Integer version = (Integer) workflowData.get("version");
        WorkflowDefinition workflowDefinition = (WorkflowDefinition) workflowData.get("workflowDefinition");
        workflowDefinition.setProcessChartContent(json_model);
        String id = null;
        if (isInsert) {//判断是否作为新版本保存模型
            id = UUID.randomUUID().toString();
            workflowProcess.setId(id);
            workflowProcess.setChartId(id);
        }else{//模型发布
            id = workflowProcess.getChartId();
        }
        
        workflowDefinition.setId(id);
        workflowDefinition.SetWorkflowProcess(workflowProcess);
        workflowDefinition.setTypeId("默认TypeId");//数据库要求该字段不为空，该字段暂无匹配项，此处为赋默认值
        workflowDefinition.setProcessId(workflowProcess.getName());
        workflowDefinition.setName(workflowProcess.getName());
        if (StringUtils.isEmpty(workflowDefinition.getPublishUser())) {
            workflowDefinition.setPublishUser(/*(String)session.getAttribute("userName")*/"登录用户");
            workflowDefinition.setPublishTime(DateUtil.getSysDate());
        }
        workflowDefinition.setUploadUser(/*(String)session.getAttribute("userName")*/"登录用户");
        workflowDefinition.setUploadTime(DateUtil.getSysDate());
        workflowDefinition.setVersion(version);
        workflowDefinition.setState(workflowProcess.getState());
        workflowDefinition.setDescription(workflowProcess.getDescription());
        workflowDefinition=persistenceService.saveOrUpdateWorkflowDefinition(workflowDefinition, Boolean.valueOf(isInsert));  
        version = workflowDefinition.getVersion();
        workflowData.put("workflowProcess", workflowProcess);
        workflowData.put("version", version);
        session.setAttribute(workflowProcess.getChartId(), workflowData);
        ResultMsg result = new ResultMsg(true, workflowProcess.getChartId());
        SysJsonUtil.returnJson(response, JSONObject.fromObject(result).toString());
        
    }
    
}
