package com.online.engine.model;

public class Org {

    private String id;
    
    private String departName;
    
    private Long parentDepartId;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDepartName() {
        return departName;
    }
    public void setDepartName(String departName) {
        this.departName = departName;
    }
    public Long getParentDepartId() {
        return parentDepartId;
    }
    public void setParentDepartId(Long parentDepartId) {
        this.parentDepartId = parentDepartId;
    }

}
