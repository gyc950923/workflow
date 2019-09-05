package com.online.engine.kernelExtensions;

import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.ProcessInstance;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.enums.NodeInstanceEventEnum;
import com.online.engine.events.INodeInstanceEvent;
import com.online.engine.exception.EngineException;
import com.online.engine.pluginKernel.IEndNodeInstance;
import com.online.engine.pluginKernel.impl.EndNodeInstance;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.workflow.process.net.EndNode;

public class EndNodeInstanceExtension implements INodeInstanceExtension,
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
		if (e.getEventType() == NodeInstanceEventEnum.nodeinstance_leaving.intValue()) {
			IToken tk = e.getToken();
			IPersistenceService persistenceService = this.ctx
					.getPersistenceService();
			// 删除同步器节点的token
			IEndNodeInstance endNodeInst = (IEndNodeInstance) e.getSource();
			persistenceService.deleteTokensForNode(e.getToken()
					.getProcessInstanceId(), tk.getNodeId());

		} else if (e.getEventType() == NodeInstanceEventEnum.nodeinstance_completed.intValue()) {
			IToken tk = e.getToken();
			ProcessInstance currentProcessInstance = (ProcessInstance) tk
					.getProcessInstance();
			IEndNodeInstance endInst = (IEndNodeInstance) e.getSource();
			
			if(null != endInst.getEndNode().getCallBackStatus() && endInst.getEndNode().getCallBackStatus() == NodeInstanceEventEnum.nodeinstance_fired.intValue()){
				
				EndNodeInstance endNodeInstance = (EndNodeInstance) e.getSource();
				IPersistenceService persistenceService = this.ctx.getPersistenceService();
				endNodeInstance.getEndNode().setProcessInstanceId(tk.getProcessInstanceId());
				persistenceService.updateWfProcessInstance(endNodeInstance);
			}
			
			currentProcessInstance.Complete(e);
		}

	}

}
