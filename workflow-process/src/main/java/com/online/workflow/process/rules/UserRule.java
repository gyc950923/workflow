package com.online.workflow.process.rules;

import com.online.workflow.process.enums.ActorAssignTypeEnum;
import com.online.workflow.process.enums.AssignmentStrategyEnum;
import com.online.workflow.process.enums.BatchApprovalEnum;
import com.online.workflow.process.enums.DefaultViewEnum;
import com.online.workflow.process.enums.LoopStrategyEnum;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.resources.Performer;

public class UserRule extends Rule {

	// / <summary>
	// / <para>返回环节的结束策略，取值为ALL或者ANY，缺省值为ALL</para>
	// / <para>如果取值为ALL,则只有其所有任务实例结束了，环节实例才可以结束。</para>
	// / <para>如果取值为ANY，则只要任何一个任务实例结束后，环节实例就可以结束。</para>
	// / 环节实例的结束操作仅执行一遍，因此后续任务实例的结束不会触发环节实例的结束操作再次执行。
	// / </summary>
	private Integer formTaskEnum=AssignmentStrategyEnum.all;
	
	
	// / <summary>获取或设置循环情况下任务执行策略，取值为REDO、SKIP和NONE,</summary>
	private Integer loopStrategy=LoopStrategyEnum.none;
	
	//是否支持批量审批
	private Boolean batchApproval = BatchApprovalEnum.forbid; //默认不支持批量审批
	/**.
	 * 获取或设置任务的缺省表单的类型，取值为EDITFORM、VIEWFORM或者LISTFORM。</para>
	 * 只有FORM类型的任务此方法才有意义。该方法的主要作用是方便系统开发，引擎不会用到该方法。
	 */
	private Integer defaultViewEnum=DefaultViewEnum.editform;// 缺省视图是editForm

	private Performer performer;
	/**
	 * 可编辑表单
	 */
	private Form editForm;
	/**
	 * 只读表单
	 */
	private Form viewForm;

	/**
	 * 列表表单
	 */
	private Form listForm;

	private Integer actorAssignType = ActorAssignTypeEnum.appoint;
    private String actorIdKey;
    private String actorNameKey;
    
	public Integer getFormTaskEnum() {
		return formTaskEnum;
	}

	public void setFormTaskEnum(Integer formTaskEnum) {
		this.formTaskEnum = formTaskEnum;
	}

	public Integer getLoopStrategy() {
		return loopStrategy;
	}

	public void setLoopStrategy(Integer loopStrategy) {
		this.loopStrategy = loopStrategy;
	}

	public Integer getDefaultViewEnum() {
		return defaultViewEnum;
	}

	public void setDefaultViewEnum(Integer defaultViewEnum) {
		this.defaultViewEnum = defaultViewEnum;
	}

	public Performer getPerformer() {
		return performer;
	}

	public void setPerformer(Performer performer) {
		this.performer = performer;
	}

	public Form getEditForm() {
		return editForm;
	}

	public void setEditForm(Form editForm) {
		this.editForm = editForm;
	}

	public Form getViewForm() {
		return viewForm;
	}

	public void setViewForm(Form viewForm) {
		this.viewForm = viewForm;
	}

	public Form getListForm() {
		return listForm;
	}

	public void setListForm(Form listForm) {
		this.listForm = listForm;
	}

    public Integer getActorAssignType() {
        return actorAssignType;
    }

    public void setActorAssignType(Integer actorAssignType) {
        this.actorAssignType = actorAssignType;
    }

    public String getActorIdKey() {
        return actorIdKey;
    }

    public void setActorIdKey(String actorIdKey) {
        this.actorIdKey = actorIdKey;
    }

    public String getActorNameKey() {
        return actorNameKey;
    }

    public void setActorNameKey(String actorNameKey) {
        this.actorNameKey = actorNameKey;
    }

	public Boolean getBatchApproval() {
		return batchApproval;
	}

	public void setBatchApproval(Boolean batchApproval) {
		this.batchApproval = batchApproval;
	}

}
