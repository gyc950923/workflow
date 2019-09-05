package com.online.workflow.design.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.online.engine.pluginDefinition.DefinitionService;
import com.online.engine.pluginDefinition.IDefinitionService;
import com.online.workflow.design.utils.NodePropertiesUtils;
import com.online.workflow.design.utils.SysJsonUtil;
import com.online.workflow.design.workflow.bean.FlowDefinitionBean;
import com.online.workflow.design.workflow.bean.FlowNodeProperties;
import com.online.workflow.design.workflow.service.FlowDefinitionService;
import com.online.workflow.design.workflow.service.FlowPropertiesService;
import com.online.workflow.process.IWFElement;
import com.online.workflow.process.WorkflowDefinition;
import com.online.workflow.process.WorkflowProcess;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 加载流程定义及节点属性 改类已停用
 */
@WebServlet("/service/model/json")
public class ModelEditorJsonRestResource extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FlowDefinitionService service = new FlowDefinitionService();
	private FlowPropertiesService proService = new FlowPropertiesService();
	private IDefinitionService definitionService = new DefinitionService();
	//原doGet
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		
		String modelId = request.getParameter("modelId");
		System.out.println("modelId:==========>" + modelId);
		
		String jsonStr = "";
		modelId="b8064b1c-bb3d-47ed-be4a-ce83f9a2da4c";
		FlowDefinitionBean flow = this.service.findFlowDefinitionBeanById(modelId);
		
		List<FlowNodeProperties> listPro = this.proService.getFlowNodePropertiesByList(modelId);
		NodePropertiesUtils.init(modelId, listPro);
		
		response.setContentType("text/html;charset=UTF-8");
		ObjectMapper objectMapper =  new ObjectMapper();
		//objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		
		try {
			
			if(null != flow){
			
				jsonStr = objectMapper.writeValueAsString(flow);
				jsonStr = jsonStr.replace("\\", "");
				jsonStr = jsonStr.replace("\"{", "{");
				jsonStr = jsonStr.replace("}\"", "}");
			}else{
				ObjectNode modelNode =  (ObjectNode)objectMapper.readTree(this.getClass().getClassLoader().getResourceAsStream("initCanvasProperties.json"));
				jsonStr = objectMapper.writeValueAsString(modelNode);
			}
			
			response.getWriter().print(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String modelId = request.getParameter("modelId");
        WorkflowDefinition workflowDefinition = definitionService.getWorkflowDefinitionByDefId(modelId);//getWorkflowDefinitionByProcessIdAndVersionNumber(modelId, 1);
        WorkflowProcess workflowProcess = workflowDefinition.getWorkflowProcess();
        HttpSession session = request.getSession();
        List<IWFElement> undoList = new ArrayList<IWFElement>();
        List<IWFElement> redoList = new ArrayList<IWFElement>();
        Map<String,Object> workflowData = new HashMap<String,Object>();
        workflowData.put("workflowDefinition", workflowDefinition);
        workflowData.put("workflowProcess", workflowProcess);
        workflowData.put("version", workflowDefinition.getVersion());
        workflowData.put("redoList", redoList);
        workflowData.put("undoList", undoList);
        session.setAttribute(modelId, workflowData);
        /*session.setAttribute("redoList", iWFElements);
        session.setAttribute("workflow", workflowProcess);
        session.setAttribute("version", workflowDefinition.getVersion());*/
        //session.setAttribute("id", workflowDefinition.getId());
        String model = workflowDefinition.getProcessChartContent();
        if (StringUtils.isEmpty(model)) {
            //当新建模板时，加载画布的初始化模型
            ObjectMapper objectMapper =  new ObjectMapper();
            ObjectNode modelNode = (ObjectNode)objectMapper.readTree(this.getClass().getClassLoader().getResourceAsStream("initCanvasProperties.json"));
            model = objectMapper.writeValueAsString(modelNode);       
        }
        String jsonStr = SysJsonUtil.getInitJson(workflowDefinition.getId(), model);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(jsonStr);
	}

}
