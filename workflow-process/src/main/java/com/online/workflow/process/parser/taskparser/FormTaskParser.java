package com.online.workflow.process.parser.taskparser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.parser.resourcesparser.FieldAuthorityParser;
import com.online.workflow.process.parser.resourcesparser.PageActionItemParser;
import com.online.workflow.process.parser.rulesparser.AdvanceRuleParser;
import com.online.workflow.process.parser.rulesparser.BackRuleParser;
import com.online.workflow.process.parser.rulesparser.UserRuleParser;
import com.online.workflow.process.resources.FieldAuthority;
import com.online.workflow.process.resources.PageActionItem;
import com.online.workflow.process.resources.XPDLNames;
import com.online.workflow.process.rules.AdvanceRule;
import com.online.workflow.process.rules.BackRule;
import com.online.workflow.process.rules.UserRule;
import com.online.workflow.process.tasks.FormTask;
import com.online.workflow.process.tasks.Task;

public class FormTaskParser extends TaskParser{

    public void doSerialize(FormTask task, Element parentElement) {
        if (ValidateUtil.isNull(task)) {
            return ;
        }
        super.doSerialize((Task)task,parentElement);
        writeUserRule(task.getUserRule(),parentElement);
        writeBackRule(task.getBackRule(),parentElement);
        writeAdvanceRule(task.getAdvanceRule(),parentElement);
        writePageActions(task.getPageActions(),parentElement);
        writeFieldAuthoritys(task.getFieldAuthoritys(),parentElement);
    }

	/**
     * 
     * 功能:序列化FieldAuthoritys<br>
     * 约束:与本函数相关的约束<br>
     * @param fieldAuthoritys
     * @param parentElement
     */
    private void writeFieldAuthoritys(List<FieldAuthority> fieldAuthoritys, Element parentElement) {
        if(ValidateUtil.isNullAndEmpty(fieldAuthoritys)){
            return ;
        }
        Element elementFieldAuthoritys = parentElement.addElement(XPDLNames.XPDL_FieldAuthoritys);
        for(FieldAuthority fieldAuthority : fieldAuthoritys){
            Element elementfieldAuthority = elementFieldAuthoritys.addElement(XPDLNames.XPDL_FieldAuthority);
            FieldAuthorityParser parser = new FieldAuthorityParser();
            parser.doSerialize(fieldAuthority,elementfieldAuthority);
        }
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
     * 功能:序列化AdvanceRule<br>
     * 约束:与本函数相关的约束<br>
     * @param advanceRule
     * @param parentElement
     */
    private void writeAdvanceRule(AdvanceRule advanceRule, Element parentElement) {
        if (ValidateUtil.isNull(advanceRule)) {
            return ;
        }
        Element element = parentElement.addElement(XPDLNames.XPDL_AdvanceRule);
        AdvanceRuleParser parser = new AdvanceRuleParser();
        parser.doSerialize(advanceRule,element);
    }

    /**
     * 
     * 功能:序列化BackRule<br>
     * 约束:与本函数相关的约束<br>
     * @param backRule
     * @param parentElement
     */
    private void writeBackRule(BackRule backRule, Element parentElement) {
        if (ValidateUtil.isNull(backRule)) {
            return ;
        }
        Element element = parentElement.addElement(XPDLNames.XPDL_BackRule);
        BackRuleParser parser = new BackRuleParser();
        parser.doSerialize(backRule,element);
    }

    /**
     * 
     * 功能:序列化userRule<br>
     * 约束:与本函数相关的约束<br>
     * @param userRule
     * @param parentElement
     */
    private void writeUserRule(UserRule userRule, Element parentElement) {
        if (ValidateUtil.isNull(userRule)) {
            return ;
        }
        Element element = parentElement.addElement(XPDLNames.XPDL_UserRule);
        UserRuleParser parser = new UserRuleParser();
        parser.doSerialize(userRule,element);
    }

    /**
     * 
     * 功能:反序列化FormTask<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param formTask
     */
    public void doParser(Element element, FormTask formTask,WorkflowProcess process) {
        super.doParser(element, (Task)formTask, process);
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_UserRule))) {
            if (ValidateUtil.isNull(formTask.getUserRule())) {
                formTask.setUserRule(new UserRule());
            }
            loadUserRule(element.element(XPDLNames.XPDL_UserRule), formTask.getUserRule(), process);
        }
        if(!ValidateUtil.isNull(element.element(XPDLNames.XPDL_BackRule))) {
            if (ValidateUtil.isNull(formTask.getBackRule())) {
                formTask.setBackRule(new BackRule());
            }
            loadBackRule(element.element(XPDLNames.XPDL_BackRule), formTask.getBackRule(), process);
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_AdvanceRule))) {
            if (ValidateUtil.isNull(formTask.getAdvanceRule())) {
                formTask.setAdvanceRule(new AdvanceRule());
            }
            loadAdvanceRule(element.element(XPDLNames.XPDL_AdvanceRule), formTask.getAdvanceRule(), process);
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_PageActions))) {
            if (ValidateUtil.isNull(formTask.getPageActions())) {
                formTask.setPageActions(new ArrayList<PageActionItem>());
            }
            loadPageActions(element.element(XPDLNames.XPDL_PageActions), formTask.getPageActions());
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_FieldAuthoritys))) {
            if (ValidateUtil.isNull(formTask.getFieldAuthoritys())) {
                formTask.setFieldAuthoritys(new ArrayList<FieldAuthority>());
            }
            loadFieldAuthoritys(element.element(XPDLNames.XPDL_FieldAuthoritys), formTask.getFieldAuthoritys());
        }
    }

    /**
     * 
     * 功能:反序列化FieldAuthoritys<br>
     * 约束:与本函数相关的约束<br>
     * @param parentElemnt
     * @param fieldAuthoritys
     */
    private void loadFieldAuthoritys(Element parentElemnt, List<FieldAuthority> fieldAuthoritys) {
        List<Element> elements = parentElemnt.elements(XPDLNames.XPDL_FieldAuthority);
        for (Element element : elements) {
            FieldAuthority fieldAuthority = new FieldAuthority();
            FieldAuthorityParser parser = new FieldAuthorityParser();
            parser.doParser(element, fieldAuthority);
            fieldAuthoritys.add(fieldAuthority);
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

    /**
     * 
     * 功能:反序列化AdvanceRule<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param advanceRule
     * @param process 
     */
    private void loadAdvanceRule(Element element, AdvanceRule advanceRule, WorkflowProcess process) {
        AdvanceRuleParser parser = new AdvanceRuleParser();
        parser.doParser(element, advanceRule); 
    }

    /**
     * 
     * 功能:反序列化BackRule<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param backRule
     * @param process 
     */
    private void loadBackRule(Element element, BackRule backRule, WorkflowProcess process) {
        BackRuleParser parser = new BackRuleParser();
        parser.doParser(element, backRule);
    }

    /**
     * 
     * 功能:反序列化UserRule<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param userRule
     * @param process 
     */
    private void loadUserRule(Element element, UserRule userRule, WorkflowProcess process) {
        UserRuleParser parser = new UserRuleParser();
        parser.doParser(element, userRule, process); 
    }

    

}
