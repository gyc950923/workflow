package com.online.engine.pluginPersistence;

import com.online.engine.Instance.IProcessInstanceHistTrace;

public interface IProcessInstanceTraceService {
	 void saveOrUpdateProcessInstanceTrace(IProcessInstanceHistTrace trace);
}
