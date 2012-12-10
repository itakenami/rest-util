/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.wadl;

import api.wadl.annotation.Param;
import java.util.ArrayList;

/**
 *
 * @author itakenami
 */
public class RestPath {
    
    public String name;
    public String id;
    public String method;
    public RestParam[] params;
    
    public RestPath(String name, String id, String method, Param[] params){
        
        this.name = name;
        this.id = id;
        this.method = method;
        
        if(params!=null){
            this.params = new RestParam[params.length];
            for(int x=0;x<params.length;x++){
                this.params[x] = new RestParam(params[x].name(), params[x].type());
            }
        }
        
    }
    
    
}
