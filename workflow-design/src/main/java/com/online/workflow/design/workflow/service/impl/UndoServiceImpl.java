package com.online.workflow.design.workflow.service.impl;

import java.util.List;

import com.online.workflow.design.workflow.service.IUndoService;
import com.online.workflow.process.IWFElement;

public class UndoServiceImpl implements IUndoService{

    @Override
    public IWFElement getWFElementByChartId(List<IWFElement> undoList, String chartId) {
        for(IWFElement iWFElement : undoList){
            if (chartId.equals(iWFElement.getChartId())) {
                return iWFElement;
            }
        }
        return null;
    }

    @Override
    public void removeWFElementByChartId(List<IWFElement> undoList, String chartId) {
        for (int i = 0; i < undoList.size(); i++) {
            if (chartId.equals(undoList.get(i).getChartId())) {
                undoList.remove(i);
                break ;
                
            }
        }   
    }

    

}
