package com.online.workflow.process.rules;

public class StartRule {
	/// <summary>
    /// 任务标题
   /// </summary>
    private  String taskName ;
    /// <summary>
    /// 获取或设置本流程全局的任务实例创建器。
    /// 如果没有设置，引擎将使用DefaultTaskInstanceCreator来创建TaskInstance。
    /// </summary>
    private String taskInstanceCreator ;

    /// <summary>
    /// 获取或设置本流程全局的FormTask Instance运行器。
    /// 如果没有设置，引擎将使用DefaultFormTaskInstanceRunner来运行TaskInstance。
    /// </summary>
    private String formTaskInstanceRunner ;

    /// <summary>
    /// 获取或设置本流程全局的ToolTask Instance运行器。
    /// 如果没有设置，引擎将使用DefaultToolTaskInstanceRunner来运行TaskInstance。
    /// </summary>
    private String toolTaskInstanceRunner ;


    /// <summary>
    /// 获取或设置本流程全局的SubflowTask Instance运行器。
    /// 如果没有设置，引擎将使用DefaultSubflowTaskInstanceRunner来运行TaskInstance。
    /// </summary>
    private String subflowTaskInstanceRunner ;


    /// <summary>
    /// 获取或设置本流程全局的FormTask Instance 终结评价器，用于告诉引擎该实例是否可以结束。<br/>
    /// 如果没有设置，引擎使用缺省实现DefaultFormTaskInstanceCompletionEvaluator。
    /// </summary>
    private String formTaskInstanceCompletionEvaluator ;

    /// <summary>
    /// 获取或设置本流程全局的ToolTask Instance 终结评价器，用于告诉引擎该实例是否可以结束。<br/>
    /// 如果没有设置，引擎使用缺省实现DefaultToolTaskInstanceCompletionEvaluator。
    /// </summary>
    private String toolTaskInstanceCompletionEvaluator ;

    /// <summary>
    /// 获取或设置本流程全局的SubflowTask Instance 终结评价器，用于告诉引擎该实例是否可以结束。<br/>
    /// 如果没有设置，引擎使用缺省实现DefaultSubflowTaskInstanceCompletionEvaluator。
    /// </summary>
    private String subflowTaskInstanceCompletionEvaluator;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskInstanceCreator() {
        return taskInstanceCreator;
    }

    public void setTaskInstanceCreator(String taskInstanceCreator) {
        this.taskInstanceCreator = taskInstanceCreator;
    }

    public String getFormTaskInstanceRunner() {
        return formTaskInstanceRunner;
    }

    public void setFormTaskInstanceRunner(String formTaskInstanceRunner) {
        this.formTaskInstanceRunner = formTaskInstanceRunner;
    }

    public String getToolTaskInstanceRunner() {
        return toolTaskInstanceRunner;
    }

    public void setToolTaskInstanceRunner(String toolTaskInstanceRunner) {
        this.toolTaskInstanceRunner = toolTaskInstanceRunner;
    }

    public String getSubflowTaskInstanceRunner() {
        return subflowTaskInstanceRunner;
    }

    public void setSubflowTaskInstanceRunner(String subflowTaskInstanceRunner) {
        this.subflowTaskInstanceRunner = subflowTaskInstanceRunner;
    }

    public String getFormTaskInstanceCompletionEvaluator() {
        return formTaskInstanceCompletionEvaluator;
    }

    public void setFormTaskInstanceCompletionEvaluator(String formTaskInstanceCompletionEvaluator) {
        this.formTaskInstanceCompletionEvaluator = formTaskInstanceCompletionEvaluator;
    }

    public String getToolTaskInstanceCompletionEvaluator() {
        return toolTaskInstanceCompletionEvaluator;
    }

    public void setToolTaskInstanceCompletionEvaluator(String toolTaskInstanceCompletionEvaluator) {
        this.toolTaskInstanceCompletionEvaluator = toolTaskInstanceCompletionEvaluator;
    }

    public String getSubflowTaskInstanceCompletionEvaluator() {
        return subflowTaskInstanceCompletionEvaluator;
    }

    public void setSubflowTaskInstanceCompletionEvaluator(String subflowTaskInstanceCompletionEvaluator) {
        this.subflowTaskInstanceCompletionEvaluator = subflowTaskInstanceCompletionEvaluator;
    }
    
    
}
