package com.online.workflow.design.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.online.workflow.design.utils.NodePropertiesUtils;
import com.online.workflow.design.workflow.service.IDelNodeService;
import com.online.workflow.design.workflow.service.impl.DelNodeServiceImpl;
import com.online.workflow.process.IWFElement;
import com.online.workflow.process.WorkflowProcess;

/**
 * 删除对应节点的属性文件
 */
@WebServlet("/delNodeProperties")
@SuppressWarnings("unchecked")
public class DelNodeProperties extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private IDelNodeService delNodeService = new DelNodeServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		boolean ret = true;
		
		String resourceIds = request.getParameter("resourceIds");
		String modelId = request.getParameter("modelId");
		
		if(null != resourceIds && (!resourceIds.equals("")) && null != modelId && (!modelId.equals(""))){
			
			String[] ids = resourceIds.split(",");
			for(String resourceId : ids){
			
				NodePropertiesUtils.delFlowNodeProperties(modelId, resourceId);
				
				System.out.println("resourceId:" + resourceId);
			}
		}
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(ret); 
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String resourceIds = request.getParameter("resourceIds");
        String ctypes = request.getParameter("ctypes");
        String modelId = request.getParameter("modelId");
        
        HttpSession session = request.getSession();
        
        if(null != resourceIds && (!resourceIds.equals("")) && null != modelId && (!modelId.equals(""))){
            Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
            WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
            List<IWFElement> undoList = (List<IWFElement>) workflowData.get("undoList");
            String[] resourceIdArray = resourceIds.split(",");
            String[] ctypeArray = ctypes.split(",");
            for(int i = 0; i < resourceIdArray.length; i++){
                IWFElement iWFElement=workflowProcess.findWFElementByChartId(resourceIdArray[i]);
                if("StartNoneEvent".equals(ctypeArray[i])){   
                    delNodeService.delStartNode(workflowProcess);         
                }else if("UserTask".equals(ctypeArray[i])){
                    delNodeService.delActivity(workflowProcess,iWFElement);
                }else if("SequenceFlow".equals(ctypeArray[i])){
                    delNodeService.delTransition(workflowProcess,iWFElement);
                }else if("EndNoneEvent".equals(ctypeArray[i])){
                    delNodeService.delEndNode(workflowProcess,iWFElement);
                }else if ("ConditionGateway".equals(ctypeArray[i])) {
                    delNodeService.delConditionGatewayEvent(workflowProcess,iWFElement);
                }
                undoList.add(iWFElement);
            }
        }
        
	}

}
