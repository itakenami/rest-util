/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.request;

import java.util.Map;
import play.libs.WS;

/**
 *
 * @author itakenami
 */
public class PlayRequest extends Request {
    
    private String resource;
    private Integer status;
    private String content_type;
    
    public PlayRequest(String resource){
        this.resource = resource;
    }

    @Override
    public String get(String url) {
        WS.HttpResponse response = WS.url(resource+url).get();
        this.status = response.getStatus();
        this.content_type = response.getHeader("Content-Type");
        return response.getString();
    }

    @Override
    public String post(String url, Map<String, String> map) {
        WS.HttpResponse response = WS.url(resource+url).setParameters(map).post();
        this.status = response.getStatus();
        this.content_type = response.getHeader("Content-Type");
        return response.getString();
    }

    @Override
    public String put(String url, Map<String, String> map) {       
        WS.HttpResponse response = WS.url(resource+url).setParameters(map).put();
        this.status = response.getStatus();
        this.content_type = response.getHeader("Content-Type");
        return response.getString();
    }

    @Override
    public String delete(String url) {
        WS.HttpResponse response = WS.url(resource+url).delete();
        this.status = response.getStatus();
        this.content_type = response.getHeader("Content-Type");
        return response.getString();
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public String getContentType() {
        return content_type;
    }

    @Override
    public String get() {
        return get("");
    }

    
    
}
