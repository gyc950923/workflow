package com.online.workflow.process.resources;

import java.util.List;

public class PageInfo {

	private String formUrl;

	private List<PageActionItem> pageActions;

	private List<FieldAuthority> fieldAuthoritys;

	private boolean isStartFlow;

	public String getFormUrl() {
		return formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}

	public List<PageActionItem> getPageActions() {
		return pageActions;
	}

	public void setPageActions(List<PageActionItem> pageActions) {
		this.pageActions = pageActions;
	}

	public List<FieldAuthority> getFieldAuthoritys() {
		return fieldAuthoritys;
	}

	public void setFieldAuthoritys(List<FieldAuthority> fieldAuthoritys) {
		this.fieldAuthoritys = fieldAuthoritys;
	}

	public boolean isStartFlow() {
		return isStartFlow;
	}

	public void setStartFlow(boolean isStartFlow) {
		this.isStartFlow = isStartFlow;
	}
	
	

}
