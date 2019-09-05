package com.online.workflow.process.net;

public class Activitys {

	/*
	 * 部门来源 50： 业务单据所属创建部门 60：根据上一步处理人所属部门 70：指定所属部门 80：根据自定义sql查询出部门id
	 */
	public Integer departmentSrc;

	/*
	 * 所有节点是否应用同一个自定义sql
	 */
	public Integer allNode;

	/*
	 * 自定义sql或者指定部门名称
	 */
	public String conditionName;

	/*
	 * 指定部门id
	 */
	public String conditionId;

	public Integer getDepartmentSrc() {
		return departmentSrc;
	}

	public void setDepartmentSrc(Integer departmentSrc) {
		this.departmentSrc = departmentSrc;
	}

	public Integer getAllNode() {
		return allNode;
	}

	public void setAllNode(Integer allNode) {
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
