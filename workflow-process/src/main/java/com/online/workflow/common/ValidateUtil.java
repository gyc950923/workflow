package com.online.workflow.common;

import java.util.Collection;
import java.util.Map;

public class ValidateUtil {

    /**
     * 
     * 功能:判断集合是否为空<br>
     * 约束:与本函数相关的约束<br>
     * @param collection 条件值
     * @return 返回布尔值
     */
    public static boolean isNullAndEmpty(Collection collection){
        return (collection == null || collection.isEmpty());
    }
    
    /**
     * 
     * 功能:判断Map是否为空<br>
     * 约束:与本函数相关的约束<br>
     * @param map 条件值
     * @return 返回值
     */
    public static boolean isNullAndEmpty(Map<String, String> map) {
        return (map == null || map.isEmpty());
    }
    
    /**
     * 
     * 功能:判断对象是否为null<br>
     * 约束:与本函数相关的约束<br>
     * @param obj 参数值
     * @return 返回布尔值
     */
    public static boolean isNull(Object obj){
        return obj == null;
    }

}
