package com.online.workflow.process.enums;

public class ActorAssignTypeEnum {
	// / <summary>
	// / 通过活动节点配置直立人
	// / </summary>
	public static final Integer appoint = 10;
	// / <summary>
	// / 通过表达式动态计算处理人
	// / </summary>
	public static final Integer expression = 20;
	
	public static final Integer sqlexpression = 30;//按sql规则
	
	public static final Integer restfulexpression = 40;//按Restful接口规则
	
	public static final Integer DEFAULTCREATEDEPARTMENT = 50; //部门来源--默认业务单据创建部门
	public static final Integer PREVIOUSHANDLER = 60; //部门来源--上一处理人
	public static final Integer DESIGNATEDDEPARTMENT = 70; //部门来源--指定部门
	public static final Integer CUSTOMIZESQL = 80; //部门来源--自定义sql
	public static final Integer ISAPPALYTONODE = 90; //部门来源--公用节点信息
	
	
}
