package com.online.engine.kernelExtensions;

import com.online.engine.Instance.IConditionForkIntance;
import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.enums.NodeInstanceEventEnum;
import com.online.engine.events.INodeInstanceEvent;
import com.online.engine.pluginConditionResolver.IConditionResolver;
import com.online.engine.pluginPersistence.IPersistenceService;

public class ConditionForkInstanceExtension implements INodeInstanceExtension,
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
	public void onNodeInstanceEventFired(INodeInstanceEvent e) {

		// 判断条件满足的线
		if (e.getEventType() == NodeInstanceEventEnum.nodeinstance_token_entered.intValue()) {

		} else if (e.getEventType() == NodeInstanceEventEnum.nodeinstance_fired.intValue()) {
			IPersistenceService persistenceService = this.ctx
					.getPersistenceService();
			persistenceService.saveOrUpdateToken(e.getToken());
		}

	}

}
