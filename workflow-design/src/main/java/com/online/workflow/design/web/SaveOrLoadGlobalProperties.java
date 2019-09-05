package com.online.workflow.design.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.forms.Form;

/**
 * 该类为原全局属性的相关操作servlet,现已不再调用
 */

/**
 * 保存、加载全局属性
 */
@WebServlet("/saveOrLoadGlobalProperties")
public class SaveOrLoadGlobalProperties extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
//	private FlowPropertiesService service = new FlowPropertiesService();
	
	/**
	 * 加载全局属性
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String jsonStr="{}";
	    HttpSession session=request.getSession();
	    WorkflowProcess workflow=(WorkflowProcess) session.getAttribute("workflow");
	    if(workflow==null){
            workflow=new WorkflowProcess();
        }
	    response.setContentType("application/json;charset=UTF-8");
	    if(workflow!=null){
	        jsonStr=JSONObject.fromObject(workflow).toString();
	    }
	    response.getWriter().print(jsonStr);
	}
	
	/**
	 * 保存全局属性
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /* 获取前台传来的属性值*/
	    String modelId  = request.getParameter("modelId");
//        String resourceId  = request.getParameter("resourceId");
	    String name = request.getParameter("name");
		String description  = request.getParameter("description");
		String entityName = request.getParameter("entityName");
		String formName  = request.getParameter("formName");
		boolean state = Boolean.valueOf(request.getParameter("state"));
		
		HttpSession session=request.getSession();
		String jsonStr="{}";
		WorkflowProcess workflow=(WorkflowProcess) session.getAttribute("workflow");
        workflow.setChartId(modelId);
        workflow.setName(name);
        workflow.setDescription(description);
        workflow.setEntityName(entityName);
        workflow.setForm(new Form(formName));
        workflow.setState(state);
        session.setAttribute("workflow",workflow);
        response.getWriter().print(jsonStr);
        /*
		if(Boolean.valueOf(option)){//option为true，则加载全局属性
		    session=request.getSession();
		    node=(FlowGlobalProperties) session.getAttribute("global_"+modelId);
		    String jsonStr=null;
		    response.setContentType("application/json;charset=UTF-8");
		    if(null != node){
                ObjectMapper objectMapper =  new ObjectMapper();
                jsonStr = objectMapper.writeValueAsString(node);
                response.getWriter().print(jsonStr); 
            }else{
                response.getWriter().print("{}");
            }
		    session=request.getSession();
		    workflow=(WorkflowProcess) session.getAttribute("globalProperties"+modelId);
		}else{
		    //option为false，则保存全局属性
		    node = new FlowGlobalProperties();
            node.setModelId(modelId);
            node.setResourceId(resourceId);
            node.setName(name);
            node.setTransaction(transaction);
            session.setAttribute("global_"+modelId, node);
            response.getWriter().print("{}");
		    
		}
		if( null != modelId && (!modelId.equals(""))){
		
			FlowGlobalProperties node = new FlowGlobalProperties();
			node.setModelId(modelId);
			node.setResourceId(modelId);
			node.setName(name);
			node.setTransaction(transaction);
			
			this.service.saveGlobal(node);
		}
		System.out.println("modelId:" + modelId); 
		System.out.println("name:" + name); 
		System.out.println("transaction:" + transaction); 
		
		
		response.getWriter().print("true");
		*/
	}
}
