/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.wadl;

/**
 *
 * @author itakenami
 */
public class RestParam {
    
    public String name;
    public String type;
    
    public RestParam(String name, String type){
        this.name = name;
        this.type = type;
    }
}
