package com.online.workflow.executeflow.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.workflow.executeflow.service.IExecuteFlowService;
import com.online.workflow.process.resources.PageInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.util.Result;

@Controller
@RequestMapping("/executeflow")
public class ExecuteFlowController {

    @Resource(name="executeFlowService")
    private IExecuteFlowService executeFlowService;
    
    @RequestMapping("/execute")
    @ResponseBody
    public Object executeFlow(String flowId,String workItemId,String entityType,String entityId,UserInfo userInfo,String actorsJson, String myVar, String comments){
        
    	return executeFlowService.executeFlow(flowId, workItemId, entityType, entityId, userInfo, actorsJson, myVar, comments,"","", "");
    }
    
    @RequestMapping("/back")
    @ResponseBody
    public Result back(String workItemId, String id, String name, String myVar, String comments){
    	return executeFlowService.back(workItemId, id, name, myVar, comments);
    }
    
    @RequestMapping("/termination")
    @ResponseBody
    public Result termination(String workItemId,UserInfo userInfo, String comments,String processid, String processInstanceId){
        Result result = executeFlowService.termination(workItemId,userInfo,comments,processid, processInstanceId);
        return result;
    }
    
    @RequestMapping("/getAuthority")
    @ResponseBody
    public PageInfo getAuthority(String workItemId,UserInfo userInfo){
        PageInfo result = executeFlowService.getAuthority(workItemId);
        return result;
    }
    
    @RequestMapping("/toberead")
    @ResponseBody
    public Object toberead(String workItemId,UserInfo userInfo){
    	Map<String, Object> result = executeFlowService.toberead(workItemId, userInfo);
        return result;
    }
}
