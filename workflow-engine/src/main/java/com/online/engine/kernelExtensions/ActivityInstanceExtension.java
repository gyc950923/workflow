package com.online.engine.kernelExtensions;

import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.enums.NodeInstanceEventEnum;
import com.online.engine.events.INodeInstanceEvent;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.IActivityInstance;
import com.online.engine.pluginPersistence.IPersistenceService;

public class ActivityInstanceExtension implements INodeInstanceExtension,
		IRuntimeContextAware {

	private RuntimeContext ctx;

	@Override
	public void setRuntimeContext(RuntimeContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public RuntimeContext getRuntimeContext() {
		return ctx;
	}

	@Override
	public void onNodeInstanceEventFired(INodeInstanceEvent e) throws EngineException {
		if (e.getEventType() == NodeInstanceEventEnum.nodeinstance_token_entered.intValue()) {

		} else if (e.getEventType() == NodeInstanceEventEnum.nodeinstance_fired.intValue()) {
			IPersistenceService persistenceService = this.getRuntimeContext()
					.getPersistenceService();
			persistenceService.saveOrUpdateToken(e.getToken());
			this.getRuntimeContext()
					.getTaskInstanceManager()
					.createTaskInstances(e.getToken(),
							(IActivityInstance) e.getSource());
		}else if (e.getEventType() == NodeInstanceEventEnum.nodeinstance_leaving.intValue()) {
            IPersistenceService persistenceService = this.getRuntimeContext()
                    .getPersistenceService();
            IToken token = e.getToken();
            persistenceService.deleteTokenById(token.getId());
        }

	}

}
