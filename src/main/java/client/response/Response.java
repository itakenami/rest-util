/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.response;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * @author itakenami
 */
public interface Response<T> {
    
    
    public boolean isOK();
    public boolean SYSERROR();
    public boolean VALIDATIONERROR();
    public String getException();
    public String getContent() throws Exception;
    public T getContent(Class<?> c) throws Exception;
    public List<T> getCollectionContent(Type c) throws Exception;
    
}
