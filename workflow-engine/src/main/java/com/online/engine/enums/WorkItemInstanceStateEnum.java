package com.online.engine.enums;

public class WorkItemInstanceStateEnum {

	public static final Integer initialized = 10;
	public static final Integer running = 20;
	public static final Integer canceled = 30;
	public static final Integer completed = 100;

	/*以下为新增状态*/
    // / <summary>暂停</summary>
    public static final Integer suspend = 40;
    // / <summary>作废</summary>
    public static final Integer scrap = 50;
    // / <summary>终止</summary>
    public static final Integer termination = 60;
}
