package com.online.workflow.process.net;

import com.online.workflow.process.AbstractWFElement;

public class Edge extends AbstractWFElement{

	 private Node fromNode;

     /// <summary>
     /// 获取或设置转移(或者循环)的目标节点。
     /// 转移的终止目标可以是EndNode、 Activity或者Synchronizer。
     /// 循环的目标节点必须是Synchronizer或者StartNode。
     /// </summary>
	 private Node toNode;

	public Node getFromNode() {
		return fromNode;
	}

	public void setFromNode(Node fromNode) {
		this.fromNode = fromNode;
	}

	public Node getToNode() {
		return toNode;
	}

	public void setToNode(Node toNode) {
		this.toNode = toNode;
	}
     
     
     
}
