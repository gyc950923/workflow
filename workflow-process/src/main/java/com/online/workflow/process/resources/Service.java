package com.online.workflow.process.resources;

import com.online.workflow.process.AbstractWFElement;

public class Service extends AbstractWFElement {

	private String Handler;

	public String getHandler() {
		return Handler;
	}

	public void setHandler(String handler) {
		Handler = handler;
	}

	public Service() {
	    
	}

	public Service(String name) {
		this.setName(name);

	}
}
