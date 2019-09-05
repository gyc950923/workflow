package com.online.workflow.process;

import java.util.List;

import com.online.workflow.process.net.Activity;

public interface IWorkflowProcess {
	Activity getEntryActivity();
	WorkflowProcess cloneWorkflowProcess();
	List<Activity> getEntryActivitys();
	IWFElement findWFElementById(String id);
	IWFElement findWFElementByChartId(String chartId);
	String validate();
	Boolean isReachable(String fromNodeId, String toNodeId);
  
}
