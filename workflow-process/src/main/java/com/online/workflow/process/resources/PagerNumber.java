package com.online.workflow.process.resources;

public class PagerNumber {
	 /// <summary>
    /// 页数
    /// </summary>
    private Integer currentPageNumber ;

    /// <summary>
    /// 每页显示多少条数据
    /// </summary>
    private Integer pageSize ;

	public Integer getCurrentPageNumber() {
		return currentPageNumber;
	}

	public void setCurrentPageNumber(Integer currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
    
    
}
