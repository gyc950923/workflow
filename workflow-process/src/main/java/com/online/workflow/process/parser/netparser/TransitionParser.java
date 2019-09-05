package com.online.workflow.process.parser.netparser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.net.Edge;
import com.online.workflow.process.net.Transition;
import com.online.workflow.process.parser.resourcesparser.PageActionItemParser;
import com.online.workflow.process.resources.PageActionItem;
import com.online.workflow.process.resources.XPDLNames;

public class TransitionParser extends EdgeParser{

    /**
     * 
     * 功能:序列化Transition<br>
     * 约束:与本函数相关的约束<br>
     * @param transition
     * @param parentElement
     */
    public void doSerialize(Transition transition, Element parentElement) {
        if (ValidateUtil.isNull(transition)) {
            return ;
        }
        super.doSerialize((Edge)transition,parentElement);
        
        if(transition.getStart() != null){
            parentElement.addAttribute("start", String.valueOf(transition.getStart()));
        }
        parentElement.addAttribute("sqlCondition", transition.getSqlCondition());
        parentElement.addAttribute("sqlOperator", transition.getSqlOperator());
        parentElement.addAttribute("sqlResult", transition.getSqlResult());
        parentElement.addAttribute("className", transition.getClassName());
        parentElement.addAttribute("methodName", transition.getMethodName());
        parentElement.addAttribute("varCondition", transition.getVarCondition());
        parentElement.addAttribute("varOperator", transition.getVarOperator());
        parentElement.addAttribute("varResult", transition.getVarResult());
        writePageActions(transition.getPageActions(),parentElement);
    }

    /**
     * 
     * 功能:序列化PageActions<br>
     * 约束:与本函数相关的约束<br>
     * @param pageActions
     * @param parentElement
     */
    private void writePageActions(List<PageActionItem> pageActions, Element parentElement) {
        if(ValidateUtil.isNullAndEmpty(pageActions)){
            return ;
        }
        Element elementPageActions = parentElement.addElement(XPDLNames.XPDL_PageActions);
        for(PageActionItem pageAction : pageActions){
            Element elementPageAction = elementPageActions.addElement(XPDLNames.XPDL_PageAction);
            PageActionItemParser parser = new PageActionItemParser();
            parser.doSerialize(pageAction,elementPageAction);
        } 
    }
	/**
     * 
     * 功能:反序列化Transition<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param transition
     */
    @SuppressWarnings("unchecked")
	public void doParser(Element element, Transition transition,WorkflowProcess process) {
        super.doParse(element, (Edge)transition, process);
        
        if (element.attributeValue("start") !=null) {
            transition.setStart(Integer.valueOf(element.attributeValue("start")));
        }
        transition.setSqlCondition(element.attributeValue("sqlCondition"));
        transition.setSqlOperator(element.attributeValue("sqlOperator"));
        transition.setSqlResult(element.attributeValue("sqlResult"));
        transition.setClassName(element.attributeValue("className"));
        transition.setMethodName(element.attributeValue("methodName"));
        transition.setVarCondition(element.attributeValue("varCondition"));
        transition.setVarOperator(element.attributeValue("varOperator"));
        transition.setVarResult(element.attributeValue("varResult"));
        
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_PageActions))) {
            if (ValidateUtil.isNull(transition.getPageActions())) {
            	transition.setPageActions(new ArrayList<PageActionItem>());
            }
            loadPageActions(element.element(XPDLNames.XPDL_PageActions), transition.getPageActions());
        }
        
    }

    /**
     * 
     * 功能:反序列化PageActions<br>
     * 约束:与本函数相关的约束<br>
     * @param parentElement
     * @param pageActions
     */
    private void loadPageActions(Element parentElement, List<PageActionItem> pageActions) {
        List<Element> elements = parentElement.elements(XPDLNames.XPDL_PageAction);
        for (Element element : elements) {
            PageActionItem pageActionItem = new PageActionItem();
            PageActionItemParser parser = new PageActionItemParser();
            parser.doParser(element, pageActionItem);
            pageActions.add(pageActionItem);
        }
    }

}
