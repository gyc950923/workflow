package com.online.workflow.process.net;

import java.util.ArrayList;
import java.util.List;

import com.online.workflow.process.AbstractWFElement;



public  class Node extends AbstractWFElement{

    //运营状态-value
	private String entityDocStatus ;
	//运营状态-key
	private String entityDocStatusName ;
	
    private List<Transition> enteringTransitions = new ArrayList<Transition>();
    
    private  List<Transition> leavingTransitions = new ArrayList<Transition>();
   
    public List<Transition> getEnteringTransitions() {
		return enteringTransitions;
	}

	public void setEnteringTransitions(List<Transition> enteringTransitions) {
		this.enteringTransitions = enteringTransitions;
	}

	public List<Transition> getLeavingTransitions() {
		return leavingTransitions;
	}

	public void setLeavingTransitions(List<Transition> leavingTransitions) {
		this.leavingTransitions = leavingTransitions;
	}
    
	public String getEntityDocStatus() {
		return entityDocStatus;
	}

	public void setEntityDocStatus(String entityDocStatus) {
		this.entityDocStatus = entityDocStatus;
	}

	public String getEntityDocStatusName() {
		return entityDocStatusName;
	}

	public void setEntityDocStatusName(String entityDocStatusName) {
		this.entityDocStatusName = entityDocStatusName;
	}

	

	
	
}
