package com.online.workflow.process.resources;

import java.util.ArrayList;
import java.util.List;

public class Performer {
	private List<UserInfo> userInfos = new ArrayList<UserInfo>();

	private List<DeptInfo> deptInfos = new ArrayList<DeptInfo>();

	private List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();

	private List<OrgRoleInfo> orgRoleInfos = new ArrayList<OrgRoleInfo>();

	private SqlInfo sqlInfos = new SqlInfo();

	private RestInfo restInfos = new RestInfo();
	
	private OrgRoleInfo orgRoleInfo= new OrgRoleInfo();

	public List<UserInfo> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(List<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}

	public List<DeptInfo> getDeptInfos() {
		return deptInfos;
	}

	public void setDeptInfos(List<DeptInfo> deptInfos) {
		this.deptInfos = deptInfos;
	}

	public List<RoleInfo> getRoleInfos() {
		return roleInfos;
	}

	public void setRoleInfos(List<RoleInfo> roleInfos) {
		this.roleInfos = roleInfos;
	}

	public SqlInfo getSqlInfos() {
		return sqlInfos;
	}

	public void setSqlInfos(SqlInfo sqlInfos) {
		this.sqlInfos = sqlInfos;
	}

	public RestInfo getRestInfos() {
		return restInfos;
	}

	public void setRestInfos(RestInfo restInfos) {
		this.restInfos = restInfos;
	}

	public List<OrgRoleInfo> getOrgRoleInfos() {
		return orgRoleInfos;
	}

	public void setOrgRoleInfos(List<OrgRoleInfo> orgRoleInfos) {
		this.orgRoleInfos = orgRoleInfos;
	}

	public OrgRoleInfo getOrgRoleInfo() {
		return orgRoleInfo;
	}

	public void setOrgRoleInfo(OrgRoleInfo orgRoleInfo) {
		this.orgRoleInfo = orgRoleInfo;
	}

}
