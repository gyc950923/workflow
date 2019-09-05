package com.online.engine.enums;


public class ProcessInstanceStateEnum {

	// / <summary>运行状态</summary>
	public static final Integer running = 10;
	// / <summary>已经结束</summary>
	public static final Integer completed = 100;
	// / <summary>被撤销</summary>
	public static final Integer canceled = 20;
	
	/*以下为新增状态*/
	// / <summary>暂停</summary>
	public static final Integer suspend = 40;
	// / <summary>作废</summary>
	public static final Integer scrap = 50;
	// / <summary>终止</summary>
	public static final Integer termination = 60;
	// / <summary>待阅</summary>
	public static final Integer toberead = 70;
}
