package com.online.workflow.process.parser;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Element;

import com.online.workflow.common.ValidateUtil;
import com.online.workflow.process.AbstractWFElement;
import com.online.workflow.process.EventListener;
import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.parser.netparser.EventListenerParser;
import com.online.workflow.process.parser.resourcesparser.MapParser;
import com.online.workflow.process.resources.XPDLNames;

public class AbstractWFElementParser {

    /**
     * 
     * 功能:序列化AbstractWFElement<br>
     * 约束:与本函数相关的约束<br>
     * @param abstractWFElement
     * @param xElement
     */
	public void doSerialize(AbstractWFElement abstractWFElement, Element xElement) {
		if (ValidateUtil.isNull(abstractWFElement)) {
            return ;
        }
		xElement.addAttribute("id", abstractWFElement.getId());
		xElement.addAttribute("chartId", abstractWFElement.getChartId());
		xElement.addAttribute("name", abstractWFElement.getName());
		xElement.addAttribute("description", abstractWFElement.getDescription());
		if(!"null".equals(abstractWFElement.getCallBackStatus())){
			xElement.addAttribute("callBackStatus", abstractWFElement.getCallBackStatus()+"");
		}
		xElement.addAttribute("restfulAddress", abstractWFElement.getRestfulAddress());
		
		writeEventListeners(abstractWFElement.getEventListeners(), xElement);
		writeExtendedAttributes(abstractWFElement.getExtendedAttributes(), xElement);

	}

	/**
	 * 
	 * 功能:序列化扩展属性ExtendedAttribute<br>
	 * 约束:与本函数相关的约束<br>
	 * @param extendedAttributes
	 * @param parentElement
	 */
	private void writeExtendedAttributes(Map<String, String> extendedAttributes, Element parentElement) {
	    if (ValidateUtil.isNullAndEmpty(extendedAttributes)) {
            return ;
        }
	    Element elementExtendedAttributes = parentElement
                .addElement(XPDLNames.XPDL_ExtendedAttributes);
	    Set<Entry<String,String>> extendedAttributesSet =extendedAttributes.entrySet();
        for (Entry<String,String> extendedAttribute : extendedAttributesSet) {
            Element element = elementExtendedAttributes
                    .addElement(XPDLNames.XPDL_ExtendedAttribute);
            MapParser parser = new MapParser();
            parser.doSerialize(extendedAttribute,element);
        }
    }

    /**
	 * 
	 * 功能:序列化监听事件器EventListener<br>
	 * 约束:与本函数相关的约束<br>
	 * @param eventListeners
	 * @param parentElement
	 */
    private void writeEventListeners(List<EventListener> eventListeners, Element parentElement) {
        if (ValidateUtil.isNullAndEmpty(eventListeners)) {
            return ;
        }
        Element elementEventListeners = parentElement
                .addElement(XPDLNames.XPDL_EventListeners);
        for (EventListener eventListener : eventListeners) {
            Element element = elementEventListeners
                    .addElement(XPDLNames.XPDL_EventListener);
            EventListenerParser parser = new EventListenerParser();
            parser.doSerialize(eventListener,element);
        }
    }

    /* ---------- 反 序 列 化 ---------- */
    
    /**
     * 
     * 功能:反序列化AbstractWFElement<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param abstractWFElement
     */
    public void doParse(Element element, AbstractWFElement abstractWFElement,WorkflowProcess process) {
    	/*if (!(abstractWFElement instanceof WorkflowProcess)) {
			abstractWFElement.setParentElement(process);
		}*/
        abstractWFElement.setId(element.attributeValue("id"));
        abstractWFElement.setChartId(element.attributeValue("chartId"));
        abstractWFElement.setName(element.attributeValue("name"));
        abstractWFElement.setDescription(element.attributeValue("description"));
        if(null != element.attributeValue("callBackStatus") && !"null".equals(element.attributeValue("callBackStatus"))){
        	abstractWFElement.setCallBackStatus(Integer.parseInt(element.attributeValue("callBackStatus")));
        }
        abstractWFElement.setRestfulAddress(element.attributeValue("restfulAddress"));
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_EventListeners))) {
            loadEventListeners(element.element(XPDLNames.XPDL_EventListeners),abstractWFElement.getEventListeners(), process);
        }
        if (!ValidateUtil.isNull(element.element(XPDLNames.XPDL_ExtendedAttributes))) {
            loadExtendedAttributes(element.element(XPDLNames.XPDL_ExtendedAttributes),abstractWFElement.getExtendedAttributes());
        }      
    }

    /**
     * 
     * 功能:反序列化ExtendedAttributes<br>
     * 约束:与本函数相关的约束<br>
     * @param parentElement
     * @param extendedAttributes
     */
    private void loadExtendedAttributes(Element parentElement, Map<String, String> extendedAttributes) {
        List<Element> elements = parentElement.elements(XPDLNames.XPDL_ExtendedAttribute);
        for(Element element : elements){
            MapParser parser = new MapParser();
            parser.doParse(element, extendedAttributes);
        }
    }

    /**
     * 
     * 功能:反序列化EventListeners<br>
     * 约束:与本函数相关的约束<br>
     * @param parentElement
     * @param eventListeners
     */
    private void loadEventListeners(Element parentElement, List<EventListener> eventListeners,WorkflowProcess process) {
        List<Element> elements = parentElement.elements(XPDLNames.XPDL_EventListener);
        for (Element element : elements) {
            EventListener eventListener = new EventListener();
            EventListenerParser parser = new EventListenerParser();
            parser.doParse(element, eventListener, process);
            eventListeners.add(eventListener);
        } 
    }

}
