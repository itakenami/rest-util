/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.request;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 *
 * @author itakenami
 */
public class SimpleRequest extends Request{
    
    private String resource;
    private Integer status;
    private String content_type;
    
    public SimpleRequest(String resource){
        this.resource = resource;
    }
    
    public String send(String method, String urlStr, Map<String,String> map)  {

        try {
            
            StringBuilder data = new StringBuilder();
            
            //String data = "";
            
            for (Map.Entry<String, String> entry : map.entrySet()) {
                data.append(entry.getKey()+"="+URLEncoder.encode(entry.getValue(), "UTF-8")+"&");
                
            }
            
            URL url = new URL(resource+urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            
            //if(method.toUpperCase().equals("PUT")){
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
            //}
            
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            
            writer.write(data.toString());
            writer.flush();
            
            StringBuilder answer = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();
            
            this.status = conn.getResponseCode();
            this.content_type = conn.getContentType();
            
            return answer.toString();
            
        } catch (Exception ex) {
            return ex.getMessage();
        }

    }
    
    private String send(String method, String urlStr) {
        
        String result;

        try {
            URL url = new URL(resource+urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            result = sb.toString();
            this.status = conn.getResponseCode();
            this.content_type = conn.getContentType();
        } catch (Exception e) {
            return e.getMessage();
        }

        return result;
    }

    @Override
    public String get(String url) {
        return send("GET", url);
    }

    @Override
    public String post(String url, Map<String, String> map) {
        return send("POST", url, map);
    }

    @Override
    public String put(String url, Map<String, String> map) {
        return send("PUT", url, map);
    }

    @Override
    public String delete(String url) {
        return send("DELETE", url);
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
