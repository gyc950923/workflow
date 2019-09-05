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

import org.apache.commons.lang.StringUtils;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.design.utils.NodePropertiesUtils;
import com.online.workflow.design.workflow.service.IDelNodeService;
import com.online.workflow.design.workflow.service.IRedoService;
import com.online.workflow.design.workflow.service.impl.DelNodeServiceImpl;
import com.online.workflow.design.workflow.service.impl.RedoServiceImpl;
import com.online.workflow.process.IWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.net.StartNode;
import com.online.workflow.process.net.Transition;

/**
 * 重复操作
 */
@WebServlet("/redoData")
@SuppressWarnings("unchecked")
public class RedoData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IDelNodeService delNodeService = new DelNodeServiceImpl();
	private IRedoService redoService = new RedoServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		boolean ret = true;
		
		String resourceIds = request.getParameter("resourceIds");
		String modelId = request.getParameter("modelId");
		if(null != resourceIds && (!resourceIds.equals("")) && null != modelId && (!modelId.equals(""))){
			
			String[] ids = resourceIds.split(",");
			for(String resourceId : ids){
				
				NodePropertiesUtils.redoAdd(modelId,resourceId); 
				System.out.println("resourceId:" + resourceId);
				}
		}
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(ret); 
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String resourceIds = request.getParameter("resourceIds");
        String ctypes = request.getParameter("ctypes");System.out.println(ctypes);
        String modelId = request.getParameter("modelId");
        
        if (StringUtils.isNotEmpty(resourceIds) && StringUtils.isNotEmpty(modelId)) {
            HttpSession session = request.getSession();
            Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
            WorkflowProcess workflowProcess = (WorkflowProcess) workflowData.get("workflowProcess");
            List<IWFElement> undoList = (List<IWFElement>) workflowData.get("undoList");
            List<IWFElement> redoList = (List<IWFElement>) workflowData.get("redoList");
            
            String[] resourceIdArray = resourceIds.split(",");
            String[] ctypeArray = ctypes.split(",");
            for(int i = 0; i < resourceIdArray.length; i++){
                IWFElement iWFElement = workflowProcess.findWFElementByChartId(resourceIdArray[i]);
                if (ValidateUtil.isNull(iWFElement)) {//回退删除操作
                    iWFElement = redoService.getWFElementByChartId(redoList,resourceIdArray[i]);
                    if("StartNoneEvent".equals(ctypeArray[i])){   
                        workflowProcess.setStartNode((StartNode) iWFElement);
                    }else if("UserTask".equals(ctypeArray[i])){
                        workflowProcess.getActivities().add((Activity) iWFElement);
                    }else if("SequenceFlow".equals(ctypeArray[i])){
                        workflowProcess.getTransitions().add((Transition) iWFElement);
                    }
                }else{//回退添加操作
                    if("StartNoneEvent".equals(ctypeArray[i])){   
                        delNodeService.delStartNode(workflowProcess);
                    }else if("UserTask".equals(ctypeArray[i])){
                        delNodeService.delActivity(workflowProcess,iWFElement);
                    }else if("SequenceFlow".equals(ctypeArray[i])){
                        delNodeService.delTransition(workflowProcess,iWFElement);
                    }
                }
                undoList.add(iWFElement);
                redoService.removeWFElementByChartId(redoList,resourceIdArray[i]);
            }      
        }
	}
	
	

}
