package com.online.engine.resource;

import com.online.workflow.process.resources.Performer;

public class PromptControlItem {

	private String activityId;
	private String activityName;
	private Performer performer;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Performer getPerformer() {
		return performer;
	}

	public void setPerformer(Performer performer) {
		this.performer = performer;
	}

}
