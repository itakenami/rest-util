/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.transform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.Result;

/**
 *
 * @author itakenami
 */
public class Render {

    public static String XML(Result obj, Exclude exclude) {

        CustomXStream xstream;
        if (exclude != null) {
            xstream = new CustomXStream(exclude);
        } else {
            xstream = new CustomXStream(null);
        }

        return xstream.toXML(obj);

    }
    
    public static String XML(Result obj) {
        return XML(obj, null);
    }
    

    public static String JSON(Result obj, Exclude exclude) {

        if (exclude != null) {
            JSONExclude je = new JSONExclude(exclude.getExclude());
            Gson gson = new GsonBuilder().setExclusionStrategies(je).create();
            return gson.toJson(obj);
        } else {
            String json = new Gson().toJson(obj);
            return json;
        }

    }
    
    public static String JSON(Result obj) {
        return JSON(obj, null);
    }
}
