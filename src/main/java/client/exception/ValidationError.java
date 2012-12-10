/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.exception;

/**
 *
 * @author itakenami
 */
public class ValidationError {

    String message;
    String key;
    String[] variables;

    public ValidationError(String key, String message, String[] variables) {
        this.message = message;
        this.key = key;
        this.variables = variables;
    }
    
    /**
     * @return The translated message
     */
    public String message() {
        return message;
    }
    
    /**
     * @return The field name
     */
    public String getKey() {
        return key;
    }
    

    @Override
    public String toString() {
        return message();
    }

}
