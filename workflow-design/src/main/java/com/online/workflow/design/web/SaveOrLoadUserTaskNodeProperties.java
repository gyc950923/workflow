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

import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.resources.DeptInfo;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.Performer;
import com.online.workflow.process.resources.SqlInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.process.rules.AdvanceRule;
import com.online.workflow.process.rules.BackRule;
import com.online.workflow.process.rules.UserRule;
import com.online.workflow.process.tasks.FormTask;
import com.online.workflow.process.tasks.Task;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 该类为原用户活动节点的相关操作servlet,现已不再调用
 */

/**
 * 加载、保存用户活动节点属性
 */
@WebServlet("/saveOrLoadUserTaskNodeProperties")
public class SaveOrLoadUserTaskNodeProperties extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 读取节点属性
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String modelId  = request.getParameter("modelId");
		String resourceId = request.getParameter("resourceId");
		String jsonStr = "{}";
		
		if(null != resourceId && (!resourceId.equals(""))
				&& null != modelId && (!modelId.equals(""))){
			HttpSession session=request.getSession();
			/*workflow节点属性*/
			WorkflowProcess workflow=(WorkflowProcess) session.getAttribute("workflow");
			Activity activity=(Activity) workflow.findWFElementByChartId(resourceId);	
			/*回退规则初始化参数*/
			jsonStr=JSONObject.fromObject(activity).toString();
			JSONArray formType=new JSONArray();
			formType.add("{'text':'可编辑表单','value':'可编辑表单'}");
			formType.add("{'text':'只读表单','value':'只读表单'}");
			formType.add("{'text':'列表表单','value':'列表表单'}");
			JSONArray returnMode=new JSONArray();
			returnMode.add("{'text':'none','value':'0'}");
			returnMode.add("{'text':'anyNode','value':'10'}");
			returnMode.add("{'text':'processNode','value':'20'}");
			JSONArray assignmentStrategy=new JSONArray();
			assignmentStrategy.add("{'text':'all','value':'10'}");
			assignmentStrategy.add("{'text':'sequence','value':'20'}");
			assignmentStrategy.add("{'text':'any','value':'30'}");
			//String[] returnMode={"0","10","20"};
			//String[] formType={"可编辑表单","只读表单","列表表单"};
			Map<String, Object> jsonMap=new HashMap<String, Object>();
			jsonMap.put("returnMode", returnMode);
			jsonMap.put("formType", formType);
			jsonMap.put("assignmentStrategy", assignmentStrategy);
			JSONObject jsonObject=JSONObject.fromObject(activity);
			JSONArray jSONArray=new JSONArray();
			jSONArray.add(jsonObject);
			jSONArray.add(jsonMap);
			jsonStr=jSONArray.toString();
		}
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print(jsonStr); 
	}
	
	/**
	 * 保存节点属性
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
	    String modelId = request.getParameter("modelId");
	    String resourceId = request.getParameter("resourceId");
		/*常规*/
	    String name = request.getParameter("name");
		String entityDocStatusName = request.getParameter("entityDocStatusName");//运营状态
		String description = request.getParameter("description");
		/*流转规则*/
		boolean isAdvanceConfirm = Boolean.valueOf(request.getParameter("isAdvanceConfirm"));
		boolean isAdjustStaff = Boolean.valueOf(request.getParameter("isAdjustStaff"));
		boolean isToBeRead = Boolean.valueOf(request.getParameter("isToBeRead"));
		/*执行规则*/
		String title = request.getParameter("title");
		String formType = request.getParameter("formType");
		String formName = request.getParameter("formName");
		//执行者
 		String userName = request.getParameter("userName");
//		String department = request.getParameter("department");
		//任务协作
		Integer assignmentStrategy = Integer.valueOf(request.getParameter("assignmentStrategy"));
		//循环策略
		/*回退规则*/
		boolean isBackConfirm = Boolean.valueOf(request.getParameter("isBackConfirm"));
		Integer returnMode=Integer.valueOf(request.getParameter("returnMode"));
		
		//回退范围
		
		if(null != resourceId && (!resourceId.equals(""))
				&& null != modelId && (!modelId.equals(""))){
		    HttpSession session=request.getSession();
            WorkflowProcess workflow=(WorkflowProcess) session.getAttribute("workflow");
            
            Activity activity=(Activity) workflow.findWFElementByChartId(resourceId);
            
            /*常规*/
            activity.setName(name);
            activity.setDescription(description);
            activity.setEntityDocStatusName(entityDocStatusName);
            
            FormTask formTask=new FormTask();
            /*流转规则*/
            AdvanceRule advanceRule=new AdvanceRule();
            advanceRule.setAdvanceConfirm(isAdvanceConfirm);
            advanceRule.setAdjustStaff(isAdjustStaff);
            advanceRule.setIsToBeRead(isToBeRead);
            formTask.setAdvanceRule(advanceRule);
            
            /*执行规则*/
            formTask.setName(title);
            Form form=new Form(formName);
            UserRule userRule=new UserRule();
            if("可编辑表单".equals(formType)){
                userRule.setEditForm(form);
            }else if ("只读表单".equals(formType)) {
                userRule.setViewForm(form);
            }else {
                userRule.setListForm(form);
            }
            
            Performer performer=new Performer();
            //执行者
            UserInfo userInfo=new UserInfo();//人员
            userInfo.setName(userName);
            List<UserInfo> userInfoList=new ArrayList<UserInfo>();
            userInfoList.add(userInfo);
            performer.setUserInfos(userInfoList);
            DeptInfo deptInfo=new DeptInfo();//部门
            List<DeptInfo> deptInfoList=new ArrayList<DeptInfo>();
            deptInfoList.add(deptInfo);
            performer.setDeptInfos(deptInfoList);
            performer.setSqlInfos(new SqlInfo());//按照sql
            
            OrgRoleInfo orgRoleInfo = new OrgRoleInfo();
            List<OrgRoleInfo> orgRoleInfoList=new ArrayList<OrgRoleInfo>();
            orgRoleInfoList.add(orgRoleInfo);
            performer.setOrgRoleInfos(orgRoleInfoList);//按照部门和角色
            
            //任务协作
            userRule.setFormTaskEnum(assignmentStrategy);
            //循环策略
            userRule.setPerformer(performer);
            
            formTask.setUserRule(userRule);
            /*回退规则*/
            BackRule backRule=new BackRule();
            backRule.setBackConfirm(isBackConfirm);
            backRule.setReturnMode(returnMode);
            backRule.setBackRange(null);//回退方式
            formTask.setBackRule(backRule);
            
            List<Task> taskList=new ArrayList<Task>();
            taskList.add(formTask);
            activity.setInlineTasks(taskList);
            
            session.setAttribute("workflow", workflow);
		}
		
		response.getWriter().print("true");
	}
}
