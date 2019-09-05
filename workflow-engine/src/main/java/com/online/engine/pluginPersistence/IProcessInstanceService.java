package com.online.engine.pluginPersistence;

import com.online.engine.Instance.IProcessInstance;

public interface IProcessInstanceService {
	IProcessInstance getProcessInstanceById(String processInstanceId);
	Boolean saveOrUpdateProcessInstance(IProcessInstance processInstance);
}
