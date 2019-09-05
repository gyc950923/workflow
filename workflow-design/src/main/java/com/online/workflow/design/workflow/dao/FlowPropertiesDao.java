package com.online.workflow.design.workflow.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.fasterxml.classmate.AnnotationConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.online.workflow.design.utils.DataConfig;
import com.online.workflow.design.web.test.TestBean;
import com.online.workflow.design.workflow.bean.FlowGlobalProperties;
import com.online.workflow.design.workflow.bean.FlowNodeProperties;

public class FlowPropertiesDao {

	private ObjectMapper objectMapper =  new ObjectMapper();
	
	
	/**
	 * 保存全局属性
	 */
	public void saveGlobal(FlowGlobalProperties fg){
		
		File f = this.getPropertiesFile(fg.getModelId());
	
		try {
			ObjectNode root = null;
			if(f.exists()){
				root = (ObjectNode)objectMapper.readTree(f);
			}else{
				root = objectMapper.createObjectNode();
			}
			root.putPOJO(DataConfig.GLOBAL_NODE_PROPERTIES_KEY, fg);
			
			objectMapper.writeValue(f, root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ObjectNode root = objectMapper.createObjectNode();
			root.putPOJO(DataConfig.GLOBAL_NODE_PROPERTIES_KEY, fg);
			
			try {
				objectMapper.writeValue(f, root);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * 保存节点属性
	 * @param fn
	 */
	public void saveNode(FlowNodeProperties fn){
		
		File f = this.getPropertiesFile(fn.getModelId());
		List<FlowNodeProperties> list = this.getFlowNodePropertiesByList(fn.getModelId());
		FlowNodeProperties old = this.getFlowNodeProperties(fn.getModelId(), fn.getResourceId());
		if(null == old){//新增
			list.add(fn);
		}else{//更新
			
			for(FlowNodeProperties fnp : list){
				
				if(fnp.getResourceId().equals(fn.getResourceId())){
					
					list.remove(fnp);
					list.add(fn);
				}
			}
		}
		
		try {
			ObjectNode root = null;
			if(f.exists()){
				root = (ObjectNode)objectMapper.readTree(f);
			}else{
				root = objectMapper.createObjectNode();
			}
			root.putPOJO(DataConfig.NODE_PROPERTIES_ARRAY_KEY, list);
			objectMapper.writeValue(f, root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据流程主键得到全局属性
	 * @param modelId
	 * @return
	 */
	public FlowGlobalProperties getFlowGlobalProperties(String modelId){
		
		File f = this.getPropertiesFile(modelId);
		FlowGlobalProperties ret = null;
		try {
			  if(f.exists()){
				  JsonNode root = objectMapper.readTree(f);
				  ObjectNode globalNode = (ObjectNode)root.get(DataConfig.GLOBAL_NODE_PROPERTIES_KEY);
				  if(null != globalNode){
					  ret = objectMapper.readValue(objectMapper.writeValueAsString(globalNode), FlowGlobalProperties.class);
				  }
			  }
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 得到指定流程下的所有节点集合
	 * @param modelId
	 * @return
	 */
	public List<FlowNodeProperties> getFlowNodePropertiesByList(String modelId){
		
		List<FlowNodeProperties> list = new ArrayList<FlowNodeProperties>();	
		JsonNode root;
		
        FlowNodeProperties[] nodesArray=null;
        try {
            Long start=new Date().getTime();
            Configuration conf=new AnnotationConfiguration().configure();
            SessionFactory sf=conf.buildSessionFactory();
            Session sess=sf.openSession();
            Transaction tx=sess.beginTransaction();
            String hql="from TestBean where modelId='"+modelId+"'";
            Query query=sess.createQuery(hql);
            TestBean testBean=(TestBean) query.list().get(0);
            tx.commit();
            sess.close();
            sf.close();
            Long end=new Date().getTime();
            System.out.println("执行SQL耗时："+(end-start)+"ms");
            root=objectMapper.readTree(testBean.getPropJson());
            ArrayNode nodes = (ArrayNode)root.get(DataConfig.NODE_PROPERTIES_ARRAY_KEY);
            nodesArray = objectMapper.readValue(testBean.getPropJson(), FlowNodeProperties[].class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(null != nodesArray && nodesArray.length > 0){
            
            for(int i = 0; i< nodesArray.length; i++){
                list.add(nodesArray[i]);
            }
        }
        /*File f = this.getPropertiesFile(modelId);
        if(f.exists()){
			try {
				root = objectMapper.readTree(f);
				ArrayNode nodes = (ArrayNode)root.get(DataConfig.NODE_PROPERTIES_ARRAY_KEY);
                FlowNodeProperties[] nodesArray = objectMapper.readValue(objectMapper.writeValueAsString(nodes), FlowNodeProperties[].class);
				
				if(null != nodesArray && nodesArray.length > 0){
					
					for(int i = 0; i< nodesArray.length; i++){
						list.add(nodesArray[i]);
					}
					//list = Arrays.asList(nodesArray);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			}
		}*/
		
		return list;
	}
	
	public void saveFlowNodePropertiesByList(String modelId,List<FlowNodeProperties> list){
		/*
		for(FlowNodeProperties fnp : list){
			this.saveNode(fnp);
		}
		*/
	    
	    Configuration conf=new AnnotationConfiguration().configure();
        SessionFactory sf=conf.buildSessionFactory();
        Session sess=sf.openSession();
        Transaction tx=sess.beginTransaction();
        String hql= "from TestBean where modelId='"+modelId+"'";
        Query query=sess.createQuery(hql);
        //News n=new News();
        TestBean bean=(TestBean) query.list().get(0);
        bean.setModelId(modelId);
        String propJson;
        try {
            propJson = objectMapper.writeValueAsString(list);
            bean.setPropJson(propJson);
        } catch (JsonProcessingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }    
        sess.saveOrUpdate(bean);
        tx.commit();
        sess.close();
        sf.close();
		
		File f = this.getPropertiesFile(modelId);
		
		ObjectNode root = null;
		try {
			if(f.exists()){
		
					root = (ObjectNode)objectMapper.readTree(f);
			
			}else{
				root = objectMapper.createObjectNode();
			}
			root.putPOJO(DataConfig.NODE_PROPERTIES_ARRAY_KEY, list);
			objectMapper.writeValue(f, root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据流程主键获取指定属性主键的属性
	 * @param flowId
	 * @param id
	 * @return
	 */
	public FlowNodeProperties getFlowNodeProperties(String modelId,String resourceId){
		
		List<FlowNodeProperties> list = this.getFlowNodePropertiesByList(modelId);
		FlowNodeProperties node = null;
		
		for(FlowNodeProperties n : list){
			if(n.getResourceId().equals(resourceId)){
				node = n;
				break;
			}
		}
		return node;
	}
	
	private File getPropertiesFile(String modelId){
		
		File p =  new File(DataConfig.DATA_PATH);
		if(!p.exists()){
			p.mkdirs();
		}
		
		File  f = new File(DataConfig.DATA_PATH + "\\" + modelId + "_prop.json");
		
		/*
		if(!f.exists()){
			
			
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		*/
		return f;
		
	}
	
	public static void main(String[] args) {
		
		FlowPropertiesDao dao = new FlowPropertiesDao();
		
		/**  保存全局属性   
		FlowGlobalProperties fg = new FlowGlobalProperties();
		fg.setModelId("111test");
		fg.setResourceId("222");
		fg.setName("自测试123");
		fg.setTransaction("自测试1描述24"); 
		
		dao.saveGlobal(fg); 
		 */
		
		/** 读取全局属性
		FlowGlobalProperties fg = dao.getFlowGlobalProperties("111test");
		
		if(null != fg){
		
			System.out.println("id:"+ fg.getResourceId() + ",flowId:" + fg.getModelId() +
				",name:" + fg.getName() + ",transaction:" + fg.getTransaction());
		}
		
		*/
		/** 保存节点属性 
		FlowNodeProperties nodePro = new FlowNodeProperties();
		nodePro.setModelId("111test");
		nodePro.setResourceId("n_qq3");
		nodePro.setName("n1_自测试12345");
		nodePro.setTransaction("n1_自测试1描述234"); 
		dao.saveNode(nodePro); 
		 */
		
		/** 读取节点属性 
		FlowNodeProperties nodePro = dao.getFlowNodeProperties("111test", "n_33333");
		if(null != nodePro){
			System.out.println("name:" + nodePro.getName() + ",transaction:" + nodePro.getTransaction());
		}
		*/
		System.out.println("=============end");
		
		System.exit(0);
	}
}
