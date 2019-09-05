package com.online.workflow.util;


public class PageModel {

    private Integer page;// 页码
    private Integer rows;// 每页显示的记录条数
    private Integer total;//记录总数
    
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getRows() {
        return rows;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
    }
    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
}
