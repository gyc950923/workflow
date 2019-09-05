package com.online.engine.pluginKernel.impl;

import java.util.ArrayList;
import java.util.List;

import com.online.engine.Instance.IToken;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.INodeInstance;
import com.online.engine.pluginKernel.ITransitionInstance;

public class NodeInstance implements INodeInstance {

	private List<ITransitionInstance> enteringTransitionInstances = new ArrayList<ITransitionInstance>();

	private List<ITransitionInstance> leavingTransitionInstances = new ArrayList<ITransitionInstance>();

	public List<ITransitionInstance> getEnteringTransitionInstances() {
		return enteringTransitionInstances;
	}

	public void setEnteringTransitionInstances(
			List<ITransitionInstance> enteringTransitionInstances) {
		this.enteringTransitionInstances = enteringTransitionInstances;
	}

	public List<ITransitionInstance> getLeavingTransitionInstances() {
		return leavingTransitionInstances;
	}

	public void setLeavingTransitionInstances(
			List<ITransitionInstance> leavingTransitionInstances) {
		this.leavingTransitionInstances = leavingTransitionInstances;
	}

	@Override
	public void addEnteringTransitionInstances(
			ITransitionInstance transitionInstance) {
		enteringTransitionInstances.add(transitionInstance);
	}

	@Override
	public void addLeavingTransitionInstances(
			ITransitionInstance transitionInstance) {
		leavingTransitionInstances.add(transitionInstance);
	}

	@Override
	public void fire(IToken token) throws EngineException {
		// 不做任何处理
	}

	@Override
	public String getId() {

		return null;
	}

}
