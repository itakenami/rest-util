/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.wadl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author itakenami
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Resource { 
    String name();
    Param[] param() default {};
}
