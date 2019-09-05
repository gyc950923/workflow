package com.online.engine.resource;

import com.online.workflow.process.enums.PageActionEnum;


public class TaskControl {
	private Integer pageActionEnum=PageActionEnum.none;
	private boolean isQuery;
	private boolean isStartFlow;
	public Integer getPageAction() {
		return pageActionEnum;
	}
	public void setPageAction(Integer pageAction) {
		this.pageActionEnum = pageAction;
	}
	public boolean isQuery() {
		return isQuery;
	}
	public void setQuery(boolean isQuery) {
		this.isQuery = isQuery;
	}
	public boolean isStartFlow() {
		return isStartFlow;
	}
	public void setStartFlow(boolean isStartFlow) {
		this.isStartFlow = isStartFlow;
	}
	
	
}
