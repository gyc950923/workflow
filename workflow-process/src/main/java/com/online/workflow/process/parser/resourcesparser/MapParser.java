package com.online.workflow.process.parser.resourcesparser;

import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Element;

public class MapParser {

    /**
     * 
     * 功能:序列化Map中的Entry对象<br>
     * 约束:与本函数相关的约束<br>
     * @param extendedAttribute
     * @param element
     */
    public void doSerialize(Entry<String, String> extendedAttribute, Element element) {
        element.addAttribute("key", extendedAttribute.getKey());
        element.addAttribute("value", extendedAttribute.getValue());    
    }
    
    /**
     * 
     * 功能:反序列化Map中的Entry对象<br>
     * 约束:与本函数相关的约束<br>
     * @param element
     * @param extendedAttributes
     */
    public void doParse(Element element, Map<String, String> extendedAttributes) {
        extendedAttributes.put(element.attributeValue("key"), element.attributeValue("value"));
    } 

}
