package com.online.engine.events;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.enums.ProcessInstanceEventEnum;

public class ProcessInstanceEvent implements IProcessInstanceEvent {

	private Integer eventType;
	private IProcessInstance processInstance;

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public IProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(IProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
 
	public void fireEvent() {
		if (getEventType() == ProcessInstanceEventEnum.before_process_instance_run.intValue()) {
			
			

		} else if (getEventType() == ProcessInstanceEventEnum.after_process_instance_complete.intValue()) {

		}
	}
}
