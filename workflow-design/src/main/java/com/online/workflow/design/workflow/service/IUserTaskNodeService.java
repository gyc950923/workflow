package com.online.workflow.design.workflow.service;

import java.util.List;
import java.util.Map;

import com.online.workflow.process.net.Activity;

import net.sf.json.JSONObject;

public interface IUserTaskNodeService {

    /**
     * 
     * 功能:装配InlineTasks数据<br>
     * 约束:与本函数相关的约束<br>
     * @param activityJson
     * @param activity
     */
    void writeInlineTasks(JSONObject activityJson, Activity activity);

    List<Map> getBackRangeList(String resourceId, List<Activity> activities);

    boolean validateRepeatName(String name, Activity activity, List<Activity> activities);

}
