package com.online.workflow.process.parser.formparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.AbstractWFElement;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.parser.AbstractWFElementParser;

public class FormParser extends AbstractWFElementParser{

    /**
     * 
     * 功能:序列化Form<br>
     * 约束:与本函数相关的约束<br>
     * @param form
     * @param parentElement
     */
    public void doSerialize(Form form, Element parentElement) {
        if (ValidateUtil.isNull(form)) {
            return ;
        }
        super.doSerialize((AbstractWFElement)form, parentElement);
        parentElement.addAttribute("url", form.getUrl());    
    }

    /**
     * 
     * 功能:反序列化Form<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param editForm
     */
    public void doParser(Element element, Form editForm,WorkflowProcess process) {
        super.doParse(element, (AbstractWFElement)editForm,process);
        editForm.setUrl(element.attributeValue("url"));
    }

    

}
