package com.online.engine.pluginPersistence;

import java.util.List;

import com.online.engine.Instance.impl.ProcessInstanceVar;

public interface IProcessInstanceVarService {
	 boolean saveProcessInstanceVariable(ProcessInstanceVar var);
	 boolean updateProcessInstanceVariable(ProcessInstanceVar var);

     List<ProcessInstanceVar> getProcessInstanceVariable(String processInstanceId);
     ProcessInstanceVar getProcessInstanceVariable(String processInstanceId, String name);
}
