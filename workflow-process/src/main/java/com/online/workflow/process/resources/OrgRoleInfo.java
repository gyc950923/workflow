package com.online.workflow.process.resources;

public class OrgRoleInfo {

	private String id;

	private String name;

	/*
	 * 部门来源 50： 业务单据所属创建部门 60：根据上一步处理人所属部门 70：指定所属部门 80：根据自定义sql查询出部门id
	 */
	private String departmentSrc;

	/*
	 * 所有节点是否应用同一个自定义sql
	 */
	private String allNode;

	/*
	 * 自定义sql或者指定部门名称
	 */
	private String conditionName;

	/*
	 * 指定部门id
	 */
	private String conditionId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartmentSrc() {
		return departmentSrc;
	}

	public void setDepartmentSrc(String departmentSrc) {
		this.departmentSrc = departmentSrc;
	}

	public String getAllNode() {
		return allNode;
	}

	public void setAllNode(String allNode) {
		this.allNode = allNode;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

}
