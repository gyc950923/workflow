package com.online.engine.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.online.engine.pluginTaskInstanceManger.MySSLSocketFactory;
import com.online.util.ExceptionUtil;

@SuppressWarnings({ "deprecation" })
public class HttpClientUtil {


    /**
     * 
     * 功能:通过HttpClient调用远程接口<br>
     * 约束:与本函数相关的约束<br>
     * @param url
     */
	public static String executeCallBack(String url, List<NameValuePair> params){
    	
    	HttpClient client = MySSLSocketFactory.getNewHttpClient();
    	CredentialsProvider credsProvider = new BasicCredentialsProvider();
    	//写入认证的用户名与密码
    	UsernamePasswordCredentials creds = new UsernamePasswordCredentials("xxx", "xxx"); 
    	//创建验证  
         credsProvider.setCredentials(  
             new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),   
             creds);
         //将认证写入到httpClient中  
         ((DefaultHttpClient)client).setCredentialsProvider(credsProvider);   
         //=========Https认证===end=============================
        
        String result = null;
        JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded"); 
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
            HttpResponse response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode()==200){
                result=EntityUtils.toString(response.getEntity());
            }else{
            	json.put("status", false);
            	json.put("data", response.toString());
            	result = json.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("status", false);
            json.put("data", ExceptionUtil.getStackTraceAsString(e));
            result = json.toString();
        }
        
        if(null == result) {
        	 json.put("status", false);
        	 json.put("data", "审批处理异常失败！");
        	 result = json.toString();
        }
		return result;
    }

    public static String creatMyVar(HashMap<String, Object> hashMap) {
        StringBuilder builder = new StringBuilder("{");
        String[] str = new String[hashMap.size()];
        int i = 0;
        for(Entry<String, Object> entity :hashMap.entrySet()){
            if (entity.getValue() instanceof Integer) {
                str[i] = HttpClientUtil.createMyVarJson("Integer",entity.getKey(),entity.getValue());
            }else if (entity.getValue() instanceof Long) {
                str[i] = HttpClientUtil.createMyVarJson("Long",entity.getKey(),entity.getValue());
            }else if (entity.getValue() instanceof Float) {
                str[i] = HttpClientUtil.createMyVarJson("Float",entity.getKey(),entity.getValue());
            }else if (entity.getValue() instanceof Double) {
                str[i] = HttpClientUtil.createMyVarJson("Double",entity.getKey(),entity.getValue());
            }else if (entity.getValue() instanceof Boolean) {
                str[i] = HttpClientUtil.createMyVarJson("Boolean",entity.getKey(),entity.getValue());
            }else {
                str[i] = HttpClientUtil.createMyVarJson("String",entity.getKey(),entity.getValue());
            }
            i++;
        }
        builder.append(StringUtils.join(str, ","));
        builder.append("}");
        return builder.toString();
    }
    
    private static String createMyVarJson(String type, String key, Object value){
        StringBuilder builder = new StringBuilder("");
        builder.append(key);
        builder.append(":");
        builder.append("\""+ type +"#"+ String.valueOf(value)+"\"");
        return builder.toString();
    }

}
