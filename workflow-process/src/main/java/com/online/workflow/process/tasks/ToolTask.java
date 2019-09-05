package com.online.workflow.process.tasks;

import com.online.workflow.process.resources.Service;

public class ToolTask extends Task{

	private Service service;

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}
}
