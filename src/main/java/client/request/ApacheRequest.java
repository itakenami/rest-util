/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.request;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author itakenami
 */
public class ApacheRequest extends Request {

    private String resource;
    private Integer status;
    private String content_type;
    
    private Proxy proxy;
    
    private class Proxy {
        
        String host;
        int port;
        String user;
        String password;
        
        public Proxy(String host, int port, String user, String password){
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
        }
    }
    

    public ApacheRequest(String resource) {
        this.resource = resource;
    }
    
    public ApacheRequest(String resource, String host, int port, String user, String password) {
        this.resource = resource;
        this.proxy = new Proxy(host, port, user, password);
    }

    @Override
    public String get(String url) {
        
        DefaultHttpClient httpclient = new DefaultHttpClient();
        
        
        try {

            HttpGet httpget = new HttpGet(resource + url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity resEntity = response.getEntity();

            this.status = response.getStatusLine().getStatusCode();
            StringBuilder answer = new StringBuilder();
            if (resEntity != null) {
                this.content_type = resEntity.getContentType().toString();
                BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
                String line;
                while ((line = reader.readLine()) != null) {
                    answer.append(line);
                }
                reader.close();
            }
            EntityUtils.consume(resEntity);
            //Pode adicionar um erro se não conseguir retornar
            return answer.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao executar requisição. Verifique a conexão com a internet ou defina corretamente o proxy.");
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    @Override
    public String post(String url, Map<String, String> map) {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        
        
        try {

            HttpPost httppost = new HttpPost(resource + url);

            MultipartEntity reqEntity = new MultipartEntity();
            Set<String> keys = map.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String k = it.next();
                reqEntity.addPart(k, new StringBody(map.get(k).toString()));
            }
            httppost.setEntity(reqEntity);
            
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            this.status = response.getStatusLine().getStatusCode();
            StringBuilder answer = new StringBuilder();
            if (resEntity != null) {
                this.content_type = resEntity.getContentType().toString();
                BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
                String line;
                while ((line = reader.readLine()) != null) {
                    answer.append(line);
                }
                reader.close();
            }
            EntityUtils.consume(resEntity);
            //Pode adicionar um erro se não conseguir retornar
            return answer.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao executar requisição. Verifique a conexão com a internet ou defina corretamente o proxy.");
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    @Override
    public String put(String url, Map<String, String> map) {
        
        DefaultHttpClient httpclient = new DefaultHttpClient();
        
        
        try {

            HttpPut httpput = new HttpPut(resource + url);

            MultipartEntity reqEntity = new MultipartEntity();
            Set<String> keys = map.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String k = it.next();
                reqEntity.addPart(k, new StringBody(map.get(k).toString()));
            }
            httpput.setEntity(reqEntity);
            
            HttpResponse response = httpclient.execute(httpput);
            HttpEntity resEntity = response.getEntity();

            this.status = response.getStatusLine().getStatusCode();
            StringBuilder answer = new StringBuilder();
            if (resEntity != null) {
                this.content_type = resEntity.getContentType().toString();
                BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
                String line;
                while ((line = reader.readLine()) != null) {
                    answer.append(line);
                }
                reader.close();
            }
            EntityUtils.consume(resEntity);
            //Pode adicionar um erro se não conseguir retornar
            return answer.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao executar requisição. Verifique a conexão com a internet ou defina corretamente o proxy.");
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    @Override
    public String delete(String url) {
        
        DefaultHttpClient httpclient = new DefaultHttpClient();
        
        try {

            HttpDelete httpdelete = new HttpDelete(resource + url);
            HttpResponse response = httpclient.execute(httpdelete);
            HttpEntity resEntity = response.getEntity();

            this.status = response.getStatusLine().getStatusCode();
            StringBuilder answer = new StringBuilder();
            if (resEntity != null) {
                this.content_type = resEntity.getContentType().toString();
                BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
                String line;
                while ((line = reader.readLine()) != null) {
                    answer.append(line);
                }
                reader.close();
            }
            EntityUtils.consume(resEntity);
            //Pode adicionar um erro se não conseguir retornar
            return answer.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao executar requisição. Verifique a conexão com a internet ou defina corretamente o proxy.");
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    @Override
    public Integer getStatus() {
        return this.status;
    }

    @Override
    public String getContentType() {
        return this.content_type;
    }
}
