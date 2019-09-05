package com.online.engine;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.pluginPersistence.IPersistenceService;
import com.online.engine.pluginTaskInstanceManger.TaskInstanceManger;
import com.online.workflow.process.WorkflowDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		
		"classpath:../config/spring-config.xml" })
public class RuntimeContextTest1 {

	/*@Autowired
	RuntimeContext runtimeContext;

	@Test
	public void test() {

		IPersistenceService pp = runtimeContext.getPersistenceService();
		List<WorkflowDefinition> jplDefinitions = pp.getWorkflowDefinitions();

	}*/
}
