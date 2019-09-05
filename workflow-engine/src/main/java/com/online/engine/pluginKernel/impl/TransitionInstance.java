package com.online.engine.pluginKernel.impl;

import com.online.engine.Instance.IToken;
import com.online.engine.enums.EdgeInstanceEventEnum;
import com.online.engine.events.EdgeInstanceEvent;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.INodeInstance;
import com.online.engine.pluginKernel.ITransitionInstance;
import com.online.workflow.process.net.Transition;

public class TransitionInstance extends EdgeInstance implements
		ITransitionInstance {

	private Transition transition;

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	public TransitionInstance(Transition transition) {
		setTransition(transition);
	}

	@Override
	public Boolean take(IToken token) throws EngineException {

		EdgeInstanceEvent e = new EdgeInstanceEvent(this);
		e.setToken(token);
		e.setEventType(EdgeInstanceEventEnum.on_taking_token);
		e.fireEvent();

		INodeInstance nodeInst = this.getLeavingNodeInstance();// 获取到流向哪个节点
		Boolean alive = token.getIsAlive();

		if (alive)
			nodeInst.fire(token);// 节点触发

		return true;

	}

}
