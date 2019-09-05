package com.online.workflow.process;

/**
 * 事件监听器
 * @author 贾朋亮,nychen2000@163.com
 */
public class EventListener {
    private String className ;
    
    /**
     * 返回监听器的实现类名称
     * @return 监听器的实现类名称
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置监听器的实现类名称
     * @param className 监听器的实现类名称
     */
    public void setClassName(String className) {
        this.className = className;
    }
    
}
