package com.kcb.mqlService.mqlFactory.exception;

public class MQLQueryNotValidException extends RuntimeException{

    public MQLQueryNotValidException() {
        super("Not Valid Query Exception");
    }

    public MQLQueryNotValidException(String message) {
        super(message);
    }


}
