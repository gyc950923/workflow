package com.online.engine.kernelExtensions;


import com.online.engine.Instance.IProcessInstanceHistTrace;
import com.online.engine.Instance.IRuntimeContextAware;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.ProcessInstanceHistTrace;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.Instance.impl.Token;
import com.online.engine.enums.EdgeInstanceEventEnum;
import com.online.engine.enums.ProcessInstanceTraceEnum;
import com.online.engine.events.IEdgeInstanceEvent;
import com.online.engine.pluginKernel.ITransitionInstance;
import com.online.workflow.process.IWFElement;
import com.online.workflow.process.net.Transition;

public class TransitionInstanceExtension implements IEdgeInstanceExtension,
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
	public void onNodeInstanceEventFired(IEdgeInstanceEvent e) {
		
		if (e.getEventType() == EdgeInstanceEventEnum.on_taking_token.intValue()) {
			 IToken token = e.getToken();
			ITransitionInstance transInst = (ITransitionInstance) e.getSource();

			if (this.getRuntimeContext().isEnableTrace() && token.getIsAlive()) {
				Transition transition = transInst.getTransition();
				IWFElement fromNode = transition.getFromNode();

				 IProcessInstanceHistTrace trace = new ProcessInstanceHistTrace();
                 trace.setProcessInstanceId(e.getToken().getProcessInstanceId());
                 trace.setStepNumber(e.getToken().getStepNumber());
                 trace.setType(ProcessInstanceTraceEnum.transition_type); 
                 trace.setFromNodeId(fromNode.getId());
                 trace.setToNodeId(transition.getToNode().getId());
                 trace.setEdgeId(transition.getId());

                 ctx.getPersistenceService().saveOrUpdateProcessInstanceTrace(trace);
			}

		}

	}
}
