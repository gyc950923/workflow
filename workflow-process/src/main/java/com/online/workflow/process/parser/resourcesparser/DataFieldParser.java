package com.online.workflow.process.parser.resourcesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.AbstractWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.parser.AbstractWFElementParser;
import com.online.workflow.process.resources.DataField;

public class DataFieldParser extends AbstractWFElementParser{

    /**
     * 
     * 功能:序列化DataField<br>
     * 约束:与本函数相关的约束<br>
     * @param dataField
     * @param parentDataField
     */
    public void doSerialize(DataField dataField, Element parentDataField) {
        if(ValidateUtil.isNull(dataField)){
            return ;
        }
        super.doSerialize((AbstractWFElement)dataField, parentDataField);
        parentDataField.addAttribute("dataType", dataField.getDataType());
        parentDataField.addAttribute("initialValue", dataField.getInitialValue());
        parentDataField.addAttribute("dataPattern", dataField.getDataPattern());
    }
    
    public void doParse(Element element, DataField dataField,WorkflowProcess process) {
        super.doParse(element, (AbstractWFElement)dataField, process);
        dataField.setDataType(element.attributeValue("dataType"));
        dataField.setInitialValue(element.attributeValue("initialValue"));
        dataField.setDataPattern(element.attributeValue("dataPattern"));
    }

    

}
