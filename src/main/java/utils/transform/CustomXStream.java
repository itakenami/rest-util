/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.transform;

import api.wadl.annotation.XMLCast;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import java.util.Map;

/**
 *
 * @author itakenami
 */
public class CustomXStream extends XStream {

    private Map<String, Boolean> exclude;

    public CustomXStream(Exclude exclude) {
        if (exclude != null) {
            this.exclude = exclude.getExclude();
        }
    }

    @Override
    protected MapperWrapper wrapMapper(MapperWrapper next) {
        
        return new MapperWrapper(next) {
            
            @Override
            public String serializedClass(Class type) {
                if (type.isAnnotationPresent(XMLCast.class)) {
                    XMLCast cast = (XMLCast) type.getAnnotation(XMLCast.class);
                    return cast.thisClassTo();
                }else{
                    return super.serializedClass(type);
                }
            }
            
            @Override
            public boolean shouldSerializeMember(Class definedIn, String fieldName) {


                if (exclude != null) {
                    if (exclude.containsKey(fieldName)) {
                        return false;
                    }
                }
                return super.shouldSerializeMember(definedIn, fieldName);
            }
        };
    }
}
