package com.online.workflow.design.web;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.workflow.common.DateUtil;
import com.online.workflow.design.utils.NodePropertiesUtils;
import com.online.workflow.design.workflow.bean.FlowDefinitionBean;
import com.online.workflow.design.workflow.bean.FlowNodeProperties;
import com.online.workflow.design.workflow.service.FlowDefinitionService;
import com.online.workflow.design.workflow.service.FlowPropertiesService;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowProcess;

/**
 * 保存流程定义及节点  该类已经暂停使用
 */
@WebServlet("/service/model/save")
public class ModelSaveRestResource extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FlowDefinitionService service = new FlowDefinitionService();
	private FlowPropertiesService proService = new FlowPropertiesService();	
	@Resource(name="persistenceService")
	private IPersistenceService persistenceService;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String modelId = req.getParameter("modelId");
		String json_xml = req.getParameter("json_xml");
		
		String svg_xml = req.getParameter("svg_xml");
		String name = req.getParameter("name");
		String description = req.getParameter("description");

		FlowDefinitionBean bean = new FlowDefinitionBean();
		bean.setModelId(modelId);
		bean.setName(name);
		bean.setDescription(description);
		bean.setModel(json_xml);
		
		this.service.save(bean);
		
		List<FlowNodeProperties> listPro = NodePropertiesUtils.getCurrentPropertiesList(modelId);
		if(null != listPro && listPro.size() > 0){
			this.proService.saveFlowNodePropertiesByList(modelId,listPro);
		}
	
		System.out.println("=========modelId:" + modelId);
		System.out.println("=========json_xml:" + json_xml);
		System.out.println("=========svg_xml:" + svg_xml);
		System.out.println("=========name:" + name);
		System.out.println("=========description:" + description);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
	    String isInsert = request.getParameter("isInsert");
	    String json_xml = request.getParameter("json_xml");
        HttpSession session = request.getSession();
        WorkflowProcess workflowProcess = (WorkflowProcess) session.getAttribute("workflow");
        Integer version = (Integer) session.getAttribute("version");
        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setProcessChartContent(json_xml);
        String id = null;//(String) session.getAttribute("id");
        if (Boolean.valueOf(isInsert)) {
            id = UUID.randomUUID().toString();
            workflowProcess.setChartId(id);
        }else{
            id = workflowProcess.getChartId();
        }
        workflowDefinition.setId(id);
        workflowDefinition.SetWorkflowProcess(workflowProcess);
        workflowDefinition.setTypeId("默认TypeId");
        workflowDefinition.setProcessId(workflowProcess.getName());
        workflowDefinition.setName(workflowProcess.getName());
        workflowDefinition.setVersion(version);
        workflowDefinition.setState(workflowProcess.getState());
        workflowDefinition.setDescription(workflowProcess.getDescription());
        workflowDefinition.setPublishTime(DateUtil.getSysDate());
        workflowDefinition=persistenceService.saveOrUpdateWorkflowDefinition(workflowDefinition, Boolean.valueOf(isInsert));  
        session.setAttribute("version", workflowDefinition.getVersion());
        //session.setAttribute("id", workflowDefinition.getId());
    }
	
}
