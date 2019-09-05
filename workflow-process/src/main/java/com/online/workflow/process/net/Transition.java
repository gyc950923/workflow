package com.online.workflow.process.net;

import java.util.ArrayList;
import java.util.List;

import com.online.workflow.process.enums.TransitionStartEnum;
import com.online.workflow.process.resources.PageActionItem;

public class Transition extends Edge {

	/** 变量功能的说明 */
	private static final long serialVersionUID = 1L;

	/* 连接线上的分支条件类型，默认为无 */
	private Integer start = TransitionStartEnum.none;

	/* sql条件 */
	private String sqlCondition;// SQL语句
	private String sqlOperator;// 运算符号
	private String sqlResult;// 运算结果

	/* 自定义方法 */
	private String className;// 绝对路径
	private String methodName;// 方法名

	/* 自定义变量 */
	private String varCondition;// 自定义变量
	private String varOperator;// 运算符号
	private String varResult;// 运算结果
	private String toNodeId;// 下一节点id

	private List<PageActionItem> pageActions = new ArrayList<PageActionItem>();

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getSqlCondition() {
		return sqlCondition;
	}

	public void setSqlCondition(String sqlCondition) {
		this.sqlCondition = sqlCondition;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getVarCondition() {
		return varCondition;
	}

	public void setVarCondition(String varCondition) {
		this.varCondition = varCondition;
	}

	public String getSqlOperator() {
		return sqlOperator;
	}

	public void setSqlOperator(String sqlOperator) {
		this.sqlOperator = sqlOperator;
	}

	public String getSqlResult() {
		return sqlResult;
	}

	public void setSqlResult(String sqlResult) {
		this.sqlResult = sqlResult;
	}

	public String getVarOperator() {
		return varOperator;
	}

	public void setVarOperator(String varOperator) {
		this.varOperator = varOperator;
	}

	public String getVarResult() {
		return varResult;
	}

	public void setVarResult(String varResult) {
		this.varResult = varResult;
	}

	public String getToNodeId() {
		return toNodeId;
	}

	public void setToNodeId(String toNodeId) {
		this.toNodeId = toNodeId;
	}

	public List<PageActionItem> getPageActions() {
		return pageActions;
	}

	public void setPageActions(List<PageActionItem> pageActions) {
		this.pageActions = pageActions;
	}

}
