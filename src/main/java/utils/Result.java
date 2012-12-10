package utils;

import api.wadl.annotation.XMLCast;
import java.util.ArrayList;
import java.util.List;
import play.data.validation.Validation;


@XMLCast(thisClassTo="Result")
public class Result {
    
    private int Return;
    private Object Content;
    private Object Exception;
   
    private Result(int RETURN, Object CONTENT, Object EXCEPTION){
        this.Return     = RETURN;
        this.Content    = CONTENT;
        this.Exception  = EXCEPTION;
    }
    
    public static Result OK(Object CONTENT){
        return new Result(0, CONTENT, "");
    }
    
    public static Result ERROR(int RESULT){
        return new Result(RESULT, "", ResultMsg.getInstance().getMessage(RESULT));
    }
    
    public static Result SYSERROR(String EXCEPTION){
        return new Result(1, "", EXCEPTION);
    }
    
    public static Result VALIDERROR(Validation errors){
        List<play.data.validation.Error> err = new ArrayList<play.data.validation.Error>();
        for (play.data.validation.Error error : errors.errors()) {
            err.add(error);
        }
        return new Result(2, "", err);
    }
    
    public int getReturn(){
        return Return;
    }
    
}

