package com.online.workflow.process.resources;

public class FieldAuthority {

    private String code;
    private Integer isVisible = 1;//0代表不可见，1代表可见
    private Integer isRead = 0 ;//0代表非只读，1代表只读
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setIsVisible(Integer isVisible){
        this.isVisible = isVisible;
    }
    public Integer getIsVisible(){
        return this.isVisible;
    }
    public void setIsRead(Integer isRead){
        this.isRead = isRead;
    }
    public Integer getIsRead(){
        return this.isRead;
    }
}
