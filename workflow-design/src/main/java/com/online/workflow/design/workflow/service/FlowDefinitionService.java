package com.online.workflow.design.workflow.service;

import com.online.workflow.design.workflow.bean.FlowDefinitionBean;
import com.online.workflow.design.workflow.dao.FlowDefinitionDao;

/**
 * 流程定义相关操作模拟service
 * @author Administrator
 *
 */
public class FlowDefinitionService {

	private FlowDefinitionDao dao = new FlowDefinitionDao();
	/**
	 * 模拟保存,先以文件形式存储
	 * @param bean
	 */
	public void save(FlowDefinitionBean bean){
		
		this.dao.save(bean); 
	}
	
	public FlowDefinitionBean findFlowDefinitionBeanById(String id){
		
		FlowDefinitionBean bean = this.dao.findFlowDefinitionBeanById(id);
		
		return bean;
	}
	
	public static void main(String[] args) {
		
		FlowDefinitionBean bean = new FlowDefinitionBean();
		bean.setModelId("1111");
		bean.setName("测试重构");
		bean.setDescription("描述字段啊");
		bean.setModel("ssssswwww");
		
		new FlowDefinitionService().save(bean);
	}
}
