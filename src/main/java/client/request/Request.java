/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.request;

import java.util.Map;

/**
 *
 * @author itakenami
 */
public abstract class Request {
    
    public abstract String get(String url);
    public abstract String post(String url, Map<String, String> map);
    public abstract String put(String url, Map<String, String> map);
    public abstract String delete(String url);
    public abstract Integer getStatus();
    public abstract String getContentType();
    
    public String get(){
        return get("");
    }
    public String post(Map<String, String> map){
        return post("",map);
    }
    public String put(Map<String, String> map){
        return put("", map);
    }
    public String delete(){
        return delete("");
    }
    
    public boolean isXML(){
        return getContentType().indexOf("xml")>-1;
    }
    
    public boolean isJSON(){
        return getContentType().indexOf("json")>-1;
    }
}
