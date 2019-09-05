package com.online.workflow.process.net;

import java.util.ArrayList;
import java.util.List;

import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.enums.ActivityCallBackEnum;
import com.online.workflow.process.enums.AssignmentStrategyEnum;
import com.online.workflow.process.enums.BackRangeEnum;
import com.online.workflow.process.resources.BackNode;
import com.online.workflow.process.resources.PageInfo;
import com.online.workflow.process.rules.UserRule;
import com.online.workflow.process.tasks.FormTask;
import com.online.workflow.process.tasks.Task;

public class Activity extends Node {

	private List<Task> InlineTasks=new ArrayList<Task>();// / <summary>task列表</summary>
	// / <summary>
	// / <para>返回环节的结束策略，取值为ALL或者ANY，缺省值为ALL</para>
	// / <para>如果取值为ALL,则只有其所有任务实例结束了，环节实例才可以结束。</para>
	// / <para>如果取值为ANY，则只要任何一个任务实例结束后，环节实例就可以结束。</para>
	// / 环节实例的结束操作仅执行一遍，因此后续任务实例的结束不会触发环节实例的结束操作再次执行。
	// / </summary>
	private Integer assignmentStrategyEnum = AssignmentStrategyEnum.all;

	private Integer callBackStatus = ActivityCallBackEnum.none;
	private String localAddress;
	private String restfulAddress;
	
	//短信模板
	private boolean isSendMsg;
	private String msgtTemplate;
	public List<Task> getInlineTasks() {
		return InlineTasks;
	}

	public void setInlineTasks(List<Task> inlineTasks) {
		InlineTasks = inlineTasks;
	}

	

	public Integer getAssignmentStrategyEnum() {
		return assignmentStrategyEnum;
	}

	public void setAssignmentStrategyEnum(Integer assignmentStrategyEnum) {
		this.assignmentStrategyEnum = assignmentStrategyEnum;
	}

	public Activity() {

		this.InlineTasks = new ArrayList<Task>();
		this.assignmentStrategyEnum = AssignmentStrategyEnum.all;

	}

	// / <summary>
	// / 返回该环节所有的Task。
	// / 这些Task是inlineTask列表和taskRef列表解析后的所有的Task的和。
	// / </summary>
	public List<Task> getTasks() {

		return getInlineTasks();
	}

	// / <summary>
	// / 获取回退范围
	// / </summary>
	// / <returns></returns>
	public List<BackNode> getBackRanges() {
		WorkflowProcess process = ((WorkflowProcess) getParentElement());

		List<BackNode> backNodes = new ArrayList<BackNode>();
		BackNode model1 = new BackNode();
		model1.setBackRangeType(BackRangeEnum.none);
		model1.setName("无");
		model1.setBackRangeType(-1);

		/*
		 * BackNode model2 = new BackNode(); model2.BackRangeType =
		 * BackRangeEnum.anyNode; model2.Name = "任意步骤"; model2.NodeId = "0";
		 * 
		 * BackNode model3 = new BackNode(); model3.BackRangeType =
		 * BackRangeEnum.processNode; model3.NodeId=process.StartNode.Code;
		 * model3.Name = process.StartNode.DisplayName;
		 * 
		 * backNodes.Add(model1); backNodes.Add(model2); backNodes.Add(model3);
		 * 
		 * List<Activity> activitys = process.Activities; foreach (var item in
		 * activitys) { BackNode model4 = new BackNode(); model4.BackRangeType =
		 * BackRangeEnum.processNode; model4.NodeId = item.Code; model4.Name =
		 * item.DisplayName; backNodes.Add(model4); }
		 */
		return backNodes;

	}

	public PageInfo findPageInfo() {
		List<Task> tasks = getTasks();
		PageInfo pageInfo = new PageInfo();
		for (Task task : tasks) {
            if (task instanceof FormTask) {
                FormTask formTask = (FormTask)task;
                UserRule userRule = formTask.getUserRule();
                pageInfo.setFormUrl(userRule.getEditForm().getUrl());
                pageInfo.setPageActions(formTask.getPageActions());
                pageInfo.setFieldAuthoritys(formTask.getFieldAuthoritys());
                break;
            }
        }
		return pageInfo;
	}

    public Integer getCallBackStatus() {
        return callBackStatus;
    }

    public void setCallBackStatus(Integer callBackStatus) {
        this.callBackStatus = callBackStatus;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getRestfulAddress() {
        return restfulAddress;
    }

    public void setRestfulAddress(String restfulAddress) {
        this.restfulAddress = restfulAddress;
    }

    
	public boolean getIsSendMsg() {
		return isSendMsg;
	}

	public void setIsSendMsg(boolean isSendMsg) {
		this.isSendMsg = isSendMsg;
	}

	public String getMsgtTemplate() {
		return msgtTemplate;
	}

	public void setMsgtTemplate(String msgtTemplate) {
		this.msgtTemplate = msgtTemplate;
	}
	
}
