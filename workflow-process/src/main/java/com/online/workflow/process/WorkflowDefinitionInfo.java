package com.online.workflow.process;

import java.util.Date;

public class WorkflowDefinitionInfo {
	// / <summary>获取或设置主键</summary>
	private String id;
	// / <summary>获取或设置流程id</summary>
	private String processId;
	// / <summary>获取或设置流程类型id</summary>
	private String typeId;
	// / <summary>获取或设置流程英文名称</summary>
	private String name;
	// / <summary>获取或设置版本号</summary>
	private Integer version;
	// / <summary>获取或设置流程业务说明</summary>
	private String description;
	// / <summary>获取或设置是启用禁用，1=启用,0禁用</summary>
	private Boolean state;
	// / <summary>获取或设置上载到数据库的操作员</summary>
	private String uploadUser;
	// / <summary>获取或设置上载到数据库的时间</summary>
	private Date uploadTime;
	// / <summary>获取或设置发布人</summary>
	private String publishUser;
	// / <summary>获取或设置发布时间</summary>
	private Date publishTime;
	// / <summary>获取或设置流程定义文件的内容。</summary>
	private String processContent;//
	private String orgId;//
	// 工作流图形xml
	private String processChartContent;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	public String getUploadUser() {
		return uploadUser;
	}
	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getPublishUser() {
		return publishUser;
	}
	public void setPublishUser(String publishUser) {
		this.publishUser = publishUser;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public String getProcessContent() {
		return processContent;
	}
	public void setProcessContent(String processContent) {
		this.processContent = processContent;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getProcessChartContent() {
		return processChartContent;
	}
	public void setProcessChartContent(String processChartContent) {
		this.processChartContent = processChartContent;
	}
	
	
	
	
}
