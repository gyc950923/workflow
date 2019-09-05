package com.online.engine.common;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerUtil {

    public String getContent(String str, HashMap<String, Object> vars) {
        Map<String,Object> param = this.getTemplatePrams(str);//获取代办描述中的需要替换的变量
        param = this.initTemplateParam(param, vars);//为变量初始化赋值
        Template template =null;
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        try {
            template = new Template("template", str, new Configuration());
            template.process(param, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }
    
    public Map<String, Object> initTemplateParam(Map<String, Object> param, HashMap<String, Object> vars) {
        if (vars != null) {
            for(Entry<String, Object> entry:param.entrySet()){
                Object value = vars.get(entry.getKey());
                if (value==null) {
                    continue;
                }
                entry.setValue(value);
            }
        }
        return param;
    }

    public Map<String,Object> getTemplatePrams(String content){
        Map<String,Object> map = new HashMap<String,Object>();
        int fromIndex = 0;
        while(content.indexOf("${", fromIndex)>-1){
            int startIndex = content.indexOf("${", fromIndex)+2;
            int endIndex = content.indexOf("}", startIndex);
            String keyName = content.substring(startIndex, content.indexOf("}", startIndex));
            map.put(keyName.trim(), content.substring(startIndex -2, endIndex+1));
            fromIndex = endIndex;
        }
        return map;
    }

}
