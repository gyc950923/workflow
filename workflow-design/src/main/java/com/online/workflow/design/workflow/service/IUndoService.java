package com.online.workflow.design.workflow.service;

import java.util.List;

import com.online.workflow.process.IWFElement;

public interface IUndoService {

    /**
     * 
     * 功能:根据ChartId获取IWFElement<br>
     * 约束:与本函数相关的约束<br>
     * @param redoList
     * @param string
     * @return
     */
    IWFElement getWFElementByChartId(List<IWFElement> undoList, String string);

    /**
     * 
     * 功能:根据ChartId删除IWFElement<br>
     * 约束:与本函数相关的约束<br>
     * @param redoList
     * @param string
     */
    void removeWFElementByChartId(List<IWFElement> redoList, String string);

}
