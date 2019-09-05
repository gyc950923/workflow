package com.online.workflow.process.resources;

public class PageActionItem {

    private String buttonName;//按钮名称
    private String methodName;//按钮触发的事件名称
    
    public String getButtonName() {
        return buttonName;
    }
    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
}
