package com.online.workflow.process.rules;

public class AdvanceRule {
	 /// <summary>
    /// 流转时是否弹出确认对话框
    /// </summary>
    private boolean isAdvanceConfirm ;

    /// <summary>
    /// 人员是否可调整
    /// </summary>
    private boolean isAdjustStaff ;
    
    /**
     * 节点是否待阅选择
     */
    private boolean isToBeRead;

	public boolean isAdvanceConfirm() {
		return isAdvanceConfirm;
	}

	public void setAdvanceConfirm(boolean isAdvanceConfirm) {
		this.isAdvanceConfirm = isAdvanceConfirm;
	}

	public boolean isAdjustStaff() {
		return isAdjustStaff;
	}

	public void setAdjustStaff(boolean isAdjustStaff) {
		this.isAdjustStaff = isAdjustStaff;
	}

	public boolean isToBeRead() {
		return isToBeRead;
	}

	public void setIsToBeRead(boolean isToBeRead) {
		this.isToBeRead = isToBeRead;
	}

}
