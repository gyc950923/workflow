package com.online.engine.Instance;

import com.online.engine.Instance.impl.RuntimeContext;

public interface IWorkflowSessionCallback {
	Object DoInWorkflowSession(RuntimeContext runtimeContext);
}
