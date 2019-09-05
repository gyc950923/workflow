package com.online.workflow.design.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings({"unchecked","rawtypes"})
public class SysJsonUtil {

    public static void returnText(HttpServletResponse response, String text){
        response.setContentType("application/text;charset=UTF-8");
        try {
            response.getWriter().print(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void returnJson(HttpServletResponse response, String jsonStr){
        response.setContentType("application/json;charset=UTF-8");
        try {
            response.getWriter().print(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * 功能:拼装前台初始化渲染所需json<br>
     * 约束:与本函数相关的约束<br>
     * @param charId
     * @param processChartContent
     * @return
     */
    public static String getInitJson(String charId, String processChartContent){
        //目标json格式"{\"modelId\":\""+charId +"\",\"model\":"+processChartContent+"}"
        StringBuilder initJson = new StringBuilder("{\"modelId\":\"");
        initJson.append(charId);
        initJson.append("\",\"model\":");
        initJson.append(processChartContent);
        initJson.append("}");
        return initJson.toString();      
    }
    
    /**
     * 
     * 功能:json串中存在List、Map等复杂对象<br>
     * 约束:与本函数相关的约束<br>
     * @param jsonArray
     * @param c
     * @param classMap
     * @return
     */
    public static <T> List<T> jSONArrayToList(JSONArray jsonArray, Class<T> c, Map<String, Class> classMap){
        Iterator<JSONObject> iter = jsonArray.iterator();
        List<T> list =new ArrayList<T>();
        while(iter.hasNext()){ 
            list.add((T) JSONObject.toBean(iter.next(), c, classMap)) ;
        }
        return list;
    }

    /**
     * 
     * 功能:json串中只有简单对象<br>
     * 约束:与本函数相关的约束<br>
     * @param jsonArray
     * @param c
     * @return
     */
    public static <T> List<T> JSONArrayToList(JSONArray jsonArray,Class<T> c){
        Iterator<JSONObject> iterator = jsonArray.iterator();
        List<T> list = new ArrayList<T>();
        while(iterator.hasNext()){
            list.add((T) JSONObject.toBean(iterator.next(), c));
        }
        return list;    
    }
    
    public static String getStrListJson(List<String> list){
        Iterator<String> iterator = list.iterator();
        StringBuilder jsonStr = new StringBuilder("[");
        while(iterator.hasNext()){
            String str = iterator.next();
            jsonStr.append("{ \"value\":\""+str +"\",\"text\":\""+str+"\"}");
            if (iterator.hasNext()) {
                jsonStr.append(",");
            }
        }
        jsonStr.append("]");
        return jsonStr.toString();
    }
}
