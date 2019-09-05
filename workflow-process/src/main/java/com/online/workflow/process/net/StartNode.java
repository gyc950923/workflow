package com.online.workflow.process.net;

public class StartNode extends Synchronizer {

	private String name = "START_NODE";

	private Node entryNode;

	public Node getEntryNode() {
		return entryNode;
	}

	public void setEntryNode(Node entryNode) {
		this.entryNode = entryNode;
	}

	public String getName() {
		return name;
	}

    public void setName(String name) {
        this.name = name;
    }
	
}
