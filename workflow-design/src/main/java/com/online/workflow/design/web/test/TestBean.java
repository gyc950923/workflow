package com.online.workflow.design.web.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 该类用于测试流程模板数据入库与出库操作，已不再使用
 */
@Entity
@Table(name="aaa")
public class TestBean {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="jsonXml")
    private String jsonXml;
    @Column(name="svgXml")
    private String svgXml;
    @Column(name="flowJson")
    private String flowJson;
    @Column(name="propJson")
    private String propJson;
    @Column(name="modelId")
    private String modelId;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getJsonXml() {
        return jsonXml;
    }
    public void setJsonXml(String jsonXml) {
        this.jsonXml = jsonXml;
    }
    public String getSvgXml() {
        return svgXml;
    }
    public void setSvgXml(String svgXml) {
        this.svgXml = svgXml;
    }
    public String getNodeJson() {
        return flowJson;
    }
    public void setNodeJson(String flowJson) {
        this.flowJson = flowJson;
    }
    public String getModelId() {
        return modelId;
    }
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
    public String getFlowJson() {
        return flowJson;
    }
    public void setFlowJson(String flowJson) {
        this.flowJson = flowJson;
    }
    public String getPropJson() {
        return propJson;
    }
    public void setPropJson(String propJson) {
        this.propJson = propJson;
    }
    
    
}
