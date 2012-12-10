/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.exception;

import java.util.List;
import play.data.validation.Validation;
import play.i18n.Messages;

/**
 *
 * @author itakenami
 */
public class ValidationException extends RuntimeException {
    
    private List<ValidationError> err;
    
    public ValidationException(List<ValidationError> err){
        this.err = err;
    }
    
    public List<ValidationError> getErrorList(){
        return err;
    }
    
    private String message(String key, ValidationError e) {
        key = Messages.get(key);
        Object[] args = new Object[e.variables.length + 1];
        System.arraycopy(e.variables, 0, args, 1, e.variables.length);
        args[0] = key;
        return Messages.get(e.message, args);
    }
    
    public void addErrorIn(Validation validation){
        
        for (ValidationError error : err) {
            Validation.addError(error.getKey(), this.message(error.getKey(), error));
        }
    }
    
}
