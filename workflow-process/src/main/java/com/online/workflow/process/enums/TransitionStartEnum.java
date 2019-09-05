package com.online.workflow.process.enums;

public class TransitionStartEnum {

    // / <summary>无判定条件</summary>
    public static final Integer none = 0;
    // / <summary>sql条件</summary>    
    public static final Integer sqlCondition = 10;
    // / <summary>原路返回</summary>
    public static final Integer methodCondition = 20;
    // / <summary>重新流转</summary>
    public static final Integer varCondition = 30;

}
