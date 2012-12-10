/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.transform;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.util.Map;

/**
 *
 * @author itakenami
 */
public class JSONExclude implements ExclusionStrategy {

    private Map<String, Boolean> exclude;

    public JSONExclude(Map<String, Boolean> exclude) {
        this.exclude = exclude;
    }

    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        String campo = f.getName();
        if(exclude.containsKey(campo)){
            return true;
        }else{
            return false;
        }
        
    }
}