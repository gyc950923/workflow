package com.online.workflow.process;

import java.util.List;
import java.util.Map;

/**
 * 工作流元素的抽象接口，工作流元素主要包括:
 * 1)业务流程 WorkflowProcess，这是顶层元素
 * 2)任务(Task)
 * 3)开始节点(StartNode)、结束节点(EndNode)、同步器(Synchronizer)、环节(Activity)
 * 4)转移(Transition)和循环(Loop)
 * 5)流程数据项(DataField)
 * 
 * @author 贾朋亮,nychen2000@163.com
 * 
 */
public interface IWFElement {

	/**
	 * 返回工作流元素的Id 工作流元素的Id采用“父Id.自身Name”的方式组织。
	 * 
	 * @return 元素Id
	 */
	public String getId();

	public String getChartId();

	/**
	 * 返回工作流元素的名称
	 * 
	 * @return 元素名称
	 */
	public String getName();

	public void setName(String name);

	/**
	 * 返回流程元素的描述
	 * 
	 * @return 流程元素描述
	 */
	public String getDescription();

	public void setDescription(String description);

	public IWFElement getParentElement();

	public void setParentElement(IWFElement parentElement);

	/**
	 * 返回事件监听器列表
	 * 
	 * @return 事件监听器列表
	 */
	public List<EventListener> getEventListeners();

	/**
	 * 返回扩展属性Map
	 * 
	 * @return
	 */
	public Map<String, String> getExtendedAttributes();
}
