package com.online.workflow.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jpl
 *
 */
public abstract class AbstractWFElement implements IWFElement, Serializable {

	private static final Long serialVersionUID = -320956235993400548L;
	private IWFElement parentElement;
	/**
	 * id
	 */
	private String id;

	/**
	 * 流程图形id
	 */
	private String chartId;

	/**
	 * 名称
	 */
	private String name;

	public IWFElement getParentElement() {
		return parentElement;
	}

	public void setParentElement(IWFElement parentElement) {
		this.parentElement = parentElement;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 事件监听器
	 */
	private List<EventListener> eventListeners = new ArrayList<EventListener>();

	/**
	 * 扩展属性
	 */
	private Map<String, String> extendedAttributes;
	
	private Integer callBackStatus;
	private String restfulAddress;

	/**
	 * 构造方法
	 */
	public AbstractWFElement() {

	}

	public String getId() {

		return id;
	}

	public String getChartId() {
		return chartId;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getDescription() {

		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;

	}

	public List<EventListener> getEventListeners() {

		return this.eventListeners;
	}

	public Map<String, String> getExtendedAttributes() {
		if (extendedAttributes == null) {
			extendedAttributes = new HashMap<String, String>();
		}
		return extendedAttributes;
	}

    public void setId(String id) {
        this.id = id;
    }


	public String getRestfulAddress() {
		return restfulAddress;
	}

	public void setRestfulAddress(String restfulAddress) {
		this.restfulAddress = restfulAddress;
	}

	public Integer getCallBackStatus() {
		return callBackStatus;
	}

	public void setCallBackStatus(Integer callBackStatus) {
		this.callBackStatus = callBackStatus;
	}
	
}
