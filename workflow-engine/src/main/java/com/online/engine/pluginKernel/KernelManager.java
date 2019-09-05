package com.online.engine.pluginKernel;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.pluginConditionResolver.IConditionResolver;
import com.online.engine.pluginKernel.impl.NetInstance;
import com.online.workflow.process.WorkflowProcess;
public class KernelManager implements IKernelManager {

	// TODO 以后放到Redis
	private HashMap<String, INetInstance> netInstanceMap = new HashMap<String, INetInstance>();
	protected RuntimeContext rtCtx = null;

	@Override
	public void setRuntimeContext(RuntimeContext ctx) {
		rtCtx = ctx;
	}

	@Override
	public RuntimeContext getRuntimeContext() {
		return rtCtx;
	}

	public INetInstance getNetInstance(String processId, int version) {
		String mapKey = processId + "_V_" + version;
		INetInstance netInstance = netInstanceMap.containsKey(mapKey) ? netInstanceMap
				.get(mapKey) : null;
		if (netInstance == null) {
			WorkflowProcess process = getRuntimeContext().getWorkflowProcess(
					processId, version);
			netInstance = this.createNetInstance(process, mapKey,this.rtCtx.getConditionResolver());
		}
		return netInstance;
	}

	@Override
	public void clearNetInstanceMap() {
		netInstanceMap.clear();

	}

	protected INetInstance createNetInstance(WorkflowProcess process,
			String mapKey, IConditionResolver conditionResolver) {
		if (process == null) {
			// TODO 异常
		}
		INetInstance netInstance = new NetInstance(process, conditionResolver);
		// netInstanceMap.Add(mapKey, netInstance);

		return netInstance;

	}

}
