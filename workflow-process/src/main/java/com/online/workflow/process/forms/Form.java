package com.online.workflow.process.forms;

import com.online.workflow.process.AbstractWFElement;

public class Form extends AbstractWFElement {
	// / <summary>表单的地址。工作流引擎不处理该url，所以其格式只要业务系统能够解析即可。</summary>
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Form() {
	    
	}
	public Form(String name) {
		this.setName(name);
	}
}
