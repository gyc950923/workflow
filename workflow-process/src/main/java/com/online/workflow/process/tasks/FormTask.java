package com.online.workflow.process.tasks;

import java.util.ArrayList;
import java.util.List;

import com.online.workflow.process.resources.FieldAuthority;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.resources.PageActionItem;
import com.online.workflow.process.rules.AdvanceRule;
import com.online.workflow.process.rules.BackRule;
import com.online.workflow.process.rules.UserRule;

/**
 * @author jpl
 *
 */
public class FormTask extends Task {

	/**
	 * 
	 */
	private static final long serialVersionUID = 328491106576187030L;
	private UserRule userRule;
	private BackRule backRule;
	private AdvanceRule advanceRule;
	private List<PageActionItem> pageActions=new ArrayList<PageActionItem>();
	private List<FieldAuthority> fieldAuthoritys=new ArrayList<FieldAuthority>();
	private OrgRoleInfo activitsPublicParam= new OrgRoleInfo();
	public UserRule getUserRule() {
		return userRule;
	}

	public void setUserRule(UserRule userRule) {
		this.userRule = userRule;
	}

	public BackRule getBackRule() {
		return backRule;
	}

	public void setBackRule(BackRule backRule) {
		this.backRule = backRule;
	}

	public AdvanceRule getAdvanceRule() {
		return advanceRule;
	}

	public void setAdvanceRule(AdvanceRule advanceRule) {
		this.advanceRule = advanceRule;
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

	public OrgRoleInfo getActivitsPublicParam() {
		return activitsPublicParam;
	}

	public void setActivitsPublicParam(OrgRoleInfo activitsPublicParam) {
		this.activitsPublicParam = activitsPublicParam;
	}



}
