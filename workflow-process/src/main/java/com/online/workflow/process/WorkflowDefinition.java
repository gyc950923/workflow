package com.online.workflow.process;

import com.online.workflow.process.parser.ProcessParserHelper;

public class WorkflowDefinition extends WorkflowDefinitionInfo {

	private WorkflowProcess workflowProcess;

	public WorkflowProcess getWorkflowProcess() {

		if (workflowProcess == null) {
			if (getProcessContent() != null && !getProcessContent().isEmpty()) {
				ProcessParserHelper parser = new ProcessParserHelper();
				return parser.XMLToProcess(getProcessContent());
			}
		}
		return workflowProcess;
	}

	public void SetWorkflowProcess(WorkflowProcess process) {

		ProcessParserHelper parser = new ProcessParserHelper();
		setProcessContent(parser.ProcessToXML(process));
	}

}
