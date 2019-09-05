package com.online.workflow.design.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.workflow.design.utils.SysJsonUtil;
import com.online.workflow.process.IWFElement;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowProcess;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
@RequestMapping("/model")
public class ModelLoadController {
    
    @Resource(name="persistenceService")
    private IPersistenceService persistenceService;
    
    @RequestMapping("/loadModel")
    public void loadModel(HttpSession session,HttpServletResponse response, 
            String modelId) throws JsonProcessingException, IOException{
        
        WorkflowDefinition workflowDefinition = persistenceService.getWorkflowDefinitionByDefId(modelId);
        WorkflowProcess workflowProcess = null ;
        if (null == workflowDefinition) {//如果查询的流程定义对象不存在则新建一个新的流程定义
            modelId = UUID.randomUUID().toString();
            workflowDefinition = new WorkflowDefinition();
            workflowDefinition.setId(modelId);
            workflowDefinition.setVersion(1);
            workflowProcess = new WorkflowProcess();
            workflowProcess.setId(modelId);
            workflowProcess.setChartId(modelId);
        }else{//通过流程定义获得流程模板
            workflowProcess = workflowDefinition.getWorkflowProcess();
        }
        //初始化session中的数据
        List<IWFElement> undoList = new ArrayList<IWFElement>();
        List<IWFElement> redoList = new ArrayList<IWFElement>();
        Map<String,Object> workflowData = new HashMap<String,Object>();
        workflowData.put("workflowDefinition", workflowDefinition);
        workflowData.put("workflowProcess", workflowProcess);
        workflowData.put("version", workflowDefinition.getVersion());
        workflowData.put("redoList", redoList);
        workflowData.put("undoList", undoList);
        session.setAttribute(modelId, workflowData);
        //获取前台界面初始化所需的json数据
        String model = workflowDefinition.getProcessChartContent();
        if (StringUtils.isEmpty(model)) {
            //当新建模板或原模板数据为空时，加载画布的初始化模型
            Scanner reader = new Scanner(this.getClass().getClassLoader().getResourceAsStream("initCanvasProperties.json"));
            model = reader.next();
            reader.close();
        }
        String jsonStr = SysJsonUtil.getInitJson(workflowDefinition.getId(), model);
        SysJsonUtil.returnJson(response, jsonStr);
        
    }
  
}
