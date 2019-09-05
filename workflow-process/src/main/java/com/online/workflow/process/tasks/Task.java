package com.online.workflow.process.tasks;

import com.online.workflow.process.AbstractWFElement;
import com.online.workflow.process.enums.LoopStrategyEnum;
import com.online.workflow.process.enums.TaskTypeEnum;
import com.online.workflow.process.resources.Duration;

public class Task extends AbstractWFElement {

	// / <summary>获取或设置任务类型,取值为FORM,TOOL,SUBFLOW,DUMMY(保留)，缺省值为FORM</summary>
	private Integer taskType = TaskTypeEnum.form;

	/**
	 * 获取或设置任务的完成期限
	 */
	private Duration duration;

	// / <summary>获取或设置任务优先级别(1.0暂时没有用到)</summary>
	private int priority;

	// / <summary>获取或设置循环情况下任务执行策略，取值为REDO、SKIP和NONE,</summary>
	//private LoopStrategyEnum loopStrategy;
	private Integer loopStrategy = LoopStrategyEnum.none;

	// / <summary>获取或设置任务实例创建器。如果没有设置，则使用所在流程的全局任务实例创建器。</summary>
	private String taskInstanceCreator;

	// / <summary>获取或设置任务实例运行器，如果没有设置，则使用所在流程的全局的任务实例运行器</summary>
	private String taskInstanceRunner;

	// /
	// <summary>获取或设置任务实例的终结评价器，用于告诉引擎，该实例是否可以结束。如果没有设置，则使用所在流程的全局的任务实例终结评价器。</summary>
	private String taskInstanceCompletionEvaluator;

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	/*public LoopStrategyEnum getLoopStrategy() {
		return loopStrategy;
	}

	public void setLoopStrategy(LoopStrategyEnum loopStrategy) {
		this.loopStrategy = loopStrategy;
	}*/

	public String getTaskInstanceCreator() {
		return taskInstanceCreator;
	}

	public Integer getLoopStrategy() {
        return loopStrategy;
    }

    public void setLoopStrategy(Integer loopStrategy) {
        this.loopStrategy = loopStrategy;
    }

    public void setTaskInstanceCreator(String taskInstanceCreator) {
		this.taskInstanceCreator = taskInstanceCreator;
	}

	public String getTaskInstanceRunner() {
		return taskInstanceRunner;
	}

	public void setTaskInstanceRunner(String taskInstanceRunner) {
		this.taskInstanceRunner = taskInstanceRunner;
	}

	public String getTaskInstanceCompletionEvaluator() {
		return taskInstanceCompletionEvaluator;
	}

	public void setTaskInstanceCompletionEvaluator(
			String taskInstanceCompletionEvaluator) {
		this.taskInstanceCompletionEvaluator = taskInstanceCompletionEvaluator;
	}
	
	
	
}
