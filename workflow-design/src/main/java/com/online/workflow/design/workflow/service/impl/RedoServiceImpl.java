package com.online.workflow.design.workflow.service.impl;

import java.util.List;

import com.online.workflow.design.workflow.service.IRedoService;
import com.online.workflow.process.IWFElement;

public class RedoServiceImpl implements IRedoService{

    @Override
    public IWFElement getWFElementByChartId(List<IWFElement> redoList, String chartId) {
        for(IWFElement iWFElement : redoList){
            if (chartId.equals(iWFElement.getChartId())) {
                return iWFElement;
            }
        }
        return null;
        
    }

    @Override
    public void removeWFElementByChartId(List<IWFElement> redoList, String chartId) {
        for (int i = 0; i < redoList.size(); i++) {
            if (chartId.equals(redoList.get(i).getChartId())) {
                redoList.remove(i);
                break ;
                
            }
        }
    }

    

}
