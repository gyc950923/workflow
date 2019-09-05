package com.online.workflow.process;

import java.util.ArrayList;
import java.util.List;

import com.online.workflow.process.forms.Form;
import com.online.workflow.process.net.Activity;
import com.online.workflow.process.net.EndNode;
import com.online.workflow.process.net.Node;
import com.online.workflow.process.net.StartNode;
import com.online.workflow.process.net.Synchronizer;
import com.online.workflow.process.net.Transition;
import com.online.workflow.process.parser.ProcessParserHelper;
import com.online.workflow.process.resources.DataField;
import com.online.workflow.process.resources.OrgRoleInfo;
import com.online.workflow.process.rules.StartRule;
import com.online.workflow.process.tasks.Task;

public class WorkflowProcess extends AbstractWFElement implements
		IWorkflowProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5001863574700172425L;

	// / <summary>获取或设置流程数据项，运行时转换为流程变量进行存储。</summary>
	private List<DataField> dataFields = new ArrayList<DataField>();

	// / <summary>获取或设置全局Task。</summary>
	private List<Task> tasks = new ArrayList<Task>();

	// / <summary>获取或设置流程环节</summary>
	private List<Activity> activities = new ArrayList<Activity>();

	// / <summary>获取或设置转移</summary>
	private List<Transition> transitions = new ArrayList<Transition>();

	// / <summary>获取或设置同步器</summary>
	private List<Synchronizer> synchronizers = new ArrayList<Synchronizer>();

	// / <summary>获取或设置开始节点</summary>
	private StartNode startNode = null;

	// / <summary>获取或设置结束节点</summary>
	private List<EndNode> endNodes = new ArrayList<EndNode>();

	// / <summary>启动规则</summary>
	private StartRule startRule = null;

	// / <summary>
	// / 业务主实体
	// / </summary>

	private String entityName;

	// / <summary>
	// / 业务主实体值
	// / </summary>
	private String entityValue;
	// / <summary>
	// / 流程表单
	// / </summary>
	private Form form;

	// / <summary>
	// / 待办描述
	// / </summary>
	private String toDoDescription;
	// / <summary>
	// / 流程状态
	// / </summary>
	private Boolean state = false;
	// / <summary>
	// / 流程描述
	// / </summary>
	private String processDescription;
	/*
	 * 部门角色中 指定部门 是否公用
	 */
	private OrgRoleInfo activitsPublicParam= new OrgRoleInfo();
	
	public WorkflowProcess() {

	}

	public List<DataField> getDataFields() {
		return dataFields;
	}

	public void setDataFields(List<DataField> dataFields) {
		this.dataFields = dataFields;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public List<Synchronizer> getSynchronizers() {
		return synchronizers;
	}

	public void setSynchronizers(List<Synchronizer> synchronizers) {
		this.synchronizers = synchronizers;
	}

	public StartNode getStartNode() {
		return startNode;
	}

	public void setStartNode(StartNode startNode) {
		this.startNode = startNode;
	}

	public List<EndNode> getEndNodes() {
		return endNodes;
	}

	public void setEndNodes(List<EndNode> endNodes) {
		this.endNodes = endNodes;
	}

	public StartRule getStartRule() {
		return startRule;
	}

	public void setStartRule(StartRule startRule) {
		this.startRule = startRule;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityValue() {
		return entityValue;
	}

	public void setEntityValue(String entityValue) {
		this.entityValue = entityValue;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public String getToDoDescription() {
		return toDoDescription;
	}

	public void setToDoDescription(String toDoDescription) {
		this.toDoDescription = toDoDescription;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public String getProcessDescription() {
		return processDescription;
	}

	public void setProcessDescription(String processDescription) {
		this.processDescription = processDescription;
	}

	// / <summary>
	// / 获取流程流转的入口活动
	// / </summary>
	// / <returns></returns>
	@Override
	public Activity getEntryActivity() {
		return null;
		// 此处注释放开会造成json转换的错误，暂时注释掉
		/*
		 * if (null == startNode) { return null; } Node node =
		 * startNode.getEntryNode();
		 * 
		 * Activity activity = (Activity)node ; return activity;
		 */
	}

	// / <summary>
	// / 获取流程的入口活动列表
	// / </summary>
	// / <returns></returns>
	@Override
	public List<Activity> getEntryActivitys() {
		List<Activity> entryActivity = new ArrayList<Activity>();

		/*
		 * List<Node> nodes = StartNode.LeavingTransitions.Select(p =>
		 * p.ToNode).ToList(); foreach (var item in nodes) { if (item is
		 * Activity) { Activity activity = item as Activity; var query =
		 * activity.InlineTasks.Where(p => p is FormTask); if (query != null) {
		 * entryActivity.Add(activity); } } }
		 */

		return entryActivity;
	}

	// / <summary>通过ID查找该流程中的任意元素</summary>
	// / <param name="id">元素的Id</param>
	// / <returns>流程元素，如：Activity,Task,Synchronizer等等</returns>
	public IWFElement findWFElementById(String id) {
		if (this.getId().equals(id)) {
			return this;
		}

		List<Task> tasksList = this.getTasks();
		for (int i = 0; i < tasksList.size(); i++) {
			Task task = (Task) tasksList.get(i);
			if (task.getId().equals(id)) {
				return task;
			}
		}

		List<Activity> activityList = this.getActivities();
		for (int i = 0; i < activityList.size(); i++) {
			Activity activity = activityList.get(i);
			if (activity.getId().equals(id)) {
				return activity;
			}
			List<Task> taskList = activity.getTasks();
			for (int j = 0; j < taskList.size(); j++) {
				Task task = taskList.get(j);
				if (id.equals(task.getId())) {
					return task;
				}
			}
		}
		if (this.getStartNode()!=null && id.equals(this.getStartNode().getId())) {
			return this.getStartNode();
		}
		List<Synchronizer> synchronizerList = this.getSynchronizers();
		for (int i = 0; i < synchronizerList.size(); i++) {
			Synchronizer synchronizer = synchronizerList.get(i);
			if (synchronizer.getId().equals(id)) {
				return synchronizer;
			}
		}

		List<EndNode> endNodeList = this.getEndNodes();
		for (int i = 0; i < endNodeList.size(); i++) {
			EndNode endNode = endNodeList.get(i);
			if (endNode.getId().equals(id)) {
				return endNode;
			}
		}

		List<Transition> transitionList = this.getTransitions();
		for (int i = 0; i < transitionList.size(); i++) {
			Transition transition = transitionList.get(i);
			if (transition.getId().equals(id)) {
				return transition;
			}
		}

		List<DataField> dataFieldList = this.getDataFields();
		for (int i = 0; i < dataFieldList.size(); i++) {
			DataField dataField = dataFieldList.get(i);
			if (dataField.getId().equals(id)) {
				return dataField;
			}
		}

		return null;
	}

	// / <summary>通过chartId查找该流程中的任意元素</summary>
	// / <param name="chartId">元素的chartId</param>
	// / <returns>流程元素，如：Activity,Task,Synchronizer等等</returns>
	@Override
	public IWFElement findWFElementByChartId(String chartId) {
		int num;
		Task task;
		if (this.getChartId().equals(chartId)) {
			return this;
		}
		List<Task> tasks = this.getTasks();
		for (num = 0; num < tasks.size(); num++) {
			task = tasks.get(num);
			if (task.getChartId().equals(chartId)) {
				return task;
			}
		}
		List<Activity> activities = this.getActivities();
		for (num = 0; num < activities.size(); num++) {
			Activity activity = activities.get(num);
			if (activity.getChartId().equals(chartId)) {
				return activity;
			}

		}
		if ((this.getStartNode() != null)
				&& this.getStartNode().getChartId().equals(chartId)) {
			return this.getStartNode();
		}
		List<Synchronizer> synchronizers = this.getSynchronizers();
		for (num = 0; num < synchronizers.size(); num++) {
			Synchronizer synchronizer = synchronizers.get(num);
			if (chartId.equals(synchronizer.getChartId())) {
				return synchronizer;
			}
		}
		List<EndNode> endNodes = this.getEndNodes();
		for (num = 0; num < endNodes.size(); num++) {
			EndNode node = endNodes.get(num);
			if (node.getChartId().equals(chartId)) {
				return node;
			}
		}
		List<Transition> transitions = this.getTransitions();
		for (num = 0; num < transitions.size(); num++) {
			Transition transition = transitions.get(num);
			if (transition.getChartId().equals(chartId)) {
				return transition;
			}
		}
		List<DataField> dataFields = this.getDataFields();
		for (num = 0; num < dataFields.size(); num++) {
			DataField field = dataFields.get(num);
			if (field.getChartId().equals(chartId)) {
				return field;
			}
		}
		return null;
	}

	// / <summary>验证workflow process是否完整正确。</summary>
	// / <returns>null表示流程正确；否则表示流程错误，返回值是错误原因</returns>
	public String validate() {
		/*
		 * String errHead = "Workflow process is invalid："; if
		 * (this.getStartNode() == null) { return errHead +
		 * "must have one start node"; } if
		 * (this.getStartNode().getLeavingTransitions().size() == 0) { return
		 * errHead + "start node must have leaving transitions."; }
		 * 
		 * List<Activity> activities = this.getActivities(); for (int i = 0; i <
		 * activities.size(); i++) { Activity activity = activities.get(i);
		 * 
		 * String theName = (StringUtils.isNotBlank(activity.getName()) ?
		 * activity.getId() : activity.getName()); if
		 * (activity.getEnteringTransitions().size() <= 0) { return errHead +
		 * "activity[" + theName + "] must have entering transition."; } if
		 * (activity.getLeavingTransitions().size() <= 0) { return errHead +
		 * "activity[" + theName + "] must have leaving transition."; }
		 * 
		 * //check tasks List<Task> taskList = activity.getTasks(); for (int j =
		 * 0; j < taskList.size(); j++) { Task task = (Task)taskList.get(j); if
		 * (task.getTaskType() == TaskTypeEnum.form) { FormTask formTask =
		 * (FormTask)task; //if (!(activity.EnteringTransition.FromNode is
		 * StartNode) && formTask.Performer == null) //{ // return errHead +
		 * "FORM-task[id=" + task.Id + "] must has a performer."; //} } else if
		 * (task.getTaskType() == TaskTypeEnum.tool) { ToolTask toolTask =
		 * (ToolTask)task; if (toolTask.getService()== null) { return errHead +
		 * "TOOL-task[id=" + task.getId() + "] must has a application."; } }
		 * else if (task.getTaskType() == TaskTypeEnum.subflow) { SubflowTask
		 * subflowTask = (SubflowTask)task; if
		 * (subflowTask.getSubWorkflowProcess() == null) { return errHead +
		 * "SUBFLOW-task[id=" + task.getId() + "] must has a subflow."; } } else
		 * { return errHead + " unknown task type of task[" + task.getId() +
		 * "]"; } } }
		 * 
		 * List<Synchronizer> synchronizers = this.getSynchronizers(); for (int
		 * i = 0; i < synchronizers.size(); i++) { Synchronizer synchronizer =
		 * synchronizers.get(i); String theName = (synchronizer.DisplayName ==
		 * null || synchronizer.DisplayName.Equals("")) ? synchronizer.Code :
		 * synchronizer.DisplayName); if (synchronizer.EnteringTransitions.Count
		 * == 0) { return errHead + "synchronizer[" + theName +
		 * "] must have entering transition."; } if
		 * (synchronizer.LeavingTransitions.Count == 0) { return errHead +
		 * "synchronizer[" + theName + "] must have leaving transition."; } }
		 * 
		 * List<EndNode> endnodes = this.EndNodes; for (int i = 0; i <
		 * endnodes.Count; i++) { EndNode endnode = endnodes[i]; String theName
		 * = (endnode.DisplayName == null || endnode.DisplayName.Equals("")) ?
		 * endnode.Code : endnode.DisplayName; if
		 * (endnode.EnteringTransitions.Count == 0) { return errHead +
		 * "end node[" + theName + "] must have entering transition."; } }
		 * 
		 * List<Transition> transitions = this.Transitions; for (int i = 0; i <
		 * transitions.Count; i++) { Transition transition = transitions[i];
		 * String theName = (transition.DisplayName == null ||
		 * transition.DisplayName.Equals("")) ? transition.Code :
		 * transition.DisplayName; if (transition.FromNode == null) { return
		 * errHead + "transition[" + theName + "] must have from node.";
		 * 
		 * } if (transition.ToNode == null) { return errHead + "transition[" +
		 * theName + "] must have to node."; } }
		 */

		return null;
	}

	// / <summary>
	// / 判断是否可以从from节点到达to节点
	// / </summary>
	// / <param name="fromNodeId">from节点的id</param>
	// / <param name="toNodeId">to节点的id</param>
	public Boolean isReachable(String fromNodeId, String toNodeId) {
		if (fromNodeId == null || toNodeId == null) {
			return false;
		}
		if (fromNodeId.equals(toNodeId)) {
			return true;
		}
		List<Node> reachableList = this.getReachableNodes(fromNodeId);

		for (int j = 0; reachableList != null && j < reachableList.size(); j++) {
			Node node = reachableList.get(j);
			if (node.getId().equals(toNodeId)) {
				return true;
			}
		}

		return false;
	}

	// / <summary>获取可以到达的节点</summary>
	// / <param name="nodeId"></param>
	// / <returns></returns>
	public List<Node> getReachableNodes(String nodeId) {
		List<Node> reachableNodesList = new ArrayList<Node>();
		/*
		 * Node node = (Node)this.findWFElementById(nodeId); List<Transition>
		 * leavingTransition = node.getLeavingTransitions(); if
		 * (leavingTransition != null) {
		 * 
		 * for (Transition item : leavingTransition) { Node toNode =
		 * item.getToNode(); if (toNode != null) { reachableNodesList. //
		 * reachableNodesList.AddRange(GetReachableNodes(toNode.Id)); } } }
		 * 
		 * 
		 * 
		 * //剔除重复节点 List<Node> tmp = new List<Node>(); Boolean alreadyInTheList
		 * = false; for (int i = 0; i < reachableNodesList.Count; i++) { Node
		 * nodeTmp = (Node)reachableNodesList[i]; alreadyInTheList = false; for
		 * (int j = 0; j < tmp.Count; j++) { Node nodeTmp2 = (Node)tmp[j]; if
		 * (nodeTmp2.Id.Equals(nodeTmp.Id)) { alreadyInTheList = true; break; }
		 * } if (!alreadyInTheList) { tmp.Add(nodeTmp); } } reachableNodesList =
		 * tmp;
		 */
		return reachableNodesList;
	}

	// / <summary>获取进入的节点(activity 或者synchronizer)</summary>
	// / <param name="nodeId"></param>
	// / <returns></returns>
	public List<Node> getEnterableNodes(String nodeId) {
		List<Node> enterableNodesList = new ArrayList<Node>();
		/*
		 * Node node = (Node)this.FindWFElementById(nodeId); var
		 * enteringTransition = node.EnteringTransitions; if (enteringTransition
		 * != null) { foreach (var item in enteringTransition) { Node fromNode =
		 * item.FromNode; if (fromNode != null) {
		 * enterableNodesList.Add(fromNode);
		 * enterableNodesList.AddRange(GetEnterableNodes(fromNode.Id)); } }
		 * 
		 * }
		 * 
		 * List<Node> tmp = new List<Node>(); Boolean alreadyInTheList = false;
		 * for (int i = 0; i < enterableNodesList.Count; i++) { Node nodeTmp =
		 * (Node)enterableNodesList[i]; alreadyInTheList = false; for (int j =
		 * 0; j < tmp.Count; j++) { Node nodeTmp2 = (Node)tmp[j]; if
		 * (nodeTmp2.Id.Equals(nodeTmp.Id)) { alreadyInTheList = true; break; }
		 * } if (!alreadyInTheList) { tmp.Add(nodeTmp); } } enterableNodesList =
		 * tmp;
		 */
		return enterableNodesList;
	}

	public void delTransitionAfter(Transition transition) {
		transition.getFromNode().getLeavingTransitions().remove(transition);
		transition.getToNode().getEnteringTransitions().remove(transition);

	}

	public WorkflowProcess cloneWorkflowProcess() {
		String strxmString = ProcessParserHelper.ProcessToXML(this);
		WorkflowProcess workflowProcess = ProcessParserHelper.XMLToProcess(strxmString);
		StartNode startNode = workflowProcess.getStartNode();
		List<Transition> transitions = workflowProcess.getTransitions();
		List<Activity> activities = workflowProcess.getActivities();
		List<Synchronizer> synchronizers = workflowProcess.getSynchronizers();
		List<EndNode> endNodes = workflowProcess.getEndNodes();
		List<Task> tasks = workflowProcess.getTasks();

		workflowProcess.setParentElement(null);
		if(startNode != null){
		    startNode.setParentElement(null);
		}

		for (int num = 0; num < transitions.size(); num++) {
			Transition transition = transitions.get(num);
			transition.setFromNode(null);
			transition.setToNode(null);
			transition.setParentElement(null);
		}
		for (Activity activity : activities) {
			activity.setParentElement(null);
		}
		for (Synchronizer synchronizer : synchronizers) {
			synchronizer.setParentElement(null);
		}
		for (EndNode endNode : endNodes) {
			endNode.setParentElement(null);
		}

		for (Task task : tasks) {
			task.setParentElement(null);
		}

		return workflowProcess;

	}

	public OrgRoleInfo getActivitsPublicParam() {
		return activitsPublicParam;
	}

	public void setActivitsPublicParam(OrgRoleInfo activitsPublicParam) {
		this.activitsPublicParam = activitsPublicParam;
	}
}
