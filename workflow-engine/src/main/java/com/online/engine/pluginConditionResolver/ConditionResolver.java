package com.online.engine.pluginConditionResolver;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;

import com.online.engine.Instance.IProcessInstance;
import com.online.engine.Instance.IToken;
import com.online.engine.Instance.impl.ProcessInstance;
import com.online.engine.Instance.impl.ProcessInstanceVar;
import com.online.engine.Instance.impl.RuntimeContext;
import com.online.engine.enums.TablesName;
import com.online.engine.pluginKernel.ITransitionInstance;
import com.online.engine.pluginPersistence.impl.PersistenceService;
import com.online.workflow.process.enums.TransitionStartEnum;
import com.online.workflow.process.net.Transition;
import com.googlecode.aviator.AviatorEvaluator;
public class ConditionResolver implements IConditionResolver {

	protected RuntimeContext rtCtx = null;

	@Override
	public void setRuntimeContext(RuntimeContext ctx) {
		rtCtx = ctx;

	}

	@Override
	public RuntimeContext getRuntimeContext() {

		return rtCtx;
	}

	public Boolean conditionCalc(Object condition,String operator,Object result) {
	    //boolean result =  ;
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("param_1", condition);
	    map.put("param_2", result);
		return (Boolean) AviatorEvaluator.execute("param_1"+operator+"param_2",map);

	}
	
	/**
     * 
     * 功能:根据分支条件过滤多余的连接线<br>
     * 约束:与本函数相关的约束<br>
     * @param leavingTransitionInstances
     * @return
     */
    public List<ITransitionInstance> conditionFilter(IToken token, List<ITransitionInstance> leavingTransitionInstances) {
        List<ITransitionInstance> transitionInstances = new ArrayList<ITransitionInstance>();
        for(int i =0; leavingTransitionInstances != null && i < leavingTransitionInstances.size(); i++){
            Transition transition = leavingTransitionInstances.get(i).getTransition();
            if (transition.getStart() == TransitionStartEnum.none.intValue()) {
                transitionInstances.add(leavingTransitionInstances.get(i));
            }else if (transition.getStart() == TransitionStartEnum.sqlCondition.intValue()) {
                if(this.sqlConditionFilter(token,transition)){
                    transitionInstances.add(leavingTransitionInstances.get(i));
                }
            }else if (transition.getStart() == TransitionStartEnum.methodCondition.intValue()){
                if(this.methodConditionFilter(token,transition)){
                    transitionInstances.add(leavingTransitionInstances.get(i));
                }
            }else if (transition.getStart() == TransitionStartEnum.varCondition.intValue()) {
                if(this.varConditionFilter(token,transition)){
                    transitionInstances.add(leavingTransitionInstances.get(i));
                }
            }
        }
        return transitionInstances;
    }
    
	
	private boolean varConditionFilter(IToken token, Transition transition) {
	    boolean flag = false;
	    IProcessInstance processInstance = token.getProcessInstance();
	    Object var = processInstance.getProcessInstanceVariablesByName(transition.getVarCondition());
	    Object result = ProcessInstanceVar.GetConditionResult(var, transition.getVarResult());
        flag = this.conditionCalc(var, transition.getVarOperator(), result);
	    
	    return flag;
    }

    /**
     * 
     * 功能:sql条件<br>
     * 约束:与本函数相关的约束<br>
     * @param token 
     * @param transition
     * @return
     */
    private boolean sqlConditionFilter(IToken token, Transition transition) {
        boolean flag = false;
        PersistenceService persistenceService = this.rtCtx.getPersistenceService();
        String sql = transition.getSqlCondition();
        /*List<ProcessInstance> processInstances = (List<ProcessInstance>) this.rtCtx.getCacheData().get(TablesName.processInstance);
        ProcessInstance processInstance = processInstances.get(0);*/
        SQLQuery sqlQuery = persistenceService.createSqlQuery(sql);
        sqlQuery.setParameter("xid", token.getProcessInstance().getEntityId());
        Object param = sqlQuery.list().get(0);
        Object result = null;
        if (param instanceof BigDecimal) {
            param = ((BigDecimal) param).doubleValue();
            result = new BigDecimal(transition.getSqlResult()).doubleValue();
        }else{
            param = String.valueOf(param).toLowerCase();
            result = transition.getSqlResult().toLowerCase();
        }
        IConditionResolver resolver = this.rtCtx.getConditionResolver();
        flag = resolver.conditionCalc(param,transition.getSqlOperator(),result);
        return flag;
    }
    
    /**
     * 
     * 功能:自定义方法<br>
     * 约束:与本函数相关的约束<br>
     * @param token 
     * @param transition 
     * @return
     */
    @SuppressWarnings("rawtypes")
    private boolean methodConditionFilter(IToken token, Transition transition) {
        boolean flag = false;
        String className = transition.getClassName();
        String methodName = transition.getMethodName();
        String entityName = methodName.substring(methodName.indexOf("(")+1, methodName.lastIndexOf(","));
        String entityId = methodName.substring(methodName.indexOf(",")+1, methodName.lastIndexOf(")"));
        methodName = methodName.substring(0,methodName.indexOf("("));
        Class clz = null;
        Object obj = null;
        Method method = null;
        try {
            clz = Class.forName(className);
            obj = clz.newInstance();
            method = obj.getClass().getDeclaredMethod(methodName, new Class[]{ String.class, String.class, String.class, String.class});
            Object[] params = new Object[]{ StringUtils.trim(entityName), token.getProcessInstance().getEntityId(),token.getProcessInstance().getCreatorId(),token.getProcessInstance().getCreatorName()};
            flag = (Boolean) method.invoke(obj, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
	/**
	 * 方式使用样例，待删除
	 */
	public static void test(String[] args) {
	    Object obj_1 = AviatorEvaluator.execute("1+1==2");
	    Object obj_2 = AviatorEvaluator.execute("1+2>2");
	    Object obj_3 = AviatorEvaluator.execute("'lx'=='admin'");
	    Object obj_4 = AviatorEvaluator.execute("'管理员'=='张三'");
	    System.out.println(obj_1 );
	    System.out.println(obj_2 );
	    System.out.println(obj_3 );
	    System.out.println(obj_4 );
	    System.out.println("---------");
	    HashMap<String, Object> params = new HashMap<String, Object>();
	    params.put("a", 16);
	    params.put("b", 5);
	    Object obj_5 = AviatorEvaluator.execute("a>b",params);
	    params.clear();
	    params.put("a", 5);
	    params.put("b", 7);
	    Object obj_6 = AviatorEvaluator.execute("a>b",params);
	    System.out.println(obj_5 );
	    System.out.println(obj_6 );
	    System.out.println("---------");
	    params.clear();
	    params.put("a", "lx");
	    params.put("b", "admin");
	    Object obj_7 = AviatorEvaluator.execute("a==b",params);
	    params.clear();
	    params.put("a", "管理员");
	    params.put("b", "管理员");
	    Object obj_8 = AviatorEvaluator.execute("a"+"=="+"b",params);
	    System.out.println(obj_7 );
	    System.out.println(obj_8 );
    }

}
