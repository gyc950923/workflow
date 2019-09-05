package com.online.workflow.process.resources;

public  class AbstractResource implements IResource{
	/**
	 * 资源的名称
	 */
    private String name;
    
    /**
     * 资源的显示名称
     */
    private String displayName;
    
    /**
     * 资源的描述
     */
    private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
