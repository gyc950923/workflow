package com.online.workflow.process.parser.rulesparser;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.parser.formparser.FormParser;
import com.online.workflow.process.parser.resourcesparser.PerformerParser;
import com.online.workflow.process.resources.Performer;
import com.online.workflow.process.resources.XPDLNames;
import com.online.workflow.process.rules.Rule;
import com.online.workflow.process.rules.UserRule;

public class UserRuleParser extends RuleParser{

    /**
     * 
     * 功能:序列化UserRule
     * 约束:与本函数相关的约束
     * @param userRule
     * @param parentElement
     */
    public void doSerialize(UserRule userRule, Element parentElement) {
        if (ValidateUtil.isNull(userRule)) {
            return ;
        }
        super.doSerialize((Rule)userRule,parentElement);
        parentElement.addAttribute("formTaskEnum", String.valueOf(userRule.getFormTaskEnum()));
        parentElement.addAttribute("loopStrategy", String.valueOf(userRule.getLoopStrategy()));
        parentElement.addAttribute("batchApproval", String.valueOf(userRule.getBatchApproval()));
        parentElement.addAttribute("defaultViewEnum", String.valueOf(userRule.getDefaultViewEnum()));
        parentElement.addAttribute("actorAssignType", String.valueOf(userRule.getActorAssignType()));
        /*parentElement.addAttribute("actorIdKey", String.valueOf(userRule.getActorIdKey()));
        parentElement.addAttribute("actorNameKey", String.valueOf(userRule.getActorNameKey()));*/
        writePerformer(userRule.getPerformer(), parentElement);
        writeEditForm(userRule.getEditForm(), parentElement);
        writeViewForm(userRule.getViewForm(), parentElement);
        writeListForm(userRule.getListForm(), parentElement);
    }

    /**
     * 
     * 功能:序列化ListForm
     * 约束:与本函数相关的约束
     * @param listForm
     * @param parentElement
     */
    private void writeListForm(Form listForm, Element parentElement) {
        if (ValidateUtil.isNull(listForm)) {
            return ;
        }
        parentElement.addElement(XPDLNames.XPDL_ListForm);
        FormParser parser = new FormParser();
        parser.doSerialize(listForm,parentElement);
    }

    /**
     * 
     * 功能:序列化ViewForm
     * 约束:与本函数相关的约束
     * @param viewForm
     * @param parentElement
     */
    private void writeViewForm(Form viewForm, Element parentElement) {
        if (ValidateUtil.isNull(viewForm)) {
            return ;
        }
        parentElement.addElement(XPDLNames.XPDL_ViewForm);
        FormParser parser = new FormParser();
        parser.doSerialize(viewForm,parentElement);
    }

    /**
     * 
     * 功能:序列化EditForm
     * 约束:与本函数相关的约束
     * @param editForm
     * @param parentElement
     */
    private void writeEditForm(Form editForm, Element parentElement) {
        if (ValidateUtil.isNull(editForm)) {
            return ;
        }
        Element element = parentElement.addElement(XPDLNames.XPDL_EditForm);
        FormParser parser = new FormParser();
        parser.doSerialize(editForm,element);
    }

    /**
     * 
     * 功能:序列化Performer
     * 约束:与本函数相关的约束
     * @param performer
     * @param parentElement
     */
    private void writePerformer(Performer performer, Element parentElement) {
        if (ValidateUtil.isNull(performer)) {
            return ;
        }
        Element elementPerformer = parentElement.addElement(XPDLNames.XPDL_Performer);
        PerformerParser parser = new PerformerParser();
        parser.doSerialize(performer,elementPerformer);      
    }

    /**
     * 
     * 功能:反序列化UserRule
     * 约束:与本函数相关的约束
     * @param element
     * @param userRule
     */
    public void doParser(Element element, UserRule userRule,WorkflowProcess process) {
        super.doParser(element, (Rule)userRule);
        if (!ValidateUtil.isNull(element.attribute("formTaskEnum"))) {
            userRule.setFormTaskEnum(Integer.valueOf(element.attributeValue("formTaskEnum")));
        }
        if (!ValidateUtil.isNull(element.attribute("loopStrategy"))) {
            userRule.setLoopStrategy(Integer.valueOf(element.attributeValue("loopStrategy")));
        }
        if (!ValidateUtil.isNull(element.attribute("batchApproval"))) {
            userRule.setBatchApproval(Boolean.valueOf(element.attributeValue("batchApproval")));
        }
        if (!ValidateUtil.isNull(element.attribute("defaultViewEnum"))) {
            userRule.setDefaultViewEnum(Integer.valueOf(element.attributeValue("defaultViewEnum")));
        }
        if (!ValidateUtil.isNull(element.attribute("actorAssignType"))) {
            userRule.setActorAssignType(Integer.valueOf(element.attributeValue("actorAssignType")));
        }
        /*userRule.setActorIdKey(element.attributeValue("actorIdKey"));
        userRule.setActorNameKey(element.attributeValue("actorNameKey"));*/
        
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_Performer))) {
            if (ValidateUtil.isNull(userRule.getPerformer())) {
                userRule.setPerformer(new Performer());
            }
            loadPerformer(element.element(XPDLNames.XPDL_Performer), userRule.getPerformer(), process);
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_EditForm))) {
            if (ValidateUtil.isNull(userRule.getEditForm())) {
                userRule.setEditForm(new Form());
            }
            loadEditForm(element.element(XPDLNames.XPDL_EditForm), userRule.getEditForm(), process);
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_ViewForm))) {
            if (ValidateUtil.isNull(userRule.getViewForm())) {
                userRule.setViewForm(new Form());
            }
            loadViewForm(element.element(XPDLNames.XPDL_ViewForm), userRule.getViewForm(), process);
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_ListForm))) {
            if (ValidateUtil.isNull(userRule.getListForm())) {
                userRule.setListForm(new Form());
            }
            loadListForm(element.element(XPDLNames.XPDL_ListForm), userRule.getListForm(), process);
        }
    }

    /**
     * 
     * 功能:反序列化ListForm
     * 约束:与本函数相关的约束
     * @param element
     * @param listForm
     */
    private void loadListForm(Element element, Form listForm,WorkflowProcess process) {
        FormParser parser = new FormParser();
        parser.doParser(element, listForm, process);
    }

    /**
     * 
     * 功能:反序列化ViewForm
     * 约束:与本函数相关的约束
     * @param element
     * @param viewForm
     */
    private void loadViewForm(Element element, Form viewForm,WorkflowProcess process) {
        FormParser parser = new FormParser();
        parser.doParser(element, viewForm, process);
    }

    /**
     * 
     * 功能:反序列化editForm
     * 约束:与本函数相关的约束
     * @param element
     * @param editForm
     */
    private void loadEditForm(Element element, Form editForm,WorkflowProcess process) {
        FormParser parser = new FormParser();
        parser.doParser(element, editForm, process);
    }

    /**
     * 
     * 功能:反序列化Performer
     * 约束:与本函数相关的约束
     * @param element
     * @param performer
     */
    private void loadPerformer(Element element, Performer performer,WorkflowProcess process) {
        PerformerParser parser = new PerformerParser();
        parser.doParser(element, performer, process);
    }

    

}
