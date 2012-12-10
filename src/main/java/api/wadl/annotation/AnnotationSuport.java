/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.wadl.annotation;


import api.wadl.RestParam;
import api.wadl.RestPath;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author itakenami
 */
public class AnnotationSuport {

    //Classes a serem geradas
    private List<Class<?>> classes;
    
    //Paths a serem gerados
    private Map<String, List<RestPath>> paths;
    private Map<String, RestParam> params;
    private String urlbase;
    private boolean change;
    
    private static AnnotationSuport instance;

    private AnnotationSuport() {
        classes = new ArrayList<Class<?>>();
        paths = new HashMap<String, List<RestPath>>();
        params = new HashMap<String, RestParam>();
        change = false;
    }

    //Adiciona uma classe a ser gerada
    public void addClass(Class<?> c) {
        if(!classes.contains(c)){
            classes.add(c);
            change = true;
        }
    }

    public static AnnotationSuport getInstance() {
        if (instance == null) {
            instance = new AnnotationSuport();
        }
        return instance;
    }
    
    private String getParamName(String var){
         int ini = var.indexOf("{")+1;
         int fim = var.indexOf("}")-ini+1;
         String id = var.substring(ini, fim);
         return id;
    }
            
    private void addPath(RestPath path){
        
        String key = path.name;
        
        if(paths.get(key)!=null){
            paths.get(key).add(path);
        }else{
            List<RestPath> lstpaths = new ArrayList<RestPath>();
            lstpaths.add(path);
            paths.put(key, lstpaths);
        }
    }
    
    public Map<String, List<RestPath>> getPaths() {
        return paths;
    }
    
    public Map<String, RestParam> getParams(){
        return params;
    }
    
    public String getUrlBase(){
        return urlbase;
    }
    
    public void setUrlBase(String url){
        this.urlbase = url;
    }

    public void processWadl() {
        
        if(change==false){
            return;
        }
        
        this.urlbase = play.Play.configuration.getProperty("rest.baseurl")==null?getUrlBase():play.Play.configuration.getProperty("rest.baseurl");        

        //Percorre as classes do projeto
        for (int x = 0; x < classes.size(); x++) {

            Class<?> cl = classes.get(x);

            //Verifica se existe notacao de recurso
            if (cl.isAnnotationPresent(Resource.class)) {
                
                Resource resource = cl.getAnnotation(Resource.class);
                
                //Define a url do recurso
                String url_resource = resource.name();
                
                if(resource.param()!=null){
                    //RestParam[] prs = new RestParam[resource.param().length];
                    for(int w=0;w<resource.param().length;w++){
                        params.put(resource.param()[w].name(), new RestParam(getParamName(resource.param()[w].name()), resource.param()[w].type()));
                    }
                }

                //Obtem os metodos da classe
                Method[] metodos = cl.getDeclaredMethods();

                //Percorre os metoso das classes
                for (int y = 0; y < metodos.length; y++) {

                    //Verifica se o metodo tem vários paths
                    if (metodos[y].isAnnotationPresent(Paths.class)) {
                        
                        //Obtem os paths
                        Paths ps = metodos[y].getAnnotation(Paths.class);

                        //Percorre os paths
                        for (int z = 0; z < ps.value().length; z++) {
                            Path p_ps = ps.value()[z];
                            
                            String name = url_resource+p_ps.name();
                            String id = p_ps.id();
                            addPath(new RestPath(name, id, p_ps.method(), p_ps.param()));
                        }

                    }else{
                        
                        if (metodos[y].isAnnotationPresent(Path.class)) {
                            Path p_ps = metodos[y].getAnnotation(Path.class);
                            
                            String name = url_resource+p_ps.name();
                            String id = p_ps.id();
                            addPath(new RestPath(name, id, p_ps.method(), p_ps.param()));
                        }
                        
                    }
                }
            }
        }
        change = false;
    }
}
