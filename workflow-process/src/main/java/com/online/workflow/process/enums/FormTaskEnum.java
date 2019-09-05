package com.online.workflow.process.enums;

public class FormTaskEnum {

	 /// <summary>
    /// 任务分配策略之一：ALL。任务分配给角色中的所有人，只有在所有工单结束结束的情况下，任务实例才结束。
    /// 用于实现会签。
    /// </summary>
	public static final Integer all=10;
    /// <summary>
    /// 任务分配策略之二：按顺序执行，待办一个处理完了另一个人再处理
    /// </summary>
	public static final Integer sequence=20;
    /// <summary>任务分配策略之三：ANY。任何一个操作角签收该任务的工单后，其他人的工单被取消掉。</summary>
	public static final Integer any=30;
}
