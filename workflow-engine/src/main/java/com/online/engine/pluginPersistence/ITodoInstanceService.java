package com.online.engine.pluginPersistence;

import java.util.List;

import com.online.engine.Instance.ITodo;
import com.online.workflow.process.resources.PagerNumber;

public interface ITodoInstanceService {

	  /// <summary>
    ///  获取待办信息
    /// </summary>
    /// <param name="actorId"></param>
    /// <param name="entityType"></param>
    /// <param name="orgId"></param>
    /// <returns></returns>
     List<ITodo> getTodoInstanceByActorId(String actorId, String entityType, String orgId);

     /// <summary>
    /// 已完成流程实例
    /// </summary>
    /// <param name="actorId"></param>
    /// <param name="entityType"></param>
    /// <param name="orgId"></param>
    /// <returns></returns>
     List<ITodo> getPassEndProcessInstByActorId(String actorId, String orgId);
       /// <summary>
    /// 获取历史记录
    /// </summary>
    /// <param name="actorId"></param>
    /// <param name="processInstId"></param>
    /// <param name="orgId"></param>
    /// <returns></returns>
     List<ITodo> getWorkflowHistory(String entityType, String entityId, String orgId, PagerNumber page);
}
