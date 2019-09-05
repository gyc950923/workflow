package com.online.workflow.design.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.workflow.design.utils.SysJsonUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.EndNode;
import com.online.workflow.process.net.StartNode;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/node")
@SuppressWarnings("unchecked")
public class StartAndEndNodeController {

    @RequestMapping("/loadStartNodeProperties")
    public void loadStartNodeProperties(HttpSession session, HttpServletResponse response,
            String modelId, String resourceId){
        if (StringUtils.isNotEmpty(resourceId) && StringUtils.isNotEmpty(modelId)) {
            
            Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
            WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
            WorkflowProcess tempWorkflowProcess = workflowProcess.cloneWorkflowProcess();
            StartNode startNode=(StartNode) tempWorkflowProcess.findWFElementByChartId(resourceId);
            JSONObject jsonObject=JSONObject.fromObject(startNode);
            SysJsonUtil.returnJson(response, jsonObject.toString());
        }
    }
    
    @RequestMapping("/saveStartNodeProperties")
    public void saveStartNodeProperties(HttpSession session, HttpServletResponse response,
            String modelId, String resourceId, String name, String description){
        Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
        WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
        StartNode startNode =(StartNode) workflowProcess.findWFElementByChartId(resourceId);
        startNode.setName(name);
        startNode.setDescription(description);
        SysJsonUtil.returnJson(response, "{}");
    }
    
    @RequestMapping("/loadEndNodeProperties")
    public void loadEndNodeProperties(HttpSession session, HttpServletResponse response,
            String modelId, String resourceId){
        if (StringUtils.isNotEmpty(resourceId) && StringUtils.isNotEmpty(modelId)) {
            Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
            WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
            WorkflowProcess tempWorkflowProcess = workflowProcess.cloneWorkflowProcess();
            EndNode endNode=(EndNode) tempWorkflowProcess.findWFElementByChartId(resourceId);
            JSONObject jsonObject=JSONObject.fromObject(endNode);
            SysJsonUtil.returnJson(response, jsonObject.toString());
        }
    }
    
    @RequestMapping("/saveEndNodeProperties")
    public void saveEndNodeProperties(HttpSession session, HttpServletResponse response,
            String modelId, String resourceId, EndNode en){
        Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
        WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
        EndNode endNode =(EndNode) workflowProcess.findWFElementByChartId(resourceId);
        endNode.setName(en.getName());
        endNode.setDescription(en.getDescription());
        endNode.setCallBackStatus(en.getCallBackStatus());
        endNode.setRestfulAddress(en.getRestfulAddress());
        SysJsonUtil.returnJson(response, "{}");
    }

}
