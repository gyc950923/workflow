package com.online.engine.Instance.impl;

import org.apache.commons.lang.StringUtils;

public class ProcessInstanceVar {
    private String id;
    private String ProcessInstanceId ;
    private String Name ;
    private String StringValue ;

    private String ValueType ;

    private String Value;
    private String OrgId ;

     public static Object GetProcessInstanceVarValue(String value) throws Exception
     {
         if (value == null)
             return null;
         int index = value.indexOf("#");
         if (index == -1)
         {
             return null;
         }
         String type = value.substring(0, index);
         String strValue = value.substring(index + 1);
         if ("String".equals(type))
         {
             return strValue;
         }
         if (StringUtils.isBlank(strValue.trim()))
         {
             return null;
         }
         if ("Integer".equals(type))
         {
             return Integer.parseInt(strValue);
         }
         if ("Long".equals(type))
         {
             return Long.parseLong(strValue);
         }
         else if ("Float".equals(type))
         {
             return Float.parseFloat(strValue);
         }
         else if ("Double".equals(type))
         {
             return Double.parseDouble(strValue);
         }
         else if ("Boolean".equals(type))
         {
             return Boolean.parseBoolean(strValue);
         }
         else
         {
             throw new Exception("引擎不支持数据类型" + type);
         }
     }

    public static Object GetConditionResult(Object value, String result){
        if (value instanceof Integer) {
             return Integer.parseInt(result);
        }else if (value instanceof Long) {
            return Long.parseLong(result);
        }else if (value instanceof Float) {
            return Float.parseFloat(result);
        }else if (value instanceof Double) {
            return Double.parseDouble(result);
        }else if (value instanceof Boolean) {
            return Boolean.parseBoolean(result);
        }else{
            return String.valueOf(result).toLowerCase();
        }
    }
    
    public void SetProcessInstanceVarValue(Object value){
        if (value instanceof Integer) {
            Value = "Integer#"+String.valueOf(value);
        }else if (value instanceof  Float) {
            Value = "Float#"+String.valueOf(value);
        }else if (value instanceof  Double) {
            Value = "Double#"+String.valueOf(value);
        }else if (value instanceof  Long) {
            Value = "Long#"+String.valueOf(value);
        }else if (value instanceof  Boolean) {
            Value = "Boolean#"+String.valueOf(value);
        }else {
            Value = "String#"+String.valueOf(value);
        }
    }
    
    public String getProcessInstanceId() {
        return ProcessInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        ProcessInstanceId = processInstanceId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name.toLowerCase();
    }

    public String getStringValue() {
        return StringValue;
    }

    public void setStringValue(String stringValue) {
        StringValue = stringValue;
    }

    public String getValueType() {
        return ValueType;
    }

    public void setValueType(String valueType) {
        ValueType = valueType;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        this.Value = value;
    }

    public String getOrgId() {
        return OrgId;
    }

    public void setOrgId(String orgId) {
        OrgId = orgId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
