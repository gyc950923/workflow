package com.online.workflow.process.enums;

public class TaskTypeEnum {
	 /// <summary>任务类型之二 ：TOOL类型，即工具类型任务，该任务自动调用java代码完成特定的工作。</summary>
	public static final Integer tool=10;
    /// <summary>任务类型之三：SUBFLOW类型，即子流程任务</summary>
	public static final Integer subflow=20;
    /// <summary>任务类型之一：FORM类型，最常见的一类任务，代表该任务需要操作员填写相关的表单。</summary>
	public static final Integer form=30;
    /// <summary>任务类型之四：DUMMY类型，该类型暂时没有用到，保留。</summary>
	public static final Integer dummy=40;
}
