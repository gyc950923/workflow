package com.online.workflow.design.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.workflow.design.utils.SysJsonUtil;
import com.online.workflow.process.IWFElement;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowProcess;

@Controller
@RequestMapping("/model")
public class ModelNewController {

    @RequestMapping("/newModel")
    public void newModel(HttpSession session, HttpServletResponse response,String newModelId){
        
        Map<String,Object> workflowData = new HashMap<String, Object>();
        WorkflowProcess workflowProcess = new WorkflowProcess();
        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        List<IWFElement> undoList = new ArrayList<IWFElement>();
        List<IWFElement> redoList = new ArrayList<IWFElement>();
        workflowProcess.setId(newModelId);
        workflowProcess.setChartId(newModelId);
        workflowData.put("workflowProcess", workflowProcess);
        workflowData.put("workflowDefinition", workflowDefinition);
        workflowData.put("version", 1);
        workflowData.put("redoList", redoList);
        workflowData.put("undoList", undoList);
        session.setAttribute(newModelId, workflowData);
        
        SysJsonUtil.returnJson(response, "");
    }
    
    @RequestMapping("/initSession")
    public void initSessionUserInfo(HttpSession session,HttpServletResponse response,String userName, String userId, String deptId){    
        try {
            userName = URLDecoder.decode(userName==null?"":userName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        session.setAttribute("userId", userId);
        session.setAttribute("userName", userName);
        SysJsonUtil.returnText(response, "");
    }

}
