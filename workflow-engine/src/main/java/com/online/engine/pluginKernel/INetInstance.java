package com.online.engine.pluginKernel;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.exception.EngineException;

public interface INetInstance {
	void run(IProcessInstance processInstance) throws EngineException;

	Object getWFElementInstance(String wfElementId);
}
