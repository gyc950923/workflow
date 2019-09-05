package com.online.workflow.process.rules;

import com.online.workflow.process.enums.BackModeEnum;
import com.online.workflow.process.resources.BackNode;

public class BackRule extends Rule {

	// / <summary>
	// / 回退时是否弹出确认对话框
	// / </summary>
	private boolean isBackConfirm;

	// / <summary>
	// / 返回方式
	// / </summary>
	private Integer returnMode=BackModeEnum.redo;

	private BackNode backRange;

	public boolean isBackConfirm() {
		return isBackConfirm;
	}

	public void setBackConfirm(boolean isBackConfirm) {
		this.isBackConfirm = isBackConfirm;
	}

	public Integer getReturnMode() {
		return returnMode;
	}

	public void setReturnMode(Integer returnMode) {
		this.returnMode = returnMode;
	}

	public BackNode getBackRange() {
		return backRange;
	}

	public void setBackRange(BackNode backRange) {
		this.backRange = backRange;
	}

}
