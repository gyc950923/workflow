package com.online.workflow.design.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.online.workflow.design.utils.SysJsonUtil;
import com.online.workflow.design.workflow.service.IInitNodeService;
import com.online.workflow.design.workflow.service.impl.InitNodeServiceImpl;
import com.online.workflow.process.IWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.net.ConditionFork;
import com.online.workflow.process.net.EndNode;
import com.online.workflow.process.net.StartNode;
import com.online.workflow.process.net.Transition;

/**
 * 初始化节点属性
 */
@WebServlet("/initNodeProperties")
@SuppressWarnings("unchecked")
public class InitNodeProperties extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private IInitNodeService initNodeService=new InitNodeServiceImpl();   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request,response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String modelId = request.getParameter("modelId");
		String resourceId = request.getParameter("resourceId");
		String fromResourceId = request.getParameter("fromResourceId");
		String toResourceId = request.getParameter("toResourceId");
		String ctype = request.getParameter("ctype");
		HttpSession session=request.getSession();
        Map<String,Object> workflowData = (Map<String,Object>) session.getAttribute(modelId);
		if(StringUtils.isNotEmpty(resourceId) && StringUtils.isNotEmpty(modelId)){
			WorkflowProcess workflowProcess=(WorkflowProcess) workflowData.get("workflowProcess");
			if(null!=workflowProcess){
			    IWFElement iWFElement=workflowProcess.findWFElementByChartId(resourceId);
			    if("StartNoneEvent".equals(ctype)){   
			        initNodeService.startNoneEvent((StartNode) iWFElement,workflowProcess,resourceId);
			    }else if("UserTask".equals(ctype)){
			        initNodeService.userTaskNoneEvent((Activity) iWFElement,workflowProcess,resourceId);
			    }else if("SequenceFlow".equals(ctype)){
			        initNodeService.sequenceFlowEvent((Transition) iWFElement,workflowProcess,resourceId,fromResourceId,toResourceId);
                }else if("EndNoneEvent".equals(ctype)){
                    initNodeService.endNoneEvent((EndNode) iWFElement,workflowProcess,resourceId);
                }else if ("ConditionGateway".equals(ctype)) {
                    initNodeService.conditionGatewayEvent((ConditionFork) iWFElement,workflowProcess,resourceId);
                }
	            session.setAttribute(modelId, workflowData);
			}
		}
		//此处打印，是为了辅助前台操作时判断是否会触发事件
		System.out.println("resourceId:" + resourceId); 
		System.out.println("ctype:" + ctype);
		
		SysJsonUtil.returnText(response, "true");
	} 

}
