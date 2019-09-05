package com.online.workflow.design.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.online.workflow.design.utils.NodePropertiesUtils;
import com.online.workflow.design.workflow.bean.FlowNodeProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 加载、保存节点属性
 */
@WebServlet("/saveOrLoadNodeProperties")
public class SaveOrLoadNodeProperties extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 读取节点属性
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String jsonStr = "{}";
		String resourceId = request.getParameter("resourceId");
		String modelId  = request.getParameter("modelId");
		
		if(null != resourceId && (!resourceId.equals(""))
				&& null != modelId && (!modelId.equals(""))){
			//FlowNodeProperties node = this.service.getFlowNodeProperties(modelId, resourceId);
			FlowNodeProperties node = NodePropertiesUtils.getUndoFlowNodePropertiesById(modelId, resourceId);
			if(null != node){
				ObjectMapper objectMapper =  new ObjectMapper();
				jsonStr = objectMapper.writeValueAsString(node);
			}
		}
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print(jsonStr); 
	}
	
	/**
	 * 保存节点属性
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String resourceId = request.getParameter("resourceId");
		String name = request.getParameter("name");
		String transaction  = request.getParameter("transaction");
		String modelId  = request.getParameter("modelId");
		
		if(null != resourceId && (!resourceId.equals(""))
				&& null != modelId && (!modelId.equals(""))){
		
			FlowNodeProperties node = new FlowNodeProperties();
			node.setModelId(modelId);
			node.setResourceId(resourceId);
			node.setName(name);
			node.setTransaction(transaction);
			
			//this.service.saveNode(node);
			NodePropertiesUtils.undoAdd(modelId, node);
		}
		System.out.println("resourceId:" + resourceId); 
		System.out.println("name:" + name); 
		System.out.println("transaction:" + transaction); 
		
		response.getWriter().print("true");
	}
}
