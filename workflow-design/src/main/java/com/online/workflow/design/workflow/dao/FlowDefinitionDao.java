package com.online.workflow.design.workflow.dao;

import java.io.File;
import java.io.IOException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.fasterxml.classmate.AnnotationConfiguration;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.online.workflow.design.utils.DataConfig;
import com.online.workflow.design.web.test.TestBean;
import com.online.workflow.design.workflow.bean.FlowDefinitionBean;

public class FlowDefinitionDao {

	public static final String DATA_PATH = "d:/flow_design/data";
	
	public  void save(FlowDefinitionBean bean){
        
		if(null != bean.getModel()){
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
			 File f = getFlowFile(bean.getModelId());
			//ObjectNode root = objectMapper.createObjectNode();
			//root.putPOJO(DataConfig.GLOBAL_NODE_PROPERTIES_KEY, fg);
			 try {
			     objectMapper.writeValue(f, bean);
			     String flowJson=objectMapper.writeValueAsString(bean);
			     Configuration conf=new Configuration().configure();
			        SessionFactory sf=conf.buildSessionFactory();
			        Session sess=sf.openSession();
			        Transaction tx=sess.beginTransaction();
			        /*String hql= "from TestBean where modelId='"+bean.getModelId()+"'";
			        Query query=sess.createQuery(hql);*/
			        TestBean testBean=new TestBean();
			        testBean.setModelId(bean.getModelId());
			        testBean.setFlowJson(flowJson);
			        sess.saveOrUpdate(testBean);
			        tx.commit();
			        sess.close();
			        sf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public  FlowDefinitionBean findFlowDefinitionBeanById(String modelId){
		
		FlowDefinitionBean ret = null;
		
		if(null != modelId && (!modelId.equals(""))){
			
			File f = getFlowFile(modelId);
			if(f.exists()){
                
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
               
                try {
                   //ret = objectMapper.readValue(f, FlowDefinitionBean.class);
               } catch (Exception e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
               }
           }
			ObjectMapper objectMappe = new ObjectMapper();
            objectMappe.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
            Configuration conf=new Configuration().configure();
            SessionFactory sf=conf.buildSessionFactory();
            Session sess=sf.openSession();
            Transaction tx=sess.beginTransaction();
            String hql= "from TestBean where modelId='"+modelId+"'";
            Query query=sess.createQuery(hql);
            TestBean bean=(TestBean) query.list().get(0);
            try {
                ret =objectMappe.readValue(bean.getFlowJson(), FlowDefinitionBean.class);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //sess.saveOrUpdate(bean);
            tx.commit();
            sess.close();
            sf.close();	
		}
		
		return ret;
	}
	
	private File getFlowFile(String modelId){
		
		File p =  new File(DataConfig.DATA_PATH);
		if(!p.exists()){
			p.mkdirs();
		}
		
		File  f = new File(DataConfig.DATA_PATH + "\\" + modelId + "_flow.json");
	
		return f;
	}
	
	
	public  void save2(FlowDefinitionBean bean){
		
		 ObjectMapper mapper = new ObjectMapper();
		 File f = new File(FlowDefinitionDao.DATA_PATH + "/" + bean.getModelId() + ".json");
		 File f2 = new File(FlowDefinitionDao.DATA_PATH + "/" + bean.getModelId() + "_model.json");
		 try {
			 
			 /*
				if(null != bean.getModel()){
					String str;
					//try {
					
						//str = mapper.writeValueAsString(mapper.readTree(bean.getModel()));
						str = bean.getModel().replaceAll("\\\\", ""); 
						System.out.println("str_" +  str.length() + ":" + str);
						bean.setModel(str);
					//} catch (IOException e) {
						// TODO Auto-generated catch block
					//	e.printStackTrace();
					//}
					
				}
				*/
			ObjectNode node = mapper.createObjectNode();
			node.put("name", bean.getName());
			node.put("modelId", bean.getModelId());
			node.put("description", bean.getDescription());
			
			mapper.writeValue(f, node);
			mapper.writeValue(f2, mapper.readTree(bean.getModel()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  FlowDefinitionBean findFlowDefinitionBeanById2(String modelId){
		
		FlowDefinitionBean bean = new FlowDefinitionBean();
		ObjectMapper mapper = new ObjectMapper();
		File f = new File(FlowDefinitionDao.DATA_PATH + "/" + modelId + ".json");
		File f2 = new File(FlowDefinitionDao.DATA_PATH + "/" + modelId + "_model.json");
		
		ObjectNode objNode = null;
		try {
			objNode = (ObjectNode)mapper.readTree(f2);
			bean = mapper.readValue(f, FlowDefinitionBean.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				//objNode =  (ObjectNode)mapper.readTree(FlowDefinitionBean.class.getClassLoader().getResourceAsStream("f2.json"));
				bean = mapper.readValue(FlowDefinitionBean.class.getClassLoader().getResourceAsStream("f2.json"), FlowDefinitionBean.class);
				bean.setModel(objNode!= null ? mapper.writeValueAsString(objNode) : null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		/*
		bean.setModelId(objNode.get("modelId")!=null ? objNode.get("modelId").asText() : null);
		bean.setName(objNode.get("name") != null ? objNode.get("name").asText() : null);
		bean.setDescription(objNode.get("description") != null ? objNode.get("description").asText() : null);
		*/
		
		
		return bean;
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FlowDefinitionBean bean  = new FlowDefinitionDao().findFlowDefinitionBeanById("1212"); 
		if(null != bean){
			System.out.println(bean.getModelId() + "," + bean.getName() + "," + bean.getDescription() + "," + bean.getModel());
		}
		System.out.println("==================end");
	}

}
