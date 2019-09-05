package com.online.workflow.process.resources;

import com.online.workflow.process.AbstractWFElement;

public class SubWorkflowProcess extends AbstractWFElement {

    public SubWorkflowProcess() {
    }
    
	public SubWorkflowProcess(String name) {
		this.setName(name);
	}

	// / <summary>所引用的流程的id</summary>
	private String workflowProcessId;

	public String getWorkflowProcessId() {
		return workflowProcessId;
	}

	public void setWorkflowProcessId(String workflowProcessId) {
		this.workflowProcessId = workflowProcessId;
	}

}
