package com.kcb.mqlService.mqlFactory.exception;

public class MQLQueryExecuteException extends RuntimeException{

    public MQLQueryExecuteException() {
        super("Can't Execute MQL Exception");
    }

    public MQLQueryExecuteException(String message) {
        super(message);
    }


}
