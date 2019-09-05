import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.online.engine.pluginConditionResolver.ConditionResolver;
import com.online.engine.pluginKernel.impl.ActivityInstance;
import com.online.engine.pluginKernel.impl.NodeInstance;
import com.online.workflow.process.net.Activity;


public class Test extends JdbcDaoSupport{

	/*static ApplicationContext springcontext;
	static {

		springcontext = new ClassPathXmlApplicationContext(
				"config/WfRuntimeContext.xml");
		springcontext.getBean("runtimeContext");
		
	}*/
	
	public static void main(String[] args) throws Exception {
		
		/*WorkFlowHelper oo = new WorkFlowHelper();
		RuntimeContext dfdContext=(RuntimeContext) springcontext.getBean("runtimeContext");*/
				ActivityInstance moel=new ActivityInstance(new Activity(), new ConditionResolver());
				moel.fire(new com.online.engine.Instance.impl.Token());
				NodeInstance moel1=new ActivityInstance(new Activity(),new ConditionResolver());
				moel1.fire(new com.online.engine.Instance.impl.Token());
	
	}
	
	
	public String Getname() {
		
	
		return "";
	}
}
