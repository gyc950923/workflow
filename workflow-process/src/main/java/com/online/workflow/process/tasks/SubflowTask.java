package com.online.workflow.process.tasks;

import com.online.workflow.process.resources.SubWorkflowProcess;

public class SubflowTask extends Task{
	
	 private SubWorkflowProcess subWorkflowProcess ;

	public SubWorkflowProcess getSubWorkflowProcess() {
		return subWorkflowProcess;
	}

	public void setSubWorkflowProcess(SubWorkflowProcess subWorkflowProcess) {
		this.subWorkflowProcess = subWorkflowProcess;
	}
}
