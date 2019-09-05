package com.online.workflow.flowdef.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.engine.WorkFlowHelper;
import com.online.engine.Instance.IWorkFlowHelper;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.engine.resource.PromptControl;
import com.online.util.Result;
import com.online.util.Result.Status;
import com.online.workflow.flowdef.service.IFlowDefService;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.resources.PageInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.util.DataGridModel;
import com.online.workflow.util.PageModel;

@Controller
@RequestMapping("/flowDef")
public class FlowDefController {

    @Resource(name="flowDefService")
    private IFlowDefService flowDefService;
    @Resource(name="persistenceService")
    private IPersistenceService persistenceService;
    @Resource
    IWorkFlowHelper workFlowHelper;    
    @RequestMapping("/flowDefList")
    @ResponseBody
    public DataGridModel flowDefList(PageModel page){
        List<Object> list =flowDefService.flowDefList(page);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(list);
        dataGridModel.setTotal(page.getTotal());
        return dataGridModel;
    }
    
    @RequestMapping("/getStudents")
    @ResponseBody
    public DataGridModel getStudents(PageModel page){
        List<Object> list =flowDefService.getStudents(page);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(list);
        dataGridModel.setTotal(page.getTotal());
        return dataGridModel;
    }
    @RequestMapping("/saveFrom")
    @ResponseBody
    public String saveFrom(String name,String age,String days,String flowId) throws EngineException{
      String entityType = "student";
      String entityId = "b6670c78e0424a35bdd069c41137ad5b";
      UserInfo userInfo = new UserInfo();
      userInfo.setId("1");
      userInfo.setName("郭宇成");
      HashMap<String, Object> vars = new HashMap<String, Object>();
      HashMap<String,String> actors=new HashMap<String,String>();
      Boolean boolean1= workFlowHelper.startWorkFlow(flowId, entityType, entityId, userInfo, actors, vars, "", "", "");
      String id = flowDefService.saveFrom(name,age,days);
        return 	"success";
    }
    @RequestMapping("/advance")
    @ResponseBody
    public String advance(String  workitem,String processid,String comments,String ywid) throws EngineException{
	   UserInfo userInfo = new UserInfo();
       userInfo.setId("2");
       userInfo.setName("aa");
       HashMap<String, String> actors = new HashMap<String, String>();
       HashMap<String, Object> vars = new HashMap<String, Object>();
 		vars.put("aAa", 20.0);
 		vars.put("Bbb", "wYd");
 		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 		vars.put("date", dateFormat.format(new Date()));
 		workFlowHelper.advance(workitem, userInfo, actors, null, vars, "同意审批");
		return "success";
}
    @RequestMapping("/back")
    @ResponseBody
    public String back(String  workitem,String comments) throws EngineException{
	   UserInfo userInfo = new UserInfo();
       userInfo.setId("2");
       userInfo.setName("aa");
       HashMap<String, Object> var = new HashMap<String, Object>();
       PromptControl promptControl=new PromptControl();
       promptControl.setComments("promt不同意");
 		boolean back = workFlowHelper.back(workitem, userInfo, promptControl, var, "不同意审批");
 		if(back){
 			return "success";
 		}
 		return "error";
    }
    @RequestMapping("/termination")
    @ResponseBody
    public void termination(String  workitem,String comments) throws EngineException{
	   UserInfo userInfo = new UserInfo();
       userInfo.setId("258369");
       userInfo.setName("管理员");
 	   workFlowHelper.termination(workitem, userInfo, "同意审批");
}
	
    

}
