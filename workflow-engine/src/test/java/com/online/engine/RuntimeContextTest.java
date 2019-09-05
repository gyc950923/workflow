package com.online.engine;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class RuntimeContextTest {

	static ApplicationContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		/*context = new ClassPathXmlApplicationContext(
				"config/WfRuntimeContext.xml");*/
		
	}

	@Test
	public void test() {

		/*RuntimeContext rContext = (RuntimeContext) context
				.getBean("runtimeContext");

		BeanFactory springBeanFactory=null;
		 springBeanFactory.getBean("runtimeContext");
		
		
		TaskInstanceManger taskInstanceManger = (TaskInstanceManger) rContext
				.getTaskInstanceManager();
		
		System.out.println(taskInstanceManger.getName());
		taskInstanceManger.setName("rrr");
		
		RuntimeContext rContext1 = (RuntimeContext) context
				.getBean("runtimeContext");
		
		TaskInstanceManger taskInstanceManger1 = (TaskInstanceManger) rContext1
				.getTaskInstanceManager();
		
		
		
		System.out.println(taskInstanceManger1.getName());*/
	}

}
