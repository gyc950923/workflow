package com.online.workflow.process.enums;

public class LoopStrategyEnum {
	// / <summary>
	// / 循环情况下，任务分配指示之一：重做<br />
	// / 对于Tool类型和Subflow类型的task会重新执行一遍
	// / 对于Form类型的Task，重新执行一遍，且将该任务实例分配给最近一次完成同一任务的操作员。(有上次处理人重新处理)
	// / </summary>
	public static final Integer redo = 10;
	// / <summary>
	// / 循环情况下，任务分配指示之二：忽略<br />
	// / 循环的情况下该任务将被忽略，即在流程实例的生命周期里，仅执行一遍。(流程中只执行一次)
	// / </summary>
	public static final Integer skip = 20;
	// / <summary>
	// / 循环的情况下，任务分配指示之三：无<br/>
	// / 对于Tool类型和Subflow类型的task会重新执行一遍，和REDO效果一样的。<br/>
	// /
	// 对于Form类型的Task，重新执行一遍，且工作流引擎仍然调用Performer属性的AssignmentHandler分配任务(每次重新分配处理人)
	// / </summary>
	public static final Integer none = 0;
}
